package BTAServerUtilities.commands.crew;

import BTAServerUtilities.utility.CommandSyntaxBuilder;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;

public class CrewCommand extends Command {
	public static CommandSyntaxBuilder syntax = new CommandSyntaxBuilder();

	private final static String COMMAND = "crew";

	public CrewCommand(){super(COMMAND, "c");}

	public static void buildRoleSyntax(){
		syntax.clear();
		syntax.append("title",                                                  "§8< Command Syntax >");
		syntax.append("create", "title",                                  "§8  > /crew create <crew name>");
		syntax.append("delete", "title",                                  "§8  > /crew delete <crew name>");
		syntax.append("invite", "title",                                  "§8  > /crew invite <username>");
		syntax.append("kick", "title",                                    "§8  > /crew kick <username>");
		syntax.append("tag", "title",                                     "§8  > /crew tag <mode>");
		syntax.append("tagView", "tag",                                   "§8    > view");
		syntax.append("tagVisible", "tag",                                "§8    > visible true/false");
		syntax.append("tagEdit", "tag",                                   "§8    > edit <mode>");
		syntax.append("tagEditDisplay", "tagEdit",                        "§8      > display <mode>");
		syntax.append("tagEditDisplayName", "tagEditDisplay",             "§8        > name <display name>");
		syntax.append("tagEditDisplayColor", "tagEditDisplay",            "§8        > color <color/hex>");
		syntax.append("tagEditDisplayUnderline", "tagEditDisplay",        "§8        > underline true/false");
		syntax.append("tagEditDisplayBold", "tagEditDisplay",             "§8        > bold true/false");
		syntax.append("tagEditDisplayItalics", "tagEditDisplay",          "§8        > italics true/false");
		syntax.append("tagEditBorder", "tagEdit",                         "§8      > border <mode>");
		syntax.append("tagEditBorderColor", "tagEditBorder",              "§8        > color <color/hex>");
		syntax.append("tagEditBorderType", "tagEditBorder",               "§8        > none/bracket/caret/curly");
		syntax.append("tagEditBorderCustom", "tagEditBorder",             "§8        > custom [<affix>]");
		syntax.append("tagEditBorderCustomAffix", "tagEditBorderCustom",  "§8          > prefix/suffix <custom affix>");
		syntax.append("etc",                                                    "§8    etc.");
	}

	@Override
	public boolean execute(CommandHandler commandHandler, CommandSender commandSender, String[] strings) {
		return false;
	}

	@Override
	public boolean opRequired(String[] strings) {
		return false;
	}

	@Override
	public void sendCommandSyntax(CommandHandler commandHandler, CommandSender commandSender) {

	}
}
