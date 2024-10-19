package BTAServerUtilities.commands.utility;

import BTAServerUtilities.utility.SyntaxBuilder;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;

public class TestCommand extends Command {

	private final static String COMMAND = "test";
	private final static String NAME = "TestCommand";

	public TestCommand(){super(COMMAND);}

	static SyntaxBuilder syntax = new SyntaxBuilder();
	public static void buildSyntax(){
		syntax.clear();
		syntax.append("title",                                                TextFormatting.LIGHT_GRAY + "< Command Syntax >");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
		if (args.length == 0) {
			return false;
		}

		sender.sendMessage(TextFormatting.RED + " " + NAME + " Error: (Invalid Syntax)");
		return false;
	}

	@Override
	public boolean opRequired(String[] args) {
		return true;
	}

	@Override
	public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
		syntax.printAllLines(sender);
	}
}
