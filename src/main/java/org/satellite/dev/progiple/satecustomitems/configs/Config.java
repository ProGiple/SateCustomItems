package org.satellite.dev.progiple.satecustomitems.configs;

import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.satellite.dev.progiple.satecustomitems.SateCustomItems;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.AbsItemComponent;
import org.satellite.dev.progiple.satecustomitems.itemManager.ComponentStorage;
import org.satellite.dev.progiple.satecustomitems.itemManager.ItemComponent;

@UtilityClass
public class Config {
    private final IConfig config;
    static {
        SateCustomItems.getINSTANCE().saveDefaultConfig();
        config = new IConfig(SateCustomItems.getINSTANCE());
    }

    public void reload() {
        config.reload(SateCustomItems.getINSTANCE());
        for (ItemComponent realizedComponent : ComponentStorage.getRealizedComponents()) {
            if (realizedComponent instanceof AbsItemComponent itemComponent) {
                itemComponent.reloadSection();
            }
        }
    }

    public ConfigurationSection getSection(String path) {
        return config.getSection(path);
    }

    public void sendMessage(CommandSender sender, String id, String... rpl) {
        config.sendMessage(sender, id, rpl);
    }
}
