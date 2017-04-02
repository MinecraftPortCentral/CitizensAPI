package net.citizensnpcs.api.ai.event;

import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;

public abstract class NavigationEvent extends AbstractEvent {
    private final Navigator navigator;
    private final Cause cause;
    private boolean isCancelled = false;

    protected NavigationEvent(Navigator navigator, Cause cause) {
        this.navigator = navigator;
        this.cause = cause;
    }

    /**
     * @return The {@link Navigator} involved in this event
     */
    public Navigator getNavigator() {
        return navigator;
    }

    /**
     * @return The {@link NPC} involved in this event
     */
    public NPC getNPC() {
        return navigator.getNPC();
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }
}
