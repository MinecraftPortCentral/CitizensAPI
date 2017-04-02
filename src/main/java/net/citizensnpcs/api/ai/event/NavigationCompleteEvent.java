package net.citizensnpcs.api.ai.event;

import net.citizensnpcs.api.ai.Navigator;
import org.spongepowered.api.event.cause.Cause;

public class NavigationCompleteEvent extends NavigationEvent {
    public NavigationCompleteEvent(Navigator navigator, Cause cause) {
        super(navigator, cause);
    }
}
