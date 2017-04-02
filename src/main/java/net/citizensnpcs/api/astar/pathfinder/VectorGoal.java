package net.citizensnpcs.api.astar.pathfinder;

import com.flowpowered.math.vector.Vector3d;
import net.citizensnpcs.api.astar.AStarGoal;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class VectorGoal implements AStarGoal<VectorNode> {
    final Vector3d goal;
    private final float leeway;

    public VectorGoal(Location<World> dest, float range) {
        if (!MinecraftBlockExaminer.canStandIn(dest.getBlock())) {
            dest = MinecraftBlockExaminer.findValidLocation(dest, 1);
        }
        this.leeway = range;
        this.goal = dest.getPosition();
    }

    @Override
    public float g(VectorNode from, VectorNode to) {
        return from.distance(to);
    }

    @Override
    public float getInitialCost(VectorNode node) {
        return (float) node.getLocation().getPosition().distance(goal);
    }

    @Override
    public float h(VectorNode from) {
        return from.heuristicDistance(goal);
    }

    @Override
    public boolean isFinished(VectorNode node) {
        double distanceSquared = node.getVector().distanceSquared(goal);
        return goal.equals(node.getLocation().getPosition()) || distanceSquared <= leeway;
    }
}
