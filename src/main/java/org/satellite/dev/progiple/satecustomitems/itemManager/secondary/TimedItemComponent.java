package org.satellite.dev.progiple.satecustomitems.itemManager.secondary;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;
import org.satellite.dev.progiple.satecustomitems.itemManager.Component;
import org.satellite.dev.progiple.satecustomitems.itemManager.ItemComponent;

@Component
public interface TimedItemComponent extends ItemComponent {
    void tick(Player handler, int amount);
    @Nullable EquipmentSlot[] getEnabledSlots();
}
