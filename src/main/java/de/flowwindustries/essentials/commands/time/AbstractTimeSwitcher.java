package de.flowwindustries.essentials.commands.time;

import de.flowwindustries.essentials.EssentialsPlugin;
import de.flowwindustries.flowwutils.commands.AbstractCommand;
import de.flowwindustries.flowwutils.exception.InvalidArgumentsException;
import de.flowwindustries.flowwutils.exception.PlayerNotFoundException;
import de.flowwindustries.flowwutils.message.MessageType;
import de.flowwindustries.flowwutils.message.PlayerMessage;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

@Log
public abstract class AbstractTimeSwitcher extends AbstractCommand {

    private final long time;
    private final String message;

    public AbstractTimeSwitcher(String configKey, String permission, String prefix) {
        super(permission, prefix);
        FileConfiguration fileConfiguration = EssentialsPlugin.getPlugin().getConfig();
        this.time = fileConfiguration.getLong("commands.time." + configKey + ".value");
        this.message = fileConfiguration.getString("commands.time." + configKey + ".message");
    }

    @Override
    protected void ingameCommand(Player player, String[] args) throws PlayerNotFoundException, InvalidArgumentsException {
        if(args.length > 0) {
            throw new InvalidArgumentsException("Too many arguments");
        }
        player.getWorld().setTime(time);
        PlayerMessage.sendMessage(
                List.of(player),
                MessageType.SUCCESS,
                EssentialsPlugin.getPrefix(),
                message
        );
    }

    @Override
    protected void consoleCommand(String[] args) throws PlayerNotFoundException, InvalidArgumentsException {
        if(args.length != 1) {
            throw new InvalidArgumentsException("Invalid amounts of arguments");
        }

        String worldString = args[0];
        World world = Optional.ofNullable(Bukkit.getWorld(worldString))
                .orElseThrow(() -> new InvalidArgumentsException("World not found"));

        world.setTime(time);
        log.info(message);
    }
}
