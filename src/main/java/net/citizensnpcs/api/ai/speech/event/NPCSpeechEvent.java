package net.citizensnpcs.api.ai.speech.event;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.speech.SpeechContext;
import net.citizensnpcs.api.event.NPCEvent;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;

/**
 * Represents an event where an NPC speaks, with {@link SpeechContext}. This event takes place before being sent to the
 * {@link VocalChord}.
 * 
 */
public class NPCSpeechEvent extends NPCEvent implements Cancellable {

    private boolean cancelled = false;

    private SpeechContext context;
    private String vocalChordName;

    public NPCSpeechEvent(SpeechContext context, String vocalChordName, Cause cause) {
        super(CitizensAPI.getNPCRegistry().getNPC(context.getTalker().getEntity()), cause);
        this.vocalChordName = vocalChordName;
        this.context = context;
    }

    /**
     * Returns the {@link SpeechContext} that will be sent to the VocalChord.
     * 
     * @return the SpeechContext
     */
    public SpeechContext getContext() {
        return context;
    }

    /**
     * @return the name of the VocalChord that will be used.
     */
    public String getVocalChordName() {
        return vocalChordName;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Sets the name of the {@link VocalChord} to be used.
     * 
     * @param vocalChordName
     *            A valid registered VocalChord name
     */
    public void setVocalChord(String name) {
        this.vocalChordName = name;
    }
}