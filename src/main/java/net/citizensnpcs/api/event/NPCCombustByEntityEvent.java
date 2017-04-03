package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;

public class NPCCombustByEntityEvent extends NPCCombustEvent {
    private final EntityDamageSource entityDamageSource;

    public NPCCombustByEntityEvent(EntityDamageSource damageSource, NPC npc) {
        super(damageSource, npc);
        this.entityDamageSource = damageSource;
    }

    /**
     * The combuster can be a WeatherStorm a Blaze, or an Entity holding a FIRE_ASPECT enchanted item.
     * 
     * @return the Entity that set the combustee alight.
     */
    public Entity getCombuster() {
        return this.entityDamageSource.getSource();
    }
}
