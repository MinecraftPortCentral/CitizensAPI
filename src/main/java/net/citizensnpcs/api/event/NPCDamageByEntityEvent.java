package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.entity.Entity;

public class NPCDamageByEntityEvent extends NPCDamageEvent {
    private final Entity damager;

    public NPCDamageByEntityEvent(NPC npc, EntityDamageByEntityEvent event) {
        super(npc, event);
        damager = event.getDamager();
    }

    public Entity getDamager() {
        return damager;
    }
}
