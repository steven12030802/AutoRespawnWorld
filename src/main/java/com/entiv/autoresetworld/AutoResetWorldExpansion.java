package com.entiv.autoresetworld;

import com.entiv.autoresetworld.task.ScheduleTask;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.*;

public class AutoResetWorldExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "AutoResetWorld";
    }

    @Override
    public @NotNull String getAuthor() {
        return "EnTIv";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (!params.startsWith("countdown")) return null;

        String task = params.substring("countdown".length() + 1);
        ScheduleTask scheduleTask = Main.getInstance().getTaskManager().getTask(task);

        long start = System.currentTimeMillis();
        long end = scheduleTask.getExpiredTime().toEpochSecond(ZoneOffset.of("+8")) * 1000;

        return getInterval(start, end);
    }

    public String getInterval(long start, long end) {

        long interval = end - start;
        long day, hour, minute, second;

        interval /= 1000;

        day = interval / 60 / 60 / 24;
        hour = interval / 60 / 60 % 24;
        minute = interval / 60 % 60;
        second = interval % 60;

        String countdown = String.format("%s 天 %s 时 %s 分 %s 秒", day, hour, minute, second);

        while (countdown.startsWith("0")) {
            countdown = countdown.replaceFirst("0 [天时分秒]", "");
        }

        return countdown;
    }
}
