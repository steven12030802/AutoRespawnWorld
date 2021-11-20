package com.entiv.autoresetworld;

import com.entiv.autoresetworld.task.CommandTask;
import com.entiv.autoresetworld.task.DeleteFileTask;
import com.entiv.autoresetworld.task.RegenWorldTask;
import com.entiv.autoresetworld.task.ScheduleTask;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {

    private final Map<String, ScheduleTask> scheduleTasks = new HashMap<>();

    public void loadScheduleTask() {
        setupScheduleTask("自动刷新世界", RegenWorldTask.class);
        setupScheduleTask("自动删除文件", DeleteFileTask.class);
        //TODO 增加延时功能, 这样有自动重启公告
        setupScheduleTask("自动执行任务", CommandTask.class);
    }

    public Collection<ScheduleTask> getScheduleTasks() {
        return scheduleTasks.values();
    }

    private void setupScheduleTask(String path, Class<? extends ScheduleTask> task) {
        ConfigurationSection section = Main.getInstance().getConfig().getConfigurationSection(path);

        if (section == null) return;

        try {

            for (String taskName : section.getKeys(false)) {
                ScheduleTask scheduleTask = task.getDeclaredConstructor(String.class).newInstance(taskName);
                scheduleTask.load();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearTasks() {
        scheduleTasks.clear();
    }

    public void addTask(String name, ScheduleTask scheduleTask) {
        scheduleTasks.put(name, scheduleTask);
    }

    public ScheduleTask getTask(String name) {
        return scheduleTasks.get(name);
    }
}
