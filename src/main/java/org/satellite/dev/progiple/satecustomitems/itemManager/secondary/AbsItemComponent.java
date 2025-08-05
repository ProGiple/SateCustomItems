package org.satellite.dev.progiple.satecustomitems.itemManager.secondary;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.menus.items.NonMenuItem;
import org.novasparkle.lunaspring.API.util.service.managers.NBTManager;
import org.satellite.dev.progiple.satecustomitems.configs.Config;
import org.satellite.dev.progiple.satecustomitems.itemManager.ItemComponent;

import java.lang.reflect.Field;

@Getter
public abstract class AbsItemComponent implements ItemComponent {
    @IgnoredField private final String id;
    @IgnoredField public ConfigurationSection itemSection;
    public AbsItemComponent(String id) {
        this.id = id;
        this.reloadSection();
    }

    @SneakyThrows
    public void reloadSection() {
        this.itemSection = Config.getSection("items." + this.id);

        if (this.itemSection == null) return;
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
        return NBTManager.hasTag(itemStack, "sci_" + this.id);
    }

    @Override
    public NonMenuItem createItem() {
        NonMenuItem item = new NonMenuItem(this.itemSection);
        NBTManager.setBool(item.getItemStack(), "sci_" + this.id, true);
        return item;
    }
}
