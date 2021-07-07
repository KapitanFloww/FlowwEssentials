package de.flowwindustries.essentials.commands.time;

import de.flowwindustries.essentials.EssentialsPlugin;

public class NightCommand extends AbstractTimeSwitcher {

    public NightCommand(String configKey, String permission) {
        super(configKey, permission);
    }

    @Override
    protected String getPrefix() {
        return EssentialsPlugin.getPrefix();
    }
}
