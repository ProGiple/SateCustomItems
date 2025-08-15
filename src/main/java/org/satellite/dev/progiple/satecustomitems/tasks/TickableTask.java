package org.satellite.dev.progiple.satecustomitems.tasks;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.novasparkle.lunaspring.API.util.utilities.LunaTask;
import org.satellite.dev.progiple.satecustomitems.itemManager.ComponentStorage;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.TimedItemComponent;

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

            PlayerInventory inventory = this.player.getInventory();
            ComponentStorage.getRealizedComponents(TimedItemComponent.class).forEach(c -> {
                Stream<ItemStack> stream = ComponentStorage.scanInventory(inventory, c);
                if (stream.count() > 0) c.tick(this.player, stream);
            });
        }
    }

    public void stop() {
        this.cancel();
        TaskManager.unregister(this);
    }
}