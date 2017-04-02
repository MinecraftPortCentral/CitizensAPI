package net.citizensnpcs.api.ai.event;

import net.citizensnpcs.api.ai.Navigator;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;

public class NavigationCancelEvent extends NavigationCompleteEvent implements Cancellable {
    private boolean isCancelled = false;
    private CancelReason reason;

    public NavigationCancelEvent(Navigator navigator, Cause cause, CancelReason reason) {
        super(navigator, cause);
        this.reason = reason;
    }

    public CancelReason getCancelReason() {
        return this.reason;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
}
