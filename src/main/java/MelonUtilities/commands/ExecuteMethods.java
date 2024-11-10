package MelonUtilities.commands;

import MelonUtilities.commands.role.CommandRole;
import MelonUtilities.config.Data;
import MelonUtilities.config.DataBank;
import MelonUtilities.config.datatypes.ConfigData;
import MelonUtilities.config.datatypes.KitData;
import MelonUtilities.config.datatypes.PlayerData;
import MelonUtilities.config.datatypes.RoleData;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.feedback.FeedbackHandler;
import MelonUtilities.utility.builders.RoleBuilder;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.command.helpers.EntitySelector;

import static net.minecraft.server.util.helper.PlayerList.updateList;

@SuppressWarnings("SameReturnValue")
public class ExecuteMethods {

	/*
	 Naming Scheme for methods in this class is:

	 (arg = command argument/literal)
	 Ex: [ arg_arg_arg ]

	 Naming can also include arguments in all caps:
	 Ex: [ arg.arg.ARG ]

	 !!!Only use capitalized arguments when necessary!!!
	 Capitalized arguments should only be used for arguments that are NOT literals, and are variable.
	 Specifically when two methods share the same base command, and need to be differentiated from each-other

	 Ex: [ role_set_defaultrole_ROLEID ]
	 	 [ role_set_defaultrole_none ]

	 * Note that both methods share the same parent argument (defaultrole), and that none is a literal (so it is not capitalized)

	 PS. Arguments inside the method name should match their registered name/literal in the ArgumentBuilder for their respective command
	*/

	public static int melonutilities_reload(CommandContext<CommandSource> context){
		FeedbackHandler.success(context, "Reloading MelonUtilities...");

		FeedbackHandler.destructive(context, "Reloading Player Data...");
		Data.playerData.loadAll(PlayerData.class);
		FeedbackHandler.success(context, "Reloaded " + Data.playerData.dataHashMap.size() + " Player(s)!");

		//TODO FeedbackHandler.destructive(source, "Building Helper Syntax...");
		//TODO HelperCommand.buildHelperSyntax();
		//TODO FeedbackHandler.success(source, "Helper Syntax Built!");

		FeedbackHandler.destructive(context, "Reloading Kit Data...");
		Data.kits.loadAll(KitData.class);
		FeedbackHandler.success(context, "Reloaded " + Data.kits.dataHashMap.size() + " Kit(s)!");

		//TODO FeedbackHandler.destructive(source, "Building Kit Syntax...");
		//TODO KitCommand.buildKitSyntax();
		//TODO FeedbackHandler.success(source, "Kit Syntax Built!");

		FeedbackHandler.destructive(context, "Reloading Role Data...");
		Data.roles.loadAll(RoleData.class);
		FeedbackHandler.success(context, "Reloaded " + Data.roles.dataHashMap.size() + " Role(s)!");

		FeedbackHandler.destructive(context, "Building Role Syntax...");
		CommandRole.buildRoleSyntax();
		FeedbackHandler.success(context, "Role Syntax Built!");

		//TODO FeedbackHandler.destructive(source, "Building Rollback Syntax...");
		//TODO RollbackCommand.buildSyntax();
		//TODO FeedbackHandler.success(source, "Rollback Syntax Built!");

		FeedbackHandler.destructive(context, "Reloading General Configs...");
		Data.configs.loadAll(ConfigData.class);
		FeedbackHandler.success(context, "Reloaded Configs!");

		FeedbackHandler.destructive(context, "Updating Player List...");
		updateList();
		FeedbackHandler.success(context, "Updated List!");
		return Command.SINGLE_SUCCESS;
	}

	public static int role_reload(CommandContext<CommandSource> context) {
		Data.roles.loadAll(RoleData.class);
		FeedbackHandler.success(context, "Reloaded %" + Data.roles.dataHashMap.size() + "% Role(s)!");
		CommandRole.buildRoleSyntax();
		FeedbackHandler.success(context, "Built Role Syntax!");
		Data.configs.loadAll(ConfigData.class);
		FeedbackHandler.success(context, "Reloaded Config!");
		return Command.SINGLE_SUCCESS;
	}

	public static int role_list(CommandContext<CommandSource> context) {
		CommandSource source = context.getSource();

		if (Data.roles.dataHashMap.isEmpty()) {
			source.sendMessage(TextFormatting.LIGHT_GRAY + "< Roles: >");
			source.sendMessage(TextFormatting.LIGHT_GRAY + "  -No Roles Created-");
			return Command.SINGLE_SUCCESS;
		}

		source.sendMessage(TextFormatting.LIGHT_GRAY + "< Roles: >");

		for (String roleID : Data.roles.dataHashMap.keySet()) {
			RoleData roleData = CommandRole.getRoleDataFromRoleID(roleID);
			source.sendMessage(TextFormatting.LIGHT_GRAY + "  > Role ID: " + TextFormatting.WHITE + TextFormatting.ITALIC + roleID + TextFormatting.LIGHT_GRAY + " - Priority: " + TextFormatting.WHITE + roleData.priority);
			source.sendMessage(TextFormatting.LIGHT_GRAY + "    > " + RoleBuilder.buildRoleDisplay(Data.roles.dataHashMap.get(roleID))
				+ RoleBuilder.buildRoleUsername(roleData, source.getSender().getDisplayName())
				+ RoleBuilder.buildRoleTextFormat(roleData) + "keep on melon(ing)!"
			);
		}

		return Command.SINGLE_SUCCESS;
	}

	public static int role_revoke(CommandContext<CommandSource> context) throws CommandSyntaxException {
		CommandSource source = context.getSource();
		String roleID = context.getArgument("roleID", String.class);
		RoleData roleData = CommandRole.getRoleDataFromRoleID(roleID);
		EntitySelector entitySelector = context.getArgument("target", EntitySelector.class);
		String target = ((Player)entitySelector.get(source).get(0)).username;

		if(!Data.roles.dataHashMap.containsKey(roleID)){
			FeedbackHandler.error(context, "Failed to Revoke Role %" + roleData.displayName + "% (Role doesn't exist!)");
			CommandRole.syntax.printLayerAndSubLayers("revoke", source);
			return Command.SINGLE_SUCCESS;
		}

		if (roleData.playersGrantedRole.contains(target)) {
			Data.roles.loadAll(RoleData.class);
			roleData.playersGrantedRole.remove(target);
			Data.roles.saveAll();
			FeedbackHandler.destructive(context, "Revoked Role %" + roleID + "% from Player %" + target);
		} else {
			FeedbackHandler.error(context, "Failed to Revoke Role %" + roleData.displayName + "% from Player %" + target);
			FeedbackHandler.error(context, "(Player does not have Role!)");
		}

		return Command.SINGLE_SUCCESS;
	}

	public static int role_grant(CommandContext<CommandSource> context) throws CommandSyntaxException {
		CommandSource source = context.getSource();
		String roleID = context.getArgument("roleID", String.class);
		RoleData roleData = CommandRole.getRoleDataFromRoleID(roleID);
		EntitySelector entitySelector = context.getArgument("target", EntitySelector.class);
		String target = ((Player)entitySelector.get(source).get(0)).username;

		if(!Data.roles.dataHashMap.containsKey(roleID)){
			FeedbackHandler.error(context, "Failed to Grant Role %" + roleData.displayName + "% (Role doesn't exist!)");
			CommandRole.syntax.printLayerAndSubLayers("grant", source);
			return Command.SINGLE_SUCCESS;
		}

		if (!roleData.playersGrantedRole.contains(target)){
			Data.roles.loadAll(RoleData.class);
			roleData.playersGrantedRole.add(target);
			Data.roles.saveAll();
			FeedbackHandler.success(context, "Granted Role %" + roleData.displayName + "% to Player %" + target);
		} else {
			FeedbackHandler.error(context, "Failed to Grant Role %" + roleData.displayName + "% to Player %" + target);
			FeedbackHandler.error(context, "(Player already has Role!)");
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int role_create(CommandContext<CommandSource> context) {
		String roleID = context.getArgument("roleID", String.class);
		RoleData roleData = CommandRole.getRoleDataFromRoleID(roleID);
		int rolePriority = context.getArgument("priorityValue", Integer.class);


		if (Data.roles.dataHashMap.containsKey(roleID)) {
			FeedbackHandler.error(context, "Failed to Create Role %" + roleData.displayName + "% (Role Already Exists)");
			return Command.SINGLE_SUCCESS;
		}

		Data.roles.getOrCreate(roleID, RoleData.class);
		Data.roles.loadAll(RoleData.class);
		roleData.displayName = roleID;
		roleData.priority = rolePriority;
		Data.roles.saveAll();

		FeedbackHandler.success(context, "Created Role %" + roleData.displayName + "% with Priority %" + roleData.priority);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_delete(CommandContext<CommandSource> context) {
		CommandSource source = context.getSource();
		String roleID = context.getArgument("roleID", String.class);
		RoleData roleData = CommandRole.getRoleDataFromRoleID(roleID);

		switch (Data.roles.remove(roleID)) {
			case DataBank.NO_ERROR:
				FeedbackHandler.destructive(context, "Deleted Role %" + roleData.displayName);
				return Command.SINGLE_SUCCESS;
			case DataBank.ROLE_DOESNT_EXIST:
				FeedbackHandler.error(context, "Failed to Delete Role %" + roleData.displayName + "% (Role Doesn't Exist)");
				CommandRole.syntax.printLayerAndSubLayers("delete", source);
				return Command.SINGLE_SUCCESS;
			case DataBank.IO_ERROR:
				FeedbackHandler.error(context, "Failed to Delete Role %" + roleData.displayName + "% (IO Error)");
				return Command.SINGLE_SUCCESS;
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int role_set_defaultrole_ROLEID(CommandContext<CommandSource> context) {
		CommandSource source = context.getSource();
		String roleID = context.getArgument("roleID", String.class);
		RoleData roleData = CommandRole.getRoleDataFromRoleID(roleID);

		for (String role : Data.roles.dataHashMap.keySet()) {
			if (roleID.equals(role)) {
				Data.configs.loadAll(ConfigData.class);
				Data.configs.getOrCreate("config", ConfigData.class).defaultRole = roleID;
				Data.configs.saveAll();
				FeedbackHandler.success(context, "Set Default Role to %" + roleData.displayName);
				return Command.SINGLE_SUCCESS;
			}
		}

		FeedbackHandler.error(context, "Failed to Set Default Role to %" + roleData.displayName);
		FeedbackHandler.error(context, "(Invalid Role)");
		CommandRole.syntax.printLayerAndSubLayers("setDefaultRole", source);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_set_defaultrole_none(CommandContext<CommandSource> context) {
		Data.configs.loadAll(ConfigData.class);
		Data.configs.getOrCreate("config", ConfigData.class).defaultRole = null;
		Data.configs.saveAll();
		FeedbackHandler.destructive(context, "Removed Default Role");
		return Command.SINGLE_SUCCESS;
	}

	public static int role_set_displaymode_single(CommandContext<CommandSource> context) {
		Data.configs.loadAll(ConfigData.class);
		Data.configs.getOrCreate("config", ConfigData.class).displayMode = "single";
		Data.configs.saveAll();
		FeedbackHandler.success(context, "Set Display Mode to %single%");
		return Command.SINGLE_SUCCESS;
	}

	public static int role_set_displaymode_multi(CommandContext<CommandSource> context) {
		Data.configs.loadAll(ConfigData.class);
		Data.configs.getOrCreate("config", ConfigData.class).displayMode = "multi";
		Data.configs.saveAll();
		FeedbackHandler.success(context, "Set Display Mode to %multi%");
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_priority(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		int priorityValue = context.getArgument("priorityValue", Integer.class);

		Data.roles.loadAll(RoleData.class);
		roleData.priority = priorityValue;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Priority for Role %" + roleData.displayName + "% to %" + priorityValue);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_name(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		String displayName = context.getArgument("displayName", String.class);

		FeedbackHandler.success(context, "Set Display Name for Role %" + roleData.displayName + "% to %" + displayName);
		Data.roles.loadAll(RoleData.class);
		roleData.displayName = displayName;
		Data.roles.saveAll();
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_color_COLOR(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		String color = context.getArgument("color", String.class);

		Data.roles.loadAll(RoleData.class);
		roleData.displayColor = color;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Display Color for Role %" + roleData.displayName + "% to %" + MUtil.colorSectionMap.get(color) + color);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_color_HEX(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));

		Data.roles.loadAll(RoleData.class);
		roleData.displayColor = hex;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Display Color for Role %" + roleData.displayName + "% to %§<" + hex + ">" + hex);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_underline(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		boolean value = context.getArgument("value", Boolean.class);

		Data.roles.loadAll(RoleData.class);
		roleData.isDisplayUnderlined = value;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Display Underline for Role %" + roleData.displayName + "% to: %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_bold(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		boolean value = context.getArgument("value", Boolean.class);

		Data.roles.loadAll(RoleData.class);
		roleData.isDisplayBold = value;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Display Bold for Role %" + roleData.displayName + "% to: %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_italics(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		boolean value = context.getArgument("value", Boolean.class);

		Data.roles.loadAll(RoleData.class);
		roleData.isDisplayItalics = value;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Display Italics for Role %" + roleData.displayName + "% to: %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_color_COLOR(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		String color = context.getArgument("color", String.class);

		Data.roles.loadAll(RoleData.class);
		roleData.displayBorderColor = color;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Display Border Color for Role %" + roleData.displayName + "% to %" + MUtil.colorSectionMap.get(color) + color);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_color_HEX(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));

		Data.roles.loadAll(RoleData.class);
		roleData.displayBorderColor = hex;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Display Border Color for Role %" + roleData.displayName + "% to %§<" + hex + ">" + hex);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_style_bracket(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));

		Data.roles.loadAll(RoleData.class);
		roleData.isDisplayBorderBracket = true;
		roleData.isDisplayBorderNone = false;
		roleData.isDisplayBorderCaret = false;
		roleData.isDisplayBorderCurly = false;
		roleData.isDisplayBorderCustom = false;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Display Border to % [Bracket] % for Role %" + roleData.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_style_curly(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));

		Data.roles.loadAll(RoleData.class);
		roleData.isDisplayBorderBracket = false;
		roleData.isDisplayBorderNone = false;
		roleData.isDisplayBorderCaret = false;
		roleData.isDisplayBorderCurly = true;
		roleData.isDisplayBorderCustom = false;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Display Border to % {Curly} % for Role %" + roleData.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_style_caret(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));

		Data.roles.loadAll(RoleData.class);
		roleData.isDisplayBorderBracket = false;
		roleData.isDisplayBorderNone = false;
		roleData.isDisplayBorderCaret = true;
		roleData.isDisplayBorderCurly = false;
		roleData.isDisplayBorderCustom = false;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Display Border to % <Caret> % for Role %" + roleData.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_style_custom_prefix(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		String customAffix = context.getArgument("customAffix", String.class);

		Data.roles.loadAll(RoleData.class);
		roleData.isDisplayBorderBracket = false;
		roleData.isDisplayBorderNone = false;
		roleData.isDisplayBorderCaret = false;
		roleData.isDisplayBorderCurly = false;
		roleData.isDisplayBorderCustom = true;
		roleData.customDisplayBorderPrefix = customAffix;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Display Border Prefix to % " + customAffix + "Custom? % for Role %" + roleData.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_style_custom_suffix(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		String customAffix = context.getArgument("customAffix", String.class);

		Data.roles.loadAll(RoleData.class);
		roleData.isDisplayBorderBracket = false;
		roleData.isDisplayBorderNone = false;
		roleData.isDisplayBorderCaret = false;
		roleData.isDisplayBorderCurly = false;
		roleData.isDisplayBorderCustom = true;
		roleData.customDisplayBorderSuffix = customAffix;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Display Border Suffix to % ?Custom" + customAffix + " % for Role %" + roleData.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_color_COLOR(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		String color = context.getArgument("color", String.class);

		Data.roles.loadAll(RoleData.class);
		roleData.usernameBorderColor = color;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Username Border Color for Role %" + roleData.displayName + "% to %" + MUtil.colorSectionMap.get(color) + color);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_color_HEX(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));

		Data.roles.loadAll(RoleData.class);
		roleData.usernameBorderColor = hex;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Username Border Color for Role %" + roleData.displayName + "% to %§<" + hex + ">" + hex);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_style_bracket(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));

		Data.roles.loadAll(RoleData.class);
		roleData.isUsernameBorderBracket = true;
		roleData.isUsernameBorderNone = false;
		roleData.isUsernameBorderCaret = false;
		roleData.isUsernameBorderCurly = false;
		roleData.isUsernameBorderCustom = false;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Username Border to % [Bracket] % for Role %" + roleData.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_style_curly(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));

		Data.roles.loadAll(RoleData.class);
		roleData.isUsernameBorderBracket = false;
		roleData.isUsernameBorderNone = false;
		roleData.isUsernameBorderCaret = false;
		roleData.isUsernameBorderCurly = true;
		roleData.isUsernameBorderCustom = false;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Username Border to % {Curly} % for Role %" + roleData.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_style_caret(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));

		Data.roles.loadAll(RoleData.class);
		roleData.isUsernameBorderBracket = false;
		roleData.isUsernameBorderNone = false;
		roleData.isUsernameBorderCaret = true;
		roleData.isUsernameBorderCurly = false;
		roleData.isUsernameBorderCustom = false;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Username Border to % <Caret> % for Role %" + roleData.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_style_custom_prefix(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		String customAffix = context.getArgument("customAffix", String.class);

		Data.roles.loadAll(RoleData.class);
		roleData.isUsernameBorderBracket = false;
		roleData.isUsernameBorderNone = false;
		roleData.isUsernameBorderCaret = false;
		roleData.isUsernameBorderCurly = false;
		roleData.isUsernameBorderCustom = true;
		roleData.customUsernameBorderPrefix = customAffix;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Username Border Prefix to % " + customAffix + "Custom? % for Role %" + roleData.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_style_custom_suffix(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		String customAffix = context.getArgument("customAffix", String.class);

		Data.roles.loadAll(RoleData.class);
		roleData.isUsernameBorderBracket = false;
		roleData.isUsernameBorderNone = false;
		roleData.isUsernameBorderCaret = false;
		roleData.isUsernameBorderCurly = false;
		roleData.isUsernameBorderCustom = true;
		roleData.customUsernameBorderSuffix = customAffix;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Username Border Suffix to % ?Custom" + customAffix + " % for Role %" + roleData.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_color_COLOR(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		String color = context.getArgument("color", String.class);

		Data.roles.loadAll(RoleData.class);
		roleData.textColor = color;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Text Color for Role %" + roleData.displayName + "% to %" + MUtil.colorSectionMap.get(color) + color);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_color_HEX(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));

		Data.roles.loadAll(RoleData.class);
		roleData.textColor = hex;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Text Color for Role %" + roleData.displayName + "% to %§<" + hex + ">" + hex);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_underline(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		boolean value = context.getArgument("value", Boolean.class);

		Data.roles.loadAll(RoleData.class);
		roleData.isTextUnderlined = value;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Text Underline for Role %" + roleData.displayName + "% to: %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_bold(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		boolean value = context.getArgument("value", Boolean.class);

		Data.roles.loadAll(RoleData.class);
		roleData.isTextBold = value;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Text Bold for Role %" + roleData.displayName + "% to: %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_italics(CommandContext<CommandSource> context) {
		RoleData roleData = CommandRole.getRoleDataFromRoleID(context.getArgument("roleID", String.class));
		boolean value = context.getArgument("value", Boolean.class);

		Data.roles.loadAll(RoleData.class);
		roleData.isTextItalics = value;
		Data.roles.saveAll();
		FeedbackHandler.success(context, "Set Text Italics for Role %" + roleData.displayName + "% to: %" + value);
		return Command.SINGLE_SUCCESS;
	}

}
