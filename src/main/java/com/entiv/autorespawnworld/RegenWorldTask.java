package com.entiv.autorespawnworld;

import com.entiv.autorespawnworld.scheduletask.ScheduleConfig;
import com.entiv.autorespawnworld.scheduletask.ScheduleTask;
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
        regenWorld();
    }

    public List<String> getGameRuleSettings() {
        return section.getStringList("游戏规则设置");
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

        Message.sendConsole("&a━━━━━━━━━━━━━━  &e正在自动刷新 " + name + " 世界  &a━━━━━━━━━━━━━━");
        Message.sendConsole(" ");

        boolean regenSuccess = multiverseCore.getMVWorldManager().regenWorld(name, true, true, seed);

        if (regenSuccess) {
            setWorldRule();
            scheduleConfig.setupNextScheduleTaskTime();
        }

        Message.sendConsole(" ");
        Message.sendConsole("&a━━━━━━━━━━━━━━  &e世界 " + name + " 自动刷新完毕  &a━━━━━━━━━━━━━━");

    }

    private void setWorldRule() {

        for (String string : getGameRuleSettings()) {

            String[] gameRules = string.split(",");

            String name = gameRules[0];
            String value = gameRules[1];

            world.setGameRuleValue(name, value);

            //region ================ 1.13-version ================

//            GameRule<?> gamerule = GameRule.getByName(name);
//
//            if (gamerule == null) {
//                throw new NullPointerException("找不到名为" + name + "的游戏规则, 请检查配置文件");
//            }
//
//            if (gamerule.getType() == Boolean.class) {
//
//                @SuppressWarnings("unchecked")
//                GameRule<Boolean> byName = (GameRule<Boolean>) gamerule;
//
//                world.setGameRule(byName, Boolean.valueOf(value));
//
//            } else if (gamerule.getType() == Integer.class) {
//
//                @SuppressWarnings("unchecked")
//                GameRule<Integer> byName = (GameRule<Integer>) gamerule;
//
//                world.setGameRule(byName, Integer.valueOf(value));
//
//            } else {
//
//                throw new Error("游戏规则设置异常, 请告知作者");
//
//            }
            //endregion
        }
    }
}
