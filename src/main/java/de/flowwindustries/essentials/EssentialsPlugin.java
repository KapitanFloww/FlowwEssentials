package de.flowwindustries.essentials;

import de.flowwindustries.essentials.commands.smoke.SmokeCommand;
import de.flowwindustries.essentials.commands.fly.FlyCommand;
import de.flowwindustries.essentials.commands.fly.SpeedCommand;
import de.flowwindustries.essentials.commands.gamemode.GamemodeCommand;
import de.flowwindustries.essentials.commands.spawn.SetSpawnCommand;
import de.flowwindustries.essentials.commands.spawn.SpawnCommand;
import de.flowwindustries.essentials.commands.time.DayCommand;
import de.flowwindustries.essentials.commands.time.NightCommand;
import de.flowwindustries.essentials.commands.time.TimeCommand;
import de.flowwindustries.essentials.configuration.DefaultConfiguration;
import de.flowwindustries.essentials.event.messages.PlayerConnectMessageListener;
import lombok.Getter;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Plugin Main class.
 */
@Log
public final class EssentialsPlugin extends JavaPlugin {

    /**
     * Singleton plugin instance.
     */
    @Getter
    private static EssentialsPlugin pluginInstance;

    @Getter
    private FileConfiguration configuration;
    
    public static final String PREFIX = "§7[§aFloww§4Essentials§7] ";

    /**
     * Startup logic.
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
        pluginInstance = this;
        String pluginVersion = pluginInstance.getDescription().getVersion();

        setupConfig();
        setupCommands();
        setupListener();

        log.info(PREFIX + "Initialization complete. Running version: " + pluginVersion);
    }

    /**
     * Shutdown logic.
     */
    @Override
    public void onDisable() {

        // Persist Smoke Locations
        SmokeCommand.persistLocations();

        this.configuration = null;
        pluginInstance = null;
        log.info(PREFIX + "Shutdown complete.");
    }

    private void setupConfig() {
        this.configuration = getConfig();
        DefaultConfiguration.setupDefaultConfiguration(configuration);
        pluginInstance.saveConfig();
    }

    private void setupCommands() {
        getCommand("gm").setExecutor(new GamemodeCommand("floww.gm"));
        getCommand("time").setExecutor(new TimeCommand("floww.time"));
        getCommand("day").setExecutor(new DayCommand("day", "floww.time"));
        getCommand("night").setExecutor(new NightCommand("night", "floww.time"));
        getCommand("setspawn").setExecutor(new SetSpawnCommand("floww.setspawn"));
        getCommand("spawn").setExecutor(new SpawnCommand("floww.spawn"));
        getCommand("fly").setExecutor(new FlyCommand("floww.fly"));
        getCommand("speed").setExecutor(new SpeedCommand("floww.speed"));
        getCommand("smoke").setExecutor(new SmokeCommand("floww.smoke"));
    }

    private void setupListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerConnectMessageListener(), this);
    }
}
