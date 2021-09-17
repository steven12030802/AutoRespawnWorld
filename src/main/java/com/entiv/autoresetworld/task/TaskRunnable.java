package com.entiv.autoresetworld.task;

import com.entiv.autoresetworld.Main;
import com.entiv.autoresetworld.task.scheduletask.ScheduleTask;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskRunnable extends BukkitRunnable {

    private static TaskRunnable taskRunnable;

    @Override
    public void run() {

        for (ScheduleTask scheduleTask : Main.getInstance().getTaskManager().getScheduleTasks()) {
            if (scheduleTask.isExpired()) {
                scheduleTask.runTask();
                scheduleTask.taskComplete();
                return;
            }
        }
    }

    public static void load() {
        if (taskRunnable == null) {
            taskRunnable = new TaskRunnable();
        }
        taskRunnable.runTaskTimer(Main.getInstance(), 0, 20);
    }
}
