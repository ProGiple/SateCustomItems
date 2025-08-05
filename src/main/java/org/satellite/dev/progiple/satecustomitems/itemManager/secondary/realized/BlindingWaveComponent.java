package org.satellite.dev.progiple.satecustomitems.itemManager.secondary.realized;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.satellite.dev.progiple.satecustomitems.itemManager.RealizedComponent;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.AbsItemComponent;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.ClickableItemComponent;

@RealizedComponent
public class BlindingWaveComponent extends AbsItemComponent implements ClickableItemComponent {
    private int radius;
    private int time;
    private int level;
    public BlindingWaveComponent() {
        super("blinding_wave");
    }

    @Override
    public boolean onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, this.time * 20, this.level - 1);
        PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, this.time * 20, this.level - 1);
        player.getWorld().getNearbyPlayers(player.getLocation(), this.radius)
                .forEach(p -> {
                    if (!p.equals(player)) {
                        p.addPotionEffect(blindness);
                        p.addPotionEffect(slowness);
                    }
                });
        return true;
    }
}
