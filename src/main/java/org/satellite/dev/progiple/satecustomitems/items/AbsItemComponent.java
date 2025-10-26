package org.satellite.dev.progiple.satecustomitems.items;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.configuration.IgnoredField;
import org.novasparkle.lunaspring.API.items.ItemComponent;
import org.novasparkle.lunaspring.API.items.secondary.BlockPlaceItemComponent;
import org.novasparkle.lunaspring.API.menus.items.NonMenuItem;
import org.novasparkle.lunaspring.API.util.service.managers.NBTManager;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.satellite.dev.progiple.satecustomitems.configs.Config;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class AbsItemComponent implements ItemComponent, BlockPlaceItemComponent {
    private static final String PERM_TEMPLATE = "satecustomitems.bypass.cooldown.";

    @IgnoredField private final String id;
    @IgnoredField public ConfigurationSection itemSection;
    @IgnoredField public List<World> blacklistedWorlds;
    public int cooldown;
    public AbsItemComponent(String id) {
        this.id = id;
        this.reloadSection();
    }

    public boolean inCooldown(Player player, Material material) {
        if (player.hasPermission(PERM_TEMPLATE + this.id) || player.hasPermission(PERM_TEMPLATE + "*")) return false;

        if (this.cooldown > 0 && player.hasCooldown(material)) {
            double seconds = LunaMath.round((double) player.getCooldown(material) / 20, 1);
            Config.sendMessage(player, "inCooldown", "seconds-%-" + seconds);
            return true;
        }

        return false;
    }

    public void putCooldown(Player player, Material material) {
        if (this.cooldown > 0
                && !player.hasPermission(PERM_TEMPLATE + this.id)
                && !player.hasPermission(PERM_TEMPLATE + "*")) player.setCooldown(material, this.cooldown);
    }

    @SneakyThrows
    public void reloadSection() {
        this.itemSection = Config.getSection("items." + this.id);
        if (this.itemSection == null) return;

        this.blacklistedWorlds = this.itemSection.getStringList("blacklist_worlds")
                .stream()
                .map(Bukkit::getWorld)
                .filter(Objects::nonNull)
                .toList();
        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(IgnoredField.class)) continue;

            Class<?> type = field.getType();
            if (type == int.class || type == Integer.class)
                field.set(this, this.itemSection.getInt(field.getName(), 0));
            else if (type == double.class || type == Double.class)
                field.set(this, this.itemSection.getDouble(field.getName(), 0));
            else
                field.set(this, this.itemSection.get(field.getName()));
        }
    }

    @Override
    public boolean itemIsComponent(ItemStack itemStack) {
        return itemStack != null && !itemStack.getType().isAir() && NBTManager.hasTag(itemStack, "sci_" + this.id);
    }

    @Override
    public boolean onPlace(BlockPlaceEvent e, ItemStack itemStack) {
        return true;
    }

    @Override
    public NonMenuItem createItem() {
        NonMenuItem item = new NonMenuItem(this.itemSection);

        ItemStack stack = item.getItemStack();
        if (item.getHeadValue() != null) NBTManager.base64head(stack, item.getHeadValue());

        NBTManager.setBool(stack, "sci_" + this.id, true);
        return item;
    }
}
