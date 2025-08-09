package org.satellite.dev.progiple.satecustomitems.itemManager.secondary.realized;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.satellite.dev.progiple.satecustomitems.SateCustomItems;
import org.satellite.dev.progiple.satecustomitems.configs.Config;
import org.satellite.dev.progiple.satecustomitems.itemManager.RealizedComponent;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.AbsItemComponent;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.ClickableItemComponent;

import java.util.Objects;
import java.util.stream.Stream;

@RealizedComponent
public class ObviousCrystalComponent extends AbsItemComponent implements ClickableItemComponent {
    private int time;
    private int radius;
    public ObviousCrystalComponent() {
        super("obvious_crystal");
    }

    @Override
    public boolean onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Stream<InvisibilityPlayer> nearby = player.getWorld().getNearbyPlayers(player.getLocation(), this.radius)
                .stream()
                .filter(p -> p.hasPotionEffect(PotionEffectType.INVISIBILITY))
                .map(p -> {
                    InvisibilityPlayer invisibilityPlayer = new InvisibilityPlayer(p);
                    invisibilityPlayer.recognize();
                    return invisibilityPlayer;
                });

        Config.sendMessage(player, "obviousCrystalUse");
        Bukkit.getScheduler().runTaskLater(SateCustomItems.getINSTANCE(), () -> nearby.forEach(InvisibilityPlayer::back), 20L * this.time);
        return true;
    }

    private class InvisibilityPlayer {
        private final Player bukkitPlayer;
        private final int seconds;
        public InvisibilityPlayer(Player player) {
            this.bukkitPlayer = player;
            this.seconds = Objects.requireNonNull(player.getPotionEffect(PotionEffectType.INVISIBILITY)).getDuration() / 20;
        }

        public void recognize() {
            this.bukkitPlayer.removePotionEffect(PotionEffectType.INVISIBILITY);
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
