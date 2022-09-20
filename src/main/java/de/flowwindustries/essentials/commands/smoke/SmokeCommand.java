package de.flowwindustries.essentials.commands.smoke;

import de.flowwindustries.essentials.EssentialsPlugin;
import de.flowwindustries.essentials.commands.AbstractCommand;
import de.flowwindustries.essentials.utils.messages.PlayerMessage;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Smoke Command to create small smokers.
 * /smoke - create new smoke
 * /smoke save - save all smokes
 * /smoke load - load all saved smokes
 * /smoke list - list all smokes
 * /smoke remove (id/all) - remove smoke with id or all smokes
 */
@Log
public class SmokeCommand extends AbstractCommand {

    public SmokeCommand(String permission) {
        super(permission);
    }

    private static final Path LOCATIONS_DATA_FILE = Path.of("plugins", "FlowwEssentials","data", "smoke-locations.dat");
    private static final Random random = new Random();

    private static int counter;

    private static Map<Integer, Location> smokeLocations; // TODO cluster locations to chunk and check chunk for once

    static {
        initializeLocations();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(EssentialsPlugin.getPluginInstance(),
                () -> smokeLocations.values().forEach(smokeLocation -> {
                    if(!smokeLocation.getChunk().isEntitiesLoaded()) {
                        return;
                    }
                    // TODO work with noise map
                    if(random.nextInt(50) % 3 == 0) {
                        return;
                    }
                    // TODO custom intensity
                    if(!(random.nextInt(50) % 3 == 0)) {
                        smokeLocation.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, smokeLocation, 0, 1, 10.0d, 0.0d, 0.01d);
                        return;
                    }
                    if(!(random.nextInt(50) % 2 == 0)) {
                        smokeLocation.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, smokeLocation, 0, 0.0d, 10.0d, 0.5d, 0.01d);
                        return;
                    }
                    if(!(random.nextInt(50) % 4 == 0)) {
                        smokeLocation.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, smokeLocation, 0, -0.4d, 10.0d, -1, 0.01d);
                    }
                }), 0l, 7l);
    }

    private static void initializeLocations() {
        try {
            if(!LOCATIONS_DATA_FILE.toFile().exists()) {
                smokeLocations = new ConcurrentHashMap<>();
            }
            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(LOCATIONS_DATA_FILE.toFile())));
            smokeLocations = (Map<Integer, Location>) in.readObject();
            setCounter();
        } catch (IOException | ClassNotFoundException ex) {
            log.warning("Could not initialize smoke locations: " + ex.getMessage());
        }
    }

    private static void setCounter() {
        AtomicInteger temp = new AtomicInteger(0);
        smokeLocations.keySet().forEach(integer -> {
            if(integer > temp.get()) {
                temp.set(integer);
            }
        });
        counter = temp.get() + 1;
    }

    public static void persistLocations() {
        try {
            if(!LOCATIONS_DATA_FILE.toFile().exists()) {
                LOCATIONS_DATA_FILE.toFile().createNewFile();
            }
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(LOCATIONS_DATA_FILE.toFile())));
            out.writeObject(smokeLocations);
            out.close();
        } catch (IOException ex) {
            log.warning("Could not persist file: " + ex.getMessage());
        }
    }

    @Override
    protected boolean playerCommand(Player player, String[] args) {
        switch (args.length) {
            case 0 -> {
                executeSmokeCreation(player);
            }
            case 1 -> {
                switch (args[0].toLowerCase(Locale.getDefault())) {
                    case "help" -> executeHelp(player);
                    case "list" -> executeListAll(player);
                    case "save" -> {
                        SmokeCommand.persistLocations();
                        PlayerMessage.success("Saved locations", player);
                    }
                    case "load" -> {
                        SmokeCommand.initializeLocations();
                        PlayerMessage.success("Initialized locations", player);
                    }
                }
            }
            case 2 -> {
                if(!args[0].equalsIgnoreCase("remove")) {
                    throw new IllegalArgumentException(INVALID_ARGUMENTS + ": %s".formatted(args[0]));
                }
                if(args[1].equalsIgnoreCase("all")) {
                    executeRemoveAll(player);
                    return false;
                }
                int id = Integer.parseInt(args[1]);
                executeRemoveSmoke(player, id);
            }
            default -> throw new IllegalArgumentException(INVALID_ARGUMENTS);
        }
        return false;
    }

    private void executeHelp(Player player) {
        PlayerMessage.info("-- Smoke Help --", player);
        PlayerMessage.info("/smoke - create new smoke", player);
        PlayerMessage.info("/smoke save - save all smokes", player);
        PlayerMessage.info("/smoke load - load all saved smokes", player);
        PlayerMessage.info("/smoke list - list all smokes", player);
        PlayerMessage.info("/smoke remove (id/all) - remove smoke with id or all smokes", player);
    }

    @Override
    protected boolean consoleCommand(String[] args) {
        Bukkit.getConsoleSender().sendMessage("Can only be used in-game");
        return false;
    }

    private static void executeListAll(Player player) {
        smokeLocations.forEach((taskId, location) -> PlayerMessage.info("Id: %s, Location: %s".formatted(taskId, location), player));
    }

    private static void executeRemoveAll(Player player) {
        smokeLocations.clear();
        PlayerMessage.info("Cleared all locations", player);
    }

    private static void executeRemoveSmoke(Player player, Integer id) {
        if(smokeLocations.containsKey(id)) {
            PlayerMessage.info("Removed location: %s".formatted(id), player);
            smokeLocations.remove(id);
            return;
        }
        PlayerMessage.warn("Could not find task: %s".formatted(id), player);
    }

    private static void executeSmokeCreation(Player player) {
        Block targetBlock = player.getTargetBlockExact(5);
        if(targetBlock == null) {
            throw new IllegalArgumentException("Must be looking at a block close-by");
        }
        int x = targetBlock.getX();
        int y = targetBlock.getY();
        int z = targetBlock.getZ();
        Location targetLocation = new Location(targetBlock.getWorld(), (x+0.5d), (y+0.5d), (z+0.5d));
        if(targetLocation.getWorld() == null) {
            throw new IllegalStateException("World must not be null");
        }
        int id = counter++;
        smokeLocations.put(id, targetLocation);
        PlayerMessage.success("Placed smoke at %s, %s, %s (taskId: %s)".formatted(targetLocation.getBlock().getX(), targetLocation.getBlock().getY(), targetLocation.getBlock().getZ(), id), player);
    }
}