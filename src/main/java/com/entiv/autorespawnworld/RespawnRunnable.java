package com.entiv.autorespawnworld;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnRunnable extends BukkitRunnable {

    WorldSetting worldSetting;

    RespawnRunnable(WorldSetting worldSetting) {
        this.worldSetting = worldSetting;
    }

    @Override
    public void run() {
        if (worldSetting.isExpired()) {
            respawnWorld();
        }
    }

    void respawnWorld() {
        MultiverseCore multiverseCore = Main.getMultiverseCore();
        boolean regenSuccess = multiverseCore.getMVWorldManager().regenWorld(worldSetting.name, true, true, "");

        if (regenSuccess) {
            setWorldRule();
            worldSetting.setupRespawnDate();
        }
    }


    private void setWorldRule() {

        World world = worldSetting.getWorld();

        for (String string : worldSetting.getGameRuleSettings()) {

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
