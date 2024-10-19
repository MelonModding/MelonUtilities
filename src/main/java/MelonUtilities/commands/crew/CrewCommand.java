package MelonUtilities.commands.crew;

import MelonUtilities.utility.SyntaxBuilder;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;

public class CrewCommand extends Command {
	public static SyntaxBuilder syntax = new SyntaxBuilder();

	private final static String COMMAND = "crew";

	public CrewCommand(){super(COMMAND, "c");}

	public static void buildRoleSyntax(){
		syntax.clear();
		syntax.append("title",                                                  TextFormatting.LIGHT_GRAY + "< Command Syntax >");
		syntax.append("create", "title",                                  TextFormatting.LIGHT_GRAY + "  > /crew create <crew name>");
		syntax.append("delete", "title",                                  TextFormatting.LIGHT_GRAY + "  > /crew delete <crew name>");
		syntax.append("invite", "title",                                  TextFormatting.LIGHT_GRAY + "  > /crew invite <username>");
		syntax.append("kick", "title",                                    TextFormatting.LIGHT_GRAY + "  > /crew kick <username>");
		syntax.append("tag", "title",                                     TextFormatting.LIGHT_GRAY + "  > /crew tag <mode>");
		syntax.append("tagView", "tag",                                   TextFormatting.LIGHT_GRAY + "    > view");
		syntax.append("tagVisible", "tag",                                TextFormatting.LIGHT_GRAY + "    > visible true/false");
		syntax.append("tagEdit", "tag",                                   TextFormatting.LIGHT_GRAY + "    > edit <mode>");
		syntax.append("tagEditDisplay", "tagEdit",                        TextFormatting.LIGHT_GRAY + "      > display <mode>");
		syntax.append("tagEditDisplayName", "tagEditDisplay",             TextFormatting.LIGHT_GRAY + "        > name <display name>");
		syntax.append("tagEditDisplayColor", "tagEditDisplay",            TextFormatting.LIGHT_GRAY + "        > color <color/hex>");
		syntax.append("tagEditDisplayUnderline", "tagEditDisplay",        TextFormatting.LIGHT_GRAY + "        > underline true/false");
		syntax.append("tagEditDisplayBold", "tagEditDisplay",             TextFormatting.LIGHT_GRAY + "        > bold true/false");
		syntax.append("tagEditDisplayItalics", "tagEditDisplay",          TextFormatting.LIGHT_GRAY + "        > italics true/false");
		syntax.append("tagEditBorder", "tagEdit",                         TextFormatting.LIGHT_GRAY + "      > border <mode>");
		syntax.append("tagEditBorderColor", "tagEditBorder",              TextFormatting.LIGHT_GRAY + "        > color <color/hex>");
		syntax.append("tagEditBorderType", "tagEditBorder",               TextFormatting.LIGHT_GRAY + "        > none/bracket/caret/curly");
		syntax.append("tagEditBorderCustom", "tagEditBorder",             TextFormatting.LIGHT_GRAY + "        > custom [<affix>]");
		syntax.append("tagEditBorderCustomAffix", "tagEditBorderCustom",  TextFormatting.LIGHT_GRAY + "          > prefix/suffix <custom affix>");
		syntax.append("etc",                                                    TextFormatting.LIGHT_GRAY + "    etc.");
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
