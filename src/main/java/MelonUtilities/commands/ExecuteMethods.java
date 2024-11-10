package MelonUtilities.commands;

import MelonUtilities.commands.role.CommandRole;
import MelonUtilities.config.Data;
import MelonUtilities.config.custom.classes.Role;
import MelonUtilities.config.datatypes.ConfigData;
import MelonUtilities.config.datatypes.KitData;
import MelonUtilities.config.datatypes.PlayerData;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.feedback.FeedbackHandler;
import MelonUtilities.utility.builders.RoleBuilder;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
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
		Data.Roles.reload();
		FeedbackHandler.success(context, "Reloaded " + Data.Roles.roleHashMap.size() + " Role(s)!");

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
		Data.Roles.reload();
		FeedbackHandler.success(context, "Reloaded %" + Data.Roles.roleHashMap.size() + "% Role(s)!");
		CommandRole.buildRoleSyntax();
		FeedbackHandler.success(context, "Built Role Syntax!");
		Data.configs.loadAll(ConfigData.class);
		FeedbackHandler.success(context, "Reloaded Config!");
		return Command.SINGLE_SUCCESS;
	}

	public static int role_list(CommandContext<CommandSource> context) {
		CommandSource source = context.getSource();

		if (Data.Roles.roleHashMap.isEmpty()) {
			source.sendMessage(TextFormatting.GRAY + "< " + TextFormatting.LIGHT_GRAY + "Roles: " + TextFormatting.GRAY + " >");
			source.sendMessage(TextFormatting.GRAY + "  -No Roles Created-");
			return Command.SINGLE_SUCCESS;
		}

		source.sendMessage(TextFormatting.GRAY + "< " + TextFormatting.LIGHT_GRAY + "Roles: " + TextFormatting.GRAY + " >");

		for (Role role : Data.Roles.roleHashMap.values()) {
			source.sendMessage(TextFormatting.GRAY + "  > " + TextFormatting.LIGHT_GRAY + "Role ID: " + TextFormatting.GRAY + "[" + TextFormatting.LIGHT_GRAY + role.roleID + TextFormatting.GRAY + "]" + TextFormatting.LIGHT_GRAY + " - Priority: " + TextFormatting.GRAY + "[" + TextFormatting.LIGHT_GRAY + role.priority + TextFormatting.GRAY + "]");
			source.sendMessage(TextFormatting.GRAY + "    > " + RoleBuilder.buildRoleDisplay(role)
				+ RoleBuilder.buildRoleUsername(role, source.getSender().getDisplayName())
				+ RoleBuilder.buildRoleTextFormat(role) + "text"
			);
		}

		return Command.SINGLE_SUCCESS;
	}

	public static int role_revoke(CommandContext<CommandSource> context) throws CommandSyntaxException {
		CommandSource source = context.getSource();
		Role role = context.getArgument("role", Role.class);

		EntitySelector entitySelector = context.getArgument("target", EntitySelector.class);
		String target = ((Player)entitySelector.get(source).get(0)).username;

		if (role.playersGrantedRole.contains(target)) {
			role.playersGrantedRole.remove(target);
			role.save();
			FeedbackHandler.destructive(context, "Revoked Role %" + role.displayName + "% from Player %" + target);
		} else {
			FeedbackHandler.error(context, "Failed to Revoke Role %" + role.displayName + "% from Player %" + target);
			FeedbackHandler.error(context, "(Player does not have Role!)");
		}

		return Command.SINGLE_SUCCESS;
	}

	public static int role_grant(CommandContext<CommandSource> context) throws CommandSyntaxException {
		CommandSource source = context.getSource();
		Role role = context.getArgument("role", Role.class);

		EntitySelector entitySelector = context.getArgument("target", EntitySelector.class);
		String target = ((Player)entitySelector.get(source).get(0)).username;

		if (!role.playersGrantedRole.contains(target)){
			role.playersGrantedRole.add(target);
			role.save();
			FeedbackHandler.success(context, "Granted Role %" + role.displayName + "% to Player %" + target);
		} else {
			FeedbackHandler.error(context, "Failed to Grant Role %" + role.displayName + "% to Player %" + target);
			FeedbackHandler.error(context, "(Player already has Role!)");
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int role_create(CommandContext<CommandSource> context) {
		String roleID = context.getArgument("roleID", String.class);
		int rolePriority = context.getArgument("priorityValue", Integer.class);

		if (Data.Roles.roleHashMap.containsKey(roleID)) {
			FeedbackHandler.error(context, "Failed to Create Role with RoleID %" + roleID + "% (Role Already Exists)");
			return Command.SINGLE_SUCCESS;
		}

		Role role = Data.Roles.create(roleID);
		role.displayName = roleID;
		role.priority = rolePriority;
		role.save();

		FeedbackHandler.success(context, "Created Role %" + role.displayName + "% with Priority %" + role.priority);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_delete(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Role role = context.getArgument("role", Role.class);

		FeedbackHandler.destructive(context, "Deleted Role %" + role.displayName);
		role.delete();
		return Command.SINGLE_SUCCESS;
	}

	public static int role_set_defaultrole_ROLEID(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Role role = context.getArgument("role", Role.class);

		Data.configs.loadAll(ConfigData.class);
		Data.configs.getOrCreate("config", ConfigData.class).defaultRole = role.roleID;
		Data.configs.saveAll();
		FeedbackHandler.success(context, "Set Default Role to %" + role.displayName);
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

	public static int role_edit_priority(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Role role = context.getArgument("role", Role.class);
		int priorityValue = context.getArgument("priorityValue", Integer.class);

		role.priority = priorityValue;
		role.save();
		FeedbackHandler.success(context, "Set Priority for Role %" + role.displayName + "% to %" + priorityValue);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_name(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String displayName = context.getArgument("displayName", String.class);

		FeedbackHandler.success(context, "Set Display Name for Role %" + role.displayName + "% to %" + displayName);
		role.displayName = displayName;
		role.save();
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_color_COLOR(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String color = context.getArgument("color", String.class);

		role.displayColor = color;
		role.save();
		FeedbackHandler.success(context, "Set Display Color for Role %" + role.displayName + "% to %" + MUtil.colorSectionMap.get(color) + color);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_color_HEX(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));

		role.displayColor = hex;
		role.save();
		FeedbackHandler.success(context, "Set Display Color for Role %" + role.displayName + "% to %§<" + hex + ">" + hex);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_underline(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		boolean value = context.getArgument("value", Boolean.class);

		role.isDisplayUnderlined = value;
		role.save();
		FeedbackHandler.success(context, "Set Display Underline for Role %" + role.displayName + "% to %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_bold(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		boolean value = context.getArgument("value", Boolean.class);

		role.isDisplayBold = value;
		role.save();
		FeedbackHandler.success(context, "Set Display Bold for Role %" + role.displayName + "% to %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_italics(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		boolean value = context.getArgument("value", Boolean.class);

		role.isDisplayItalics = value;
		role.save();
		FeedbackHandler.success(context, "Set Display Italics for Role %" + role.displayName + "% to %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_color_COLOR(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String color = context.getArgument("color", String.class);

		role.displayBorderColor = color;
		role.save();
		FeedbackHandler.success(context, "Set Display Border Color for Role %" + role.displayName + "% to %" + MUtil.colorSectionMap.get(color) + color);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_color_HEX(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));

		role.displayBorderColor = hex;
		role.save();
		FeedbackHandler.success(context, "Set Display Border Color for Role %" + role.displayName + "% to %§<" + hex + ">" + hex);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_style_none(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);

		role.isUsernameBorderBracket = false;
		role.isUsernameBorderNone = true;
		role.isUsernameBorderCaret = false;
		role.isUsernameBorderCurly = false;
		role.isUsernameBorderCustom = false;
		role.save();
		FeedbackHandler.success(context, "Set Display Border to % □None□ % for Role %" + role.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_style_bracket(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);

		role.isDisplayBorderBracket = true;
		role.isDisplayBorderNone = false;
		role.isDisplayBorderCaret = false;
		role.isDisplayBorderCurly = false;
		role.isDisplayBorderCustom = false;
		role.save();
		FeedbackHandler.success(context, "Set Display Border to % [Bracket] % for Role %" + role.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_style_curly(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);

		role.isDisplayBorderBracket = false;
		role.isDisplayBorderNone = false;
		role.isDisplayBorderCaret = false;
		role.isDisplayBorderCurly = true;
		role.isDisplayBorderCustom = false;
		role.save();
		FeedbackHandler.success(context, "Set Display Border to % {Curly} % for Role %" + role.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_style_caret(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);

		role.isDisplayBorderBracket = false;
		role.isDisplayBorderNone = false;
		role.isDisplayBorderCaret = true;
		role.isDisplayBorderCurly = false;
		role.isDisplayBorderCustom = false;
		role.save();
		FeedbackHandler.success(context, "Set Display Border to % <Caret> % for Role %" + role.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_style_custom_prefix(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String customAffix = context.getArgument("customAffix", String.class);

		role.isDisplayBorderBracket = false;
		role.isDisplayBorderNone = false;
		role.isDisplayBorderCaret = false;
		role.isDisplayBorderCurly = false;
		role.isDisplayBorderCustom = true;
		role.customDisplayBorderPrefix = customAffix;
		role.save();
		FeedbackHandler.success(context, "Set Display Border Prefix to % " + customAffix + " % for Role %" + role.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_style_custom_suffix(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String customAffix = context.getArgument("customAffix", String.class);

		role.isDisplayBorderBracket = false;
		role.isDisplayBorderNone = false;
		role.isDisplayBorderCaret = false;
		role.isDisplayBorderCurly = false;
		role.isDisplayBorderCustom = true;
		role.customDisplayBorderSuffix = customAffix;
		role.save();
		FeedbackHandler.success(context, "Set Display Border Suffix to % " + customAffix + " % for Role %" + role.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_color_COLOR(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String color = context.getArgument("color", String.class);

		role.usernameBorderColor = color;
		role.save();
		FeedbackHandler.success(context, "Set Username Border Color for Role %" + role.displayName + "% to %" + MUtil.colorSectionMap.get(color) + color);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_color_HEX(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));

		role.usernameBorderColor = hex;
		role.save();
		FeedbackHandler.success(context, "Set Username Border Color for Role %" + role.displayName + "% to %§<" + hex + ">" + hex);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_style_none(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);

		role.isUsernameBorderBracket = false;
		role.isUsernameBorderNone = true;
		role.isUsernameBorderCaret = false;
		role.isUsernameBorderCurly = false;
		role.isUsernameBorderCustom = false;
		role.save();
		FeedbackHandler.success(context, "Set Username Border to % □None□ % for Role %" + role.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_style_bracket(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);

		role.isUsernameBorderBracket = true;
		role.isUsernameBorderNone = false;
		role.isUsernameBorderCaret = false;
		role.isUsernameBorderCurly = false;
		role.isUsernameBorderCustom = false;
		role.save();
		FeedbackHandler.success(context, "Set Username Border to % [Bracket] % for Role %" + role.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_style_curly(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);

		role.isUsernameBorderBracket = false;
		role.isUsernameBorderNone = false;
		role.isUsernameBorderCaret = false;
		role.isUsernameBorderCurly = true;
		role.isUsernameBorderCustom = false;
		role.save();
		FeedbackHandler.success(context, "Set Username Border to % {Curly} % for Role %" + role.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_style_caret(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);

		role.isUsernameBorderBracket = false;
		role.isUsernameBorderNone = false;
		role.isUsernameBorderCaret = true;
		role.isUsernameBorderCurly = false;
		role.isUsernameBorderCustom = false;
		role.save();
		FeedbackHandler.success(context, "Set Username Border to % <Caret> % for Role %" + role.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_style_custom_prefix(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String customAffix = context.getArgument("customAffix", String.class);

		role.isUsernameBorderBracket = false;
		role.isUsernameBorderNone = false;
		role.isUsernameBorderCaret = false;
		role.isUsernameBorderCurly = false;
		role.isUsernameBorderCustom = true;
		role.customUsernameBorderPrefix = customAffix;
		role.save();
		FeedbackHandler.success(context, "Set Username Border Prefix to % " + customAffix + " % for Role %" + role.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_style_custom_suffix(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String customAffix = context.getArgument("customAffix", String.class);

		role.isUsernameBorderBracket = false;
		role.isUsernameBorderNone = false;
		role.isUsernameBorderCaret = false;
		role.isUsernameBorderCurly = false;
		role.isUsernameBorderCustom = true;
		role.customUsernameBorderSuffix = customAffix;
		role.save();
		FeedbackHandler.success(context, "Set Username Border Suffix to % " + customAffix + " % for Role %" + role.displayName);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_color_COLOR(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String color = context.getArgument("color", String.class);

		role.textColor = color;
		role.save();
		FeedbackHandler.success(context, "Set Text Color for Role %" + role.displayName + "% to %" + MUtil.colorSectionMap.get(color) + color);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_color_HEX(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));

		role.textColor = hex;
		role.save();
		FeedbackHandler.success(context, "Set Text Color for Role %" + role.displayName + "% to %§<" + hex + ">" + hex);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_underline(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		boolean value = context.getArgument("value", Boolean.class);

		role.isTextUnderlined = value;
		role.save();
		FeedbackHandler.success(context, "Set Text Underline for Role %" + role.displayName + "% to %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_bold(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		boolean value = context.getArgument("value", Boolean.class);

		role.isTextBold = value;
		role.save();
		FeedbackHandler.success(context, "Set Text Bold for Role %" + role.displayName + "% to %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_italics(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		boolean value = context.getArgument("value", Boolean.class);

		role.isTextItalics = value;
		role.save();
		FeedbackHandler.success(context, "Set Text Italics for Role %" + role.displayName + "% to %" + value);
		return Command.SINGLE_SUCCESS;
	}

}
