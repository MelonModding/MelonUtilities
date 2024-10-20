package MelonUtilities.commands.tpa;

import MelonUtilities.utility.FeedbackHandler;
import MelonUtilities.utility.TpaManager;
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
			FeedbackHandler.error(sender, "Failed to tpa, are you sure there is a pending request?");
		}
		return true;
	}

	@Override
	public boolean opRequired(String[] args) {
		return false;
	}

	@Override
	public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
		FeedbackHandler.syntax(sender, "< Command Syntax >");
		FeedbackHandler.syntax(sender, "  > /tpaccept");
	}
}
