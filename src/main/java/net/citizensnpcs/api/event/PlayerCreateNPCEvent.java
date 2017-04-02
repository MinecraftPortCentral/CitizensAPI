package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;

public class PlayerCreateNPCEvent extends CommandSenderCreateNPCEvent implements Cancellable {
    public PlayerCreateNPCEvent(Player player, NPC npc) {
        super(player, npc);
    }

    /**
     * @return The {@link Player} creating the NPC.
     */
    @Override
    public Player getCreator() {
        return (Player) super.getCreator();
    }
}
