package com.entiv.autorespawnworld;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnRunnable extends BukkitRunnable {

    @Override
    public void run() {
        for (RespawnWorld loadedWorld : RespawnWorld.getLoadedWorlds()) {
            if (loadedWorld.dateConfig.isExpired()) {
                loadedWorld.respawnWorld();
            }
        }
    }

}
