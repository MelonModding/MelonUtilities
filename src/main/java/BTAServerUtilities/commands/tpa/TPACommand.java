package BTAServerUtilities.commands.tpa;

import BTAServerUtilities.utility.TpaManager;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandError;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
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
			sender.sendMessage("§eCould not find this " + args[0] + " you seek");
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
		commandSender.sendMessage("§8< Command Syntax >");
		commandSender.sendMessage("§8  > /tpa <destination player> [here]");
	}
}
