package com.entiv.autoresetworld.scheduletask;

import java.util.ArrayList;
import java.util.List;

public interface ScheduleTask {

    List<ScheduleTask> scheduleTasks = new ArrayList<>();

    // 到期后运行的任务
    void runTask();

    boolean isExpired();

    default void load() {
        scheduleTasks.add(this);
    }

    void setExpired(boolean expired);

    String getName();
}
