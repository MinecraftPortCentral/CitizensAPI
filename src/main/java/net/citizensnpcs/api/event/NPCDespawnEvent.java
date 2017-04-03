package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.event.Cancellable;

/**
 * Called when an NPC despawns.
 */
public class NPCDespawnEvent extends NPCEvent implements Cancellable {
    private boolean cancelled;
    private final DespawnReason reason;

    public NPCDespawnEvent(NPC npc, DespawnReason reason) {
        super(npc);
        this.reason = reason;
    }

    public DespawnReason getReason() {
        return reason;
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