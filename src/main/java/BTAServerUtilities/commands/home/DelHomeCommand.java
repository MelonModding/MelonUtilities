package BTAServerUtilities.commands.home;

import BTAServerUtilities.utility.CommandSyntaxBuilder;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;

public class DelHomeCommand extends Command {

	public DelHomeCommand() {
		super("delhome");
	}

	CommandSyntaxBuilder syntax = new CommandSyntaxBuilder();

	public void buildRoleSyntax(){
		syntax.clear();
		syntax.append("title",                            "§8< Command Syntax >");
		syntax.append("delhome",                          "§8  > /delhome [<home name>]");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {


		return true;
	}

	@Override
	public boolean opRequired(String[] strings) {
		return false;
	}

	@Override
	public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
		syntax.printAllLines(sender);
	}
}
