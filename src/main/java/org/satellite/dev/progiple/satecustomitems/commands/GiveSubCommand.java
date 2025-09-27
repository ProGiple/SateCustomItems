package org.satellite.dev.progiple.satecustomitems.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.commands.LunaExecutor;
import org.novasparkle.lunaspring.API.commands.annotations.Permissions;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satecustomitems.configs.Config;
import org.satellite.dev.progiple.satecustomitems.itemManager.ComponentStorage;
import org.satellite.dev.progiple.satecustomitems.itemManager.ItemComponent;

import java.util.List;
import java.util.stream.Collectors;

@SubCommand(appliedCommand = "satecustomitems", commandIdentifiers = "give")
@Permissions("#.give")
public class GiveSubCommand implements LunaExecutor {
    // /sci give player id amount

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> list) {
        return list.size() == 1 ? Utils.getPlayerNicks(list.get(0)) :
                list.size() == 2 ? Utils.tabCompleterFiltering(ComponentStorage.getRealizedComponents()
                        .stream()
                        .map(ItemComponent::getId)
                        .collect(Collectors.toSet()), list.get(1)) :
                        list.size() == 3 ? List.of("<количество>"): null;
    }

    @Override
    public void invoke(CommandSender sender, String[] strings) {
        if (strings.length < 3) {
            Config.sendMessage(sender, "noArgs");
            return;
        }

        Player player = Bukkit.getPlayer(strings[1]);
        if (player == null || !player.isOnline()) {
            Config.sendMessage(sender, "playerIsOffline", "player-%-" + strings[1]);
            return;
        }

        int amount = strings.length >= 4 ? Math.max(LunaMath.toInt(strings[3], 1), 1) : 1;
        ItemComponent component = ComponentStorage.getComponent(strings[2]);

        component.createItem().setAmount(amount).give(player);
    }
}
