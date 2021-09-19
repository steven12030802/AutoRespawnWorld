package com.entiv.autoresetworld.taskmode;

import com.entiv.autoresetworld.Main;

import java.time.LocalDateTime;

public interface Preservable {

    default void saveExpiredTime(String path, String time) {
        Main.getInstance().getConfig().set(path + ".到期时间", time);
        Main.getInstance().saveConfig();
    }

    LocalDateTime getExpiredTime();
}
