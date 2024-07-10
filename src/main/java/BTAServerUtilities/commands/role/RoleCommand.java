package BTAServerUtilities.commands.role;

import BTAServerUtilities.config.Data;
import BTAServerUtilities.config.datatypes.ConfigData;
import BTAServerUtilities.utility.CommandSyntaxBuilder;
import BTAServerUtilities.utility.RoleBuilder;
import BTAServerUtilities.config.datatypes.RoleData;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;

@SuppressWarnings("SameReturnValue")
public class RoleCommand extends Command {

	private final static String COMMAND = "role";

	public RoleCommand(){super(COMMAND);}

	public static RoleData getRoleFromArg(String arg){return Data.roles.getOrCreate(arg, RoleData.class);}
	static CommandSyntaxBuilder syntax = new CommandSyntaxBuilder();

	public static void buildRoleSyntax(){
		syntax.clear();
		syntax.append("title",                                                  "§8< Command Syntax >");
		syntax.append("create",                                                 "§8  > /role create <role id> [<priority>]");
		syntax.append("delete",                                                 "§8  > /role delete <role id>");
		syntax.append("edit",                                                   "§8  > /role edit <role id> <mode>");
		syntax.append("priority", "edit",                                 "§8    > priority <priority value>");
/*		syntax.append("perms", "edit",                                    "§8    > perms <permission>");*/
		syntax.append("display", "edit",                                  "§8    > display <style>");
		syntax.append("displayName", "display",                           "§8      > name <display name>");
		syntax.append("displayColor", "display",                          "§8      > color <color/hex>");
		syntax.append("displayUnderline", "display",                      "§8      > underline true/false");
		syntax.append("displayBold", "display",                           "§8      > bold true/false");
		syntax.append("displayItalics", "display",                        "§8      > italics true/false");
		syntax.append("displayBorder", "display",                         "§8      > border <style>");
		syntax.append("displayBorderColor", "displayBorder",              "§8        > color <color/hex>");
		syntax.append("displayBorderType", "displayBorder",               "§8        > none/bracket/caret/curly");
		syntax.append("displayBorderCustom", "displayBorder",             "§8        > custom [<affix>]");
		syntax.append("displayBorderCustomAffix", "displayBorderCustom",  "§8          > prefix/suffix <custom affix>");
		syntax.append("username", "edit",                                 "§8    > username <style>");
		/*syntax.append("usernameColor", "username",                        "§8      > color <color/hex>");
		syntax.append("usernameUnderline", "username",                    "§8      > underline true/false");
		syntax.append("usernameBold", "username",                         "§8      > bold true/false");
		syntax.append("usernameItalics", "username",                      "§8      > italics true/false");*/
		syntax.append("usernameBorder", "username",                       "§8      > border <style>");
		syntax.append("usernameBorderColor", "usernameBorder",            "§8        > color <color/hex>");
		syntax.append("usernameBorderType", "usernameBorder",             "§8        > none/bracket/caret/curly");
		syntax.append("usernameBorderCustom", "usernameBorder",           "§8        > custom [<affix>]");
		syntax.append("usernameBorderCustomAffix", "usernameBorderCustom","§8          > prefix/suffix <custom affix>");
		syntax.append("text", "edit",                                     "§8    > text <style>");
		syntax.append("textColor", "text",                                "§8      > color <color/hex> (*bug: hex won't wrap!)");
		syntax.append("textUnderline", "text",                            "§8      > underline true/false");
		syntax.append("textBold", "text",                                 "§8      > bold true/false");
		syntax.append("textItalics", "text",                              "§8      > italics true/false");
		syntax.append("grant",                                                  "§8  > /role grant <role id> [<username>]");
		syntax.append("revoke",                                                 "§8  > /role revoke <role id> [<username>]");
		syntax.append("set",                                                    "§8  > /role set <mode>");
		syntax.append("setDefaultRole", "set",                            "§8    > defaultRole <role id>/none");
		syntax.append("setDisplayMode", "set",                            "§8    > displayMode single/multi");
		syntax.append("list",                                                   "§8  > /role list");
		syntax.append("reload",                                                 "§8  > /role reload");
	}


	private boolean create(CommandSender sender, String[] args){

		if (args.length == 1) {
			sender.sendMessage("§eFailed to Create Role (Invalid Syntax)");
			syntax.printLayerAndSubLayers("create", sender);
			return true;
		}

		String role = args[1];

		if (Data.roles.dataHashMap.containsKey(role)) {
			sender.sendMessage("§eFailed to Create Role: " + role + " (Role Already Exists)");
			return true;
		}

		Data.roles.getOrCreate(role, RoleData.class);
		Data.roles.loadAll(RoleData.class);
		getRoleFromArg(role).displayName = role;
		Data.roles.saveAll();

		if (args.length == 3){
			getRoleFromArg(role).priority = Integer.parseInt(args[2]);
		}

		sender.sendMessage("§5Created Role: " + getRoleFromArg(role).displayName + " with Priority: " + getRoleFromArg(role).priority);
		return true;
	}

	private boolean delete(CommandSender sender, String[] args){
		if (args.length == 1) {
			sender.sendMessage("§eFailed to Delete Role (Invalid Syntax)");
			syntax.printLayerAndSubLayers("delete", sender);
			return true;
		}

		String role = args[1];

		switch (Data.roles.remove(role)) {
			case 0:
				sender.sendMessage("§1Deleted Role: " + role);
				return true;
			case 1:
				sender.sendMessage("§eFailed to Delete Role: " + role + " (Role Doesn't Exist)");
				syntax.printLayerAndSubLayers("delete", sender);
				return true;
			case 2:
				sender.sendMessage("§eFailed to Delete Role: " + role + " (IO Error)");
				return true;
		}

		sender.sendMessage("§eFailed to Delete Role (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("delete", sender);
		return true;
    }

	private boolean reload(CommandSender sender){
		Data.roles.loadAll(RoleData.class);
		sender.sendMessage("§5Reloaded " + Data.roles.dataHashMap.size() + " Role(s)!");
		RoleCommand.buildRoleSyntax();
		sender.sendMessage("§5Built Role Syntax!");
		Data.configs.loadAll(ConfigData.class);
		sender.sendMessage("§5Reloaded Config!");
		return true;
	}

	private boolean edit(CommandSender sender, String[] args){

		if(args.length == 1){
			sender.sendMessage("§eFailed to Edit Role (Invalid Syntax)");
			syntax.printLayerAndSubLayers("edit", sender);
			return true;
		}

		if(!Data.roles.dataHashMap.containsKey(args[1])){
			sender.sendMessage("§eFailed to Edit Role (Invalid Role)");
			syntax.printLayer("edit", sender);
			return true;
		}

		if(args.length == 2){
			sender.sendMessage("§eFailed to Edit Role (Invalid Syntax)");
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

		sender.sendMessage("§eFailed to Edit Role (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("edit", sender);
		return true;
    }

	private boolean priority(CommandSender sender, String[] args){

		if(args.length == 3){
			sender.sendMessage("§eFailed to Edit Role Priority (Invalid Syntax)");
			syntax.printLayer("priority", sender);
			return true;
		}

		if(args.length == 4){
			Data.roles.loadAll(RoleData.class);
			getRoleFromArg(args[1]).priority = Integer.parseInt(args[3]);
			Data.roles.saveAll();
			sender.sendMessage("§5Set Priority for Role " + args[1] + " to: §0" + args[3]);
			return true;
		}

		sender.sendMessage("§eFailed to Edit Role Priority (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("priority", sender);
		return true;
	}

	private boolean display(CommandSender sender, String[] args){

		if(args.length == 3){
			sender.sendMessage("§eFailed to Edit Role Display (Invalid Syntax)");
			syntax.printLayer("display", sender);
			return true;
		}

		switch(args[3]){
			case "name":
				return EditRoleDisplaySubCommand.displayName(sender, args);
			case "color":
				return EditRoleDisplaySubCommand.displayColor(sender, args);
			case "underline":
				return EditRoleDisplaySubCommand.displayUnderline(sender, args);
			case "bold":
				return EditRoleDisplaySubCommand.displayBold(sender, args);
			case "italics":
				return EditRoleDisplaySubCommand.displayItalics(sender, args);
			case "border":
				return EditRoleDisplaySubCommand.displayBorder(sender, args);
		}

		sender.sendMessage("§eFailed to Edit Role Display (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("display", sender);
		return true;
	}

	private boolean username(CommandSender sender, String[] args){

		if(args.length == 3){
			sender.sendMessage("§eFailed to Edit Role Username (Invalid Syntax)");
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
				return EditRoleUsernameSubCommand.usernameBorder(sender, args);
		}

		sender.sendMessage("§eFailed to Edit Role Username (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("username", sender);
		return true;
	}

	private boolean text(CommandSender sender, String[] args){

		if(args.length == 3){
			sender.sendMessage("§eFailed to Edit Role Text (Invalid Syntax)");
			syntax.printLayerAndSubLayers("text", sender);
			return true;
		}

		switch(args[3]){
			case "color":
				return EditRoleTextSubCommand.textColor(sender, args);
			case "underline":
				return EditRoleTextSubCommand.textUnderline(sender, args);
			case "bold":
				return EditRoleTextSubCommand.textBold(sender, args);
			case "italics":
				return EditRoleTextSubCommand.textItalics(sender, args);
		}

		sender.sendMessage("§eFailed to Edit Role Text (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("text", sender);
		return true;
	}

	private boolean grant(CommandSender sender, String[] args){

		if(args.length == 1){
			sender.sendMessage("§eFailed to Grant Role (Invalid Syntax)");
			syntax.printLayerAndSubLayers("grant", sender);
			return true;
		}


		if(!Data.roles.dataHashMap.containsKey(args[1])){
			sender.sendMessage("§eFailed to Grant Role (Role doesn't exist!)");
			syntax.printLayerAndSubLayers("grant", sender);
			return true;
		}

		RoleData roleData = getRoleFromArg(args[1]);

		if(args.length == 2 && !roleData.playersGrantedRole.contains(sender.getPlayer().username)){
			Data.roles.loadAll(RoleData.class);
			getRoleFromArg(args[1]).playersGrantedRole.add(sender.getPlayer().username);
			Data.roles.saveAll();
			sender.sendMessage("§5Granted Role: " + args[1] + " to player: §0" + sender.getPlayer().username);
			return true;
		} else if (args.length == 3 && !roleData.playersGrantedRole.contains(args[2])){
			Data.roles.loadAll(RoleData.class);
			getRoleFromArg(args[1]).playersGrantedRole.add(args[2]);
			Data.roles.saveAll();
			sender.sendMessage("§5Granted Role: " + args[1] + " to player: §0" + args[2]);
			return true;
		} else if (roleData.playersGrantedRole.contains(sender.getPlayer().username) || roleData.playersGrantedRole.contains(args[2])) {
			sender.sendMessage("§eFailed to Grant Role (Player already has Role!)");
			return true;
		}


		sender.sendMessage("§eFailed to Grant Role (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("grant", sender);
		return true;
	}

	private boolean revoke(CommandSender sender, String[] args){

		if(args.length == 1){
			sender.sendMessage("§eFailed to Revoke Role (Invalid Syntax)");
			syntax.printLayerAndSubLayers("revoke", sender);
			return true;
		}

		if(!Data.roles.dataHashMap.containsKey(args[1])){
			sender.sendMessage("§eFailed to Revoke Role (Role doesn't exist!)");
			syntax.printLayerAndSubLayers("revoke", sender);
			return true;
		}

		RoleData roleData = getRoleFromArg(args[1]);

		if (args.length == 3 && roleData.playersGrantedRole.contains(args[2])) {
			Data.roles.loadAll(RoleData.class);
			getRoleFromArg(args[1]).playersGrantedRole.remove(args[2]);
			Data.roles.saveAll();
			sender.sendMessage("§1Revoked Role: " + args[1] + " from player: §0" + args[2]);
			return true;
		} else if (args.length == 2 && roleData.playersGrantedRole.contains(sender.getPlayer().username)){
			Data.roles.loadAll(RoleData.class);
			getRoleFromArg(args[1]).playersGrantedRole.remove(sender.getPlayer().username);
			Data.roles.saveAll();
			sender.sendMessage("§1Revoked Role: " + args[1] + " from player: §0" + sender.getPlayer().username);
			return true;
		} else if (!roleData.playersGrantedRole.contains(sender.getPlayer().username) || !roleData.playersGrantedRole.contains(args[2])) {
			sender.sendMessage("§eFailed to Revoke Role (Player does not have Role!)");
			return true;
		}

		sender.sendMessage("§eFailed to Revoke Role (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("revoke", sender);
		return true;
	}

	private boolean list(CommandSender sender) {
		if (Data.roles.dataHashMap.isEmpty()) {
			sender.sendMessage("§8< Roles: >");
			sender.sendMessage("§8  -No Roles Created-");
			return true;
		}

		sender.sendMessage("§8< Roles: >");

		for (String role : Data.roles.dataHashMap.keySet()) {
			sender.sendMessage("§8  > Role ID: " + TextFormatting.WHITE + TextFormatting.ITALIC + role + "§8 - Priority: " + TextFormatting.WHITE + getRoleFromArg(role).priority);
			sender.sendMessage("§8    > " + RoleBuilder.buildRoleDisplay(Data.roles.dataHashMap.get(role))
												+ RoleBuilder.buildRoleUsername(Data.roles.dataHashMap.get(role), sender.getPlayer().getDisplayName())
												+ RoleBuilder.buildRoleTextFormat(Data.roles.dataHashMap.get(role)) + "text");
		}

		return true;
	}

	private boolean set(CommandSender sender, String[] args) {
		if (args.length == 1) {
			sender.sendMessage("§eFailed to Set Role Value (Invalid Syntax)");
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

		sender.sendMessage("§eFailed to Set Role Value (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("set", sender);
		return true;
	}

	private boolean setDefaultRole(CommandSender sender, String[] args){

		if(args.length == 2){
			sender.sendMessage("§eFailed to Set Default Role (Invalid Syntax)");
			syntax.printLayerAndSubLayers("setDefaultRole", sender);
			return true;
		}

		if(args.length == 3) {
			for (String role : Data.roles.dataHashMap.keySet()) {
				if (args[2].equals(role)) {
					Data.configs.loadAll(ConfigData.class);
					Data.configs.getOrCreate("config", ConfigData.class).defaultRole = args[2];
					Data.configs.saveAll();
					sender.sendMessage("§5Set defaultRole to: " + args[2]);
					return true;
				} else if (args[2].equals("none")) {
					Data.configs.loadAll(ConfigData.class);
					Data.configs.getOrCreate("config", ConfigData.class).defaultRole = null;
					Data.configs.saveAll();
					sender.sendMessage("§5Set defaultRole to: none");
					return true;
				}
			}
			sender.sendMessage("§eFailed to Set Default Role (Invalid Role)");
			syntax.printLayerAndSubLayers("setDefaultRole", sender);
			return true;
		}

		sender.sendMessage("§eFailed to Set Default Role (Default Error) (Invalid Syntax?)");
		syntax.printLayerAndSubLayers("setDefaultRole", sender);
		return true;
	}

	private boolean setDisplayMode(CommandSender sender, String[] args){

		if(args.length == 2){
			sender.sendMessage("§eFailed to Set Display Mode (Invalid Syntax)");
			syntax.printLayerAndSubLayers("setDisplayMode", sender);
			return true;
		}

		if(args.length == 3 && args[2].equals("single")) {
			Data.configs.loadAll(ConfigData.class);
			Data.configs.getOrCreate("config", ConfigData.class).displayMode = "single";
			Data.configs.saveAll();
			sender.sendMessage("§5Set displayMode to: single");
			return true;
		} else if (args.length == 3 && args[2].equals("multi")) {
			Data.configs.loadAll(ConfigData.class);
			Data.configs.getOrCreate("config", ConfigData.class).displayMode = "multi";
			Data.configs.saveAll();
			sender.sendMessage("§5Set displayMode to: multi");
			return true;
		}

		sender.sendMessage("§eFailed to Set Display Mode (Default Error) (Invalid Syntax?)");
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


		sender.sendMessage("§eRole Command Failed (Invalid Syntax)");
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
