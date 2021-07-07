package de.flowwindustries.essentials.commands.time;

import de.flowwindustries.essentials.EssentialsPlugin;
import lombok.extern.java.Log;

@Log
public class DayCommand extends AbstractTimeSwitcher {

    public DayCommand(String configKey, String permission) {
        super(configKey, permission);
    }

    protected String getPrefix() {
        return EssentialsPlugin.getPrefix();
    }
}
