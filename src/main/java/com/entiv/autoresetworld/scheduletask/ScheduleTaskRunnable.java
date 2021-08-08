package com.entiv.autoresetworld.scheduletask;

import com.entiv.autoresetworld.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class ScheduleTaskRunnable extends BukkitRunnable {

    private static ScheduleTaskRunnable scheduleTaskRunnable;

    @Override
    public void run() {
        for (ScheduleTask scheduleTask : ScheduleTask.scheduleTasks) {
            if (scheduleTask.isExpired()) {
                scheduleTask.runTask();
                return;
            }
        }
    }

    public static void load() {
        if (scheduleTaskRunnable == null) {
            scheduleTaskRunnable = new ScheduleTaskRunnable();
        }
        scheduleTaskRunnable.runTaskTimer(Main.getInstance(), 0, 50);
    }

}
