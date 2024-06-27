package de.flowwindustries.essentials.commands.ping;

import de.flowwindustries.essentials.commands.AbstractCommand;
import lombok.extern.java.Log;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Log
public class PingCommand extends AbstractCommand {

    public PingCommand(String permission) {
        super(permission);
    }

    @Override
    protected boolean playerCommand(Player player, String[] args) {
        final var ping = player.getPing();
        player.sendMessage(ChatColor.GREEN + "Pong (" + ChatColor.GRAY + ping + "ms" + ChatColor.GREEN + ")");
        return true;
    }

    @Override
    protected boolean consoleCommand(String[] args) {
        // Nothing to do
        return false;
    }
}
