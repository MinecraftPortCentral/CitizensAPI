package net.citizensnpcs.api.event;

import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.event.cause.entity.damage.source.BlockDamageSource;

public class NPCCombustByBlockEvent extends NPCCombustEvent {
    private final BlockDamageSource blockDamageSource;

    public NPCCombustByBlockEvent(BlockDamageSource blockDamageSource, NPC npc) {
        super(blockDamageSource, npc);
        this.blockDamageSource = blockDamageSource;
    }

    /**
     * The combuster can be lava or a block that is on fire.
     * <p />
     * WARNING: block may be null.
     * 
     * @return the Block that set the combustee alight.
     */
    public BlockDamageSource getCombuster() {
        return this.blockDamageSource;
    }
}
