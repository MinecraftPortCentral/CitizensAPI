package net.citizensnpcs.api.astar.pathfinder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import net.citizensnpcs.api.astar.Agent;
import net.citizensnpcs.api.astar.Plan;
import net.citizensnpcs.api.astar.pathfinder.PathPoint.PathCallback;
import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class Path implements Plan {
    private List<Location<World>> blockList;
    private int index = 0;
    private final PathEntry[] path;

    public Path(Collection<Vector3d> vector) {
        this.path = Iterables.toArray(Iterables.transform(vector, new Function<Vector3d, PathEntry>() {
            @Override
            public PathEntry apply(Vector3d input) {
                return new PathEntry(input, Collections.<PathCallback> emptyList());
            }
        }), PathEntry.class);
    }

    Path(Iterable<VectorNode> unfiltered) {
        this.path = cull(unfiltered);
    }

    private PathEntry[] cull(Iterable<VectorNode> unfiltered) {
        // possibly expose cullability in an API
        List<PathEntry> path = Lists.newArrayList();
        for (VectorNode node : unfiltered) {
            Vector3d vector = node.getLocation().getPosition();
            path.add(new PathEntry(vector, node.callbacks));
        }
        return path.toArray(new PathEntry[path.size()]);
    }

    public void debug() {
        for (Player player : Sponge.getServer().getOnlinePlayers()) {
            for (PathEntry entry : path) {
                final Location<World> location = new Location<World>(player.getWorld(), entry.vector);
                player.sendBlockChange(location.getBlockPosition(), BlockTypes.YELLOW_FLOWER.getDefaultState());
            }
        }
    }

    public void debugEnd() {
        for (Player player : Sponge.getServer().getOnlinePlayers()) {
            for (PathEntry entry : path) {
                final Location<World> location = new Location<World>(player.getWorld(), entry.vector);
                player.sendBlockChange(location.getBlockPosition(), location.getBlock());
            }
        }
    }

    public Vector3d getCurrentVector() {
        return path[index].vector;
    }

    public Iterable<Vector3d> getPath() {
        return Iterables.transform(Arrays.asList(path), new Function<PathEntry, Vector3d>() {
            @Override
            public Vector3d apply(PathEntry input) {
                return input.vector;
            }
        });
    }

    @Override
    public boolean isComplete() {
        return index >= path.length;
    }

    public void run(NPC npc) {
        path[index].run(npc);
    }

    @Override
    public String toString() {
        return Arrays.toString(path);
    }

    @Override
    public void update(Agent agent) {
        if (isComplete()) {
            return;
        }
        ++index;
    }

    private class PathEntry {
        final List<PathCallback> callbacks;
        final Vector3d vector;

        private PathEntry(Vector3d vector, List<PathCallback> callbacks) {
            this.vector = vector;
            this.callbacks = callbacks;
        }

        private Location<World> getBlockUsingWorld(World world) {
            return new Location<World>(world, this.vector);
        }

        public void run(final NPC npc) {
            if (callbacks == null)
                return;
            final Location<World> location = getBlockUsingWorld(npc.getEntity().getWorld());
            for (PathCallback callback : callbacks) {
                if (blockList == null) {
                    blockList = Lists.transform(Arrays.asList(path), new Function<PathEntry, Location<World>>() {
                        @Override
                        public Location<World> apply(PathEntry input) {
                            return input.getBlockUsingWorld(npc.getEntity().getWorld());
                        }
                    });
                }
                ListIterator<Location<World>> vec = blockList.listIterator();
                if (index > 0) {
                    while (index != vec.nextIndex()) {
                        vec.next();
                    }
                }
                callback.run(npc, location, vec);
            }
        }

        @Override
        public String toString() {
            return vector.toString();
        }
    }
}