package com.entiv.autoresetworld.taskmode;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Pattern;

//TODO 整一个记录时间的接口, 刷新完毕时判断是否时记录时间的接口, 是就强转并save
public abstract class TaskMode {

    protected final String name;
    protected final String setting;

    protected LocalTime localTime;

    // second:30 代表每 30 秒刷新一次
    // minute:10 代表每 10 分钟刷新一次
    // hour:3 代表每 3 小时刷新一次
    //
    // date:10-01, 7:56:00 代表每年的 10 月 1 日 7:56 刷新一次
    //
    // day:1, 20:00:00 代表每天 20:00 刷新一次
    // day:14, 0:00:00 代表每 14 天的 0:00 刷新一次
    // week:4, 23:59:00 代表每周 4 的 23:59 刷新一次
    // week:1, 6:00:00 代表每周 1 的 6:00 刷新一次
    // month:15, 12:34:45 代表每月 15 日的 12:34:45 刷新一次
    // year:233, 0:00:00 代表每年的第 233 天的 0:00:00 刷新一次

    public TaskMode(String config) {

        if (config.contains(",")) {
            String[] split = config.split(",");
            Pattern pattern = Pattern.compile("^(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$");

            String localTimeFormat = split[1].trim();

            if (!pattern.matcher(localTimeFormat).find()) {
                throw new IllegalArgumentException("时间参数 " + localTimeFormat + " 异常, 请确认格式为 hh:mm:ss, 如 07:56:03");
            }

            localTime = LocalTime.parse(localTimeFormat);
            config = split[0];
        }

        name = config.split(":")[0];
        setting = config.split(":")[1];
    }

    public abstract LocalDateTime getExpiredTime();

    public static TaskMode of(String config) {

        if (config.startsWith("second")) return new Second(config);
        if (config.startsWith("minute")) return new Minute(config);
        if (config.startsWith("hour")) return new Hour(config);
        if (config.startsWith("date")) return new Date(config);
        if (config.startsWith("day")) return new Day(config);
        if (config.startsWith("week")) return new Week(config);
        if (config.startsWith("month")) return new Month(config);
        if (config.startsWith("year")) return new Year(config);

        throw new IllegalArgumentException("日期设置配置错误, 请检查配置文件");
    }
}
