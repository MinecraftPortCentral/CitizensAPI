package net.citizensnpcs.api.ai.flocking;

import java.util.Collection;

import com.flowpowered.math.vector.Vector3d;
import net.citizensnpcs.api.npc.NPC;

public class CohesionBehavior implements FlockBehavior {
    private final double weight;

    public CohesionBehavior(double weight) {
        this.weight = weight;
    }

    @Override
    public Vector3d getVector(NPC npc, Collection<NPC> nearby) {
        Vector3d positions = new Vector3d(0, 0, 0);
        for (NPC neighbor : nearby) {
            if (!neighbor.isSpawned())
                continue;
            positions = positions.add(neighbor.getEntity().getLocation().getPosition());
        }
        Vector3d center = positions.mul((double) 1 / nearby.size());
        return npc.getEntity().getLocation().getPosition().sub(center).mul(weight);
    }

}
