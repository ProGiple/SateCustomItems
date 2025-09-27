package org.satellite.dev.progiple.satecustomitems.itemManager.secondary.realized;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.satellite.dev.progiple.satecustomitems.configs.Config;
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
    public boolean onClick(PlayerInteractEvent event, ItemStack itemStack) {
        if (!event.getAction().name().contains("RIGHT")) return false;

        Player player = event.getPlayer();
        if (this.blacklistedWorlds.contains(player.getWorld().getName())) return true;
        if (this.inCooldown(player, itemStack.getType())) return true;

        PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, this.time * 20, this.level - 1);
        PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, this.time * 20, this.level - 1);
        player.getWorld().getNearbyPlayers(player.getLocation(), this.radius)
                .forEach(p -> {
                    if (!p.equals(player)) {
                        Config.sendMessage(p, "blindingWaveUseFrom", "player-%-" + player.getName());
                        p.addPotionEffect(blindness);
                        p.addPotionEffect(slowness);
                    }
                });

        this.putCooldown(player, itemStack.getType());
        itemStack.setAmount(itemStack.getAmount() - 1);

        Config.sendMessage(player, "blindingWaveUse");
        return true;
    }
}
