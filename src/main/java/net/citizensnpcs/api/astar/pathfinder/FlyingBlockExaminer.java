package net.citizensnpcs.api.astar.pathfinder;

import java.util.List;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.Lists;

public class FlyingBlockExaminer implements NeighbourGeneratorBlockExaminer {
    @Override
    public float getCost(Location<World> source, PathPoint point) {
        Vector3d pos = point.getVector();
        BlockType above = source.getBlockRelative(Direction.UP).getBlockType();
        BlockType in = source.getBlockType();
        if (above == BlockTypes.WEB || in == BlockTypes.WEB) {
            return 0.5F;
        }
        return 0F;
    }

    @Override
    public List<PathPoint> getNeighbours(Location<World> source, PathPoint point) {
        List<PathPoint> neighbours = Lists.newArrayList();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0)
                        continue;
                    neighbours.add(point.createAtOffset(point.getVector().add(new Vector(x, y, z))));
                }
            }
        }
        return neighbours;
    }

    @Override
    public PassableState isPassable(Location<World> source, PathPoint point) {
        Vector3d pos = point.getVector();
        BlockType above = source.getBlockRelative(Direction.UP).getBlockType();
        BlockType in = source.getBlockType();
        if (MinecraftBlockExaminer.isLiquid(above, in)) {
            return PassableState.UNPASSABLE;
        }
        return PassableState.fromBoolean(MinecraftBlockExaminer.canStandIn(above, in));
    }

    private static final Vector UP = new Vector(0, 1, 0);
}