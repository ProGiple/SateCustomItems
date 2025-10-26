package org.satellite.dev.progiple.satecustomitems.items.realized;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.configuration.IgnoredField;
import org.novasparkle.lunaspring.API.items.RealizedComponent;
import org.novasparkle.lunaspring.API.items.secondary.TimedItemComponent;
import org.novasparkle.lunaspring.API.util.service.managers.VaultManager;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.satellite.dev.progiple.satecustomitems.configs.Config;
import org.satellite.dev.progiple.satecustomitems.items.AbsItemComponent;

import java.util.stream.Stream;

@RealizedComponent
@Getter
public class MoneyGeneratorComponent extends AbsItemComponent implements TimedItemComponent {
    @IgnoredField
    private final EquipmentSlot[] enabledSlots = {EquipmentSlot.OFF_HAND, EquipmentSlot.HAND};
    private double gived_money_per_item;
    public MoneyGeneratorComponent() {
        super("money_generator");
    }

    @Override
    public void tick(Player handler, Stream<ItemStack> stream) {
        if (this.blacklistedWorlds.contains(handler.getWorld())) return;

        int amount = stream.mapToInt(ItemStack::getAmount).sum();
        double money = LunaMath.round(amount * this.gived_money_per_item, 1);
        VaultManager.deposit(handler, money);
        Config.sendMessage(handler, "generators.money", "money-%-" + money);
    }
}
