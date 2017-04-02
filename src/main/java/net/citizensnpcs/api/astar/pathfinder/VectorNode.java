package net.citizensnpcs.api.astar.pathfinder;

import java.util.List;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.Lists;

import net.citizensnpcs.api.astar.AStarNode;
import net.citizensnpcs.api.astar.Plan;
import net.citizensnpcs.api.astar.pathfinder.BlockExaminer.PassableState;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class VectorNode extends AStarNode implements PathPoint {
    private float blockCost = -1;
    List<PathCallback> callbacks;
    private final BlockExaminer[] examiners;
    private final VectorGoal goal;
    private Location<World> location;

    public VectorNode(VectorGoal goal,Location<World> location, BlockExaminer... examiners) {
        this.location =location;;
        this.examiners = examiners == null ? new BlockExaminer[] {} : examiners;
        this.goal = goal;
    }

    public Location<World> getLocation() {
        return this.location;
    }

    @Override
    public void addCallback(PathCallback callback) {
        if (callbacks == null) {
            callbacks = Lists.newArrayList();
        }
        callbacks.add(callback);
    }

    @Override
    public Plan buildPlan() {
        Iterable<VectorNode> parents = getParents();
        return new Path(parents);
    }

    @Override
    public VectorNode createAtOffset(Vector3d mod) {
        return new VectorNode(goal, new Location<World>(this.location.getExtent(), mod), examiners);
    }

    public float distance(VectorNode to) {
        return (float) location.getPosition().distance(to.location.getPosition());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        VectorNode other = (VectorNode) obj;
        if (location == null) {
            if (other.location != null) {
                return false;
            }
        } else if (!location.equals(other.location)) {
            return false;
        }
        return true;
    }

    private float getBlockCost() {
        if (blockCost == -1) {
            blockCost = 0;
            for (BlockExaminer examiner : examiners) {
                blockCost += examiner.getCost(location, this);
            }
        }
        return blockCost;
    }

    @Override
    public Vector3d getGoal() {
        return goal.goal;
    }

    @Override
    public Iterable<AStarNode> getNeighbours() {
        List<PathPoint> neighbours = null;
        for (BlockExaminer examiner : examiners) {
            if (examiner instanceof NeighbourGeneratorBlockExaminer) {
                neighbours = ((NeighbourGeneratorBlockExaminer) examiner).getNeighbours(this.location, this);
                break;
            }
        }
        if (neighbours == null) {
            neighbours = getNeighbours(this.location, this);
        }
        List<AStarNode> nodes = Lists.newArrayList();
        for (PathPoint sub : neighbours) {
            if (!isPassable(sub))
                continue;
            nodes.add((AStarNode) sub);
        }
        return nodes;
    }

    public List<PathPoint> getNeighbours(Location<World> source, PathPoint point) {
        List<PathPoint> neighbours = Lists.newArrayList();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0)
                        continue;
                    if (x != 0 && z != 0)
                        continue;
                    Vector3d mod = location.getPosition().add(new Vector3d(x, y, z));
                    if (mod.equals(location))
                        continue;
                    neighbours.add(point.createAtOffset(mod));
                }
            }
        }
        return neighbours;
    }

    @Override
    public PathPoint getParentPoint() {
        return (PathPoint) getParent();
    }

    @Override
    public Vector3d getVector() {
        return location.getPosition();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime + ((location == null) ? 0 : location.hashCode());
    }

    public float heuristicDistance(Vector3d goal) {
        return (float) (location.getPosition().distance(goal) + getBlockCost()) * TIEBREAKER;
    }

    private boolean isPassable(PathPoint mod) {
        boolean passable = false;
        for (BlockExaminer examiner : examiners) {
            PassableState state = examiner.isPassable(location, mod);
            if (state == PassableState.IGNORE)
                continue;
            passable |= state == PassableState.PASSABLE ? true : false;
        }
        return passable;
    }

    @Override
    public void setVector(Vector3d vector) {
        this.location = new Location<World>(this.location.getExtent(), vector);
    }

    private static final float TIEBREAKER = 1.001f;
}