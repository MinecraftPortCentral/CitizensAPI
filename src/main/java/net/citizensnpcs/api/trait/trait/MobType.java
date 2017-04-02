package net.citizensnpcs.api.trait.trait;

import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;

/**
 * Represents an NPC's mob type.
 */
@TraitName("type")
public class MobType extends Trait {
    private EntityType type = EntityTypes.PLAYER;

    public MobType() {
        super("type");
    }

    /**
     * Gets the type of mob that an NPC is.
     *
     * @return The mob type
     */
    public EntityType getType() {
        return type;
    }

    @Override
    public void load(DataKey key) {
        type = Sponge.getRegistry().getType(EntityType.class, key.getString("")).orElse(EntityTypes.PLAYER);
    }

    @Override
    public void onSpawn() {
        type = npc.getEntity().getType();
    }

    @Override
    public void save(DataKey key) {
        key.setString("", type.getId());
    }

    /**
     * Sets the type of mob that an NPC is.
     *
     * @param type
     *            Mob type to set the NPC as
     */
    public void setType(EntityType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MobType{" + type + "}";
    }
}