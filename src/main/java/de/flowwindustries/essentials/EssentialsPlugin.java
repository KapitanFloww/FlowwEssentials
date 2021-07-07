package de.flowwindustries.essentials;

import de.flowwindustries.essentials.commands.fly.FlyCommand;
import de.flowwindustries.essentials.commands.fly.SpeedCommand;
import de.flowwindustries.essentials.commands.spawn.SetspawnCommand;
import de.flowwindustries.essentials.commands.spawn.SpawnCommand;
import de.flowwindustries.essentials.commands.time.DayCommand;
import de.flowwindustries.essentials.commands.gamemode.GamemodeCommand;
import de.flowwindustries.essentials.commands.time.NightCommand;
import de.flowwindustries.essentials.commands.time.TimeCommand;
import de.flowwindustries.essentials.event.messages.PlayerConnectMessageListener;
import lombok.Getter;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Log
public final class EssentialsPlugin extends JavaPlugin {

    @Getter
    private FileConfiguration configuration = getConfig();

    @Getter
    private static EssentialsPlugin plugin;

    @Getter
    private static String prefix;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        setupDefaultProps();
        prefix = configuration.getString("plugin.prefix") +  " ";

        setupCommands();
        setupListener();

        log.info("FlowwEssentials loaded!");
    }

    @Override
    public void onDisable() {
        log.info("Shutting down...");
    }

    private void setupDefaultProps() {
        //Setup values
        configuration.addDefault("plugin.prefix", "§7[§4Floww§aEssentials§7]");

        configuration.addDefault("commands.time.day.value", 6000L);
        configuration.addDefault("commands.time.night.value", 15000L);
        configuration.addDefault("commands.time.day.message", "Let there be light!");
        configuration.addDefault("commands.time.night.message", "Darkness rises!");

        configuration.addDefault("spawn.x", 0);
        configuration.addDefault("spawn.y", 100);
        configuration.addDefault("spawn.z", 0);
        configuration.addDefault("spawn.pitch", 0);
        configuration.addDefault("spawn.yaw", 0);
        configuration.addDefault("spawn.world", "world");

        configuration.addDefault("messages.join", "§7Welcome player %s to FlowwIndustries");
        configuration.addDefault("messages.leave", "§7%s has left the server");

        //Save configuration
        configuration.options().copyDefaults(true);
        plugin.saveConfig();
    }

    private void setupCommands() {
        getCommand("gm").setExecutor(new GamemodeCommand("floww.gm"));
        getCommand("time").setExecutor(new TimeCommand("floww.time"));
        getCommand("day").setExecutor(new DayCommand("day", "floww.time"));
        getCommand("night").setExecutor(new NightCommand("night", "floww.time"));
        getCommand("setspawn").setExecutor(new SetspawnCommand(configuration, "floww.setspawn"));
        getCommand("spawn").setExecutor(new SpawnCommand(configuration, "floww.spawn"));
        getCommand("fly").setExecutor(new FlyCommand("floww.fly"));
        getCommand("speed").setExecutor(new SpeedCommand("floww.speed"));
    }

    private void setupListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerConnectMessageListener(configuration), this);
    }

}
