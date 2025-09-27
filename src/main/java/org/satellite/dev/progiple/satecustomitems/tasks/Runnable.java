package org.satellite.dev.progiple.satecustomitems.tasks;

import lombok.AllArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class Runnable extends BukkitRunnable {
    private final java.lang.Runnable runnable;

    @Override
    public void run() {
        TaskManager.register(this.getTaskId());
        this.runnable.run();
    }

    @Override
    public synchronized void cancel() {
        TaskManager.unregister(this.getTaskId());
        super.cancel();
    }
}
