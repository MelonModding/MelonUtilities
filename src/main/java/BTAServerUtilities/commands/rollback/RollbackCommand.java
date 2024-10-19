package BTAServerUtilities.commands.rollback;

import BTAServerUtilities.rollback.RollbackManager;
import BTAServerUtilities.utility.SyntaxBuilder;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;

public class RollbackCommand extends Command {

	private final static String COMMAND = "rollback";
	private final static String NAME = "RollbackCommand";

	public RollbackCommand(){super(COMMAND, "rb");}

	static SyntaxBuilder syntax = new SyntaxBuilder();
	public static void buildSyntax(){
		syntax.clear();
		syntax.append("title",                                                TextFormatting.LIGHT_GRAY + "< Command Syntax >");
	}

	private boolean takeSnapshot(CommandHandler handler, CommandSender sender, String[] args){
		RollbackManager.TakeModifiedChunkSnapshot();
		sender.sendMessage(TextFormatting.LIME + "Snap!");
		return true;
	}

	private boolean loadSnapshot(CommandHandler handler, CommandSender sender, String[] args){
		//RollbackManager.loadChunkIntoWorldFromCompound(sender.getWorld(), );
		sender.sendMessage(TextFormatting.LIME + "Snap!");
		return true;
	}


	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
		if (args.length == 0) {
			return false;
		}

		switch(args[0].toLowerCase()){
			case "takesnapshot":
			case "ts":
				return takeSnapshot(handler, sender, args);
			case "loadsnapshot":
			case "ls":
				return loadSnapshot(handler, sender,args);
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
