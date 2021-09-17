package com.entiv.autoresetworld;

import com.entiv.autoresetworld.task.TaskManager;
import com.entiv.autoresetworld.task.TaskRunnable;
import com.entiv.autoresetworld.task.scheduletask.ScheduleTask;
import com.entiv.autoresetworld.utils.Message;
import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main extends JavaPlugin {

    private static Main plugin;
    private static MultiverseCore multiverseCore;
    private static TaskManager taskManager;

    public static Main getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        multiverseCore = (MultiverseCore) Main.getInstance().getServer().getPluginManager().getPlugin("Multiverse-Core");
        taskManager = new TaskManager();

        String[] message = {
                "&e&l" + getName() + "&a 插件&e v" + getDescription().getVersion() + " &a已启用",
                "&a插件制作作者:&e EnTIv &aQQ群:&e 600731934"
        };
        Message.sendConsole(message);

        saveDefaultConfig();

        taskManager.loadScheduleTask();
        TaskRunnable.load();

        Objects.requireNonNull(getCommand("AutoResetWorld")).setTabCompleter(this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new AutoResetWorldExpansion().register();
        }
    }

    @Override
    public void onDisable() {
        String[] message = {
                "&e&l" + getName() + "&a 插件&e v" + getDescription().getVersion() + " &a已卸载",
                "&a插件制作作者:&e EnTIv &aQQ群:&e 600731934"
        };
        Message.sendConsole(message);
    }

    public MultiverseCore getMultiverseCore() {
        return multiverseCore;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.isOp()) return true;

        if (args.length == 0) {
            Message.send(sender,
                    "",
                    "&6━━━━━━━━━━━━━━&e  自动刷新世界指令帮助  &6━━━━━━━━━━━━━━",
                    "",
                    "&b ━ &a/asw reload &7重载配置文件",
                    "&b ━ &a/asw runtask 世界名 &7手动刷新世界"
            );
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            Main plugin = Main.getInstance();
            plugin.reloadConfig();

            taskManager.clearTasks();
            taskManager.loadScheduleTask();

            Message.send(sender, "&9&l" + plugin.getName() + "&6&l >> &a配置文件重载完毕");

            return true;
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("runTask"))) {

            String name = args[1];
            ScheduleTask task = taskManager.getTask(name);

            if (task == null) {
                Message.send(sender, "&9&l" + plugin.getName() + "&6&l >> &c检测不到名为 &b" + name + "&c 的自动刷新任务!");
                return true;
            }

            task.setExpired(true);

            return true;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (args.length == 1) {
            return Stream.of("reload", "runtask")
                    .filter(s -> s.toLowerCase().startsWith(args[0]))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            return taskManager.getScheduleTasks().stream()
                    .map(ScheduleTask::getName)
                    .filter(s -> s.toLowerCase().startsWith(args[1]))
                    .collect(Collectors.toList());
        }
        return super.onTabComplete(sender, command, alias, args);
    }
}

