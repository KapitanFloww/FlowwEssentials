package de.flowwindustries.essentials.commands.time;

import de.flowwindustries.essentials.EssentialsPlugin;
import de.flowwindustries.essentials.commands.AbstractCommand;
import de.flowwindustries.essentials.utils.messages.PlayerMessage;
import de.flowwindustries.essentials.utils.parsing.SpigotStringParser;
import lombok.extern.java.Log;
import org.bukkit.World;
import org.bukkit.entity.Player;

import static de.flowwindustries.essentials.EssentialsPlugin.PREFIX;

/**
 * Time Command.
 * <ul>
 *     <li>Usage: /time {0-10} [world]</li>
 *     <li>Permission: floww.time</li>
 * </ul>
 */
@Log
public class TimeCommand extends AbstractCommand {

    private static final String TIME_CHANGED = "Set the time to %s";

    public TimeCommand(String permission) {
        super(permission);
    }

    @Override
    protected boolean playerCommand(Player player, String[] args) {
        try {
            if(args.length == 0) {
                throw new IllegalArgumentException(INVALID_ARGUMENTS);
            }
            int time = SpigotStringParser.parseIntSafe(args[0]);

            World world;
            switch (args.length) {
                case 1 -> world = player.getWorld();
                case 2 -> world = SpigotStringParser.parseWorldSafe(args[1]);
                default -> throw new IllegalArgumentException(INVALID_ARGUMENTS);
            }

            world.setTime(time);

            PlayerMessage.success(String.format(TIME_CHANGED, time), player);
            return true;

        } catch (IllegalArgumentException ex) {
            PlayerMessage.warn(ex.getMessage(), player);
        }
        return false;
    }

    @Override
    protected boolean consoleCommand(String[] args) {
        try {
            if(args.length != 2) { //time [number] [world]
                throw new IllegalArgumentException(INVALID_ARGUMENTS);
            }

            int time = SpigotStringParser.parseIntSafe(args[0]);

            World world = SpigotStringParser.parseWorldSafe(args[1]);

            world.setTime(time);

            log.info(PREFIX + String.format(TIME_CHANGED, time));
            return true;

        } catch (IllegalArgumentException ex) {
            log.warning(PREFIX + ex.getMessage());
        }
        return false;
    }
}
