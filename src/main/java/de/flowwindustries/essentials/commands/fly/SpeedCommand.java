package de.flowwindustries.essentials.commands.fly;

import de.flowwindustries.essentials.EssentialsPlugin;
import de.flowwindustries.essentials.commands.AbstractCommand;
import de.flowwindustries.essentials.utils.messages.PlayerMessage;
import lombok.extern.java.Log;
import org.bukkit.entity.Player;

import static de.flowwindustries.essentials.EssentialsPlugin.PREFIX;
import static de.flowwindustries.essentials.utils.parsing.SpigotStringParser.parseFloatSafe;
import static de.flowwindustries.essentials.utils.parsing.SpigotStringParser.parsePlayerSafe;

/**
 * Speed command.
 * <ul>
 *     <li>Usage: /speed {0-10} [player]</li>
 *     <li>Permission: floww.speed</li>
 * </ul>
 */
@Log
public class SpeedCommand extends AbstractCommand {

    public static final String SPEED_OF_PLAYER_CHANGED = "Speed of player %s has been set to: %s";
    public static final String OWN_SPEED_CHANGED = "Your speed has been set to: %s";
    public static final String FLY_SPEED_INVALID = "Fly speed must be between 0 and 10";

    public SpeedCommand(String permission) {
        super(permission);
    }

    @Override
    protected boolean playerCommand(Player player, String[] args) {
        try {
            float amount;
            switch (args.length) {
                case 1 -> { //speed <1-10>

                    amount = getSpeedSafe(args[0]);
                    player.setFlySpeed(amount);

                    PlayerMessage.success(String.format(OWN_SPEED_CHANGED, amount*10), player);
                }
                case 2 -> { //speed <1-10> [player]

                    amount = getSpeedSafe(args[0]);
                    Player targetPlayer = parsePlayerSafe(args[1]);
                    targetPlayer.setFlySpeed(amount);

                    PlayerMessage.success(String.format(SPEED_OF_PLAYER_CHANGED, targetPlayer.getName(), (amount * 10)), player, targetPlayer);
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
            if (args.length != 2) {
                throw new IllegalArgumentException(INVALID_ARGUMENTS);
            }
            float amount = getSpeedSafe(args[0]);

            Player target = parsePlayerSafe(args[1]);
            target.setFlySpeed(amount);

            PlayerMessage.success(String.format(OWN_SPEED_CHANGED, amount*10));
            log.info(PREFIX + String.format(SPEED_OF_PLAYER_CHANGED, target.getName(), amount*10));
            return true;

        } catch (IllegalArgumentException ex) {
            log.warning(PREFIX + ex.getMessage());
        }
        return false;
    }

    private float getSpeedSafe(String string) throws IllegalArgumentException {
        float amount = parseFloatSafe(string);
        amount = amount / 10;
        if(amount < 0 || amount > 1)
            throw new IllegalArgumentException(FLY_SPEED_INVALID);
        return amount;
    }
}
