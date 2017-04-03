package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;

public class NPCDamageEntityEvent extends NPCDamageEvent {

    public NPCDamageEntityEvent(NPC npc, EntityDamageSource damageSource) {
        super(npc, damageSource);
    }

    public EntityDamageSource getDamageSource() {
        
    }
}
