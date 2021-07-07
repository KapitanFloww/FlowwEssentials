package de.flowwindustries.essentials.commands.fly;

import de.flowwindustries.essentials.EssentialsPlugin;
import de.flowwindustries.flowwutils.commands.AbstractCommand;
import de.flowwindustries.flowwutils.exception.InvalidArgumentsException;
import de.flowwindustries.flowwutils.exception.PlayerNotFoundException;
import de.flowwindustries.flowwutils.message.MessageType;
import de.flowwindustries.flowwutils.message.PlayerMessage;
import de.flowwindustries.flowwutils.safe.SpigotParser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Speed Command.
 * /speed {0-10} [player]
 * floww.speed
 */
public class SpeedCommand extends AbstractCommand {

    public SpeedCommand(String permission) {
        super(permission);
    }

    @Override
    protected void ingameCommand(Player player, String[] args) throws PlayerNotFoundException, InvalidArgumentsException {
        float amount = 0.1f;
        switch (args.length) {
            case 1: //speed <1-10>
                amount = getSpeedSafe(args[0]);
                player.setFlySpeed(amount);


                PlayerMessage.sendMessage(player, MessageType.SUCCESS, EssentialsPlugin.getPrefix(), "You have set your speed to " + (amount*10) );
                break;
            case 2: //speed <1-10> [player]

                amount = getSpeedSafe(args[0]);
                Player targetPlayer = SpigotParser.getPlayerSafe(args[1]);

                targetPlayer.setFlySpeed(amount);

                PlayerMessage.sendMessage(List.of(player, targetPlayer), MessageType.SUCCESS, EssentialsPlugin.getPrefix(), "Flight speed of " + targetPlayer.getName() + " has been set to " + (amount*10) );
                break;
            default:
                throw new InvalidArgumentsException();
        }

    }

    @Override
    protected void consoleCommand(String[] args) throws PlayerNotFoundException, InvalidArgumentsException {
        if (args.length != 2) {
            throw new InvalidArgumentsException();
        }

        float amount = getSpeedSafe(args[0]);
        Player target = SpigotParser.getPlayerSafe(args[1]);

        target.setFlySpeed(amount);

        PlayerMessage.sendMessage(target, MessageType.SUCCESS, EssentialsPlugin.getPrefix(), "Your flightspeed has been set to " + (amount*10));
        Bukkit.getConsoleSender().sendMessage("Flightspeed of " + target.getName() + "has been set to " + (amount*10));
    }

    private float getSpeedSafe(String string) throws InvalidArgumentsException {
        float amount = SpigotParser.getFloatSafe(string);
        amount = amount / 10;
        if(amount < 0 || amount > 1)
            throw new InvalidArgumentsException("Fly speed must be between 0 and 10");
        return amount;
    }


    @Override
    protected String getPrefix() {
        return EssentialsPlugin.getPrefix();
    }
}
