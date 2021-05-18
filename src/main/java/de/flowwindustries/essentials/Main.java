package de.flowwindustries.essentials;

import de.flowwindustries.essentials.commands.GamemodeCommand;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Getter
    private static Main plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        //Reigster commands
        getCommand("gm").setExecutor(new GamemodeCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
