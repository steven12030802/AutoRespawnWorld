package com.entiv.autoresetworld.taskmanager;

import com.entiv.autoresetworld.Main;
import com.entiv.autoresetworld.taskmanager.scheduletask.ScheduleTask;
import org.bukkit.scheduler.BukkitRunnable;

public class ScheduleTaskRunnable extends BukkitRunnable {

    private static ScheduleTaskRunnable scheduleTaskRunnable;

    @Override
    public void run() {
        for (ScheduleTask scheduleTask : ScheduleTask.scheduleTasks) {
            if (scheduleTask.isExpired()) {
                scheduleTask.runTask();
                scheduleTask.taskComplete();
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
