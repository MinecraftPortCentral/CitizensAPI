package net.citizensnpcs.api.persistence;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import net.citizensnpcs.api.util.DataKey;

public class LocationPersister implements Persister<Location<World>> {
    @Override
    public Location<World> create(DataKey root) {
        if (!root.keyExists("world"))
            return null;
        World world = Sponge.getServer().getWorld(root.getString("world")).orElse(null);
        double x = root.getDouble("x"), y = root.getDouble("y"), z = root.getDouble("z");
        float yaw = (float) root.getDouble("yaw"), pitch = (float) root.getDouble("pitch");
        return world == null ? new LazilyLoadedLocation(root.getString("world"), x, y, z, yaw, pitch)
                : new Location<World>(world, x, y, z, yaw, pitch);
    }

    private double round(double z) {
        if (Double.isInfinite(z) || Double.isNaN(z)) {
            return z;
        }
        return new BigDecimal(z).setScale(4, RoundingMode.DOWN).doubleValue();
    }

    @Override
    public void save(Location<World> location, DataKey root) {
        if (location.getExtent() != null) {
            root.setString("world", location.getExtent().getName());
        }
        root.setDouble("x", round(location.getX()));
        root.setDouble("y", round(location.getY()));
        root.setDouble("z", round(location.getZ()));
        root.setDouble("yaw", location.getYaw());
        root.setDouble("pitch", location.getPitch());
    }

    public static class LazilyLoadedLocation extends Location<World> {
        private final String worldName;

        public LazilyLoadedLocation(String world, double x, double y, double z, float yaw, float pitch) {
            super(null, x, y, z, yaw, pitch);
            this.worldName = world;
        }

        @Override
        public World getWorld() {
            if (super.getWorld() == null) {
                super.setWorld(Sponge.getServer().getWorld(this.worldName).get());
            }
            return super.getWorld();
        }
    }
}
