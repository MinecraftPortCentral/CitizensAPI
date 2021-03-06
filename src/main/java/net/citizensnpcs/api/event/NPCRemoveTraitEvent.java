package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;

import org.bukkit.event.HandlerList;

public class NPCRemoveTraitEvent extends NPCTraitEvent {
    public NPCRemoveTraitEvent(NPC npc, Trait trait) {
        super(npc, trait);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
