package com.entiv.autoresetworld.taskmanager.scheduletask;

import com.entiv.autoresetworld.Main;
import com.entiv.autoresetworld.taskmanager.ScheduleConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public abstract class ScheduleTask {

    protected final String name;

    protected final ScheduleConfig scheduleConfig;
    protected final ConfigurationSection section;

    protected boolean isExpired = false;
    public final static List<ScheduleTask> scheduleTasks = new ArrayList<>();

    public ScheduleTask(String path, String name) {
        this.name = name;

        section = Main.getInstance().getConfig().getConfigurationSection(path);
        scheduleConfig = new ScheduleConfig(path);
    }

    // 到期后运行的任务
    public abstract void runTask();

    public boolean isExpired() {

        if (scheduleConfig.isExpired()) {
            setExpired(true);
        }

        return isExpired;
    }

    public void load() {
        scheduleTasks.add(this);
    }

    public void setExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }

    public String getName() {
        return name;
    }
}
