package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.Cancellable;

public class CommandSenderCreateNPCEvent extends NPCCreateEvent implements Cancellable {
    private boolean cancelled;
    private final CommandSource creator;
    private String reason;

    public CommandSenderCreateNPCEvent(CommandSource sender, NPC npc) {
        super(npc);
        creator = sender;
    }

    /**
     * @return The reason for cancelling the event
     * @see #getCancelReason()
     */
    public String getCancelReason() {
        return reason;
    }

    /**
     * @return The {@link CommandSender} creating the NPC.
     */
    public CommandSource getCreator() {
        return creator;
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
     * Sets the reason for cancelling the event. This will be sent to the {@link CommandSender} creator to explain why
     * the NPC cannot be created.
     * 
     * @param reason
     *            The reason explaining the cancellation
     */
    public void setCancelReason(String reason) {
        this.reason = reason;
    }

}
