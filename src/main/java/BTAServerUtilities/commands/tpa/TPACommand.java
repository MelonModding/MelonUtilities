package BTAServerUtilities.commands.tpa;

import BTAServerUtilities.utility.TpaManager;
import net.minecraft.core.net.command.*;
import net.minecraft.server.entity.player.EntityPlayerMP;

public class TPACommand extends Command {
	private final static String COMMAND = "tpa";

	public TPACommand() {super(COMMAND);}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
		if (!sender.isPlayer()) throw new CommandError("Must be used by a player!");
		if (args.length != 1 && args.length != 2) return false;

		EntityPlayerMP player = (EntityPlayerMP) sender.getPlayer();
		EntityPlayerMP receiver = (EntityPlayerMP) handler.getPlayer(args[0]);
		if (receiver == null) {
			sender.sendMessage(TextFormatting.RED + "Could not find this " + args[0] + " you seek");
		    return false;
	    }
	    boolean here = args.length == 2 && args[1].equals("here");
		TpaManager.addRequest(player, receiver, here);
		return true;
	}

	@Override
	public boolean opRequired(String[] args) {
		return false;
	}

	@Override
	public void sendCommandSyntax(CommandHandler commandHandler, CommandSender commandSender) {
		commandSender.sendMessage(TextFormatting.LIGHT_GRAY + "< Command Syntax >");
		commandSender.sendMessage(TextFormatting.LIGHT_GRAY + "  > /tpa <destination player> [here]");
	}
}
