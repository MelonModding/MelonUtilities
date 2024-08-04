package BTAServerUtilities.commands.tpa;

import BTAServerUtilities.commands.tpa.utility.TpaManager;
import net.minecraft.core.net.command.*;
import net.minecraft.server.entity.player.EntityPlayerMP;

public class TPAcceptCommand extends Command {
	private final static String COMMAND = "tpaccept";

	public TPAcceptCommand() {
		super(COMMAND);
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
		if (!sender.isPlayer()) throw new CommandError("Must be used by a player!");
		if (!TpaManager.accept((EntityPlayerMP) sender.getPlayer())) {
			sender.sendMessage(TextFormatting.RED + "§eFailed to tpa, are you sure there is a pending request?");
		}
		return true;
	}

	@Override
	public boolean opRequired(String[] args) {
		return false;
	}

	@Override
	public void sendCommandSyntax(CommandHandler commandHandler, CommandSender commandSender) {
		commandSender.sendMessage("§8< Command Syntax >");
		commandSender.sendMessage("§8  > /tpaccept");
	}
}
