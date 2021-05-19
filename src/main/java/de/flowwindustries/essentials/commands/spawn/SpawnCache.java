package de.flowwindustries.essentials.commands.spawn;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

public class SpawnCache {

    @Setter @Getter
    private static Location spawnLocation = null;
}
