package com.entiv.autoresetworld.task.scheduletask;

import org.bukkit.Bukkit;

import java.util.List;

public class CommandTask extends ScheduleTask {

    public CommandTask(String name) {
        super("自动执行指令." + name, name);
    }

    @Override
    public void runTask() {
        runCommand();
    }

    public List<String> getCommands() {
        return section.getStringList("执行指令");
    }

    private void runCommand() {
        for (String command : getCommands()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
}
