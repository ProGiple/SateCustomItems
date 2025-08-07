package org.satellite.dev.progiple.satecustomitems.itemManager.secondary;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.satellite.dev.progiple.satecustomitems.itemManager.Component;
import org.satellite.dev.progiple.satecustomitems.itemManager.ItemComponent;

import java.util.stream.Stream;

@Component
public interface TimedItemComponent extends ItemComponent {
    void tick(Player handler, Stream<ItemStack> componentItems);
    @Nullable EquipmentSlot[] getEnabledSlots();
}
