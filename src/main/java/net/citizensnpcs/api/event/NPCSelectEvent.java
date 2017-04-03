package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.command.CommandSource;

/**
 * Called when an NPC is selected by a player.
 */
public class NPCSelectEvent extends NPCEvent {
    private final CommandSource sender;

    public NPCSelectEvent(NPC npc, CommandSource sender) {
        super(npc);
        this.sender = sender;
    }

    /**
     * Gets the selector of the NPC.
     * 
     * @return CommandSource that selected an NPC
     */
    public CommandSource getSelector() {
        return sender;
    }
}