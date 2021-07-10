package com.entiv.autorespawnworld;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class RespawnWorld {

    public final String name;
    public final DateConfig dateConfig;
    private static final List<RespawnWorld> worlds = new ArrayList<>();

    public RespawnWorld(String name) {
        this.name = name;
        dateConfig = new DateConfig(name);
    }

    public static List<RespawnWorld> getLoadedWorlds() {
        return worlds;
    }

    public void load() {
        worlds.add(this);
    }

    public World getWorld() {
        return Bukkit.getWorld(name);
    }

    public List<String> getGameRuleSettings() {
        return dateConfig.getConfig().getStringList("游戏规则设置");
    }

    public void respawnWorld() {
        MultiverseCore multiverseCore = Main.getMultiverseCore();

        boolean changeSeed = dateConfig.getConfig().getBoolean("更换种子");

        String seed;

        if (changeSeed) {
            seed = "";
        } else {
            seed = String.valueOf(getWorld().getSeed());

        }

        boolean regenSuccess = multiverseCore.getMVWorldManager().regenWorld(name, true, true, seed);

        if (regenSuccess) {
            setWorldRule();
            dateConfig.setupTriggerTime();
        }
    }

    private void setWorldRule() {

        World world = getWorld();

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
