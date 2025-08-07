package org.satellite.dev.progiple.satecustomitems.itemManager.secondary.realized;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.util.service.managers.VaultManager;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.satellite.dev.progiple.satecustomitems.configs.Config;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.AbsItemComponent;
import org.satellite.dev.progiple.satecustomitems.itemManager.RealizedComponent;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.IgnoredField;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.TimedItemComponent;

import java.util.stream.Stream;

@RealizedComponent @Getter
public class MoneyGeneratorComponent extends AbsItemComponent implements TimedItemComponent {
    @IgnoredField
    private final EquipmentSlot[] enabledSlots = {EquipmentSlot.OFF_HAND, EquipmentSlot.HAND};
    private double gived_money_per_item;
    public MoneyGeneratorComponent() {
        super("money_generator");
    }

    @Override
    public void tick(Player handler, Stream<ItemStack> stream) {
        int amount = stream.mapToInt(ItemStack::getAmount).sum();
        double money = LunaMath.round(amount * this.gived_money_per_item, 1);
        VaultManager.deposit(handler, money);
        Config.sendMessage(handler, "moneyGen", "money-%-" + money);
    }
}
