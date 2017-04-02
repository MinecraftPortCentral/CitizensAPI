package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.event.cause.Cause;

/**
 * Represents an event thrown by an NPC.
 */
public abstract class NPCEvent extends CitizensEvent {
    final NPC npc;

    protected NPCEvent(NPC npc, Cause cause) {
        super(cause);
        this.npc = npc;
    }

    /**
     * Get the npc involved in the event.
     * 
     * @return the npc involved in the event
     */
    public NPC getNPC() {
        return npc;
    }
}