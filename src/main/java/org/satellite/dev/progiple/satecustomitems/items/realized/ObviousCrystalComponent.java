package org.satellite.dev.progiple.satecustomitems.items.realized;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.novasparkle.lunaspring.API.items.RealizedComponent;
import org.novasparkle.lunaspring.API.items.secondary.ClickableItemComponent;
import org.novasparkle.lunaspring.API.util.utilities.Runnable;
import org.satellite.dev.progiple.satecustomitems.SateCustomItems;
import org.satellite.dev.progiple.satecustomitems.configs.Config;
import org.satellite.dev.progiple.satecustomitems.items.AbsItemComponent;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RealizedComponent
public class ObviousCrystalComponent extends AbsItemComponent implements ClickableItemComponent {
    private int time;
    private int radius;
    public ObviousCrystalComponent() {
        super("obvious_crystal");
    }

    @Override
    public boolean onClick(PlayerInteractEvent event, ItemStack itemStack) {
        if (!event.getAction().name().contains("RIGHT")) return false;

        Player player = event.getPlayer();
        if (this.blacklistedWorlds.contains(player.getWorld())) return true;
        if (this.inCooldown(player, itemStack.getType())) return true;

        Set<InvisibilityPlayer> nearby = player.getWorld().getNearbyPlayers(player.getLocation(), this.radius)
                .stream()
                .filter(p -> p.hasPotionEffect(PotionEffectType.INVISIBILITY))
                .map(InvisibilityPlayer::new)
                .collect(Collectors.toSet());

        Config.sendMessage(player, "obviousCrystal.use");
        nearby.forEach(p -> p.recognize(player));

        this.putCooldown(player, itemStack.getType());
        itemStack.setAmount(itemStack.getAmount() - 1);

        if (SateCustomItems.getINSTANCE().pluginIsEnabled()) Runnable.start(() -> nearby.forEach(InvisibilityPlayer::back))
                .runTaskLater(SateCustomItems.getINSTANCE(), 20L * this.time);
        return true;
    }

    private class InvisibilityPlayer {
        private final Player bukkitPlayer;
        private final int seconds;
        public InvisibilityPlayer(Player player) {
            this.bukkitPlayer = player;
            this.seconds = Objects.requireNonNull(player.getPotionEffect(PotionEffectType.INVISIBILITY)).getDuration() / 20;
        }

        public void recognize(Player user) {
            this.bukkitPlayer.removePotionEffect(PotionEffectType.INVISIBILITY);
            Config.sendMessage(this.bukkitPlayer, "obviousCrystal.useFrom", "player-%-" + user.getName());
        }

        public void back() {
            if (!this.bukkitPlayer.isOnline() || this.bukkitPlayer.hasPotionEffect(PotionEffectType.INVISIBILITY)) return;

            int duration = this.seconds - ObviousCrystalComponent.this.time;
            if (duration <= 1) return;

            PotionEffect effect = new PotionEffect(PotionEffectType.INVISIBILITY, duration * 20, 0);
            this.bukkitPlayer.addPotionEffect(effect);
        }
    }
}
