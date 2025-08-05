package org.satellite.dev.progiple.satecustomitems.itemManager;

import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.menus.items.NonMenuItem;

@Component
public interface ItemComponent {
    boolean itemIsComponent(ItemStack itemStack);
    NonMenuItem createItem();
}
