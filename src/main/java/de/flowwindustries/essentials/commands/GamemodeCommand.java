package de.flowwindustries.essentials.commands;

import de.flowwindustries.essentials.Main;
import de.flowwindustries.flowwutils.exception.InsufficientPermissionException;
import de.flowwindustries.flowwutils.exception.InvalidArgumentsException;
import de.flowwindustries.flowwutils.exception.PlayerNotFoundException;
import de.flowwindustries.flowwutils.message.MessageType;
import de.flowwindustries.flowwutils.message.PlayerMessage;
import de.flowwindustries.flowwutils.safe.SpigotParser;
import lombok.extern.java.Log;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

/**
 * Gamemode Command.
 * /gm {0,1,2,3} [player]
 * floww.gm
 */
@Log
public class GamemodeCommand implements CommandExecutor {

    private static final String PERMISSION = "floww.gm";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                try {
                    if(!player.hasPermission(PERMISSION))
                        throw new InsufficientPermissionException(PERMISSION);
                    ingameCommand(player, args);
                    return true;
                } catch (PlayerNotFoundException | InvalidArgumentsException | InsufficientPermissionException e) {
                    PlayerMessage.sendMessage(
                            List.of(player),
                            MessageType.ERROR,
                            Main.getPlugin().getDescription().getPrefix(),
                            e.getMessage()
                    );
                    return false;
                }

            } else {
                try {
                    consoleCommand(args);
                    return true;
                } catch (PlayerNotFoundException | InvalidArgumentsException e) {
                    log.warning(ChatColor.RED + e.getMessage());
                    return false;
                }
            }


    }

    private void consoleCommand(String[] args) throws InvalidArgumentsException, PlayerNotFoundException {
        if(args.length != 2) // gamemode {0,1,2,3} {player}
            throw new InvalidArgumentsException("Invalid amount of arguments");

        //parse
        int gmNumber = SpigotParser.getIntegerSafe(args[0]);
        Player target = SpigotParser.getPlayerSafe(args[1]);
        GameMode gameMode = mapGamemodeSafe(gmNumber);

        //Set gamemode
        target.setGameMode(gameMode);

        //Send messages
        PlayerMessage.sendMessage(List.of(target), MessageType.SUCCESS, Main.getPlugin().getDescription().getPrefix(), "Changed gamemode to " + gameMode.name());
    }

    private void ingameCommand(Player player, String[] args) throws InvalidArgumentsException, PlayerNotFoundException {
        if(args.length == 1) { //gamemode {0,1,2,3}
            //parse
            int gmNumber = SpigotParser.getIntegerSafe(args[0]);
            GameMode gameMode = mapGamemodeSafe(gmNumber);

            //Set gamemode
            player.setGameMode(gameMode);

            //Send messages
            PlayerMessage.sendMessage(
                    List.of(player),
                    MessageType.SUCCESS,
                    Main.getPlugin().getDescription().getPrefix(),
                    String.format("Changed gamemode to %s", gameMode.name())
            );

        } else if(args.length == 2) { //gamemode {0,1,2,3} {player}
            //parse
            int gmNumber = SpigotParser.getIntegerSafe(args[0]);
            Player target = SpigotParser.getPlayerSafe(args[1]);
            GameMode gameMode = mapGamemodeSafe(gmNumber);

            //Set gamemode
            target.setGameMode(gameMode);

            //Send messages
            PlayerMessage.sendMessage(
                    List.of(player, target),
                    MessageType.SUCCESS,
                    Main.getPlugin().getDescription().getPrefix(),
                    String.format("Changed gamemode of %s to %s", target.getName(), gameMode.name())
            );

        } else
            throw new InvalidArgumentsException("Invalid amount of arguments");
    }

    private GameMode mapGamemode(int number) {
        if(number == 0)
            return GameMode.SURVIVAL;
        if(number == 1)
            return GameMode.CREATIVE;
        if(number == 2)
            return GameMode.ADVENTURE;
        if(number == 3)
            return GameMode.SPECTATOR;
        return null;
    }

    private GameMode mapGamemodeSafe(int number) {
        return Optional.ofNullable(mapGamemode(number))
                .orElseThrow(() -> new IllegalArgumentException("Could not map gamemode. Valid numbers are 0, 1, 2,3 "));
    }

}
