package org.satellite.dev.progiple.satecustomitems.handlers;

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
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.TimedItemComponent;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class LeaveJoinHandler implements Listener {
    private final Set<TickableTask> taskSet = new HashSet<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        new TickableTask(player).runTaskLaterAsynchronously(SateCustomItems.getINSTANCE(), 180L);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Utils.find(this.taskSet, t -> t.player.equals(player)).ifPresent(TickableTask::cancel);
    }

    private class TickableTask extends LunaTask {
        private final Player player;
        public TickableTask(Player player) {
            super(0);
            this.player = player;
        }

        @Override @SneakyThrows
        @SuppressWarnings("all")
        public void start() {
            LeaveJoinHandler.this.taskSet.add(this);
            while (this.isActive() && LeaveJoinHandler.this.taskSet.contains(this)) {
                Thread.sleep(1000L);
                Set<ItemComponent> usedComponents = new HashSet<>();

                PlayerInventory inventory = this.player.getInventory();
                for (ItemComponent component : ComponentStorage.getRealizedComponents()) {
                    if (!(component instanceof TimedItemComponent timed)) continue;

                    Stream<ItemStack> itemStackStream = timed.getEnabledSlots().length <= 0 ?
                            Arrays.stream(inventory.getStorageContents()) :
                            Arrays.stream(timed.getEnabledSlots()).map(s -> inventory.getItem(s));

                    int sum = (int) itemStackStream
                            .filter(i -> i != null && !i.getType().isAir() && timed.itemIsComponent(i))
                            .mapToInt(i -> i.getAmount())
                            .sum();

                    if (sum <= 0) continue;
                    timed.tick(this.player, sum);
                }

                usedComponents = null;
            }
        }

        @Override
        public void cancel() {
            LeaveJoinHandler.this.taskSet.remove(this);
            super.cancel();
        }
    }
}
