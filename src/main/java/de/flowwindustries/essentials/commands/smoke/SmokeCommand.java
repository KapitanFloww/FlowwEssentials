package de.flowwindustries.essentials.commands.smoke;

import de.flowwindustries.essentials.EssentialsPlugin;
import de.flowwindustries.essentials.commands.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SmokeCommand extends AbstractCommand {

    public SmokeCommand(String permission) {
        super(permission);
    }

    private static final List<Integer> runningTasks = new ArrayList<>();
    private static final Random random = new Random();

    @Override
    protected boolean playerCommand(Player player, String[] args) {
        switch (args.length) {
            case 0 -> {
                executeSmokeCreation(player);
            }
            case 1 -> {
                if(args[0].equalsIgnoreCase("list")) {
                    runningTasks.forEach(taskId -> {
                        player.sendMessage("Task: %s".formatted(taskId));
                    });
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

    @Override
    protected boolean consoleCommand(String[] args) {
        Bukkit.getConsoleSender().sendMessage("Must be used in-game");
        return false;
    }

    private static void executeRemoveAll(Player player) {
        List<Integer> taskIds = new ArrayList<>(runningTasks);
        taskIds.forEach(taskId -> executeRemoveSmoke(player, taskId));
    }

    private static void executeRemoveSmoke(Player player, int id) {
        Integer taskId = id;
        if(runningTasks.contains(taskId)) {
            Bukkit.getScheduler().cancelTask(taskId);
            player.sendMessage("Removed effect");
            runningTasks.remove(taskId);
            return;
        }
        player.sendMessage("Could not find task %s".formatted(id));
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
        Integer taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(EssentialsPlugin.getPluginInstance(),
                () -> {
                    if(random.nextInt(50) % 3 == 0) {
                        return;
                    }
                    targetLocation.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, targetLocation, 0, 1, 10.0d, 0.0d, 0.01d);
                    targetLocation.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, targetLocation, 0, 0.0d, 10.0d, 0.5d, 0.01d);
                    targetLocation.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, targetLocation, 0, -0.4d, 10.0d, -1, 0.01d);
                }, 0l, 7l);
        runningTasks.add(taskId);
        player.sendMessage("Placed smoke at %s, %s, %s (taskId: %s)".formatted(targetLocation.getBlock().getX(), targetLocation.getBlock().getY(), targetLocation.getBlock().getZ(), taskId));
    }
}