package net.citizensnpcs.api.ai.event;

import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.ai.StuckAction;

import org.spongepowered.api.event.cause.Cause;

public class NavigationStuckEvent extends NavigationEvent {
    private StuckAction action;

    public NavigationStuckEvent(Navigator navigator, Cause cause, StuckAction action) {
        super(navigator, cause);
        this.action = action;
    }

    public StuckAction getAction() {
        return action;
    }

    public void setAction(StuckAction action) {
        this.action = action;
    }
}
