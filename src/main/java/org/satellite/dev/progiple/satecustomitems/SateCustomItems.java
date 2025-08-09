package org.satellite.dev.progiple.satecustomitems;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.novasparkle.lunaspring.API.commands.LunaExecutor;
import org.novasparkle.lunaspring.LunaPlugin;
import org.satellite.dev.progiple.satecustomitems.handlers.BlockPlaceHandler;
import org.satellite.dev.progiple.satecustomitems.handlers.InteractHandler;
import org.satellite.dev.progiple.satecustomitems.handlers.LeaveJoinHandler;
import org.satellite.dev.progiple.satecustomitems.itemManager.ComponentStorage;
import org.satellite.dev.progiple.satecustomitems.itemManager.secondary.realized.antimatterClot.LockedManager;
import org.satellite.dev.progiple.satecustomitems.tasks.TaskManager;

public final class SateCustomItems extends LunaPlugin {
    @Getter private static SateCustomItems INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        super.onEnable();

        Bukkit.getScheduler().runTask(INSTANCE, ComponentStorage::loadComponents);
        this.registerListeners(
                new InteractHandler(),
                new LeaveJoinHandler(),
                new BlockPlaceHandler(),
                new LockedManager.Handler());

        LunaExecutor.initialize(INSTANCE);
    }

    @Override
    public void onDisable() {
        TaskManager.stopAll();
        super.onDisable();
    }
}
