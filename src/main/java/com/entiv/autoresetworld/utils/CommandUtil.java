package com.entiv.autoresetworld.utils;

import com.entiv.autoresetworld.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandUtil {

    public static void execute(Player player, List<String> commands) {

        if (commands == null || commands.isEmpty()) return;

        for (String string : commands) {
            int separator = string.indexOf(":");
            String type = string.substring(0, separator);
            String command = string.substring(separator + 1).replace("%player%", player.getName()).trim();

            ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();

            System.out.println(type);
            switch (type) {

                case "console":

                    Bukkit.dispatchCommand(consoleSender, command);
                    break;

                case "player":

                    System.out.println(command);
                    player.performCommand(command);
                    break;

                case "op":

                    if (player.isOp()) {
                        player.performCommand(command);
                    } else {
                        player.setOp(true);
                        player.performCommand(command);
                        player.setOp(false);
                    }

                    break;

                default:

                    Message.sendConsole("&9&l" + Main.getInstance().getName() + "&6&l >> &c" + "命令类型 " + type + " 不存在, 请检查配置文件");
            }
        }
    }
}
