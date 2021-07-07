package de.flowwindustries.essentials.commands.spawn;

import de.flowwindustries.essentials.EssentialsPlugin;
import de.flowwindustries.flowwutils.commands.AbstractCommand;
import de.flowwindustries.flowwutils.exception.InvalidArgumentsException;
import de.flowwindustries.flowwutils.exception.PlayerNotFoundException;
import de.flowwindustries.flowwutils.message.MessageType;
import de.flowwindustries.flowwutils.message.PlayerMessage;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

@Log
public class SpawnCommand extends AbstractCommand {

    private final FileConfiguration configuration;

    public SpawnCommand(FileConfiguration configuration, String permission) {
        super(permission);
        this.configuration = configuration;
    }

    @Override
    protected void ingameCommand(Player player, String[] args) throws PlayerNotFoundException, InvalidArgumentsException {
        if(args.length != 0)
            throw new InvalidArgumentsException("Too many arguments");

        Location spawnLocation = null;

        if(SpawnCache.getSpawnLocation() == null) { //SpawnCache not set
            String worldString = Optional.ofNullable(configuration.getString("spawn.world"))
                    .orElseThrow(() -> new IllegalStateException("World config key not found"));

            World world = Optional.ofNullable(Bukkit.getWorld(worldString))
                    .orElseThrow(() -> new IllegalStateException("World not found"));

            float yaw = (float) configuration.getDouble("spawn.yaw");
            float pitch = (float) configuration.getDouble("spawn.pitch");

            spawnLocation = new Location(
                    world,
                    configuration.getDouble("spawn.x"),
                    configuration.getDouble("spawn.y"),
                    configuration.getDouble("spawn.z"),
                    yaw,
                    pitch
            );

            //Set the cache
            SpawnCache.setSpawnLocation(spawnLocation);
            log.info("Setting spawn cache");
        } else { //SpawnCache set
            //Get the cache
            spawnLocation = SpawnCache.getSpawnLocation();
            log.info("Loading spawn cache");
        }
        player.teleport(spawnLocation);
        PlayerMessage.sendMessage(
                List.of(player),
                MessageType.SUCCESS,
                EssentialsPlugin.getPrefix(),
                "You have been teleported to spawn"
        );
    }

    @Override
    protected void consoleCommand(String[] args) throws PlayerNotFoundException, InvalidArgumentsException {
        log.info("Can only be used ingame!");
    }


    @Override
    protected String getPrefix() {
        return EssentialsPlugin.getPrefix();
    }
}
