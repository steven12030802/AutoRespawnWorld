package com.entiv.autoresetworld.scheduletask;

import org.bukkit.scheduler.BukkitRunnable;

public class ScheduleTaskRunnable extends BukkitRunnable {

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
