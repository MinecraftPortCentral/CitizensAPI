package net.citizensnpcs.api.astar.pathfinder;

import net.minecraft.block.state.IBlockState;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class ChunkBlockSource extends CachingChunkBlockSource<Chunk> {
    public ChunkBlockSource(Location<World> location, float radius) {
        super(location, radius);
    }

    public ChunkBlockSource(World world, int x, int z, float radius) {
        super(world, x, z, radius);
    }

    @Override
    protected Chunk getChunkObject(int x, int z) {
        return world.getChunk(x, 0, z).orElse(null);
    }

    /*@Override
    protected int getId(Chunk chunk, int x, int y, int z) {
        return chunk.getBlock(x, y, z).getTypeId();
    }*/

    @Override
    protected int getLightLevel(Chunk chunk, int x, int y, int z) {
        return ((IBlockState) chunk.getBlock(x, y, z)).getLightValue();
    }
}
