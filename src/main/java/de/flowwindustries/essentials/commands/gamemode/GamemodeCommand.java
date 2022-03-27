package de.flowwindustries.essentials.commands.gamemode;

import de.flowwindustries.essentials.EssentialsPlugin;
import de.flowwindustries.essentials.commands.AbstractCommand;
import de.flowwindustries.essentials.utils.messages.PlayerMessage;
import de.flowwindustries.essentials.utils.parsing.SpigotStringParser;
import lombok.extern.java.Log;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import static de.flowwindustries.essentials.EssentialsPlugin.PREFIX;
import static de.flowwindustries.essentials.commands.gamemode.GameModeMapper.mapGameModeSafe;

/**
 * Gamemode command.
 * <ul>
 *     <li>Usage: /gm {0,1,2,3} [player]</li>
 *     <li>Permission: floww.gm</li>
 * </ul>
 */
@Log
public class GamemodeCommand extends AbstractCommand {

    public static final String GAMEMODE_CHANGED = "Changed gamemode to %s";
    public static final String GAMEMODE_CHANGED_OF = "Changed gamemode of %s to %s";

    public GamemodeCommand(String permission) {
        super(permission);
    }

    @Override
    protected boolean playerCommand(Player player, String[] args) {
        try {
            switch (args.length) {
                case 1 -> {
                    int gmNumber = SpigotStringParser.parseIntSafe(args[0]);
                    GameMode gameMode = mapGameModeSafe(gmNumber);

                    player.setGameMode(gameMode);

                    PlayerMessage.success(String.format(GAMEMODE_CHANGED, gameMode.name()), player);
                }
                case 2 -> {
                    int gmNumber = SpigotStringParser.parseIntSafe(args[0]);
                    Player target = SpigotStringParser.parsePlayerSafe(args[1]);
                    GameMode gameMode = mapGameModeSafe(gmNumber);

                    target.setGameMode(gameMode);

                    String message = player == target ?
                            String.format(GAMEMODE_CHANGED, gameMode.name()) :
                            String.format(GAMEMODE_CHANGED_OF, target.getName(), gameMode.name());

                    if(player == target) {
                        PlayerMessage.success(message, player);
                    } else {
                        PlayerMessage.success(message, player, target);
                    }
                }
                default -> throw new IllegalArgumentException(INVALID_ARGUMENTS);
            }
            return true;

        } catch (IllegalArgumentException ex) {
            PlayerMessage.warn(ex.getMessage(), player);
        }
        return false;
    }

    @Override
    protected boolean consoleCommand(String[] args) {
        try {
            if (args.length != 2) // gamemode {0,1,2,3} [player]
                throw new IllegalArgumentException(INVALID_ARGUMENTS);

            int gmNumber = SpigotStringParser.parseIntSafe(args[0]);
            Player target = SpigotStringParser.parsePlayerSafe(args[1]);
            GameMode gameMode = mapGameModeSafe(gmNumber);

            target.setGameMode(gameMode);

            PlayerMessage.success(String.format(GAMEMODE_CHANGED, gameMode.name()), target);
            log.info(PREFIX + String.format(GAMEMODE_CHANGED_OF, target.getName(), gameMode.name()));
            return true;

        } catch (IllegalArgumentException ex) {
            log.warning(PREFIX + ex.getMessage());
        }
        return false;
    }
}
