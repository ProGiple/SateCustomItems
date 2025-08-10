package org.satellite.dev.progiple.satecustomitems.itemManager.secondary;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.satellite.dev.progiple.satecustomitems.itemManager.Component;
import org.satellite.dev.progiple.satecustomitems.itemManager.ItemComponent;

@Component
public interface InventoryClickItemComponent extends ItemComponent {
    void onClick(InventoryClickEvent event);
}
