package goldenage.omnimod.homes.commands;

import goldenage.omnimod.homes.HomesSingleton;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.entity.player.EntityPlayerMP;

public class CommandDelHome extends Command {
	public CommandDelHome() {
		super("delhome");
	}

	@Override
	public boolean execute(CommandHandler commandHandler, CommandSender commandSender, String[] strings) {
		String homeName = "default";

		if (strings.length > 0) {
			homeName = strings[0];
		}

		EntityPlayer player = commandSender.getPlayer();

		boolean success = HomesSingleton.getInstance().removePlayerHome(player.username, homeName);

		if (!success) {
			commandSender.sendMessage(TextFormatting.RED + "Home does not exist!");
			return true;
		}

		commandSender.sendMessage(TextFormatting.GREEN + "Home deleted.");

		return true;
	}

	@Override
	public boolean opRequired(String[] strings) {
		return false;
	}

	@Override
	public void sendCommandSyntax(CommandHandler commandHandler, CommandSender commandSender) {
		commandSender.sendMessage("/delhome <home>");
	}
}
