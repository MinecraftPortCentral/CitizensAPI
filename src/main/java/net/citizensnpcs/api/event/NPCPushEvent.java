package net.citizensnpcs.api.event;

import com.flowpowered.math.vector.Vector3d;
import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;

public class NPCPushEvent extends NPCEvent implements Cancellable {
    private boolean cancelled;
    private Vector3d collisionVector;

    public NPCPushEvent(NPC npc, Vector3d vector, Cause cause) {
        super(npc, cause);
        this.collisionVector = vector;
    }

    /**
     * Return the collision {@link Vector} being applied to the NPC.
     * 
     * @return The collision vector
     */
    public Vector3d getCollisionVector() {
        return collisionVector;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;

    }

    /**
     * Sets the collision {@link Vector} to be applied to the NPC.
     * 
     * @param vector
     *            The new collision vector
     */
    public void setCollisionVector(Vector3d vector) {
        this.collisionVector = vector;
    }

}
