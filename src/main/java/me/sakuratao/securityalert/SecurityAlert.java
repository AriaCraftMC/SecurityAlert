package me.sakuratao.securityalert;

import lombok.Getter;
import me.sakuratao.securityalert.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SecurityAlert extends JavaPlugin {

    @Getter
    public FileConfiguration config;

    public static SecurityAlert INSTANCE;

    @Override
    public void onEnable() {

        INSTANCE = this;

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(), this);

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) saveDefaultConfig();
        config = YamlConfiguration.loadConfiguration(configFile);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
