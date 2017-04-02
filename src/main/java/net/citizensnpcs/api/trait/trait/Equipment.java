package net.citizensnpcs.api.trait.trait;

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;
import net.citizensnpcs.api.util.ItemStorage;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumHand;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.ArmorEquipable;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.monster.Enderman;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

/**
 * Represents an NPC's equipment. This only is applicable to human and enderman NPCs.
 */
@TraitName("equipment")
public class Equipment extends Trait {
    private final ItemStack[] equipment = new ItemStack[6];

    public Equipment() {
        super("equipment");
    }

    /**
     * @see #get(int)
     */
    public ItemStack get(EquipmentSlot slot) {
        return get(slot.getIndex());
    }

    /**
     * Get an NPC's equipment from the given slot.
     *
     * @param slot
     *            Slot where the armor is located (0-5)
     * @return ItemStack from the given armor slot
     */
    public ItemStack get(int slot) {
        if (npc.getEntity() instanceof Enderman && slot != 0)
            throw new IllegalArgumentException("Slot must be 0 for enderman");
        else if (slot < 0 || slot > 5)
            throw new IllegalArgumentException("Slot must be between 0 and 5");

        return equipment[slot];
    }

    /**
     * Get all of an NPC's equipment.
     *
     * @return An array of an NPC's equipment
     */
    public ItemStack[] getEquipment() {
        return equipment;
    }

    /**
     * Get all of the equipment as a {@link Map}.
     *
     * @return A mapping of slot to item
     */
    public Map<EquipmentSlot, ItemStack> getEquipmentBySlot() {
        Map<EquipmentSlot, ItemStack> map = Maps.newEnumMap(EquipmentSlot.class);
        map.put(EquipmentSlot.HAND, equipment[0]);
        map.put(EquipmentSlot.HELMET, equipment[1]);
        map.put(EquipmentSlot.CHESTPLATE, equipment[2]);
        map.put(EquipmentSlot.LEGGINGS, equipment[3]);
        map.put(EquipmentSlot.BOOTS, equipment[4]);
        map.put(EquipmentSlot.OFF_HAND, equipment[5]);
        return map;
    }

    private ArmorEquipable getEquipmentFromEntity(Entity entity) {
        if (entity instanceof Player)
            return new PlayerEquipmentWrapper((Player) entity);
        else if (entity instanceof Living)
            return ((Living) entity).getEquipment();
        else if (entity instanceof ArmorStand)
            return new ArmorStandEquipmentWrapper((ArmorStand) entity);
        throw new RuntimeException("Unsupported entity equipment");
    }

    @Override
    public void load(DataKey key) throws NPCLoadException {
        if (key.keyExists("hand")) {
            equipment[0] = ItemStorage.loadItemStack(key.getRelative("hand"));
        }
        if (key.keyExists("helmet")) {
            equipment[1] = ItemStorage.loadItemStack(key.getRelative("helmet"));
        }
        if (key.keyExists("chestplate")) {
            equipment[2] = ItemStorage.loadItemStack(key.getRelative("chestplate"));
        }
        if (key.keyExists("leggings")) {
            equipment[3] = ItemStorage.loadItemStack(key.getRelative("leggings"));
        }
        if (key.keyExists("boots")) {
            equipment[4] = ItemStorage.loadItemStack(key.getRelative("boots"));
        }
        if (key.keyExists("offhand")) {
            equipment[5] = ItemStorage.loadItemStack(key.getRelative("offhand"));
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onSpawn() {
        if (!(npc.getEntity() instanceof Living) && !(npc.getEntity() instanceof ArmorStand))
            return;
        if (npc.getEntity() instanceof Enderman) {
            Enderman enderman = (Enderman) npc.getEntity();
            if (equipment[0] != null) {
                enderman.setCarriedMaterial(equipment[0].getData());
            }
        } else {
            ArmorEquipable equip = getEquipmentFromEntity(npc.getEntity());
            if (equipment[0] != null) {
                equip.setItemInHand(HandTypes.MAIN_HAND, equipment[0]);
            }
            equip.setHelmet(equipment[1]);
            equip.setChestplate(equipment[2]);
            equip.setLeggings(equipment[3]);
            equip.setBoots(equipment[4]);
            equip.setItemInHand(HandTypes.OFF_HAND, equipment[5]);
        }
        if (npc.getEntity() instanceof Player) {
            ((Player) npc.getEntity()).updateInventory();
        }
    }

    @Override
    public void save(DataKey key) {
        saveOrRemove(key.getRelative("hand"), equipment[0]);
        saveOrRemove(key.getRelative("helmet"), equipment[1]);
        saveOrRemove(key.getRelative("chestplate"), equipment[2]);
        saveOrRemove(key.getRelative("leggings"), equipment[3]);
        saveOrRemove(key.getRelative("boots"), equipment[4]);
        saveOrRemove(key.getRelative("offhand"), equipment[5]);
    }

    private void saveOrRemove(DataKey key, ItemStack item) {
        if (item != null) {
            ItemStorage.saveItem(key, item);
        } else {
            if (key.keyExists("")) {
                key.removeKey("");
            }
        }
    }

    /**
     * @see #set(int, ItemStack)
     */
    public void set(EquipmentSlot slot, ItemStack item) {
        set(slot.getIndex(), item);
    }

    /**
     * Set the armor from the given slot as the given item.
     *
     * @param slot
     *            Slot of the armor (must be between 0 and 5)
     * @param item
     *            Item to set the armor as
     */
    @SuppressWarnings("deprecation")
    public void set(int slot, ItemStack item) {
        if (!(npc.getEntity() instanceof Living) && !(npc.getEntity() instanceof ArmorStand))
            return;
        if (npc.getEntity() instanceof Enderman) {
            if (slot != 0)
                throw new UnsupportedOperationException("Slot can only be 0 for enderman");
            ((Enderman) npc.getEntity()).setCarriedMaterial(item.getData());
        } else {
            EntityEquipment equip = getEquipmentFromEntity(npc.getEntity());
            switch (slot) {
                case 0:
                    equip.setItemInMainHand(item);
                    break;
                case 1:
                    equip.setHelmet(item);
                    break;
                case 2:
                    equip.setChestplate(item);
                    break;
                case 3:
                    equip.setLeggings(item);
                    break;
                case 4:
                    equip.setBoots(item);
                    break;
                case 5:
                    equip.setItemInOffHand(item);
                    break;
                default:
                    throw new IllegalArgumentException("Slot must be between 0 and 5");
            }

            equipment[slot] = item;
        }
        if (npc.getEntity() instanceof Player) {
            ((Player) npc.getEntity()).updateInventory();
        }
    }

    @Override
    public String toString() {
        return "{hand=" + equipment[0] + ",helmet=" + equipment[1] + ",chestplate=" + equipment[2] + ",leggings="
                + equipment[3] + ",boots=" + equipment[4] + ",offhand=" + equipment[5] + "}";
    }

    private static class ArmorStandEquipmentWrapper implements ArmorEquipable {
        private final ArmorStand entity;
        private final EntityArmorStand mcArmorStand;

        public ArmorStandEquipmentWrapper(ArmorStand entity) {
            this.entity = entity;
            this.mcArmorStand = (EntityArmorStand) entity;
        }

        @Override
        public void clear() {
            entity.setItemInHand(HandTypes.MAIN_HAND, null);
            entity.setChestplate(null);
            entity.setHelmet(null);
            entity.setBoots(null);
            entity.setLeggings(null);
        }

        public ItemStack[] getArmorContents() {
            return new ItemStack[] { entity.getHelmet().orElse(null), entity.getChestplate().orElse(null), entity.getLeggings().orElse(null),
                    entity.getBoots().orElse(null) };
        }

        @Override
        public Optional<ItemStack> getBoots() {
            return entity.getBoots();
        }

        public float getBootsDropChance() {
            return 0;
        }

        @Override
        public Optional<ItemStack> getChestplate() {
            return entity.getChestplate();
        }

        public float getChestplateDropChance() {
            return 0;
        }

        @Override
        public Optional<ItemStack> getHelmet() {
            return entity.getHelmet();
        }

        public float getHelmetDropChance() {
            return 0;
        }

        public Entity getHolder() {
            return entity;
        }

        public Optional<ItemStack> getItemInHand(HandType handType) {
            return entity.getItemInHand(handType);
        }

        public float getItemInHandDropChance(HandType handType) {
            return 0;
        }

        @Override
        public Optional<ItemStack> getLeggings() {
            return entity.getLeggings();
        }

        public float getLeggingsDropChance() {
            return 0;
        }

        public void setArmorContents(ItemStack[] arg0) {
            entity.setHelmet(arg0[EquipmentSlot.HELMET.index - 1]);
            entity.setChestplate(arg0[EquipmentSlot.CHESTPLATE.index - 1]);
            entity.setLeggings(arg0[EquipmentSlot.LEGGINGS.index - 1]);
            entity.setBoots(arg0[EquipmentSlot.BOOTS.index - 1]);
        }

        @Override
        public void setBoots(ItemStack arg0) {
            entity.setBoots(arg0);
        }

        public void setBootsDropChance(float arg0) {
        }

        @Override
        public void setChestplate(ItemStack arg0) {
            entity.setChestplate(arg0);
        }

        public void setChestplateDropChance(float arg0) {
        }

        @Override
        public void setHelmet(ItemStack arg0) {
            entity.setHelmet(arg0);
        }

        public void setHelmetDropChance(float arg0) {
        }

        @Override
        public void setItemInHand(HandType handType, ItemStack arg0) {
            entity.setItemInHand(handType, arg0);
        }

        public void setItemInHandDropChance(HandType handType, float arg0) {
        }

        @Override
        public void setLeggings(ItemStack arg0) {
            entity.setLeggings(arg0);
        }

        public void setLeggingsDropChance(float arg0) {
        }
    }

    public enum EquipmentSlot {
        BOOTS(4),
        CHESTPLATE(2),
        HAND(0),
        HELMET(1),
        LEGGINGS(3),
        OFF_HAND(5);
        private int index;

        EquipmentSlot(int index) {
            this.index = index;
        }

        int getIndex() {
            return index;
        }
    }

    private static class PlayerEquipmentWrapper implements ArmorEquipable {
        private final Player player;

        private PlayerEquipmentWrapper(Player player) {
            this.player = player;
        }

        @Override
        public void clear() {
            player.getInventory().clear();
        }

        @Override
        public ItemStack[] getArmorContents() {
            return player.getInventory().getArmorContents();
        }

        @Override
        public ItemStack getBoots() {
            return player.getInventory().getBoots();
        }

        @Override
        public float getBootsDropChance() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ItemStack getChestplate() {
            return player.getInventory().getChestplate();
        }

        @Override
        public float getChestplateDropChance() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ItemStack getHelmet() {
            return player.getInventory().getHelmet();
        }

        @Override
        public float getHelmetDropChance() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Entity getHolder() {
            return player;
        }

        @Override
        public ItemStack getItemInHand() {
            return player.getItemInHand();
        }

        @Override
        public float getItemInHandDropChance() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ItemStack getItemInMainHand() {
            return player.getInventory().getItemInMainHand();
        }

        @Override
        public float getItemInMainHandDropChance() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ItemStack getItemInOffHand() {
            return player.getInventory().getItemInOffHand();
        }

        @Override
        public float getItemInOffHandDropChance() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ItemStack getLeggings() {
            return player.getInventory().getLeggings();
        }

        @Override
        public float getLeggingsDropChance() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setArmorContents(ItemStack[] items) {
            player.getInventory().setArmorContents(items);
        }

        @Override
        public void setBoots(ItemStack boots) {
            player.getInventory().setBoots(boots);
        }

        @Override
        public void setBootsDropChance(float chance) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setChestplate(ItemStack chestplate) {
            player.getInventory().setChestplate(chestplate);
        }

        @Override
        public void setChestplateDropChance(float chance) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setHelmet(ItemStack helmet) {
            player.getInventory().setHelmet(helmet);
        }

        @Override
        public void setHelmetDropChance(float chance) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setItemInHand(ItemStack stack) {
            player.setItemInHand(stack);
        }

        @Override
        public void setItemInHandDropChance(float chance) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setItemInMainHand(ItemStack arg0) {
            player.getInventory().setItemInMainHand(arg0);
        }

        @Override
        public void setItemInMainHandDropChance(float arg0) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setItemInOffHand(ItemStack arg0) {
            player.getInventory().setItemInOffHand(arg0);
        }

        @Override
        public void setItemInOffHandDropChance(float arg0) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setLeggings(ItemStack leggings) {
            player.getInventory().setLeggings(leggings);
        }

        @Override
        public void setLeggingsDropChance(float chance) {
            throw new UnsupportedOperationException();
        }
    }
}