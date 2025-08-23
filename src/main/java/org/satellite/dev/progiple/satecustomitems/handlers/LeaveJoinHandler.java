package org.satellite.dev.progiple.satecustomitems.handlers;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.novasparkle.lunaspring.API.util.utilities.LunaTask;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satecustomitems.SateCustomItems;
import org.satellite.dev.progiple.satecustomitems.itemManager.ComponentStorage;
import org.satellite.dev.progiple.satecustomitems.itemManager.ItemComponent;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.JoinItemComponent;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.QuitItemComponent;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.TimedItemComponent;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.realized.antimatterClot.LockedManager;
import org.satellite.dev.progiple.satecustomitems.tasks.TaskManager;
import org.satellite.dev.progiple.satecustomitems.tasks.TickableTask;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Getter
public class LeaveJoinHandler implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        new TickableTask(player).runTaskLaterAsynchronously(SateCustomItems.getINSTANCE(), 30L);

        PlayerInventory playerInventory = player.getInventory();
        ComponentStorage.getRealizedComponents(JoinItemComponent.class).forEach(c -> {
            c.onJoin(player, ComponentStorage.scanInventory(playerInventory, c));
        });
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        LockedManager.remove(player.getUniqueId());
        TaskManager.get(player).ifPresent(TickableTask::stop);

        PlayerInventory playerInventory = player.getInventory();
        ComponentStorage.getRealizedComponents(QuitItemComponent.class).forEach(c -> {
            c.onQuit(player, ComponentStorage.scanInventory(playerInventory, c));
        });
    }
}
