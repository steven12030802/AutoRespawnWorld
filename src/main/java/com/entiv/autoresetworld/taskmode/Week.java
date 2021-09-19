package com.entiv.autoresetworld.taskmode;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class Week extends TaskMode implements Preservable {

    Week(String config) {
        super(config);
    }

    @Override
    public LocalDateTime getExpiredTime() {

        int dayOfWeek = Integer.parseInt(setting);
        LocalDateTime expiredTime = LocalDateTime.now()
                .with(DayOfWeek.of(dayOfWeek))
                .with(localTime);

        if (LocalDateTime.now().isAfter(expiredTime)) {
            return expiredTime.plusWeeks(1);
        }
        return expiredTime;
    }
}
