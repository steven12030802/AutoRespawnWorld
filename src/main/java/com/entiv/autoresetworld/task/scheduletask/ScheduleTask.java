package com.entiv.autoresetworld.task.scheduletask;

import com.entiv.autoresetworld.Main;
import com.entiv.autoresetworld.task.TaskConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class ScheduleTask {

    protected final String name;

    protected final TaskConfig taskConfig;
    protected final ConfigurationSection section;

    protected boolean isExpired = false;

    public ScheduleTask(String path, String name) {
        this.name = name;

        FileConfiguration config = Main.getInstance().getConfig();
        section = Objects.requireNonNull(config.getConfigurationSection(path), "配置文件路径 " + path + " 加载失败, 请检查配置文件");
        taskConfig = new TaskConfig(path);
    }

    // 到期后运行的任务
    public abstract void runTask();

    public boolean isExpired() {

        if (taskConfig.isExpired()) {
            setExpired(true);
        }

        return isExpired;
    }

    public LocalDateTime getScheduleDateTime() {
        return taskConfig.getScheduleDateTime();
    }

    public void load() {
        Main.getInstance().getTaskManager().addTask(name, this);
    }

    public void setExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }

    public void taskComplete() {
        setExpired(false);
        taskConfig.setupNextScheduleTaskTime();
    }

    public String getName() {
        return name;
    }
}
