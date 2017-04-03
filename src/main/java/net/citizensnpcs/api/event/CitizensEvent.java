package net.citizensnpcs.api.event;

import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;

/**
 * Represents an event thrown by Citizens.
 */
public abstract class CitizensEvent extends AbstractEvent {
    private final Cause cause;

    protected CitizensEvent(Cause cause) {
        this.cause = cause;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }
}