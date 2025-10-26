package org.satellite.dev.progiple.satecustomitems;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.commands.CommandInitializer;
import org.novasparkle.lunaspring.API.events.CooldownPrevent;
import org.novasparkle.lunaspring.API.items.ComponentStorage;
import org.novasparkle.lunaspring.LunaPlugin;
import org.satellite.dev.progiple.satecustomitems.items.CursoredItemComponent;
import org.satellite.dev.progiple.satecustomitems.items.realized.antimatterClot.LockedManager;

@Getter
public final class SateCustomItems extends LunaPlugin {
    @Getter private static SateCustomItems INSTANCE;
    @Accessors(fluent = true) private boolean pluginIsEnabled = true;

    @Override
    public void onEnable() {
        INSTANCE = this;
        super.onEnable();

        ComponentStorage.loadComponents(INSTANCE, "#.items.realized");
        this.registerListeners(new LockedManager.Handler(), new Handler());

        CommandInitializer.initialize(INSTANCE, "#.commands");
    }

    @Override
    public void onDisable() {
        this.pluginIsEnabled = false;
        super.onDisable();
    }

    private static class Handler implements Listener {
        private final CooldownPrevent<HumanEntity> cache = new CooldownPrevent<>(75);

        @EventHandler
        public void onClick(InventoryClickEvent e) {
            ItemStack itemStack = e.getCursor();
            if (itemStack == null || itemStack.getType().isAir()) return;

            CursoredItemComponent itemComponent = ComponentStorage.getComponent(itemStack, CursoredItemComponent.class);
            if (itemComponent == null || this.cache.isCancelled(e, e.getWhoClicked())) return;

            itemComponent.onClick(e);
        }
    }
}
