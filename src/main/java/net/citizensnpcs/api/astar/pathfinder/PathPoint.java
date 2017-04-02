package net.citizensnpcs.api.astar.pathfinder;

import java.util.ListIterator;

import com.flowpowered.math.vector.Vector3d;
import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public interface PathPoint {
    void addCallback(PathCallback callback);

    PathPoint createAtOffset(Vector3d vector);

    Vector3d getGoal();

    PathPoint getParentPoint();

    Vector3d getVector();

    void setVector(Vector3d vector);

    public static interface PathCallback {
        void run(NPC npc, Location<World> point, ListIterator<Location<World>> path);
    }
}
