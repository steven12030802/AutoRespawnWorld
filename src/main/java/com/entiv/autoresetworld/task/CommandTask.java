package com.entiv.autoresetworld.task;

import com.entiv.autoresetworld.Main;
import com.entiv.autoresetworld.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.util.List;

public class CommandTask extends ScheduleTask {

    public CommandTask(String name) {
        super("自动执行任务." + name, name);
    }

    @Override
    public void runTask() {
        runCommand();
    }

    public List<String> getCommands() {
        return config.getStringList("执行任务");
    }

    private void runCommand() {

        List<String> commands = getCommands();

        if (commands == null || commands.isEmpty()) return;

        for (String string : commands) {
            int separator = string.indexOf(":");

            if (separator == -1) {
                Message.sendConsole("&9&l" + Main.getInstance().getName() + "&6&l >> &c" + "执行任务&e " + string + "&c 配置错误, 请检查配置文件");
                return;
            }

            String type = string.substring(0, separator);
            String command = string.substring(separator + 1).trim();

            ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();

            switch (type) {

                case "console":

                    Bukkit.dispatchCommand(consoleSender, command);
                    break;

                case "broadcast":

                    Bukkit.broadcastMessage(command);
                    break;

                default:
                    Message.sendConsole("&9&l" + Main.getInstance().getName() + "&6&l >> &c" + "命令类型 " + type + " 不存在, 请检查配置文件");
            }
        }
    }
}
