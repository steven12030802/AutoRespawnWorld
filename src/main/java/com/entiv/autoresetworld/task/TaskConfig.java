package com.entiv.autoresetworld.task;

import com.entiv.autoresetworld.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;

public class TaskConfig {

    String path;

    public TaskConfig(String path) {
        this.path = path;
    }

    public ConfigurationSection getConfig() {
        FileConfiguration config = Main.getInstance().getConfig();
        ConfigurationSection section = config.getConfigurationSection(path);

        if (section == null) throw new NullPointerException("配置文件错误, 路径 " + path + " 不存在, 请检查配置文件!");

        return section;
    }

    public LocalDateTime getScheduleDateTime() {

        String scheduleTime = getConfig().getString("触发时间", "-1");

        if (scheduleTime.equals("-1")) return null;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss");
        LocalDateTime scheduleDateTime;

        try {

            scheduleDateTime = LocalDateTime.parse(scheduleTime, dateTimeFormatter);

        } catch (DateTimeParseException e) {
            setupNextScheduleTaskTime();
            scheduleTime = getConfig().getString("触发时间", "-1");
            scheduleDateTime = LocalDateTime.parse(scheduleTime, dateTimeFormatter);
            Bukkit.getLogger().log(Level.WARNING, "AutoResetWorld 触发时间错误, 已重置触发时间");

        }

        return scheduleDateTime;
    }

    public boolean isExpired() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime respawnDateTime = getScheduleDateTime();

        if (respawnDateTime == null) {
            setupNextScheduleTaskTime();
            return false;
        }

        return now.isAfter(respawnDateTime);
    }

    public void setupNextScheduleTaskTime() {

        String config = getConfig().getString("日期设置");

        if (config == null) throw new NullPointerException("配置文件有误! 请检查配置文件!");

        int index = config.indexOf(",");

        String mode = config.substring(0, config.indexOf(",")).substring(0, config.indexOf(":"));
        String modeConfig = config.substring(0, config.indexOf(",")).split(":")[1];

        LocalTime time = LocalTime.parse(config.substring(index + 1).trim(), DateTimeFormatter.ofPattern("hh:mm:ss"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss");


        if (mode.equalsIgnoreCase("second")) {

            int second = Integer.parseInt(modeConfig);
            LocalDateTime taskTime = LocalDateTime.now().plusSeconds(second);
            getConfig().set("触发时间", taskTime.format(formatter));

        } else if (mode.equalsIgnoreCase("minute")) {

            int amount = Integer.parseInt(modeConfig);
            LocalDateTime taskTime = LocalDateTime.now().plusMinutes(amount);
            getConfig().set("触发时间", taskTime.format(formatter));

        } else if (mode.equalsIgnoreCase("hour")) {

            int hour = Integer.parseInt(modeConfig);
            LocalDateTime taskTime = LocalDateTime.now().plusHours(hour);
            getConfig().set("触发时间", taskTime.format(formatter));

        } else if (mode.equalsIgnoreCase("day")) {

            int day = Integer.parseInt(modeConfig);
            LocalDateTime taskTime = LocalDateTime.now().plusDays(day).with(time);
            getConfig().set("触发时间", taskTime.format(formatter));

        } else if (mode.equalsIgnoreCase("week")) {

            int dayOfWeek = Integer.parseInt(modeConfig);
            LocalDateTime taskTime = LocalDateTime.now()
                    .plusWeeks(1)
                    .with(DayOfWeek.of(dayOfWeek))
                    .with(time);

            getConfig().set("触发时间", taskTime.format(formatter));

        } else if (mode.equalsIgnoreCase("month")) {

            int month = Integer.parseInt(modeConfig);
            LocalDateTime taskTime = LocalDateTime.now()
                    .plusMonths(1)
                    .withDayOfMonth(month)
                    .with(time);

            getConfig().set("触发时间", taskTime.format(formatter));


        } else if (mode.equalsIgnoreCase("year")) {

            int dayOfYear = Integer.parseInt(modeConfig);
            LocalDateTime taskTime = LocalDateTime.now()
                    .plusYears(1)
                    .withDayOfYear(dayOfYear)
                    .with(time);

            getConfig().set("触发时间", taskTime.format(formatter));



        } else if (mode.equalsIgnoreCase("date")) {

            LocalDate date = LocalDate.parse(modeConfig);

            LocalDateTime taskTime = LocalDateTime.now()
                    .withMonth(date.getMonthValue())
                    .withDayOfMonth(date.getDayOfMonth())
                    .withHour(time.getHour())
                    .withMinute(time.getMinute())
                    .withSecond(time.getSecond());

            getConfig().set("触发时间", taskTime.format(formatter));
        }

        Main.getInstance().saveConfig();
    }
}
