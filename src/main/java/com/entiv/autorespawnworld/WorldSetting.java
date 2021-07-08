package com.entiv.autorespawnworld;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public class WorldSetting {

    public final String name;

    public WorldSetting(String name) {
        this.name = name;
    }

    public ConfigurationSection getConfig() {

        FileConfiguration config = Main.getInstance().getConfig();

        ConfigurationSection section = config.getConfigurationSection("自动刷新世界." + name);

        if (section == null) throw new NullPointerException("世界" + name + "不存在, 请检查配置文件!");

        return section;
    }

    public World getWorld() {
        return Bukkit.getWorld(name);
    }

    public List<String> getGameRuleSettings() {
        return getConfig().getStringList("游戏规则设置");
    }

    public void setupRespawnDate() {

        String dateConfig = getConfig().getString("刷新日期设置");
        String timeConfig = getConfig().getString("刷新时间设置");

        if (dateConfig == null || timeConfig == null) throw new NullPointerException("配置文件有误! 请检查配置文件!");

        String[] dateSetting = dateConfig.split(",");
        String[] timeSetting = timeConfig.split(":");

        String mode = dateSetting[0];
        int day = Integer.parseInt(dateSetting[1]);

        int hour = Integer.parseInt(timeSetting[0]);
        int minute = Integer.parseInt(timeSetting[1]);

        LocalDateTime respawnTime = calculateRespawnTime(mode, day, hour, minute);

        String format = String.format("%d,%d,%d,%d,%d", respawnTime.getYear(), respawnTime.getMonthValue(), respawnTime.getDayOfMonth(), respawnTime.getHour(), respawnTime.getMinute());

        getConfig().set("下次刷新时间", format);
        Main.getInstance().saveConfig();
    }

    public LocalDateTime getRespawnDateTime() {

        String date = getConfig().getString("下次刷新时间");

        if (date == null || date.equals("-1")) return null;

        String[] split = date.split(",");

        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);
        int hour = Integer.parseInt(split[3]);
        int minute = Integer.parseInt(split[4]);

        return LocalDateTime.of(year, month, day, hour, minute);
    }

    public boolean isExpired() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(getRespawnDateTime());
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
