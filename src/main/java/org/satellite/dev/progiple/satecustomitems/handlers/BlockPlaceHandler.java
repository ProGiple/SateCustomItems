package org.satellite.dev.progiple.satecustomitems.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.satellite.dev.progiple.satecustomitems.itemManager.ComponentStorage;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.BlockPlaceItemComponent;

public class BlockPlaceHandler implements Listener {
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        ItemStack itemStack = e.getItemInHand();

        BlockPlaceItemComponent component = ComponentStorage.getComponent(itemStack, BlockPlaceItemComponent.class);
        if (component != null) {
            if (component.onPlace(e, itemStack)) e.setCancelled(true);
        }
    }
}
