package net.citizensnpcs.api.ai;

import net.citizensnpcs.api.astar.pathfinder.MinecraftBlockExaminer;
import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.cause.entity.teleport.EntityTeleportCause;
import org.spongepowered.api.event.cause.entity.teleport.TeleportCause;
import org.spongepowered.api.event.cause.entity.teleport.TeleportTypes;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class TeleportStuckAction implements StuckAction {
    private TeleportStuckAction() {
        // singleton
    }

    private boolean canStand(Location<World> location) {
        return MinecraftBlockExaminer.canStandIn(location.getBlock().getType())
                && MinecraftBlockExaminer.canStandIn(location.getRelative(Direction.UP).getBlockType());
    }

    @Override
    public boolean run(NPC npc, Navigator navigator) {
        if (!npc.isSpawned())
            return false;
        Location<World> base = navigator.getTargetAsLocation();
        if (npc.getEntity().getWorld().equals(base.getExtent())
                && npc.getEntity().getLocation().getPosition().distanceSquared(base.getPosition()) <= RANGE) {
            return true;
        }

        int iterations = 0;
        while (!canStand(base)) {
            if (iterations++ >= MAX_ITERATIONS) {
                break;
            }
            base = base.getRelative(Direction.UP);
        }

        npc.teleport(base, Cause.of(NamedCause.source(EntityTeleportCause.builder().entity(npc.getEntity()).type(TeleportTypes.ENTITY_TELEPORT).build())));
        return false;
    }

    public static TeleportStuckAction INSTANCE = new TeleportStuckAction();
    private static final int MAX_ITERATIONS = 10;
    private static final double RANGE = 10;
}
