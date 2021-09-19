package com.entiv.autoresetworld.taskmode;

import java.time.LocalDateTime;

public class Day extends TaskMode implements Preservable {

    Day(String config) {
        super(config);
    }

    @Override
    public LocalDateTime getExpiredTime() {

        int day = Integer.parseInt(setting);
        LocalDateTime expiredTime = LocalDateTime.now().with(localTime);

        if (LocalDateTime.now().isAfter(expiredTime)) {
            return expiredTime.plusDays(day);
        }

        return expiredTime;
    }
}
