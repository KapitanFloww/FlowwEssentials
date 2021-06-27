package de.flowwindustries.essentials.commands.fly;

import de.flowwindustries.essentials.EssentialsPlugin;
import de.flowwindustries.flowwutils.commands.AbstractCommand;
import de.flowwindustries.flowwutils.exception.InvalidArgumentsException;
import de.flowwindustries.flowwutils.exception.PlayerNotFoundException;
import de.flowwindustries.flowwutils.message.MessageType;
import de.flowwindustries.flowwutils.message.PlayerMessage;
import de.flowwindustries.flowwutils.safe.SpigotParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Fly Command.
 * /fly [player]
 * floww.fly
 */
public class FlyCommand extends AbstractCommand {

    public FlyCommand(String permission, String prefix) {
        super(permission, prefix);
    }

    @Override
    protected void ingameCommand(Player player, String[] args) throws PlayerNotFoundException, InvalidArgumentsException {
        switch (args.length) {
            case 0:
                boolean state = player.isFlying();

                player.setAllowFlight(!state);
                player.setFlying(!state);

                String status = !state ? "§aenabled§e" : "§cdisabled§e";
                player.sendMessage(
                        String.format("%s %sFlight-mode has been %s",
                                EssentialsPlugin.getPrefix(),
                                ChatColor.YELLOW,
                                status));
                break;
            case 1:
                Player target = SpigotParser.getPlayerSafe(args[0]);
                boolean targetState = player.isFlying();

                target.setAllowFlight(!targetState);
                target.setFlying(!targetState);

                PlayerMessage.sendMessage(
                        List.of(player, target),
                        MessageType.SUCCESS,
                        EssentialsPlugin.getPrefix(),
                        targetState ? "Disabled flight mode for " + target.getName() : "Enabled flight mode for " + target.getName()
                );
                break;
            default:
                throw new InvalidArgumentsException();
        }

    }

    @Override
    protected void consoleCommand(String[] args) throws PlayerNotFoundException, InvalidArgumentsException {
        if(args.length != 1) {
            throw new InvalidArgumentsException();
        }

        Player target = SpigotParser.getPlayerSafe(args[0]);
        boolean state = target.isFlying();

        target.setAllowFlight(!state);
        target.setFlying(!state);

        String message = state ? "Disabled flight mode for " + target.getName() : "Enabled flight mode for " + target.getName();

        PlayerMessage.sendMessage(
                List.of(target),
                MessageType.SUCCESS,
                EssentialsPlugin.getPrefix(),
                message
                );

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + message);
    }


}
