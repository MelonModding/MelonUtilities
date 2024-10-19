package MelonUtilities.commands.misc;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;

/**
 * This command gives the player a compass.
 ***********************************************************************************/

public class CompassCommand extends Command {
	private final static String COMMAND = "compass";
	private final static String NAME = "Compass";

	public CompassCommand() {
        super(COMMAND);
    }

	@Override
	public boolean execute(CommandHandler commandHandler, CommandSender commandSender, String[] strings) {
		// Give the player a compass
		commandSender.getPlayer().inventory.insertItem(new ItemStack(Item.toolCompass), false);
		return false;
	}

	@Override
	public boolean opRequired(String[] strings) {
		return false;
	}

	@Override
	public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
		// Feedback to the player that it executed
		handler.sendCommandFeedback(sender,"Given 1x " + NAME + " to " + sender.getPlayer().username);
	}
}
