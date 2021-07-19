package com.entiv.autoresetworld.scheduletask;

import com.entiv.autoresetworld.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class ScheduleConfig {

    String path;

    public ScheduleConfig(String path) {
        this.path = path;
    }

    public String getDateConfig() {
        return getConfig().getString("日期设置");
    }

    public String getTimeConfig() {
        return getConfig().getString("时间设置");
    }

    public String getScheduleTime() {
        return getConfig().getString("触发时间");
    }

    public ConfigurationSection getConfig() {
        FileConfiguration config = Main.getInstance().getConfig();
        ConfigurationSection section = config.getConfigurationSection(path);

        if (section == null) throw new NullPointerException("配置文件错误, 请检查配置文件!");

        return section;
    }


    public LocalDateTime getScheduleDateTime() {

        String scheduleTime = getScheduleTime();

        if (scheduleTime == null || scheduleTime.equals("-1")) return null;

        String[] split = scheduleTime.split(",");

        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);
        int hour = Integer.parseInt(split[3]);
        int minute = Integer.parseInt(split[4]);

        return LocalDateTime.of(year, month, day, hour, minute);
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

        String timeConfig = getTimeConfig();
        String dateConfig = getDateConfig();

        if (dateConfig == null || timeConfig == null) throw new NullPointerException("配置文件有误! 请检查配置文件!");

        String[] dateSetting = dateConfig.split(",");
        String[] timeSetting = timeConfig.split(":");

        String mode = dateSetting[0];
        int day = Integer.parseInt(dateSetting[1]);

        int hour = Integer.parseInt(timeSetting[0]);
        int minute = Integer.parseInt(timeSetting[1]);

        LocalDateTime respawnTime = calculateRespawnTime(mode, day, hour, minute);

        String format = String.format("%d,%d,%d,%d,%d", respawnTime.getYear(), respawnTime.getMonthValue(), respawnTime.getDayOfMonth(), respawnTime.getHour(), respawnTime.getMinute());

        getConfig().set("触发时间", format);
        Main.getInstance().saveConfig();
    }

    private LocalDateTime calculateRespawnTime(String mode, int day, int hour, int minute) {

        LocalDateTime today = LocalDateTime.now();

        if (mode.equalsIgnoreCase("day")) {

            return today.plusDays(day)
                    .withHour(hour)
                    .withMinute(minute);

        } else if (mode.equalsIgnoreCase("week")) {

            return today.plusWeeks(1)
                    .with(DayOfWeek.of(day))
                    .withHour(hour)
                    .withMinute(minute);

        } else if (mode.equalsIgnoreCase("month")) {

            return today.plusMonths(1)
                    .withDayOfMonth(day)
                    .withHour(hour)
                    .withMinute(minute);

        } else {
            throw new NullPointerException("刷新时间配置错误, 请检查配置文件!");
        }
    }
}
