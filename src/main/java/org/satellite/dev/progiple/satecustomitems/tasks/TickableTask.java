package org.satellite.dev.progiple.satecustomitems.tasks;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.novasparkle.lunaspring.API.util.utilities.LunaTask;
import org.satellite.dev.progiple.satecustomitems.itemManager.ComponentStorage;
import org.satellite.dev.progiple.satecustomitems.itemManager.ItemComponent;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.TimedItemComponent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Getter
public class TickableTask extends LunaTask {
    private final Player player;
    public TickableTask(Player player) {
        super(0);
        this.player = player;
    }

    @Override @SneakyThrows
    @SuppressWarnings("all")
    public void start() {
        TaskManager.register(this);
        while (this.isActive() && TaskManager.check(this)) {
            Thread.sleep(1000L);
            Set<ItemComponent> usedComponents = new HashSet<>();

            PlayerInventory inventory = this.player.getInventory();
            for (ItemComponent component : ComponentStorage.getRealizedComponents()) {
                if (!(component instanceof TimedItemComponent timed)) continue;

                Stream<ItemStack> itemStackStream = timed.getEnabledSlots().length <= 0 ?
                        Arrays.stream(inventory.getStorageContents()) :
                        Arrays.stream(timed.getEnabledSlots()).map(s -> inventory.getItem(s));
                timed.tick(this.player, itemStackStream
                        .filter(i -> i != null &&
                                !i.getType().isAir() &&
                                timed.itemIsComponent(i)));
            }

            usedComponents = null;
        }
    }

    public void stop() {
        this.cancel();
        TaskManager.unregister(this);
    }
}