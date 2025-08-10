package org.satellite.dev.progiple.satecustomitems.itemManager.secondary;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.satellite.dev.progiple.satecustomitems.itemManager.Component;
import org.satellite.dev.progiple.satecustomitems.itemManager.SlotFilteringComponent;

import java.util.stream.Stream;

@Component
public interface JoinItemComponent extends SlotFilteringComponent {
    void onJoin(Player handler, Stream<ItemStack> componentItems);
}
