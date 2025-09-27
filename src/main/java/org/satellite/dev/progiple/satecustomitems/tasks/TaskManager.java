package org.satellite.dev.progiple.satecustomitems.tasks;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class TaskManager {
    private final List<Integer> tasksId = new ArrayList<>();
    private final List<TickableTask> tasks = new ArrayList<>();

    public void register(TickableTask tickableTask) {
        tasks.add(tickableTask);
    }

    public void register(int id) {
        tasksId.add(id);
    }

    public void unregister(TickableTask tickableTask) {
        tasks.remove(tickableTask);
    }

    public void unregister(int id) {
        tasksId.remove(id);
    }

    public boolean check(TickableTask tickableTask) {
        return tasks.contains(tickableTask);
    }

    public void stopAll() {
        tasksId.forEach(Utils::cancelTask);
        tasksId.clear();

        tasks.forEach(TickableTask::cancel);
        tasks.clear();
    }

    public Optional<TickableTask> get(Player player) {
        return Utils.find(tasks, t -> t.getPlayer().equals(player));
    }
}
