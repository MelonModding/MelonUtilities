package MelonUtilities.commands.tpa;

import MelonUtilities.utility.FeedbackHandler;
import MelonUtilities.utility.TpaManager;
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
			FeedbackHandler.error(sender, "No pending request found to deny");
		}
		return true;
	}

	@Override
	public boolean opRequired(String[] args) {
		return false;
	}

	@Override
	public void sendCommandSyntax(CommandHandler commandHandler, CommandSender sender) {
		FeedbackHandler.syntax(sender, "< Command Syntax >");
		FeedbackHandler.syntax(sender, "  > /tpadeny");
	}
}
