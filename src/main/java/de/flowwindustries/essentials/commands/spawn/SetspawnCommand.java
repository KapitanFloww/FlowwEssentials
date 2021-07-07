package de.flowwindustries.essentials.commands.spawn;

import de.flowwindustries.essentials.EssentialsPlugin;
import de.flowwindustries.flowwutils.commands.AbstractCommand;
import de.flowwindustries.flowwutils.exception.InvalidArgumentsException;
import de.flowwindustries.flowwutils.exception.PlayerNotFoundException;
import de.flowwindustries.flowwutils.message.MessageType;
import de.flowwindustries.flowwutils.message.PlayerMessage;
import de.flowwindustries.flowwutils.safe.SpigotParser;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

@Log
public class SetspawnCommand extends AbstractCommand {

    private final FileConfiguration fileConfiguration;

    public SetspawnCommand(FileConfiguration fileConfiguration, String permission) {
        super(permission);
        this.fileConfiguration = fileConfiguration;
    }

    @Override
    protected void ingameCommand(Player player, String[] args) throws PlayerNotFoundException, InvalidArgumentsException {
        if(args.length != 0) {
            throw new InvalidArgumentsException("Too many arguments");
        }

        Location location = player.getLocation();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        double pitch = location.getPitch();
        double yaw = location.getYaw();

        World world = Optional.ofNullable(location.getWorld())
                .orElseThrow(() -> new IllegalStateException("Invalid world"));

        fileConfiguration.set("spawn.x", x);
        fileConfiguration.set("spawn.y", y);
        fileConfiguration.set("spawn.z", z);
        fileConfiguration.set("spawn.pitch", pitch);
        fileConfiguration.set("spawn.yaw", yaw);
        fileConfiguration.set("spawn.world", world.getName());

        EssentialsPlugin.getPlugin().saveConfig();

        PlayerMessage.sendMessage(
                List.of(player),
                MessageType.SUCCESS,
                EssentialsPlugin.getPrefix(),
                "Spawnpoint saved"
        );

        //Updating cache
        SpawnCache.setSpawnLocation(location);
        log.info("Setting spawn cache");
    }

    @Override
    protected void consoleCommand(String[] args) throws PlayerNotFoundException, InvalidArgumentsException {
        if(args.length != 4) { //setspawn x y z world
            throw new InvalidArgumentsException("Invalid amount of arguments");
        }

        double x = SpigotParser.getDoubleSafe(args[0]);
        double y = SpigotParser.getDoubleSafe(args[1]);
        double z = SpigotParser.getDoubleSafe(args[2]);

        Optional.ofNullable(Bukkit.getWorld(args[3]))
                .orElseThrow(() -> new IllegalStateException("World not found"));

        String worldName = args[3];

        double pitch = 0d;
        double yaw = 0d;

        fileConfiguration.set("spawn.x", x);
        fileConfiguration.set("spawn.y", y);
        fileConfiguration.set("spawn.z", z);
        fileConfiguration.set("spawn.pitch", pitch);
        fileConfiguration.set("spawn.yaw", yaw);
        fileConfiguration.set("spawn.world", worldName);

        EssentialsPlugin.getPlugin().saveConfig();
        log.info("Spawnpoint saved");

        //Updating cache
        SpawnCache.setSpawnLocation(new Location(Bukkit.getWorld(worldName), x, y, z));
        log.info("Setting spawn cache");
    }

    @Override
    protected String getPrefix() {
        return EssentialsPlugin.getPrefix();
    }
}
