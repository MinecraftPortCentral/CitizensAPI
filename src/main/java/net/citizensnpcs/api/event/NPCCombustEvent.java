package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;

public class NPCCombustEvent extends NPCEvent implements Cancellable {
    private boolean cancelled;
    private final DamageSource damageSource;

    public NPCCombustEvent(DamageSource damageSource, NPC npc) {
        super(npc, Cause.of(NamedCause.source(damageSource)));
        this.damageSource = damageSource;
    }

    /**
     * @return the amount of time (in seconds) the combustee should be alight for
     */
    public int getDuration() {
        // TODO
        return 0;//event.getDuration();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * The number of seconds the combustee should be alight for.
     * <p />
     * This value will only ever increase the combustion time, not decrease existing combustion times.
     * 
     * @param duration
     *            the time in seconds to be alight for.
     */
    public void setDuration(int duration) {
        // TODO
        // event.setDuration(duration);
    }
}
