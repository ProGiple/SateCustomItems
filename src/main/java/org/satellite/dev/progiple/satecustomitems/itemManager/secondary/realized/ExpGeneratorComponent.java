package org.satellite.dev.progiple.satecustomitems.itemManager.secondary.realized;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.satellite.dev.progiple.satecustomitems.SateCustomItems;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.AbsItemComponent;
import org.satellite.dev.progiple.satecustomitems.itemManager.RealizedComponent;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.IgnoredField;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.TimedItemComponent;

import java.util.stream.Stream;

@RealizedComponent @Getter
public class ExpGeneratorComponent extends AbsItemComponent implements TimedItemComponent {
    @IgnoredField
    private final EquipmentSlot[] enabledSlots = {EquipmentSlot.OFF_HAND, EquipmentSlot.HAND};
    private int gived_exp_per_item;
    public ExpGeneratorComponent() {
        super("exp_generator");
    }

    @Override
    public void tick(Player handler, Stream<ItemStack> stream) {
        int amount = stream.mapToInt(ItemStack::getAmount).sum();
        Bukkit.getScheduler().runTask(SateCustomItems.getINSTANCE(), () -> handler.giveExp(amount * this.gived_exp_per_item));
    }
}
