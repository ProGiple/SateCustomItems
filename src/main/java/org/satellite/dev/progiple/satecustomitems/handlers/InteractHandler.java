package org.satellite.dev.progiple.satecustomitems.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.events.CooldownPrevent;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.ClickableItemComponent;
import org.satellite.dev.progiple.satecustomitems.itemManager.ComponentStorage;
import org.satellite.dev.progiple.satecustomitems.itemManager.ItemComponent;

import java.util.UUID;

public class InteractHandler implements Listener {
    private final CooldownPrevent<UUID> cashes = new CooldownPrevent<>(150);

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand.getType().isAir() || player.isSneaking()) return;

        ItemComponent component = ComponentStorage.getComponent(hand);
        if (!(component instanceof ClickableItemComponent clickableItemComponent)
            || this.cashes.isCancelled(e, player.getUniqueId())) return;

        e.setCancelled(true);
        if (clickableItemComponent.onClick(e)) hand.setAmount(hand.getAmount() - 1);
    }
}
