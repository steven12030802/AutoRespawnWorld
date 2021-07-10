package com.entiv.autorespawnworld.scheduletask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface ScheduleTask {

    List<ScheduleTask> scheduleTasks = new ArrayList<>();

    // 到期后运行的任务
    void runTask();

    default boolean isExpired() {
        return true;
    }

    default void load() {
        scheduleTasks.add(this);
    }
}
