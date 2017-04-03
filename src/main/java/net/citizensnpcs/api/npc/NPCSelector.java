package net.citizensnpcs.api.npc;

import org.spongepowered.api.command.CommandSource;

public interface NPCSelector {
    NPC getSelected(CommandSource sender);
}
