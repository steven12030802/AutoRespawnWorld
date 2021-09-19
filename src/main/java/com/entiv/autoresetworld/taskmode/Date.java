package com.entiv.autoresetworld.taskmode;

import com.entiv.autoresetworld.taskmode.Preservable;
import com.entiv.autoresetworld.taskmode.TaskMode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Date extends TaskMode implements Preservable {

    public Date(String setting) {
        super(setting);
    }

    @Override
    public LocalDateTime getExpiredTime() {

        String[] split = setting.split("-");

        int month = Integer.parseInt(split[0]);
        int day = Integer.parseInt(split[1]);

        LocalDate date = LocalDate.of(LocalDate.now().getYear(), month, day);
        LocalDateTime expiredTime = LocalDateTime.of(date, localTime);

        if (LocalDateTime.now().isAfter(expiredTime)) {
            return expiredTime.plusYears(1);
        }

        return expiredTime;
    }


}
