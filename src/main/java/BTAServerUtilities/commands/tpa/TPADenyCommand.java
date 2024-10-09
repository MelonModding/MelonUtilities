package BTAServerUtilities.commands.tpa;

import BTAServerUtilities.utility.TpaManager;
import net.minecraft.core.net.command.*;
import net.minecraft.server.entity.player.EntityPlayerMP;

public class TPADenyCommand extends Command {
	private final static String COMMAND = "tpadeny";

	public TPADenyCommand() {
		super(COMMAND);
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
		if (!sender.isPlayer()) throw new CommandError("Must be used by a player!");
		if (!TpaManager.deny((EntityPlayerMP) sender.getPlayer())) {
			sender.sendMessage(TextFormatting.RED + "No pending request found to deny");
		}
		return true;
	}

	@Override
	public boolean opRequired(String[] args) {
		return false;
	}

	@Override
	public void sendCommandSyntax(CommandHandler commandHandler, CommandSender commandSender) {
		commandSender.sendMessage(TextFormatting.LIGHT_GRAY + "< Command Syntax >");
		commandSender.sendMessage(TextFormatting.LIGHT_GRAY + "  > /tpadeny");
	}
}
