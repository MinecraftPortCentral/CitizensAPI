package net.citizensnpcs.api.astar.pathfinder;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public interface BlockExaminer {
    float getCost(Location<World> source, PathPoint point);

    PassableState isPassable(Location<World> source, PathPoint point);

    public enum PassableState {
        IGNORE,
        PASSABLE,
        UNPASSABLE;

        public static PassableState fromBoolean(boolean b) {
            return b ? PASSABLE : UNPASSABLE;
        }
    }
}