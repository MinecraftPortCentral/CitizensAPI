package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.Cancellable;

public class EntityTargetNPCEvent extends NPCEvent implements Cancellable {
    private boolean cancelled;

    private final EntityTargetEvent event;

    public EntityTargetNPCEvent(EntityTargetEvent event, NPC npc) {
        super(npc);
        this.event = event;
    }

    /**
     * Returns the Entity involved in this event
     * 
     * @return Entity who is involved in this event
     */
    public Entity getEntity() {
        return event.getEntity();
    }

    /**
     * Returns the reason for the targeting
     * 
     * @return The reason
     */
    public TargetReason getReason() {
        return event.getReason();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
