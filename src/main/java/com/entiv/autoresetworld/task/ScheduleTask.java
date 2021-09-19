package com.entiv.autoresetworld.task;

import com.entiv.autoresetworld.Main;
import com.entiv.autoresetworld.taskmode.Preservable;
import com.entiv.autoresetworld.taskmode.TaskMode;
import org.bukkit.configuration.ConfigurationSection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Pattern;

public abstract class ScheduleTask {

    protected final String name;
    protected final String task;

    protected final ConfigurationSection config;

    protected boolean isExpired = false;
    protected TaskMode taskMode;
    protected LocalDateTime expiredTime;

    public ScheduleTask(String path, String name) {
        this.name = name;
        task = path.split("\\.")[0];

        Main plugin = Main.getInstance();

        config = Objects.requireNonNull(plugin.getConfig().getConfigurationSection(path), "配置文件路径 " + path + " 加载失败, 请检查配置文件");
        taskMode = TaskMode.of(config.getString("时间设置", ""));
        expiredTime = getExpiredTime();
    }

    // 到期后运行的任务
    public abstract void runTask();

    public boolean isExpired() {

        if (LocalDateTime.now().isAfter(expiredTime)) {
            setExpired(true);
        }

        return isExpired;
    }

    public LocalDateTime getExpiredTime() {

        String time = config.getString("到期时间", "");
        // 匹配格式: 2021-12-03, 12:00:00
        Pattern pattern = Pattern.compile("^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29), (?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss");

        // 如果找到且能用, 返回正确时间, 如果模式能保存, 储存时间, 再找一次, 如果不能, 直接返回准确时间
        if (pattern.matcher(time).find()) {
            return LocalDateTime.parse(time, formatter);
        } else if (taskMode instanceof Preservable) {

            Preservable preservableTask = (Preservable) taskMode;
            String expiredTime = preservableTask.getExpiredTime().format(formatter);

            preservableTask.saveExpiredTime(config.getCurrentPath(), expiredTime);
            return LocalDateTime.parse(expiredTime, formatter);
        }

        return taskMode.getExpiredTime();
    }

    public void load() {
        Main.getInstance().getTaskManager().addTask(getFullTaskName(), this);
    }

    public void setExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }

    public void taskComplete() {
        setExpired(false);
        expiredTime = getExpiredTime();
    }

    public String getName() {
        return name;
    }

    public String getFullTaskName() {
        return task + "-" + name;
    }
}
