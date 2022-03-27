package de.flowwindustries.essentials.commands.spawn;

import de.flowwindustries.essentials.EssentialsPlugin;
import de.flowwindustries.essentials.commands.AbstractCommand;
import de.flowwindustries.essentials.utils.messages.PlayerMessage;
import de.flowwindustries.essentials.utils.parsing.SpigotStringParser;
import lombok.extern.java.Log;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static de.flowwindustries.essentials.EssentialsPlugin.PREFIX;

/**
 * Spawn command.
 * <ul>
 *     <li>Usage: /spawn]</li>
 *     <li>Permission: floww.spawn</li>
 * </ul>
 */

@Log
public class SpawnCommand extends AbstractCommand {

    private static final String TELEPORT_SUCCESS = "You have been teleported to spawn";

    private final FileConfiguration configuration;

    public SpawnCommand(String permission) {
        super(permission);
        this.configuration = EssentialsPlugin.getPluginInstance().getConfiguration();
    }

    @Override
    protected boolean playerCommand(Player player, String[] args) {
        try {
            if(args.length != 0) {
                throw new IllegalArgumentException(INVALID_ARGUMENTS);
            }

            Location spawnLocation = SetSpawnCommand.getSpawnCache();
            if(spawnLocation == null) { //SpawnCache not set
                log.config(PREFIX + "Loading spawn from config");
                String worldString = configuration.getString("spawn.world");
                World world = SpigotStringParser.parseWorldSafe(worldString);
                double x = configuration.getDouble("spawn.x");
                double y = configuration.getDouble("spawn.y");
                double z = configuration.getDouble("spawn.z");
                float yaw = (float) configuration.getDouble("spawn.yaw");
                float pitch = (float) configuration.getDouble("spawn.pitch");

                spawnLocation = new Location(world, x, y, z, yaw, pitch);
                //Set the cache
                SetSpawnCommand.setSpawnCache(spawnLocation);
            }

            player.teleport(spawnLocation);
            PlayerMessage.success(TELEPORT_SUCCESS, player);
            return true;

        } catch (IllegalArgumentException ex) {
            PlayerMessage.warn(ex.getMessage(), player);
        }
        return false;
    }

    @Override
    protected boolean consoleCommand(String[] args) {
        log.warning(PREFIX + "Can only be used by players");
        return false;
    }
}
