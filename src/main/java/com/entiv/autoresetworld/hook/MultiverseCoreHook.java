package com.entiv.autoresetworld.hook;

import com.onarandombox.MultiverseCore.MultiverseCore;

import static org.bukkit.Bukkit.getServer;

public class MultiverseCoreHook {

    public static boolean isEnable() {
        return getServer().getPluginManager().getPlugin("Multiverse-Core") != null;
    }

    public static MultiverseCore getPlugin() {
        return (MultiverseCore) getServer().getPluginManager().getPlugin("Multiverse-Core");
    }
}
