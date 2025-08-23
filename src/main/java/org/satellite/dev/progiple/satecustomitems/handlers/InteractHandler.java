package org.satellite.dev.progiple.satecustomitems.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.events.CooldownPrevent;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.ClickableItemComponent;
import org.satellite.dev.progiple.satecustomitems.itemManager.ComponentStorage;

import java.util.UUID;

public class InteractHandler implements Listener {
    private final CooldownPrevent<UUID> cashes = new CooldownPrevent<>(100);

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand.getType().isAir()) return;

        ClickableItemComponent component = ComponentStorage.getComponent(hand, ClickableItemComponent.class);
        if (component == null || this.cashes.isCancelled(e, player.getUniqueId())) return;

        if (component.onClick(e, hand)) e.setCancelled(true);
    }
}
