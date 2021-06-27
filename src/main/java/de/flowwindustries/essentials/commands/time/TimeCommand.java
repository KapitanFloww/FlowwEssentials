package de.flowwindustries.essentials.commands.time;

import de.flowwindustries.essentials.EssentialsPlugin;
import de.flowwindustries.flowwutils.commands.AbstractCommand;
import de.flowwindustries.flowwutils.exception.InvalidArgumentsException;
import de.flowwindustries.flowwutils.exception.PlayerNotFoundException;
import de.flowwindustries.flowwutils.message.MessageType;
import de.flowwindustries.flowwutils.message.PlayerMessage;
import de.flowwindustries.flowwutils.safe.SpigotParser;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

@Log
public class TimeCommand extends AbstractCommand {

    public TimeCommand(String permission, String prefix) {
        super(permission, prefix);
    }

    @Override
    protected void ingameCommand(Player player, String[] args) throws PlayerNotFoundException, InvalidArgumentsException {

        if(args.length != 1) {
            throw new InvalidArgumentsException("Invalid amount of arguments");
        }
        World world = player.getWorld();
        int timeNumber = SpigotParser.getIntegerSafe(args[0]);

        world.setTime(timeNumber);
        PlayerMessage.sendMessage(
                List.of(player),
                MessageType.SUCCESS,
                EssentialsPlugin.getPrefix(),
                "Set world time to " + timeNumber
        );
    }

    @Override
    protected void consoleCommand(String[] args) throws PlayerNotFoundException, InvalidArgumentsException {
        if(args.length != 2) { //time [number] [world]
            throw new InvalidArgumentsException("Invalid amount of arguments");
        }

        int timeNumber = SpigotParser.getIntegerSafe(args[0]);
        String worldString = args[1];

        World world = Optional.ofNullable(Bukkit.getWorld(worldString))
                .orElseThrow(() -> new InvalidArgumentsException("World " + worldString + " not found"));

        world.setTime(timeNumber);
        log.info("Set the time to " + timeNumber);
    }
}
