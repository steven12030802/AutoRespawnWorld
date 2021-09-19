package com.entiv.autoresetworld.taskmode;

import com.entiv.autoresetworld.taskmode.TaskMode;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class Year extends TaskMode implements Preservable {

    Year(String config) {
        super(config);
    }

    @Override
    public LocalDateTime getExpiredTime() {

        int dayOfYear = Integer.parseInt(setting);
        LocalDateTime expiredTime = LocalDateTime.now().withDayOfYear(dayOfYear).with(localTime);

        if (LocalDateTime.now().isAfter(expiredTime)) {
            return expiredTime.plusYears(1);
        }
        return expiredTime;
    }
}
