package net.citizensnpcs.api.astar.pathfinder;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;

public interface NeighbourGeneratorBlockExaminer extends BlockExaminer {
    public List<PathPoint> getNeighbours(Location<World> source, PathPoint point);
}
