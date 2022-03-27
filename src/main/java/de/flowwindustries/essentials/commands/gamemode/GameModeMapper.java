package de.flowwindustries.essentials.commands.gamemode;

import org.bukkit.GameMode;

import java.util.Optional;

/**
 * Utility class to map an {@link Integer} to a {@link org.bukkit.GameMode}.
 */
public class GameModeMapper {

    private static final String GAMEMODE_MAP_ERROR = "Could not map to gamemode. Valid numbers are: 0, 1, 2, 3";

    /**
     * Map an {@link Integer} to an {@link GameMode}.
     * @param number the integer
     * @return the mapped {@link GameMode}
     * @throws IllegalArgumentException if the number cannot be mapped to a gamemode
     */
    public static GameMode mapGameModeSafe(int number) {
        return Optional.ofNullable(mapGameModeIntern(number))
                .orElseThrow(() -> new IllegalArgumentException(GAMEMODE_MAP_ERROR));
    }

    private static GameMode mapGameModeIntern(int number) {
        switch (number) {
            case 0 -> {return GameMode.SURVIVAL;}
            case 1 -> {return GameMode.CREATIVE;}
            case 2 -> {return GameMode.ADVENTURE;}
            case 3 -> {return GameMode.SPECTATOR;}
        }
        return null;
    }
}
