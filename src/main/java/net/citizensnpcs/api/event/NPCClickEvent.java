package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;

/**
 * Represents an event where an NPC was clicked by a player.
 */
public abstract class NPCClickEvent extends NPCEvent implements Cancellable {
    private boolean cancelled = false;
    private final Player clicker;

    protected NPCClickEvent(NPC npc, Player clicker) {
        super(npc);
        this.clicker = clicker;
    }

    /**
     * Gets the player that clicked the NPC.
     *
     * @return Player that clicked the NPC
     */
    public Player getClicker() {
        return clicker;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}