package de.flowwindustries.essentials.commands.time;

import lombok.extern.java.Log;

@Log
public class DayCommand extends AbstractTimeSwitcher {

    public DayCommand(String configKey, String permission, String prefix) {
        super(configKey, permission, prefix);
    }
}
