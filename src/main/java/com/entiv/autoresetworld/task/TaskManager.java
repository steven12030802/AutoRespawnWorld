package com.entiv.autoresetworld.task;

import com.entiv.autoresetworld.Main;
import com.entiv.autoresetworld.task.scheduletask.CommandTask;
import com.entiv.autoresetworld.task.scheduletask.DeleteFileTask;
import com.entiv.autoresetworld.task.scheduletask.RegenWorldTask;
import com.entiv.autoresetworld.task.scheduletask.ScheduleTask;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {

    private final Map<String, ScheduleTask> scheduleTasks = new HashMap<>();

    //TODO 能否自动从包里找到对应的子类注册?
    public void loadScheduleTask() {

        setupScheduleTask("自动刷新世界", RegenWorldTask.class);
        setupScheduleTask("自动删除文件", DeleteFileTask.class);
        setupScheduleTask("自动执行指令", CommandTask.class);

    }

    public Collection<ScheduleTask> getScheduleTasks() {
        return scheduleTasks.values();
    }

    private void setupScheduleTask(String path, Class<? extends ScheduleTask> task) {
        ConfigurationSection section = Main.getInstance().getConfig().getConfigurationSection(path);
        Validate.notNull(section, "配置文件路径 " + path + " 错误, 请检查配置文件");

        try {

            for (String taskName : section.getKeys(false)) {
                ScheduleTask regenWorldTask = task.getDeclaredConstructor(String.class).newInstance(taskName);
                regenWorldTask.load();
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
