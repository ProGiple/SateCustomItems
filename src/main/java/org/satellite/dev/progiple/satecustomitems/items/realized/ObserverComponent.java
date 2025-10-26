package org.satellite.dev.progiple.satecustomitems.items.realized;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.potion.PotionEffectType;
import org.novasparkle.lunaspring.API.items.RealizedComponent;
import org.novasparkle.lunaspring.API.items.secondary.ClickableItemComponent;
import org.novasparkle.lunaspring.API.menus.items.NonMenuItem;
import org.satellite.dev.progiple.satecustomitems.configs.Config;
import org.satellite.dev.progiple.satecustomitems.items.AbsItemComponent;

import java.util.Comparator;

@RealizedComponent
public class ObserverComponent extends AbsItemComponent implements ClickableItemComponent {
    private int radius;
    private boolean hide_invisibilities;
    public ObserverComponent() {
        super("observer");
    }

    @Override
    public boolean onClick(PlayerInteractEvent event, ItemStack itemStack) {
        Action action = event.getAction();
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) return false;

        Player player = event.getPlayer();
        if (this.inCooldown(player, Material.COMPASS)) return true;

        Location location = player.getLocation();
        Player nearbyPlayer = player.getWorld().getNearbyPlayers(location, this.radius).stream()
                .filter(p -> !p.hasPermission("satecustomitems.observer.hide")
                        && (!this.hide_invisibilities || !p.hasPotionEffect(PotionEffectType.INVISIBILITY))
                        && !p.equals(player)
                        && player.canSee(p))
                .min(Comparator.comparingDouble(p -> p.getLocation().distance(location)))
                .orElse(null);

        this.putCooldown(player, Material.COMPASS);
        if (nearbyPlayer == null) {
            Config.sendMessage(player, "observer.noPlayersNear");
            return true;
        }

        CompassMeta compassMeta = (CompassMeta) itemStack.getItemMeta();
        compassMeta.setLodestone(nearbyPlayer.getLocation());
        compassMeta.setLodestoneTracked(false);
        itemStack.setItemMeta(compassMeta);

        Config.sendMessage(player, "observer.observerScan", "player-%-" + nearbyPlayer.getName());
        return true;
    }

    @Override
    public NonMenuItem createItem() {
        NonMenuItem nonMenuItem = super.createItem();
        nonMenuItem.setMaterial(Material.COMPASS);
        return nonMenuItem;
    }

    @Override
    public boolean itemIsComponent(ItemStack itemStack) {
        return itemStack.getType() == Material.COMPASS && super.itemIsComponent(itemStack);
    }
}
