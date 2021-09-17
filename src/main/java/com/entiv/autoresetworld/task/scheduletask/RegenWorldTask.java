package com.entiv.autoresetworld.task.scheduletask;

import com.entiv.autoresetworld.Main;
import com.entiv.autoresetworld.utils.Message;
import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.List;
import java.util.Objects;

public class RegenWorldTask extends ScheduleTask {

    private final World world;

    public RegenWorldTask(String name) {
        super("自动刷新世界." + name, name);
        this.world = Objects.requireNonNull(Bukkit.getWorld(name), "世界 " + name + " 不存在, 请检查配置文件");
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
        MultiverseCore multiverseCore = Main.getInstance().getMultiverseCore();
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

            Message.sendConsole(" ");
            Message.sendConsole("&a━━━━━━━━━━━━━━  &e世界 " + name + " 自动刷新完毕  &a━━━━━━━━━━━━━━");
        }, 100);
    }

    private void runResetCommand() {

        for (String command : getResetCommand()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%world%", world.getName()));
        }

    }
}