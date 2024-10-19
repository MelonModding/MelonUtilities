package MelonUtilities.commands.kit;

import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;

public class KittenCommand extends Command {
	private final static String COMMAND = "kitten";
	private final static String NAME = "Kitten";

	public KittenCommand() {
		super(COMMAND);
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] strings) {
		if(Math.random() > .5)
		sender.sendMessage("/ᐠ-ꞈ-ᐟ\\ ɴʏᴀ~");
		else{
		sender.sendMessage("/ᐠ - ˕ -マ ɴʏᴀᴀᴀᴀᴀ!");
		}
		return true;
	}

	@Override
	public boolean opRequired(String[] strings) {
		return false;
	}

	@Override
	public void sendCommandSyntax(CommandHandler commandHandler, CommandSender commandSender) {

	}
}
