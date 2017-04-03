package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.entity.Entity;

public class NPCCollisionEvent extends NPCEvent {
    private final Entity entity;

    public NPCCollisionEvent(NPC npc, Entity entity) {
        super(npc);
        this.entity = entity;
    }

    /**
     * Returns the {@link Entity} that collided with the {@link NPC}.
     * 
     * @return The collided entity
     */
    public Entity getCollidedWith() {
        return entity;
    }
}
