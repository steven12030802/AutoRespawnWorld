package com.entiv.autoresetworld.taskmode;

import com.entiv.autoresetworld.taskmode.TaskMode;

import java.time.LocalDateTime;

public class Minute extends TaskMode {

    Minute(String config) {
        super(config);
    }

    @Override
    public LocalDateTime getExpiredTime() {
        int minute = Integer.parseInt(setting);
        return LocalDateTime.now().plusMinutes(minute);
    }
}
