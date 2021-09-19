package com.entiv.autoresetworld.taskmode;

import com.entiv.autoresetworld.taskmode.TaskMode;

import java.time.LocalDateTime;

public class Hour extends TaskMode {
    Hour(String config) {
        super(config);
    }

    @Override
    public LocalDateTime getExpiredTime() {
        int hour = Integer.parseInt(setting);
        return LocalDateTime.now().plusHours(hour);
    }
}
