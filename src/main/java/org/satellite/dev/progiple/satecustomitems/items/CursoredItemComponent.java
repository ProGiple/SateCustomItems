package org.satellite.dev.progiple.satecustomitems.items;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.novasparkle.lunaspring.API.items.ItemComponent;

public interface CursoredItemComponent extends ItemComponent {
    void onClick(InventoryClickEvent e);
}
