package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * Called when an NPC spawns.
 */
public class NPCSpawnEvent extends NPCEvent implements Cancellable {
    private boolean cancelled = false;

    private final Location<World> location;

    public NPCSpawnEvent(NPC npc, Location<World> location) {
        super(npc);
        this.location = location;
    }

    /**
     * Gets the location where the NPC was spawned.
     * 
     * @return Location where the NPC was spawned
     */
    public Location<World> getLocation() {
        return location;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}