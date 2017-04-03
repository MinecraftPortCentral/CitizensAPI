package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import org.spongepowered.api.command.CommandSource;

public class NPCTraitCommandAttachEvent extends NPCEvent {
    private final CommandSource sender;
    private final Class<? extends Trait> traitClass;

    public NPCTraitCommandAttachEvent(NPC npc, Class<? extends Trait> traitClass, CommandSource sender) {
        super(npc);
        this.traitClass = traitClass;
        this.sender = sender;
    }

    public CommandSource getCommandSender() {
        return sender;
    }

    public Class<? extends Trait> getTraitClass() {
        return traitClass;
    }

}
