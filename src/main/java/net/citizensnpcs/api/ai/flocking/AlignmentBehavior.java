package net.citizensnpcs.api.ai.flocking;

import java.util.Collection;

import com.flowpowered.math.vector.Vector3d;
import net.citizensnpcs.api.npc.NPC;

public class AlignmentBehavior implements FlockBehavior {
    private final double weight;

    public AlignmentBehavior(double weight) {
        this.weight = weight;
    }

    @Override
    public Vector3d getVector(NPC npc, Collection<NPC> nearby) {
        Vector3d velocities = new Vector3d(0, 0, 0);
        for (NPC neighbor : nearby) {
            if (!neighbor.isSpawned())
                continue;
            velocities = velocities.add(neighbor.getEntity().getVelocity());
        }
        Vector3d desired = velocities.mul((double) 1 / nearby.size());
        return desired.sub(npc.getEntity().getVelocity()).mul(weight);
    }
}
