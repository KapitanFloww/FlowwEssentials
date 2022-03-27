package de.flowwindustries.essentials.commands.fly;

import de.flowwindustries.essentials.commands.AbstractCommand;
import de.flowwindustries.essentials.utils.messages.PlayerMessage;
import de.flowwindustries.essentials.utils.parsing.SpigotStringParser;
import lombok.extern.java.Log;
import org.bukkit.entity.Player;

import static de.flowwindustries.essentials.EssentialsPlugin.PREFIX;

/**
 * Fly command.
 * <ul>
 *     <li>Usage: /fly [player]</li>
 *     <li>Permission: floww.fly</li>
 * </ul>
 */
@Log
public class FlyCommand extends AbstractCommand {

    private static final String DISABLE_FLIGHT_FOR = "Disabled flight mode for %s";
    private static final String ENABLE_FLIGHT_FOR = "Enabled flight mode for %s";
    private static final String ENABLED_FLIGHT = "Enabled fight mode for you";
    private static final String DISABLED_FLIGHT = "Disabled flight mode for you";

    public FlyCommand(String permission) {
        super(permission);
    }

    @Override
    protected boolean playerCommand(Player player, String[] args) {
        try {
            switch (args.length) {
                case 0 -> {
                    boolean state = player.isFlying();
                    player.setAllowFlight(!state);
                    player.setFlying(!state);

                    String successNoArg = !state ? ENABLED_FLIGHT : DISABLED_FLIGHT;
                    PlayerMessage.success(successNoArg, player);
                }
                case 1 -> {
                    Player target = SpigotStringParser.parsePlayerSafe(args[0]);
                    boolean targetState = player.isFlying();
                    target.setAllowFlight(!targetState);
                    target.setFlying(!targetState);

                    String successOneArg = getTargetMsg(targetState, target.getName());
                    if(player == target) {
                        PlayerMessage.success(ENABLED_FLIGHT, player);
                    } else {
                        PlayerMessage.success(successOneArg, player, target);
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
            if(args.length != 1) {
                throw new IllegalArgumentException(INVALID_ARGUMENTS);
            }

            Player target = SpigotStringParser.parsePlayerSafe(args[0]);

            boolean state = target.isFlying();
            target.setAllowFlight(!state);
            target.setFlying(!state);

            String message = getTargetMsg(state, target.getName());
            PlayerMessage.success(message, target);
            log.info(PREFIX + message);
            return true;
        } catch (IllegalArgumentException ex) {
            log.warning(PREFIX + ex.getMessage());
        }
        return false;
    }

    private String getTargetMsg(boolean state, String targetName) {
        return state ? String.format(DISABLE_FLIGHT_FOR, targetName) : String.format(ENABLE_FLIGHT_FOR, targetName);
    }
}
