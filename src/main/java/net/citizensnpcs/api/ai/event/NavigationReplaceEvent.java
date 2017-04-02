package net.citizensnpcs.api.ai.event;

import net.citizensnpcs.api.ai.Navigator;
import org.spongepowered.api.event.cause.Cause;

public class NavigationReplaceEvent extends NavigationCancelEvent {
    public NavigationReplaceEvent(Navigator navigator, Cause cause) {
        super(navigator, cause, CancelReason.REPLACE);
    }
}
