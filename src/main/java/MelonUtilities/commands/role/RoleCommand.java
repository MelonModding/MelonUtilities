package MelonUtilities.commands.role;

import MelonUtilities.commands.role.subcommands.EditRoleDisplaySubcommand;
import MelonUtilities.commands.role.subcommands.EditRoleTextSubcommand;
import MelonUtilities.commands.role.subcommands.EditRoleUsernameSubcommand;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.ConfigData;
import MelonUtilities.utility.SyntaxBuilder;
import MelonUtilities.utility.RoleBuilder;
import MelonUtilities.config.datatypes.RoleData;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;

@SuppressWarnings("SameReturnValue")
public class RoleCommand extends Command {

	private final static String COMMAND = "role";

	public RoleCommand(){super(COMMAND, "r");}

	public static RoleData getRoleFromArg(String arg){return Data.roles.getOrCreate(arg, RoleData.class);}

	public static SyntaxBuilder syntax = new SyntaxBuilder();
	public static void buildRoleSyntax(){
		syntax.clear();
		syntax.append("title",                                                  TextFormatting.LIGHT_GRAY + "< Command Syntax >");
		syntax.append("create", "title",                                  TextFormatting.LIGHT_GRAY + "  > /role create <role id> [<priority>]");
		syntax.append("delete", "title",                                  TextFormatting.LIGHT_GRAY + "  > /role delete <role id>");
		syntax.append("edit", "title",                                    TextFormatting.LIGHT_GRAY + "  > /role edit <role id> <mode>");
		syntax.append("priority", "edit",                                 TextFormatting.LIGHT_GRAY + "    > priority <priority value>");
/*		syntax.append("perms", "edit",                                    TextFormatting.LIGHT_GRAY + "    > perms <permission>");*/
		syntax.append("display", "edit",                                  TextFormatting.LIGHT_GRAY + "    > display <style>");
		syntax.append("displayName", "display",                           TextFormatting.LIGHT_GRAY + "      > name <display name>");
		syntax.append("displayColor", "display",                          TextFormatting.LIGHT_GRAY + "      > color <color/hex>");
		syntax.append("displayUnderline", "display",                      TextFormatting.LIGHT_GRAY + "      > underline true/false");
		syntax.append("displayBold", "display",                           TextFormatting.LIGHT_GRAY + "      > bold true/false");
		syntax.append("displayItalics", "display",                        TextFormatting.LIGHT_GRAY + "      > italics true/false");
		syntax.append("displayBorder", "display",                         TextFormatting.LIGHT_GRAY + "      > border <style>");
		syntax.append("displayBorderColor", "displayBorder",              TextFormatting.LIGHT_GRAY + "        > color <color/hex>");
		syntax.append("displayBorderType", "displayBorder",               TextFormatting.LIGHT_GRAY + "        > none/bracket/caret/curly");
		syntax.append("displayBorderCustom", "displayBorder",             TextFormatting.LIGHT_GRAY + "        > custom [<affix>]");
		syntax.append("displayBorderCustomAffix", "displayBorderCustom",  TextFormatting.LIGHT_GRAY + "          > prefix/suffix <custom affix>");
		syntax.append("username", "edit",                                 TextFormatting.LIGHT_GRAY + "    > username <style>");
		/*syntax.append("usernameColor", "username",                        TextFormatting.LIGHT_GRAY + "      > color <color/hex>");
		syntax.append("usernameUnderline", "username",                    TextFormatting.LIGHT_GRAY + "      > underline true/false");
		syntax.append("usernameBold", "username",                         TextFormatting.LIGHT_GRAY + "      > bold true/false");
		syntax.append("usernameItalics", "username",                      TextFormatting.LIGHT_GRAY + "      > italics true/false");*/
		syntax.append("usernameBorder", "username",                       TextFormatting.LIGHT_GRAY + "      > border <style>");
		syntax.append("usernameBorderColor", "usernameBorder",            TextFormatting.LIGHT_GRAY + "        > color <color/hex>");
		syntax.append("usernameBorderType", "usernameBorder",             TextFormatting.LIGHT_GRAY + "        > none/bracket/caret/curly");
		syntax.append("usernameBorderCustom", "usernameBorder",           TextFormatting.LIGHT_GRAY + "        > custom [<affix>]");
		syntax.append("usernameBorderCustomAffix", "usernameBorderCustom",TextFormatting.LIGHT_GRAY + "          > prefix/suffix <custom affix>");
		syntax.append("text", "edit",                                     TextFormatting.LIGHT_GRAY + "    > text <style>");
		syntax.append("textColor", "text",                                TextFormatting.LIGHT_GRAY + "      > color <color/hex> (*bug: hex won't wrap!)");
		syntax.append("textUnderline", "text",                            TextFormatting.LIGHT_GRAY + "      > underline true/false");
		syntax.append("textBold", "text",                                 TextFormatting.LIGHT_GRAY + "      > bold true/false");
		syntax.append("textItalics", "text",                              TextFormatting.LIGHT_GRAY + "      > italics true/false");
		syntax.append("grant", "title",                                   TextFormatting.LIGHT_GRAY + "  > /role grant <role id> [<username>]");
		syntax.append("revoke", "title",                                  TextFormatting.LIGHT_GRAY + "  > /role revoke <role id> [<username>]");
		syntax.append("set", "title",                                     TextFormatting.LIGHT_GRAY + "  > /role set <mode>");
		syntax.append("setDefaultRole", "set",                            TextFormatting.LIGHT_GRAY + "    > defaultRole <role id>/none");
		syntax.append("setDisplayMode", "set",                            TextFormatting.LIGHT_GRAY + "    > displayMode single/multi");
		syntax.append("list", "title",                                    TextFormatting.LIGHT_GRAY + "  > /role list");
		syntax.append("reload", "title",                                  TextFormatting.LIGHT_GRAY + "  > /role reload");
	}


	private boolean create(CommandSender sender, String[] args){

		if (args.length == 1) {
			sender.sendMessage(TextFormatting.RED + "Failed to Create Role (Invalid Syntax)");
			syntax.printLayerAndSubLayers("create", sender);
			return true;
		}

		String role = args[1];

		if (Data.roles.dataHashMap.containsKey(role)) {
			sender.sendMessage(TextFormatting.RED + "Failed to Create Role: " + role + " (Role Already Exists)");
			return true;
		}

		Data.roles.getOrCreate(role, RoleData.class);
		Data.roles.loadAll(RoleData.class);
		getRoleFromArg(role).displayName = role;
		Data.roles.saveAll();

		if (args.length == 3){
			getRoleFromArg(role).priority = Integer.parseInt(args[2]);
		}

		sender.sendMessage(TextFormatting.LIME + "Created Role: " + getRoleFromArg(role).displayName + " with Priority: " + getRoleFromArg(role).priority);
		return true;
	}

	private boolean delete(CommandSender sender, String[] args){
		if (args.length == 1) {
			sender.sendMessage(TextFormatting.RED + "Failed to Delete Role (Invalid Syntax)");
			syntax.printLayerAndSubLayers("delete", sender);
			return true;
		}

		String role = args[1];

		switch (Data.roles.remove(role)) {
			case 0:
				sender.sendMessage(TextFormatting.ORANGE + "Deleted Role: " + role);
				return true;
			case 1:
				sender.sendMessage(TextFormatting.RED + "Failed to Delete Role: " + role + " (Role Doesn't Exist)");
				syntax.printLayerAndSubLayers("delete", sender);
				return true;
			case 2:
				sender.sendMessage(TextFormatting.RED + "Failed to Delete Role: " + role + " (IO Error)");
				return true;
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Delete Role (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("delete", sender);
		return true;
    }

	private boolean reload(CommandSender sender){
		Data.roles.loadAll(RoleData.class);
		sender.sendMessage(TextFormatting.LIME + "Reloaded " + Data.roles.dataHashMap.size() + " Role(s)!");
		RoleCommand.buildRoleSyntax();
		sender.sendMessage(TextFormatting.LIME + "Built Role Syntax!");
		Data.configs.loadAll(ConfigData.class);
		sender.sendMessage(TextFormatting.LIME + "Reloaded Config!");
		return true;
	}

	private boolean edit(CommandSender sender, String[] args){

		if(args.length == 1){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Role (Invalid Syntax)");
			syntax.printLayerAndSubLayers("edit", sender);
			return true;
		}

		if(!Data.roles.dataHashMap.containsKey(args[1])){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Role (Invalid Role)");
			syntax.printLayer("edit", sender);
			return true;
		}

		if(args.length == 2){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Role (Invalid Syntax)");
			syntax.printLayer("edit", sender);
			return true;
		}

		switch(args[2]){
			case "priority":
				return priority(sender, args);
			case "display":
				return display(sender, args);
			case "username":
				return username(sender, args);
			case "text":
				return text(sender, args);
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Edit Role (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("edit", sender);
		return true;
    }

	private boolean priority(CommandSender sender, String[] args){

		if(args.length == 3){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Role Priority (Invalid Syntax)");
			syntax.printLayer("priority", sender);
			return true;
		}

		if(args.length == 4){
			Data.roles.loadAll(RoleData.class);
			getRoleFromArg(args[1]).priority = Integer.parseInt(args[3]);
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set Priority for Role " + args[1] + " to: " + TextFormatting.LIGHT_GRAY + args[3]);
			return true;
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Edit Role Priority (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("priority", sender);
		return true;
	}

	private boolean display(CommandSender sender, String[] args){

		if(args.length == 3){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Role Display (Invalid Syntax)");
			syntax.printLayer("display", sender);
			return true;
		}

		switch(args[3]){
			case "name":
				return EditRoleDisplaySubcommand.displayName(sender, args);
			case "color":
				return EditRoleDisplaySubcommand.displayColor(sender, args);
			case "underline":
				return EditRoleDisplaySubcommand.displayUnderline(sender, args);
			case "bold":
				return EditRoleDisplaySubcommand.displayBold(sender, args);
			case "italics":
				return EditRoleDisplaySubcommand.displayItalics(sender, args);
			case "border":
				return EditRoleDisplaySubcommand.displayBorder(sender, args);
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Edit Role Display (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("display", sender);
		return true;
	}

	private boolean username(CommandSender sender, String[] args){

		if(args.length == 3){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Role Username (Invalid Syntax)");
			syntax.printLayerAndSubLayers("username", sender);
			return true;
		}

		switch(args[3]){
			/*case "color":
				return EditRoleUsername.usernameColor(sender, args);
			case "underline":
				return EditRoleUsername.usernameUnderline(sender, args);
			case "bold":
				return EditRoleUsername.usernameBold(sender, args);
			case "italics":
				return EditRoleUsername.usernameItalics(sender, args);*/
			case "border":
				return EditRoleUsernameSubcommand.usernameBorder(sender, args);
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Edit Role Username (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("username", sender);
		return true;
	}

	private boolean text(CommandSender sender, String[] args){

		if(args.length == 3){
			sender.sendMessage(TextFormatting.RED + "Failed to Edit Role Text (Invalid Syntax)");
			syntax.printLayerAndSubLayers("text", sender);
			return true;
		}

		switch(args[3]){
			case "color":
				return EditRoleTextSubcommand.textColor(sender, args);
			case "underline":
				return EditRoleTextSubcommand.textUnderline(sender, args);
			case "bold":
				return EditRoleTextSubcommand.textBold(sender, args);
			case "italics":
				return EditRoleTextSubcommand.textItalics(sender, args);
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Edit Role Text (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("text", sender);
		return true;
	}

	private boolean grant(CommandSender sender, String[] args){

		if(args.length == 1){
			sender.sendMessage(TextFormatting.RED + "Failed to Grant Role (Invalid Syntax)");
			syntax.printLayerAndSubLayers("grant", sender);
			return true;
		}


		if(!Data.roles.dataHashMap.containsKey(args[1])){
			sender.sendMessage(TextFormatting.RED + "Failed to Grant Role (Role doesn't exist!)");
			syntax.printLayerAndSubLayers("grant", sender);
			return true;
		}

		RoleData roleData = getRoleFromArg(args[1]);

		if(args.length == 2 && !roleData.playersGrantedRole.contains(sender.getPlayer().username)){
			Data.roles.loadAll(RoleData.class);
			getRoleFromArg(args[1]).playersGrantedRole.add(sender.getPlayer().username);
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Granted Role: " + args[1] + " to player: " + TextFormatting.LIGHT_GRAY + sender.getPlayer().username);
			return true;
		} else if (args.length == 3 && !roleData.playersGrantedRole.contains(args[2])){
			Data.roles.loadAll(RoleData.class);
			getRoleFromArg(args[1]).playersGrantedRole.add(args[2]);
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Granted Role: " + args[1] + " to player: " + TextFormatting.LIGHT_GRAY + args[2]);
			return true;
		} else if (roleData.playersGrantedRole.contains(sender.getPlayer().username) || roleData.playersGrantedRole.contains(args[2])) {
			sender.sendMessage(TextFormatting.RED + "Failed to Grant Role (Player already has Role!)");
			return true;
		}


		sender.sendMessage(TextFormatting.RED + "Failed to Grant Role (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("grant", sender);
		return true;
	}

	private boolean revoke(CommandSender sender, String[] args){

		if(args.length == 1){
			sender.sendMessage(TextFormatting.RED + "Failed to Revoke Role (Invalid Syntax)");
			syntax.printLayerAndSubLayers("revoke", sender);
			return true;
		}

		if(!Data.roles.dataHashMap.containsKey(args[1])){
			sender.sendMessage(TextFormatting.RED + "Failed to Revoke Role (Role doesn't exist!)");
			syntax.printLayerAndSubLayers("revoke", sender);
			return true;
		}

		RoleData roleData = getRoleFromArg(args[1]);

		if (args.length == 3 && roleData.playersGrantedRole.contains(args[2])) {
			Data.roles.loadAll(RoleData.class);
			getRoleFromArg(args[1]).playersGrantedRole.remove(args[2]);
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.ORANGE + "Revoked Role: " + args[1] + " from player: " + TextFormatting.LIGHT_GRAY + args[2]);
			return true;
		} else if (args.length == 2 && roleData.playersGrantedRole.contains(sender.getPlayer().username)){
			Data.roles.loadAll(RoleData.class);
			getRoleFromArg(args[1]).playersGrantedRole.remove(sender.getPlayer().username);
			Data.roles.saveAll();
			sender.sendMessage(TextFormatting.ORANGE + "Revoked Role: " + args[1] + " from player: " + TextFormatting.LIGHT_GRAY + sender.getPlayer().username);
			return true;
		} else if (!roleData.playersGrantedRole.contains(sender.getPlayer().username) || !roleData.playersGrantedRole.contains(args[2])) {
			sender.sendMessage(TextFormatting.RED + "Failed to Revoke Role (Player does not have Role!)");
			return true;
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Revoke Role (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("revoke", sender);
		return true;
	}

	private boolean list(CommandSender sender) {
		if (Data.roles.dataHashMap.isEmpty()) {
			sender.sendMessage(TextFormatting.LIGHT_GRAY + "< Roles: >");
			sender.sendMessage(TextFormatting.LIGHT_GRAY + "  -No Roles Created-");
			return true;
		}

		sender.sendMessage(TextFormatting.LIGHT_GRAY + "< Roles: >");

		for (String role : Data.roles.dataHashMap.keySet()) {
			sender.sendMessage(TextFormatting.LIGHT_GRAY + "  > Role ID: " + TextFormatting.WHITE + TextFormatting.ITALIC + role + TextFormatting.LIGHT_GRAY + " - Priority: " + TextFormatting.WHITE + getRoleFromArg(role).priority);
			sender.sendMessage(TextFormatting.LIGHT_GRAY + "    > " + RoleBuilder.buildRoleDisplay(Data.roles.dataHashMap.get(role))
												+ RoleBuilder.buildRoleUsername(Data.roles.dataHashMap.get(role), sender.getPlayer().getDisplayName())
												+ RoleBuilder.buildRoleTextFormat(Data.roles.dataHashMap.get(role)) + "text");
		}

		return true;
	}

	private boolean set(CommandSender sender, String[] args) {
		if (args.length == 1) {
			sender.sendMessage(TextFormatting.RED + "Failed to Set Role Value (Invalid Syntax)");
			syntax.printLayerAndSubLayers("set", sender);
			return true;
		}

		if (args.length >= 2) {
			switch(args[1]){
				case "defaultRole" :
					return setDefaultRole(sender, args);
				case "displayMode" :
					return setDisplayMode(sender, args);
			}
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Set Role Value (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("set", sender);
		return true;
	}

	private boolean setDefaultRole(CommandSender sender, String[] args){

		if(args.length == 2){
			sender.sendMessage(TextFormatting.RED + "Failed to Set Default Role (Invalid Syntax)");
			syntax.printLayerAndSubLayers("setDefaultRole", sender);
			return true;
		}

		if(args.length == 3) {
			for (String role : Data.roles.dataHashMap.keySet()) {
				if (args[2].equals(role)) {
					Data.configs.loadAll(ConfigData.class);
					Data.configs.getOrCreate("config", ConfigData.class).defaultRole = args[2];
					Data.configs.saveAll();
					sender.sendMessage(TextFormatting.LIME + "Set defaultRole to: " + args[2]);
					return true;
				} else if (args[2].equals("none")) {
					Data.configs.loadAll(ConfigData.class);
					Data.configs.getOrCreate("config", ConfigData.class).defaultRole = null;
					Data.configs.saveAll();
					sender.sendMessage(TextFormatting.LIME + "Set defaultRole to: none");
					return true;
				}
			}
			sender.sendMessage(TextFormatting.RED + "Failed to Set Default Role (Invalid Role)");
			syntax.printLayerAndSubLayers("setDefaultRole", sender);
			return true;
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Set Default Role (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("setDefaultRole", sender);
		return true;
	}

	private boolean setDisplayMode(CommandSender sender, String[] args){

		if(args.length == 2){
			sender.sendMessage(TextFormatting.RED + "Failed to Set Display Mode (Invalid Syntax)");
			syntax.printLayerAndSubLayers("setDisplayMode", sender);
			return true;
		}

		if(args.length == 3 && args[2].equals("single")) {
			Data.configs.loadAll(ConfigData.class);
			Data.configs.getOrCreate("config", ConfigData.class).displayMode = "single";
			Data.configs.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set displayMode to: single");
			return true;
		} else if (args.length == 3 && args[2].equals("multi")) {
			Data.configs.loadAll(ConfigData.class);
			Data.configs.getOrCreate("config", ConfigData.class).displayMode = "multi";
			Data.configs.saveAll();
			sender.sendMessage(TextFormatting.LIME + "Set displayMode to: multi");
			return true;
		}

		sender.sendMessage(TextFormatting.RED + "Failed to Set Display Mode (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("setDisplayMode", sender);
		return true;
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args){

		if (args.length == 0) {
			return false;
		}

		switch(args[0]){
			case "create" :
				return create(sender, args);
			case "delete" :
				return delete(sender, args);
			case "reload" :
				return reload(sender);
			case "edit" :
				return edit(sender, args);
			case "grant" :
				return grant(sender, args);
			case "revoke" :
				return revoke(sender, args);
			case "list" :
				return list(sender);
			case "set" :
				return set(sender, args);
		}


		sender.sendMessage(TextFormatting.RED + "Role Command Failed (Invalid Syntax)");
		syntax.printAllLines(sender);
		return true;
	}

	@Override
	public boolean opRequired(String[] strings) {
		return true;
	}

	@Override
	public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
		syntax.printAllLines(sender);
	}
}
