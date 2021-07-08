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

        setupRespawnTime();
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


    private void setupRespawnTime() {
        ConfigurationSection section = getConfig().getConfigurationSection("自动刷新世界");
        if (section == null) throw new NullPointerException("配置文件错误, 请检查配置文件");

        for (String world : section.getKeys(false)) {
            WorldSetting worldSetting = new WorldSetting(world);

            if (worldSetting.getRespawnDateTime() == null) {
                worldSetting.setupRespawnDate();
            }
        }
    }

    public static MultiverseCore getMultiverseCore() {
        return multiverseCore;
    }

    private void setupRespawnRunnable() {
        ConfigurationSection section = getConfig().getConfigurationSection("自动刷新世界");

        if (section == null) throw new NullPointerException("配置有误, 请检查配置文件");

        for (String name : section.getKeys(false)) {

            World world = Bukkit.getWorld(name);

            if (world == null) {
                Bukkit.getLogger().warning("世界" + name + "不存在, 请检查配置文件");
                continue;
            }

            RespawnRunnable respawnRunnable = new RespawnRunnable(new WorldSetting(name));
            respawnRunnable.runTaskTimer(this, 0, 100);
        }
    }
}
