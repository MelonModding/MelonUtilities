package MelonUtilities.commands.utility;

import MelonUtilities.utility.FeedbackHandler;
import MelonUtilities.utility.SyntaxBuilder;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSource;
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
	public boolean execute(CommandHandler handler, CommandSource source, String[] args) {
		if (args.length == 0) {
			return false;
		}

		FeedbackHandler.error(source,  " " + NAME + " Error: (Invalid Syntax)");
		return false;
	}

	@Override
	public boolean opRequired(String[] args) {
		return true;
	}

	@Override
	public void sendCommandSyntax(CommandHandler handler, CommandSource source) {
		syntax.printAllLines(source);
	}
}
