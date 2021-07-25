package com.entiv.autoresetworld;

import com.entiv.autoresetworld.scheduletask.ScheduleConfig;
import com.entiv.autoresetworld.scheduletask.ScheduleTask;
import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class RegenWorldTask implements ScheduleTask {

    private final String name;
    private final World world;
    private final ScheduleConfig scheduleConfig;
    private final ConfigurationSection section;

    public RegenWorldTask(String name) {
        this.name = name;
        this.world = Bukkit.getWorld(name);

        String path = "自动刷新世界." + name;

        section = Main.getInstance().getConfig().getConfigurationSection(path);
        scheduleConfig = new ScheduleConfig(path);
    }

    @Override
    public boolean isExpired() {
        return scheduleConfig.isExpired();
    }

    @Override
    public void runTask() {
        world.getPlayers().forEach(player -> player.performCommand("spawn"));
        regenWorld();
    }

    public List<String> getResetCommand() {
        return section.getStringList("刷新执行指令");
    }

    public void regenWorld() {

        MultiverseCore multiverseCore = Main.getMultiverseCore();
        boolean changeSeed = section.getBoolean("更换种子");

        String seed;

        if (changeSeed) {
            seed = "";
        } else {
            seed = String.valueOf(world.getSeed());
        }

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            Message.sendConsole("&a━━━━━━━━━━━━━━  &e正在自动刷新 " + name + " 世界  &a━━━━━━━━━━━━━━");
            Message.sendConsole(" ");

            boolean regenSuccess = multiverseCore.getMVWorldManager().regenWorld(name, true, true, seed);

            if (regenSuccess) {
                runResetCommand();
            } else {
                Message.sendConsole("&9&l" + Main.getInstance().getName() + "&6&l>> &c世界重置失败, 请检查是否是主世界, 主世界无法刷新");
            }

            scheduleConfig.setupNextScheduleTaskTime();

            Message.sendConsole(" ");
            Message.sendConsole("&a━━━━━━━━━━━━━━  &e世界 " + name + " 自动刷新完毕  &a━━━━━━━━━━━━━━");
        }, 20);
    }

    private void runResetCommand() {

        for (String command : getResetCommand()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%world%", world.getName()));
        }
    }
}
