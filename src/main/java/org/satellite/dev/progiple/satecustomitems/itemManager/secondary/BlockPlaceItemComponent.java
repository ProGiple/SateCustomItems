package org.satellite.dev.progiple.satecustomitems.itemManager.secondary;

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.satellite.dev.progiple.satecustomitems.itemManager.Component;
import org.satellite.dev.progiple.satecustomitems.itemManager.ItemComponent;

@Component
public interface BlockPlaceItemComponent extends ItemComponent {
    boolean onPlace(BlockPlaceEvent e, ItemStack itemStack);
}
