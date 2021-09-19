package com.entiv.autoresetworld.taskmode;

import com.entiv.autoresetworld.taskmode.TaskMode;

import java.time.LocalDateTime;

public class Second extends TaskMode {

    Second(String config) {
        super(config);
    }

    @Override
    public LocalDateTime getExpiredTime() {
        int second = Integer.parseInt(setting);
        return LocalDateTime.now().plusSeconds(second);
    }
}
