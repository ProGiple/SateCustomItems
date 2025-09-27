package org.satellite.dev.progiple.satecustomitems.itemManager.secondary.realized.antimatterClot;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.satellite.dev.progiple.satecustomitems.configs.Config;
import org.satellite.dev.progiple.satecustomitems.itemManager.RealizedComponent;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.AbsItemComponent;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.ClickableItemComponent;

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
        if (this.blacklistedWorlds.contains(player.getWorld().getName())) return true;

        if (this.inCooldown(player, itemStack.getType())) return true;
        player.getWorld().getNearbyPlayers(player.getLocation(), this.radius)
                .forEach(p -> {
                    LockedManager.add(p.getUniqueId());
                    p.setCooldown(Material.ENDER_PEARL, this.time * 20);
                    p.setCooldown(Material.FIREWORK_ROCKET, this.time * 20);
                    p.setCooldown(Material.ELYTRA, this.time * 20);

                    if (!p.equals(player))
                        Config.sendMessage(p, "antimatterClotUseFrom", "player-%-" + player.getName());
                });

        this.putCooldown(player, itemStack.getType());
        itemStack.setAmount(itemStack.getAmount() - 1);

        Config.sendMessage(player, "antimatterClotUse");
        return true;
    }
}
