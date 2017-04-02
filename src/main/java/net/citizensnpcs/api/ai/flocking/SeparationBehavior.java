package net.citizensnpcs.api.ai.flocking;

import java.util.Collection;

import com.flowpowered.math.vector.Vector3d;
import net.citizensnpcs.api.npc.NPC;

public class SeparationBehavior implements FlockBehavior {
    private final double weight;

    public SeparationBehavior(double weight) {
        this.weight = weight;
    }

    @Override
    public Vector3d getVector(NPC npc, Collection<NPC> nearby) {
        Vector3d steering = new Vector3d(0, 0, 0);
        Vector3d pos = npc.getEntity().getLocation().getPosition();
        int c = 0;
        for (NPC neighbor : nearby) {
            if (!neighbor.isSpawned())
                continue;
            double dist = neighbor.getEntity().getLocation().getPosition().distance(pos);
            Vector3d repulse = pos.sub(neighbor.getEntity().getLocation().getPosition()).normalize()
                    .div(new Vector3d(dist, dist, dist));
            steering = repulse.add(steering);
            c++;
        }
        steering = steering.div(new Vector3d(c, c, c));
        return steering.sub(npc.getEntity().getVelocity()).mul(this.weight);
    }
}
