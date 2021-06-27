package de.flowwindustries.essentials.event.messages;

import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

@RequiredArgsConstructor
public class PlayerConnectMessageListener implements Listener {

    private String joinMessage;
    private String leaveMessage;

    private final FileConfiguration configuration;

    public void handlePlayerJoin(PlayerJoinEvent event) {
        if(joinMessage == null) {
            joinMessage = Optional.ofNullable(configuration.getString("messages.join"))
                    .orElse("Welcome to the server!");
        }
        String msg = String.format(joinMessage, event.getPlayer().getName());
        event.setJoinMessage(msg);
    }

    public void handlePlayerLeave(PlayerQuitEvent event) {
        if(leaveMessage == null) {
            leaveMessage = Optional.ofNullable(configuration.getString("messages.leave"))
                    .orElse("%s has left the server");
        }
        String msg = String.format(leaveMessage, event.getPlayer().getName());
        event.setQuitMessage(msg);
    }
}
