package org.satellite.dev.progiple.satecustomitems.commands;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.commands.annotations.Permissions;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.satellite.dev.progiple.satecustomitems.configs.Config;

@SubCommand(appliedCommand = "satecustomitems", commandIdentifiers = "reload")
@Permissions("#.reload")
public class ReloadSubCommand implements Invocation {
    @Override
    public void invoke(CommandSender sender, String[] strings) {
        Config.reload();
        Config.sendMessage(sender, "reload");
    }
}
