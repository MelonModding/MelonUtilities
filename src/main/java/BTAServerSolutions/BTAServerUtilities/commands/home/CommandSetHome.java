package BTAServerSolutions.BTAServerUtilities.commands.home;

import BTAServerSolutions.BTAServerUtilities.utility.Position;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;

public class CommandSetHome extends Command {
	public CommandSetHome() {
		super("sethome");
	}

	@Override
	public boolean execute(CommandHandler commandHandler, CommandSender commandSender, String[] strings) {
		String homeName = "default";

		if (strings.length > 0) {
			homeName = strings[0];
		}

		EntityPlayer player = commandSender.getPlayer();

		Position playerPosition = new Position((int) player.x, (int) player.y, (int) player.z, player.dimension);

		boolean success = HomesSingleton.getInstance().addPlayerHome(player.username, homeName, playerPosition);

		if (!success) {
			commandSender.sendMessage(TextFormatting.RED + "Cannot set any more homes!");
			return true;
		}

		commandSender.sendMessage(TextFormatting.GREEN + "Home set successfully.");

		return true;
	}

	@Override
	public boolean opRequired(String[] strings) {
		return false;
	}

	@Override
	public void sendCommandSyntax(CommandHandler commandHandler, CommandSender commandSender) {
		commandSender.sendMessage("/sethome <home>");
	}
}
