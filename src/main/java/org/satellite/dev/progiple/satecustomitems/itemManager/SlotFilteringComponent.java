package org.satellite.dev.progiple.satecustomitems.itemManager;

import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

public interface SlotFilteringComponent extends ItemComponent {
    @Nullable EquipmentSlot[] getEnabledSlots();
}