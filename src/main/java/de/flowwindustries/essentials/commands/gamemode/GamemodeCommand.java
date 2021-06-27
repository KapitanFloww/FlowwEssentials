package de.flowwindustries.essentials.commands.gamemode;

import de.flowwindustries.essentials.EssentialsPlugin;
import de.flowwindustries.flowwutils.commands.AbstractCommand;
import de.flowwindustries.flowwutils.exception.InvalidArgumentsException;
import de.flowwindustries.flowwutils.exception.PlayerNotFoundException;
import de.flowwindustries.flowwutils.message.MessageType;
import de.flowwindustries.flowwutils.message.PlayerMessage;
import de.flowwindustries.flowwutils.safe.SpigotParser;
import lombok.extern.java.Log;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Gamemode Command.
 * /gm {0,1,2,3} [player]
 * floww.gm
 */
@Log
public class GamemodeCommand extends AbstractCommand {

    private static final String PERMISSION = "floww.gm";

    public GamemodeCommand(String permission, String prefix) {
        super(permission, prefix);
    }

    protected void consoleCommand(String[] args) throws InvalidArgumentsException, PlayerNotFoundException {
        if (args.length != 2) // gamemode {0,1,2,3} {player}
            throw new InvalidArgumentsException("Invalid amount of arguments");

        //parse
        int gmNumber = SpigotParser.getIntegerSafe(args[0]);
        Player target = SpigotParser.getPlayerSafe(args[1]);
        GameMode gameMode = mapGamemodeSafe(gmNumber);

        //Set gamemode
        target.setGameMode(gameMode);

        //Send messages
        PlayerMessage.sendMessage(List.of(target), MessageType.SUCCESS, EssentialsPlugin.getPrefix(), "Changed gamemode to " + gameMode.name());
    }

    protected void ingameCommand(Player player, String[] args) throws InvalidArgumentsException, PlayerNotFoundException {
        if (args.length == 1) { //gamemode {0,1,2,3}
            //parse
            int gmNumber = SpigotParser.getIntegerSafe(args[0]);
            GameMode gameMode = mapGamemodeSafe(gmNumber);

            //Set gamemode
            player.setGameMode(gameMode);

            //Send messages
            PlayerMessage.sendMessage(
                    List.of(player),
                    MessageType.SUCCESS,
                    EssentialsPlugin.getPrefix(),
                    String.format("Changed gamemode to %s", gameMode.name())
            );

        } else if (args.length == 2) { //gamemode {0,1,2,3} {player}
            //parse
            int gmNumber = SpigotParser.getIntegerSafe(args[0]);
            Player target = SpigotParser.getPlayerSafe(args[1]);
            GameMode gameMode = mapGamemodeSafe(gmNumber);

            //Set gamemode
            target.setGameMode(gameMode);

            //Send messages
            Collection<Player> sendTo = player == target ?
                    List.of(player) : List.of(player, target);

            String message = player == target ?
                    String.format("Changed gamemode to %s", gameMode.name()) : String.format("Changed gamemode of %s to %s", target.getName(), gameMode.name());

            PlayerMessage.sendMessage(
                    sendTo,
                    MessageType.SUCCESS,
                    EssentialsPlugin.getPrefix(),
                    message
            );

        } else
            throw new InvalidArgumentsException("Invalid amount of arguments");
    }

    private GameMode mapGamemode(int number) {
        if (number == 0)
            return GameMode.SURVIVAL;
        if (number == 1)
            return GameMode.CREATIVE;
        if (number == 2)
            return GameMode.ADVENTURE;
        if (number == 3)
            return GameMode.SPECTATOR;
        return null;
    }

    private GameMode mapGamemodeSafe(int number) {
        return Optional.ofNullable(mapGamemode(number))
                .orElseThrow(() -> new IllegalArgumentException("Could not map gamemode. Valid numbers are 0, 1, 2,3 "));
    }

}
