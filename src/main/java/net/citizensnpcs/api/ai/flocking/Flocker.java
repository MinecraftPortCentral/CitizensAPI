package net.citizensnpcs.api.ai.flocking;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.flowpowered.math.vector.Vector3d;
import net.citizensnpcs.api.npc.NPC;

public class Flocker implements Runnable {
    private final List<FlockBehavior> behaviors;
    private final NPCFlock flock;
    private double maxForce = 1.5;
    private final NPC npc;

    public Flocker(NPC npc, NPCFlock flock, FlockBehavior... behaviors) {
        this.npc = npc;
        this.flock = flock;
        this.behaviors = Arrays.asList(behaviors);
    }

    @Override
    public void run() {
        Collection<NPC> nearby = flock.getNearby(npc);
        if (nearby.isEmpty())
            return;
        Vector3d base = new Vector3d(0, 0, 0);
        for (FlockBehavior behavior : behaviors) {
            base.add(behavior.getVector(npc, nearby));
        }
        base = clip(maxForce, base);
        npc.getEntity().setVelocity(npc.getEntity().getVelocity().add(base));
    }

    public void setMaxForce(double maxForce) {
        this.maxForce = maxForce;
    }

    private static Vector3d clip(double max, Vector3d vector) {
        if (vector.length() > max) {
            return vector.normalize().mul(max);
        }
        return vector;
    }

    public static double HIGH_INFLUENCE = 1.0 / 20.0;
    public static double LOW_INFLUENCE = 1.0 / 200.0;
}
