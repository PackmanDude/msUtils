package com.github.minersstudios.msutils.anomalies;

import com.github.minersstudios.mscore.utils.ItemUtils;
import com.github.minersstudios.msutils.MSUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public class AnomalyIgnorableItems {
    private final @NotNull Map<EquipmentSlot, ItemStack> includedItems;
    private final int breakingPerAction;

    public AnomalyIgnorableItems(
            @NotNull Map<EquipmentSlot, ItemStack> includedItems,
            int breakingPerAction
    ) {
        this.includedItems = includedItems;
        this.breakingPerAction = breakingPerAction;
    }

    @Contract("null, null -> false")
    public boolean isIgnorableItem(@Nullable EquipmentSlot equipmentSlot, @Nullable ItemStack item) {
        if (equipmentSlot == null || item == null) return false;
        ItemStack ignorableItem = this.includedItems.get(equipmentSlot);
        return ignorableItem == null
                || item.getType() == ignorableItem.getType()
                && item.getItemMeta().getCustomModelData() == ignorableItem.getItemMeta().getCustomModelData();
    }

    public boolean hasIgnorableItems(@NotNull PlayerInventory inventory) {
        for (Map.Entry<EquipmentSlot, ItemStack> playerEquippedItem : getEquippedItems(inventory).entrySet()) {
            if (!this.includedItems.containsKey(playerEquippedItem.getKey())) continue;
            if (!this.isIgnorableItem(playerEquippedItem.getKey(), playerEquippedItem.getValue())) return false;
        }
        return true;
    }

    public void damageIgnorableItems(@NotNull PlayerInventory inventory) {
        for (Map.Entry<EquipmentSlot, ItemStack> playerEquippedItem : getEquippedItems(inventory).entrySet()) {
            EquipmentSlot equipmentSlot = playerEquippedItem.getKey();
            ItemStack item = playerEquippedItem.getValue();

            if (
                    this.includedItems.containsKey(equipmentSlot)
                    && this.isIgnorableItem(equipmentSlot, item)
            ) {
                Bukkit.getScheduler().runTask(
                        MSUtils.getInstance(),
                        () -> ItemUtils.damageItem((Player) Objects.requireNonNull(inventory.getHolder()), equipmentSlot, item, this.breakingPerAction)
                );
            }
        }
    }

    public @NotNull Map<EquipmentSlot, ItemStack> getIncludedItems() {
        return this.includedItems;
    }

    public int getBreakingValue() {
        return this.breakingPerAction;
    }

    private static @NotNull Map<@NotNull EquipmentSlot, @Nullable ItemStack> getEquippedItems(@NotNull PlayerInventory inventory) {
        Map<EquipmentSlot, ItemStack> playerEquippedItems = new HashMap<>();
        playerEquippedItems.put(EquipmentSlot.HEAD, inventory.getHelmet());
        playerEquippedItems.put(EquipmentSlot.CHEST, inventory.getChestplate());
        playerEquippedItems.put(EquipmentSlot.LEGS, inventory.getLeggings());
        playerEquippedItems.put(EquipmentSlot.FEET, inventory.getBoots());
        return playerEquippedItems;
    }
}
