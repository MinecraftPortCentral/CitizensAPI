package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;

public class NPCDamageEvent extends NPCEvent implements Cancellable {
    private final DamageSource damageSource;
    private boolean isCancelled = false;

    public NPCDamageEvent(NPC npc, DamageSource damageSource) {
        super(npc);
        this.damageSource = damageSource;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    public void setDamage(int damage) {
        event.setDamage(damage);
    }
}
