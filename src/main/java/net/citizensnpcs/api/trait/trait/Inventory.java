package net.citizensnpcs.api.trait.trait;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;
import net.citizensnpcs.api.util.ItemStorage;
import org.spongepowered.api.entity.living.animal.Horse;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.inventory.ItemStack;

/**
 * Represents an NPC's inventory.
 */
@TraitName("inventory")
public class Inventory extends Trait {
    private ItemStack[] contents;
    private org.spongepowered.api.item.inventory.Inventory view;
    private final Set<InventoryView> views = new HashSet<InventoryView>();

    public Inventory() {
        super("inventory");
        this.contents = new ItemStack[72];
    }

    /**
     * Gets the contents of an NPC's inventory.
     *
     * @return ItemStack array of an NPC's inventory contents
     */
    public ItemStack[] getContents() {
        if (this.view != null) {
            for (int i = 0; i < this.view.getSize(); i++) {
                this.view.setItem(i, this.contents[i]);
            }
            return this.view.getContents();
        }
        return this.contents;
    }

    @Listener
    public void inventoryCloseEvent(InteractInventoryEvent.Close event) {
        if (!this.views.contains(event.getView()))
            return;
        ItemStack[] contents = event.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            this.contents[i] = contents[i];
        }
        if (this.npc.getEntity() instanceof InventoryHolder) {
            int maxSize = ((InventoryHolder) this.npc.getEntity()).getInventory().getStorageContents().length;
            ((InventoryHolder) this.npc.getEntity()).getInventory().setStorageContents(Arrays.copyOf(contents, maxSize));
        }
        this.views.remove(event.getView());
    }

    @Override
    public void load(DataKey key) throws NPCLoadException {
        this.contents = parseContents(key);
    }

    @Override
    public void onSpawn() {
        setContents(this.contents);
        int size = this.npc.getEntity() instanceof Player ? 36
                                                          : this.npc.getEntity() instanceof InventoryHolder
                  ? ((InventoryHolder) this.npc.getEntity()).getInventory().getSize() : this.contents.length;
        int rem = size % 9;
        if (rem != 0) {
            size += 9 - rem; // round up to nearest multiple of 9
        }
        this.view = Bukkit.createInventory(
            this.npc.getEntity() instanceof InventoryHolder ? ((InventoryHolder) this.npc.getEntity()) : null, size,
            this.npc.getName() + "'s Inventory");
    }

    public void openInventory(Player sender) {
        for (int i = 0; i < this.view.getSize(); i++) {
            this.view.setItem(i, this.contents[i]);
        }
        this.views.add(sender.openInventory(this.view));
    }

    private ItemStack[] parseContents(DataKey key) throws NPCLoadException {
        ItemStack[] contents = new ItemStack[72];
        for (DataKey slotKey : key.getIntegerSubKeys()) {
            contents[Integer.parseInt(slotKey.name())] = ItemStorage.loadItemStack(slotKey);
        }
        return contents;
    }

    @Override
    public void run() {
        if (this.npc.getEntity() instanceof Player) {
            this.contents = ((Player) this.npc.getEntity()).getInventory().getContents();
        }
        Iterator<InventoryView> itr = this.views.iterator();
        while (itr.hasNext()) {
            InventoryView iview = itr.next();
            if (!iview.getPlayer().isValid()) {
                iview.close();
                itr.remove();
            }
        }
    }

    @Override
    public void save(DataKey key) {
        int slot = 0;
        for (ItemStack item : this.contents) {
            // Clear previous items to avoid conflicts
            key.removeKey(String.valueOf(slot));
            if (item != null) {
                ItemStorage.saveItem(key.getRelative(String.valueOf(slot)), item);
            }
            slot++;
        }
    }

    /**
     * Sets the contents of an NPC's inventory.
     *
     * @param contents
     *            ItemStack array to set as the contents of an NPC's inventory
     */
    public void setContents(ItemStack[] contents) {
        this.contents = Arrays.copyOf(contents, 72);
        org.spongepowered.api.item.inventory.Inventory dest = null;
        int maxCopySize = -1;
        if (this.npc.getEntity() instanceof Player) {
            dest = ((Player) this.npc.getEntity()).getInventory();
            maxCopySize = 36;
        } else if (this.npc.getEntity() instanceof StorageMinecart) {
            dest = ((StorageMinecart) this.npc.getEntity()).getInventory();
        } else if (this.npc.getEntity() instanceof Horse) {
            dest = ((Horse) this.npc.getEntity()).getInventory();
        }

        if (dest == null)
            return;
        if (maxCopySize == -1) {
            maxCopySize = dest.getSize();
        }

        for (int i = 0; i < maxCopySize; i++) {
            dest.setItem(i, contents[i]);
        }
    }

    @Override
    public String toString() {
        return "Inventory{" + Arrays.toString(this.contents) + "}";
    }
}
