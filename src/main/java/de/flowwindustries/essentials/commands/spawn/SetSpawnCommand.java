package de.flowwindustries.essentials.commands.spawn;

import de.flowwindustries.essentials.EssentialsPlugin;
import de.flowwindustries.essentials.commands.AbstractCommand;
import de.flowwindustries.essentials.utils.messages.PlayerMessage;
import de.flowwindustries.essentials.utils.parsing.SpigotStringParser;
import lombok.Getter;
import lombok.extern.java.Log;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Optional;

import static de.flowwindustries.essentials.EssentialsPlugin.PREFIX;

/**
 * SetSpawn command.
 * <ul>
 *     <li>Usage: /setspawn or /setspawn x y z world - if used from console</li>
 *     <li>Permission: floww.setspawn</li>
 * </ul>
 */

@Log
public class SetSpawnCommand extends AbstractCommand {

    private static final String SPAWNPOINT_SET = "Spawnpoint has been set!";
    @Getter
    private static Location spawnCache;
    private final FileConfiguration fileConfiguration;

    public SetSpawnCommand(String permission) {
        super(permission);
        this.fileConfiguration = EssentialsPlugin.getPluginInstance().getConfiguration();
    }

    @Override
    protected boolean playerCommand(Player player, String[] args) {
        try {
            if(args.length != 0) {//setspawn
                throw new IllegalArgumentException(INVALID_ARGUMENTS);
            }

            Location location = player.getLocation();
            World world = Optional.ofNullable(location.getWorld()).orElseThrow(() -> new IllegalStateException("Unable to find world"));

            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();
            float pitch = location.getPitch();
            float yaw = location.getYaw();

            setSpawnConfig(x, y, z, pitch, yaw, world.getName());

            PlayerMessage.success(SPAWNPOINT_SET, player);

            //Updating cache
            setSpawnCache(location);
            return true;

        } catch (IllegalArgumentException ex) {
            PlayerMessage.warn(ex.getMessage(), player);
        }
        return false;
    }

    @Override
    protected boolean consoleCommand(String[] args) {
        try {
            if(args.length != 4) {//setspawn x y z world
                throw new IllegalArgumentException(INVALID_ARGUMENTS);
            }

            World world = SpigotStringParser.parseWorldSafe(args[3]);
            double x = SpigotStringParser.parseDoubleSafe(args[0]);
            double y = SpigotStringParser.parseDoubleSafe(args[1]);
            double z = SpigotStringParser.parseDoubleSafe(args[2]);
            float pitch = 0f;
            float yaw = 0f;

            setSpawnConfig(x, y, z, pitch, yaw, world.getName());

            log.info(PREFIX + "Spawnpoint saved");

            //Updating cache
            setSpawnCache(new Location(world, x, y, z, yaw, pitch));
            return true;

        } catch (IllegalArgumentException ex) {
            log.warning(PREFIX + ex.getMessage());
        }
        return false;
    }

    /**
     * Set the spawn cache.
     * @param spawnCache the new cache value
     */
    public static void setSpawnCache(Location spawnCache) {
        SetSpawnCommand.spawnCache = spawnCache;
        log.info(PREFIX + "Set SpawnCache to " + spawnCache);
    }

    private void setSpawnConfig(double x, double y, double z, double pitch, double yaw, String worldName) {
        fileConfiguration.set("spawn.x", x);
        fileConfiguration.set("spawn.y", y);
        fileConfiguration.set("spawn.z", z);
        fileConfiguration.set("spawn.pitch", pitch);
        fileConfiguration.set("spawn.yaw", yaw);
        fileConfiguration.set("spawn.world", worldName);
        EssentialsPlugin.getPluginInstance().saveConfig();
    }
}
