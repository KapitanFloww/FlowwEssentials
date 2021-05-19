package de.flowwindustries.essentials.commands;

import de.flowwindustries.essentials.Main;
import de.flowwindustries.flowwutils.exception.InsufficientPermissionException;
import de.flowwindustries.flowwutils.exception.InvalidArgumentsException;
import de.flowwindustries.flowwutils.exception.PlayerNotFoundException;
import de.flowwindustries.flowwutils.message.MessageType;
import de.flowwindustries.flowwutils.message.PlayerMessage;
import lombok.extern.java.Log;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@Log
public abstract class AbstractCommand implements CommandExecutor {

    private String permission;

    public AbstractCommand(String permission) {
        this.permission = permission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            try {
                if (!player.hasPermission(permission))
                    throw new InsufficientPermissionException(permission);
                ingameCommand(player, args);
                return true;
            } catch (PlayerNotFoundException | InvalidArgumentsException | InsufficientPermissionException | IllegalStateException e) {
                PlayerMessage.sendMessage(
                        List.of(player),
                        MessageType.ERROR,
                        Main.getPrefix(),
                        e.getMessage()
                );
                return false;
            }
        } else {
            try {
                consoleCommand(args);
                return true;
            } catch (PlayerNotFoundException | InvalidArgumentsException | IllegalStateException e) {
                log.warning(ChatColor.RED + e.getMessage());
                return false;
            }
        }
    }

    protected abstract void ingameCommand(Player player, String[] args) throws PlayerNotFoundException, InvalidArgumentsException;

    protected abstract void consoleCommand(String[] args) throws PlayerNotFoundException, InvalidArgumentsException;
}
