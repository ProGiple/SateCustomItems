package org.satellite.dev.progiple.satecustomitems;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.novasparkle.lunaspring.API.commands.CommandInitializer;
import org.novasparkle.lunaspring.LunaPlugin;
import org.satellite.dev.progiple.satecustomitems.handlers.BlockPlaceHandler;
import org.satellite.dev.progiple.satecustomitems.handlers.InteractHandler;
import org.satellite.dev.progiple.satecustomitems.handlers.InventoryClickHandler;
import org.satellite.dev.progiple.satecustomitems.handlers.LeaveJoinHandler;
import org.satellite.dev.progiple.satecustomitems.itemManager.ComponentStorage;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.realized.antimatterClot.LockedManager;
import org.satellite.dev.progiple.satecustomitems.tasks.TaskManager;

@Getter
public final class SateCustomItems extends LunaPlugin {
    @Getter private static SateCustomItems INSTANCE;
    @Accessors(fluent = true) private boolean pluginIsEnabled = true;

    @Override
    public void onEnable() {
        INSTANCE = this;
        super.onEnable();

        ComponentStorage.loadComponents();
        this.registerListeners(
                new InventoryClickHandler(),
                new InteractHandler(),
                new LeaveJoinHandler(),
                new BlockPlaceHandler(),
                new LockedManager.Handler());

        CommandInitializer.initialize(INSTANCE, "#.commands");
    }

    @Override
    public void onDisable() {
        this.pluginIsEnabled = false;
        TaskManager.stopAll();
        super.onDisable();
    }
}
