package net.citizensnpcs.api.ai.goals;

import java.util.Random;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.event.NavigationCompleteEvent;
import net.citizensnpcs.api.ai.tree.BehaviorGoalAdapter;
import net.citizensnpcs.api.ai.tree.BehaviorStatus;
import net.citizensnpcs.api.astar.pathfinder.MinecraftBlockExaminer;
import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class WanderGoal extends BehaviorGoalAdapter {
    private boolean forceFinish;
    private final NPC npc;
    private final Random random = new Random();
    private final int xrange;
    private final int yrange;

    private WanderGoal(NPC npc, int xrange, int yrange) {
        this.npc = npc;
        this.xrange = xrange;
        this.yrange = yrange;
    }

    private Location<World> findRandomPosition() {
        Location<World>base = npc.getEntity().getLocation();
        Location<World> found = null;
        for (int i = 0; i < 10; i++) {
            int x = base.getBlockX() + random.nextInt(2 * xrange) - xrange;
            int y = base.getBlockY() + random.nextInt(2 * yrange) - yrange;
            int z = base.getBlockZ() + random.nextInt(2 * xrange) - xrange;
            Location<World> loc = new Location<World>(base.getExtent(), x, y, z);
            if (MinecraftBlockExaminer.canStandOn(loc)
                    && MinecraftBlockExaminer.canStandIn(loc.getRelative(Direction.UP).getBlock())) {
                found = loc.add(0, 1, 0);
                break;
            }
        }
        return found;
    }

    @Listener
    public void onFinish(NavigationCompleteEvent event) {
        forceFinish = true;
    }

    @Override
    public void reset() {
        forceFinish = false;
    }

    @Override
    public BehaviorStatus run() {
        if (!npc.getNavigator().isNavigating() || forceFinish)
            return BehaviorStatus.SUCCESS;
        return BehaviorStatus.RUNNING;
    }

    @Override
    public boolean shouldExecute() {
        if (!npc.isSpawned() || npc.getNavigator().isNavigating())
            return false;
        Location<World> dest = findRandomPosition();
        if (dest == null)
            return false;
        npc.getNavigator().setTarget(dest);
        CitizensAPI.registerEvents(this);
        return true;
    }

    public static WanderGoal createWithNPC(NPC npc) {
        return createWithNPCAndRange(npc, 10, 2);
    }

    public static WanderGoal createWithNPCAndRange(NPC npc, int xrange, int yrange) {
        return new WanderGoal(npc, xrange, yrange);
    }
}