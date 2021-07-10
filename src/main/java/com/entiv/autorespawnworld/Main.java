package com.entiv.autorespawnworld;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main plugin;
    private static MultiverseCore multiverseCore;

    public static Main getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        multiverseCore = (MultiverseCore) Main.getInstance().getServer().getPluginManager().getPlugin("Multiverse-Core");

        String[] message = {
                "&e&l" + getName() + "&a 插件&e v" + getDescription().getVersion() + " &a已启用",
                "&a插件制作作者:&e EnTIv &aQQ群:&e 600731934"
        };
        Message.sendConsole(message);

        saveDefaultConfig();
        PluginCommand command = Bukkit.getPluginCommand("AutoRespawnWorld");

        if (command != null) {
            command.setExecutor(new MainCommand());
        }

        setupRespawnWorld();
        setupRespawnRunnable();
    }

    @Override
    public void onDisable() {
        String[] message = {
                "&e&l" + getName() + "&a 插件&e v" + getDescription().getVersion() + " &a已卸载",
                "&a插件制作作者:&e EnTIv &aQQ群:&e 600731934"
        };
        Message.sendConsole(message);
    }

    private void setupRespawnWorld() {
        ConfigurationSection section = getConfig().getConfigurationSection("自动刷新世界");
        if (section == null) throw new NullPointerException("配置文件错误, 请检查配置文件");

        for (String worldName : section.getKeys(false)) {

            World world = Bukkit.getWorld(worldName);

            if (world == null) {
                Message.sendConsole("&c世界 &e" + worldName + "&c 不存在, 请检查配置文件");
                continue;
            }

            RespawnWorld respawnWorld = new RespawnWorld(worldName);
            respawnWorld.load();

            if (respawnWorld.dateConfig.getRespawnDateTime() == null) {
                respawnWorld.dateConfig.setupTriggerTime();
            }
        }
    }

    public static MultiverseCore getMultiverseCore() {
        return multiverseCore;
    }

    private void setupRespawnRunnable() {
        RespawnRunnable respawnRunnable = new RespawnRunnable();
        respawnRunnable.runTaskTimer(this, 0, 100);
    }
}

