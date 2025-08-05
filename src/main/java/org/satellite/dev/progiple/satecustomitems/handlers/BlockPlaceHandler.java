package org.satellite.dev.progiple.satecustomitems.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.satellite.dev.progiple.satecustomitems.itemManager.ComponentStorage;

public class BlockPlaceHandler implements Listener {
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        ItemStack itemStack = e.getItemInHand();
        if (ComponentStorage.getComponent(itemStack) != null) e.setCancelled(true);
    }
}
