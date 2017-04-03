package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * Called when an NPC teleports.
 */
public class NPCTeleportEvent extends NPCEvent implements Cancellable {
    private boolean cancelled;
    private final Location<World> to;

    public NPCTeleportEvent(NPC npc, Location<World> to) {
        super(npc);
        this.to = to;
    }

    public Location<World> getFrom() {
        return npc.getStoredLocation();
    }

    public Location<World> getTo() {
        return to;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}