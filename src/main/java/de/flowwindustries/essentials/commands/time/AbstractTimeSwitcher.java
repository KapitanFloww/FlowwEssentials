package de.flowwindustries.essentials.commands.time;

import de.flowwindustries.essentials.EssentialsPlugin;
import de.flowwindustries.essentials.commands.AbstractCommand;
import de.flowwindustries.essentials.utils.messages.PlayerMessage;
import de.flowwindustries.essentials.utils.parsing.SpigotStringParser;
import lombok.extern.java.Log;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static de.flowwindustries.essentials.EssentialsPlugin.PREFIX;

/**
 * Parental command class for /day and /night commands.
 */
@Log
public abstract class AbstractTimeSwitcher extends AbstractCommand {

    private final long time;
    private final String message;

    public AbstractTimeSwitcher(String configKey, String permission) {
        super(permission);
        FileConfiguration fileConfiguration = EssentialsPlugin.getPluginInstance().getConfig();
        this.time = fileConfiguration.getLong("commands.time." + configKey + ".value");
        this.message = fileConfiguration.getString("commands.time." + configKey + ".message");
    }

    @Override
    protected boolean playerCommand(Player player, String[] args) {
        try {
            if(args.length > 0) {
                throw new IllegalArgumentException(INVALID_ARGUMENTS);
            }
            player.getWorld().setTime(time);
            PlayerMessage.success(message, player);
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
            World world = SpigotStringParser.parseWorldSafe(args[0]);
            world.setTime(time);

            log.info(PREFIX + message);
        } catch (IllegalArgumentException ex) {
            log.warning(PREFIX + ex.getMessage());
        }
        return false;
    }
}
