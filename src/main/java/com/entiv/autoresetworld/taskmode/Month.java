package com.entiv.autoresetworld.taskmode;

import com.entiv.autoresetworld.taskmode.TaskMode;

import java.time.LocalDateTime;

public class Month extends TaskMode implements Preservable {

    Month(String config) {
        super(config);
    }

    @Override
    public LocalDateTime getExpiredTime() {

        int dayOfMonth = Integer.parseInt(setting);
        LocalDateTime expiredTime = LocalDateTime.now().withDayOfMonth(dayOfMonth).with(localTime);

        if (LocalDateTime.now().isAfter(expiredTime)) {
            return expiredTime.plusMonths(1);
        }

        return expiredTime;
    }
}
