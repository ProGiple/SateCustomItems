package org.satellite.dev.progiple.satecustomitems.items.realized;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.novasparkle.lunaspring.API.items.RealizedComponent;
import org.novasparkle.lunaspring.API.items.secondary.ClickableItemComponent;
import org.novasparkle.lunaspring.API.util.utilities.AnnounceUtils;
import org.satellite.dev.progiple.satecustomitems.configs.Config;
import org.satellite.dev.progiple.satecustomitems.items.AbsItemComponent;

@RealizedComponent
public class ImpulseComponent extends AbsItemComponent implements ClickableItemComponent {
    private double verticalPower;
    private double horizontalPower;
    public ImpulseComponent() {
        super("impulse");
    }

    @Override
    public boolean onClick(PlayerInteractEvent event, ItemStack itemStack) {
        if (!event.getAction().name().contains("RIGHT")) return false;

        Player player = event.getPlayer();
        if (this.blacklistedWorlds.contains(player.getWorld())) return true;
        if (this.inCooldown(player, itemStack.getType())) return true;

        player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, player.getLocation(), 10, 0.5, 0.5, 0.5, 0.5);
        player.getWorld().getNearbyPlayers(player.getLocation(), 25).forEach(p -> AnnounceUtils.sound(p, Sound.ENTITY_GENERIC_EXPLODE));

        Vector direction = player.getLocation().getDirection().normalize();
        direction.setY(this.verticalPower);
        Vector boost = direction.multiply(this.horizontalPower);

        this.putCooldown(player, itemStack.getType());
        itemStack.setAmount(itemStack.getAmount() - 1);

        player.setVelocity(player.getVelocity().add(boost));
        Config.sendMessage(player, "impulse.use");
        return true;
    }

}