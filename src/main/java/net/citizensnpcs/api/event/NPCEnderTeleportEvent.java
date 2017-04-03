package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.event.Cancellable;

/**
 * Called when an NPC is teleported after using an ender pearl.
 */
public class NPCEnderTeleportEvent extends NPCEvent implements Cancellable {
    private boolean cancelled;

    public NPCEnderTeleportEvent(NPC npc) {
        super(npc);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean arg0) {
        this.cancelled = arg0;
    }
}