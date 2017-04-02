package net.citizensnpcs.api.astar.pathfinder;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.property.block.SolidCubeProperty;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import com.flowpowered.math.vector.Vector3d;
import net.citizensnpcs.api.ai.event.CancelReason;
import net.citizensnpcs.api.ai.event.NavigatorCallback;
import net.citizensnpcs.api.astar.pathfinder.PathPoint.PathCallback;
import net.citizensnpcs.api.npc.NPC;
import net.minecraft.block.Block;

public class MinecraftBlockExaminer implements BlockExaminer {
    @Override
    public float getCost(Location<World> source, PathPoint point) {
        Vector3d pos = point.getVector();
        BlockType above = source.getRelative(Direction.UP).getBlockType();
        BlockType below = source.getRelative(Direction.DOWN).getBlockType();
        BlockType in = source.getBlockType();
        if (above == BlockTypes.WEB || in == BlockTypes.WEB)
            return 1F;
        if (below == BlockTypes.SOUL_SAND || below == BlockTypes.ICE)
            return 1F;
        if (isLiquid(above, below, in))
            return 0.5F;
        return 0F; // TODO: add light level-specific costs
    }

    private boolean isClimbable(BlockType mat) {
        return mat == BlockTypes.LADDER || mat == BlockTypes.VINE;
    }

    @Override
    public PassableState isPassable(Location<World> source, PathPoint point) {
        Vector3d pos = point.getVector();
        BlockState above = source.getRelative(Direction.UP).getBlock();
        BlockState below = source.getRelative(Direction.DOWN).getBlock();
        BlockState in = source.getBlock();
        if (!canStandOn(below)) {
            return PassableState.UNPASSABLE;
        }
        if ((isClimbable(above.getType()) && isClimbable(in.getType())) || (isClimbable(in.getType()) && isClimbable(below.getType()))) {
            point.addCallback(new LadderClimber());
        } else if (!canStandIn(above) || !canStandIn(in)) {
            return PassableState.UNPASSABLE;
        }
        if (!canJumpOn(below.getType())) {
            if (point.getParentPoint() == null) {
                return PassableState.UNPASSABLE;
            }
            Vector3d parentPos = point.getParentPoint().getVector();
            if ((parentPos.getX() != pos.getX() || parentPos.getZ() != pos.getZ())
                    && pos.clone().sub(point.getParentPoint().getVector()).getY() == 1) {
                return PassableState.UNPASSABLE;
            }
        }
        return PassableState.PASSABLE;
    }

    private class LadderClimber implements PathCallback {
        boolean added = false;
        ListIterator<Location<World>> current;

        @Override
        public void run(final NPC npc, Location<World> point, ListIterator<Location<World>> path) {
            current = path;
            if (added || npc.data().<Boolean> get("running-ladder", false)) {
                added = true;
                return;
            }
            Runnable callback = new Runnable() {
                @Override
                public void run() {
                    if (npc.getEntity().getLocation().getBlockType() == BlockTypes.LADDER
                            && current.next().getY() > current.previous().getY()) {
                        final Vector3d vec = npc.getEntity().getVelocity();
                        npc.getEntity().setVelocity(new Vector3d(vec.getX(), 0.3, vec.getZ()));
                    }
                }
            };
            npc.getNavigator().getLocalParameters().addSingleUseCallback(new NavigatorCallback() {
                @Override
                public void onCompletion(CancelReason cancelReason) {
                    npc.data().set("running-ladder", false);
                }
            });
            npc.getNavigator().getLocalParameters().addRunCallback(callback);
            added = true;
        }
    }

    private static boolean canJumpOn(BlockType mat) {
        return !NOT_JUMPABLE.contains(mat);
    }

    public static boolean canStandIn(BlockState... blockStates) {
        for (BlockState state : blockStates) {
            if (state.getProperty(SolidCubeProperty.class).isPresent()) {
                return false;
            }
        }
        return true;
    }

    public static boolean canStandOn(Location<World> location) {
        Location<World> up = location.getRelative(Direction.UP);
        return canStandOn(up.getBlock()) && canStandIn(up.getBlock())
                && canStandIn(up.getRelative(Direction.UP).getBlock());
    }

    public static boolean canStandOn(BlockState state) {
        return !UNWALKABLE.contains(state.getType()) && state.getProperty(SolidCubeProperty.class).isPresent();
    }

    private static boolean contains(BlockType[] search, BlockType... find) {
        for (BlockType haystack : search) {
            for (BlockType needle : find) {
                if (haystack == needle)
                    return true;
            }
        }
        return false;
    }

    public static Location<World> findValidLocation(Location<World> location, int radius) {
        if (canStandIn(location.getBlock()) && canStandOn(location.getRelative(Direction.DOWN)))
            return location;
        for (int y = 0; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    Location<World> relative = new Location<World>(location.getExtent(), x, y, z);
                    if (canStandIn(relative.getRelative(Direction.UP).getBlock()) && canStandIn(relative.getBlock())
                            && canStandOn(location.getRelative(Direction.DOWN))) {
                        return relative;
                    }
                }
            }
        }
        return location;
    }

    public static boolean isDoor(BlockType in) {
        return DOORS.contains(in);
    }

    public static boolean isLiquid(BlockType... materials) {
        return contains(materials, BlockTypes.WATER, BlockTypes.FLOWING_WATER, BlockTypes.LAVA, BlockTypes.FLOWING_LAVA);
    }

    public static boolean validPosition(Location<World> in) {
        return canStandIn(in.getBlock()) && canStandIn(in.getRelative(Direction.UP).getBlock())
                && canStandOn(in.getRelative(Direction.DOWN));
    }

    private static final List<BlockType> DOORS = Arrays.asList(BlockTypes.IRON_DOOR, BlockTypes.WOODEN_DOOR,
            BlockTypes.SPRUCE_DOOR, BlockTypes.BIRCH_DOOR, BlockTypes.JUNGLE_DOOR, BlockTypes.ACACIA_DOOR,
            BlockTypes.DARK_OAK_DOOR);
    private static final List<BlockType> NOT_JUMPABLE = Arrays.asList(BlockTypes.FENCE, /*BlockTypes.IRON_FENCE,*/
            BlockTypes.NETHER_BRICK_FENCE, BlockTypes.COBBLESTONE_WALL, BlockTypes.SPRUCE_FENCE, BlockTypes.BIRCH_FENCE,
            BlockTypes.JUNGLE_FENCE, BlockTypes.ACACIA_FENCE, BlockTypes.DARK_OAK_FENCE);
    private static final List<BlockType> UNWALKABLE = Arrays.asList(BlockTypes.AIR, BlockTypes.LAVA, BlockTypes.FLOWING_LAVA,
            BlockTypes.CACTUS);
}
