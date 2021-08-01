package com.entiv.autoresetworld.scheduletask;

import com.entiv.autoresetworld.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class ScheduleTaskRunnable extends BukkitRunnable {

    private static final BukkitRunnable scheduleTaskRunnable = new ScheduleTaskRunnable();

    private ScheduleTaskRunnable() {
        scheduleTaskRunnable.runTaskTimer(Main.getInstance(), 1200, 100);
    }

    @Override
    public void run() {
        for (ScheduleTask scheduleTask : ScheduleTask.scheduleTasks) {
            if (scheduleTask.isExpired()) {
                scheduleTask.runTask();
                return;
            }
        }
    }
}
