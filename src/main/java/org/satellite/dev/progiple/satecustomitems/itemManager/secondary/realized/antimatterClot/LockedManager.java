package org.satellite.dev.progiple.satecustomitems.itemManager.secondary.realized.antimatterClot;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.satellite.dev.progiple.satecustomitems.configs.Config;
import org.satellite.dev.progiple.satecustomitems.itemManager.ComponentStorage;

import java.util.*;

@UtilityClass
public class LockedManager {
    private final Map<UUID, Long> lockedPlayers = new HashMap<>();

    public void add(UUID uuid) {
        remove(uuid);
        lockedPlayers.put(uuid, System.currentTimeMillis());
    }

    public void remove(UUID uuid) {
        lockedPlayers.remove(uuid);
    }

    public boolean contains(UUID uuid) {
        AntiMatterClotComponent component = ComponentStorage.getComponent(AntiMatterClotComponent.class);
        int timeMillis = component == null ? 20000 : component.getTime() * 1000;

        return lockedPlayers.containsKey(uuid) &&
                lockedPlayers.get(uuid) + timeMillis > System.currentTimeMillis();
    }

    public static class Handler implements Listener {
        @EventHandler
        public void onUse(PlayerInteractEvent e) {
            Player player = e.getPlayer();
            if (!contains(player.getUniqueId())) return;

            ItemStack item = e.getItem();
            if (item != null) {
                if (item.getType() == Material.ENDER_PEARL) {
                    Config.sendMessage(player, "pearlsIsDisabled");
                    e.setCancelled(true);
                }
                else if (item.getType() == Material.FIREWORK_ROCKET) {
                    Config.sendMessage(player, "fireworksIsDisabled");
                    e.setCancelled(true);
                }
            }
        }

        @EventHandler
        public void onGlideToggle(EntityToggleGlideEvent event) {
            if (event.getEntity() instanceof Player player && event.isGliding()) {
                if (!contains(player.getUniqueId())) return;
                event.setCancelled(true);
            }
        }
    }
}
