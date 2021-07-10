package com.entiv.autorespawnworld;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class DeleteManager {

    public ConfigurationSection getConfig() {
        FileConfiguration config = Main.getInstance().getConfig();
        return config.getConfigurationSection("自动删除文件");
    }
}
