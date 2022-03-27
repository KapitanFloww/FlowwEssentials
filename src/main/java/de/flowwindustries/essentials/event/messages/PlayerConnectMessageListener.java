package de.flowwindustries.essentials.event.messages;

import de.flowwindustries.essentials.EssentialsPlugin;
import lombok.extern.java.Log;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static de.flowwindustries.essentials.EssentialsPlugin.PREFIX;
import static de.flowwindustries.essentials.utils.exceptions.InsufficientPermissionException.CONFIGURATION_KEY_MISSING;

/**
 * Listener to handle when a player joins or quits.
 */
@Log
public class PlayerConnectMessageListener implements Listener {


    private static final String JOIN_KEY = "messages.join";
    private static final String QUIT_KEY = "messages.quit";

    private static final String JOIN_MESSAGE = EssentialsPlugin.getPluginInstance().getConfiguration().getString(JOIN_KEY);
    private static final String QUIT_MESSAGE = EssentialsPlugin.getPluginInstance().getConfiguration().getString(QUIT_KEY);

    /**
     * Handle {@link PlayerJoinEvent}s.
     * @param event the event to handle
     */
    public void handlePlayerJoin(PlayerJoinEvent event) {
        if(JOIN_MESSAGE == null) {
            log.warning(PREFIX + CONFIGURATION_KEY_MISSING + JOIN_KEY);
            return;
        }
        String msg = String.format(JOIN_MESSAGE, event.getPlayer().getName());
        event.setJoinMessage(msg);
    }

    /**
     * Handle {@link PlayerQuitEvent}s.
     * @param event the event to handle
     */
    public void handlePlayerLeave(PlayerQuitEvent event) {
        if(QUIT_MESSAGE == null) {
            log.warning(PREFIX + CONFIGURATION_KEY_MISSING + QUIT_KEY);
            return;
        }
        String msg = String.format(QUIT_MESSAGE, event.getPlayer().getName());
        event.setQuitMessage(msg);
    }
}
