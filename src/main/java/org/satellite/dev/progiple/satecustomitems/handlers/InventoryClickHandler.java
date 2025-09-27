package org.satellite.dev.progiple.satecustomitems.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.events.CooldownPrevent;
import org.satellite.dev.progiple.satecustomitems.itemManager.ComponentStorage;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.InventoryClickItemComponent;

import java.util.UUID;

public class InventoryClickHandler implements Listener {
    private final CooldownPrevent<UUID> cashes = new CooldownPrevent<>(100);

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType().isAir()) return;

        InventoryClickItemComponent component = ComponentStorage.getComponent(item, InventoryClickItemComponent.class);
        if (component == null) return;

        if (this.cashes.isCancelled(e, player.getUniqueId())) {
            e.setCancelled(true);
            return;
        }
        component.onClick(e);
    }
}
