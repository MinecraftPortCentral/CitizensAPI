package net.citizensnpcs.api.ai.flocking;

import java.util.Collection;

import com.flowpowered.math.vector.Vector3d;
import net.citizensnpcs.api.npc.NPC;

public interface FlockBehavior {
    Vector3d getVector(NPC npc, Collection<NPC> nearby);
}
