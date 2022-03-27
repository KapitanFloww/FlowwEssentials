package de.flowwindustries.essentials.configuration;

import org.bukkit.configuration.file.FileConfiguration;

public class DefaultConfiguration {

    public static void setupDefaultConfiguration(FileConfiguration configuration) {
        //Setup values
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

        configuration.addDefault("messages.join", "§7Welcome %s to FlowwIndustries");
        configuration.addDefault("messages.leave", "§7%s has left the server");

        configuration.addDefault("messages.insufficient-permission", "§cYou are lacking required permissions");

        //Save configuration
        configuration.options().copyDefaults(true);
    }
}
