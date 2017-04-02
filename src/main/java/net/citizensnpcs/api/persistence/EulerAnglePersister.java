package net.citizensnpcs.api.persistence;

import com.flowpowered.math.vector.Vector3d;
import net.citizensnpcs.api.util.DataKey;

public class EulerAnglePersister implements Persister<Vector3d> {
    @Override
    public Vector3d create(DataKey root) {
        double x = root.getDouble("x");
        double y = root.getDouble("y");
        double z = root.getDouble("z");
        return new Vector3d(x, y, z);
    }

    @Override
    public void save(Vector3d angle, DataKey root) {
        root.setDouble("x", angle.getX());
        root.setDouble("y", angle.getY());
        root.setDouble("z", angle.getZ());
    }
}
