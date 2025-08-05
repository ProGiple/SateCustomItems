package org.satellite.dev.progiple.satecustomitems.itemManager.secondary;

import org.bukkit.event.player.PlayerInteractEvent;
import org.satellite.dev.progiple.satecustomitems.itemManager.Component;
import org.satellite.dev.progiple.satecustomitems.itemManager.ItemComponent;

@Component
public interface ClickableItemComponent extends ItemComponent {
    boolean onClick(PlayerInteractEvent event);
}
