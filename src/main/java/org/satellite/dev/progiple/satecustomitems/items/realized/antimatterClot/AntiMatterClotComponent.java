package org.satellite.dev.progiple.satecustomitems.items.realized.antimatterClot;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.items.RealizedComponent;
import org.novasparkle.lunaspring.API.items.secondary.ClickableItemComponent;
import org.satellite.dev.progiple.satecustomitems.configs.Config;
import org.satellite.dev.progiple.satecustomitems.items.AbsItemComponent;

@RealizedComponent
public class AntiMatterClotComponent extends AbsItemComponent implements ClickableItemComponent {
    @Getter private int time;
    private int radius;
    public AntiMatterClotComponent() {
        super("antimatter_clot");
    }

    @Override
    public boolean onClick(PlayerInteractEvent e, ItemStack itemStack) {
        if (!e.getAction().name().contains("RIGHT")) return false;

        Player player = e.getPlayer();
        if (this.blacklistedWorlds.contains(player.getWorld())) return true;

        if (this.inCooldown(player, itemStack.getType())) return true;
        player.getWorld().getNearbyPlayers(player.getLocation(), this.radius)
                .forEach(p -> {
                    LockedManager.add(p.getUniqueId());
                    p.setCooldown(Material.ENDER_PEARL, this.time * 20);
                    p.setCooldown(Material.FIREWORK_ROCKET, this.time * 20);
                    p.setCooldown(Material.ELYTRA, this.time * 20);

                    if (!p.equals(player))
                        Config.sendMessage(p, "antimatterClot.useFrom", "player-%-" + player.getName());
                });

        this.putCooldown(player, itemStack.getType());
        itemStack.setAmount(itemStack.getAmount() - 1);

        Config.sendMessage(player, "antimatterClot.use");
        return true;
    }
}
