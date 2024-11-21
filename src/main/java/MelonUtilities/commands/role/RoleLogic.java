package MelonUtilities.commands.role;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.data.Role;
import MelonUtilities.interfaces.PlayerCustomInputFunctionInterface;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.classes.Icon;
import MelonUtilities.utility.feedback.FeedbackHandler;
import MelonUtilities.utility.builders.RoleBuilder;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Items;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.command.helpers.EntitySelector;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import org.useless.serverlibe.api.gui.GuiHelper;
import org.useless.serverlibe.api.gui.ServerGuiBuilder;
import org.useless.serverlibe.api.gui.slot.ServerSlotButton;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("SameReturnValue")
public class RoleLogic {

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


	static Icon inputUsernameIcon = new Icon("[Input Username]", (byte) TextFormatting.WHITE.id, Items.LABEL.getDefaultStack());
	public static int role(PlayerServer sender) {
		ServerGuiBuilder roleGui = new ServerGuiBuilder();

		roleGui.setSize(0);
		//-------------------------------------
		roleGui.setContainerSlot(2, (roleGuiInventory -> new ServerSlotButton(roleGrantIcon.icon, roleGuiInventory, 2, () -> {
			ServerGuiBuilder roleGrantGui = new ServerGuiBuilder();
			roleGrantGui.setSize((int)Math.floor((Data.Roles.roleDataHashMap.size() + 1) / 9.0F));
			int i = 0;
			for(Role role : Data.Roles.roleDataHashMap.values()){
				int finalI = i;
				Icon roleIcon = new Icon(role.roleID, (byte) TextFormatting.WHITE.id, Items.LABEL.getDefaultStack());
				//-------------------------------------
				roleGrantGui.setContainerSlot(i, (roleGrantGuiInventory -> new ServerSlotButton(roleIcon.icon, roleGrantGuiInventory, finalI, () -> {
					ServerGuiBuilder roleGrantToGui = new ServerGuiBuilder();
					List<PlayerServer> onlinePlayers = MinecraftServer.getInstance().playerList.playerEntities;
					roleGrantToGui.setSize((int)Math.floor((onlinePlayers.size() + 2) / 9.0F));
					int j = 0;
					for(PlayerServer target : onlinePlayers){
						int finalJ = j;
						Icon playerIcon = new Icon(target.username, (byte) TextFormatting.WHITE.id, Items.ARMOR_CHESTPLATE_IRON.getDefaultStack());
						//-------------------------------------
						roleGrantToGui.setContainerSlot(j, (roleGrantToGuiInventory -> new ServerSlotButton(playerIcon.icon, roleGrantToGuiInventory, finalJ, () -> {
							role_grant(role, target, sender);
							sender.usePersonalCraftingInventory();
						})));
						//-------------------------------------
						j++;
					}
					int finalJ1 = j;
					roleGrantToGui.setContainerSlot(j+1, (roleGrantToGuiInventory -> new ServerSlotButton(inputUsernameIcon.icon, roleGrantToGuiInventory, finalJ1+1, () -> {
						((PlayerCustomInputFunctionInterface) sender).melonutilities$setCustomInputFunction(customInput -> role_grant(role, customInput, sender));
						sender.usePersonalCraftingInventory();
					})));
					GuiHelper.openCustomServerGui(sender, roleGrantToGui.build(sender, "To Player: "));
				})));
				//-------------------------------------
				i++;
			}
			GuiHelper.openCustomServerGui(sender, roleGrantGui.build(sender, "Grant Role: "));
		})));
		//-------------------------------------
		roleGui.setContainerSlot(3, (inventory -> new ServerSlotButton(roleReloadIcon.icon, inventory, 3, () -> {
			//role_reload(context);
			sender.usePersonalCraftingInventory();
		})));
		//-------------------------------------
		roleGui.setContainerSlot(5, (inventory -> new ServerSlotButton(roleListIcon.icon, inventory, 5, () -> {
			//role_list(context);
			sender.usePersonalCraftingInventory();
		})));
		//-------------------------------------
		roleGui.setContainerSlot(6, (inventory -> new ServerSlotButton(roleRevokeIcon.icon, inventory, 6, () -> {
			ServerGuiBuilder roleRevokeGui = new ServerGuiBuilder();
			roleRevokeGui.setSize(0);

		})));
		//-------------------------------------
		GuiHelper.openCustomServerGui(sender, roleGui.build(sender, "Role Command:"));

		//FeedbackHandler.success(context, "Opened Role GUI!");
		return Command.SINGLE_SUCCESS;
	}

	static Icon roleReloadIcon = new Icon("[Reload]", (byte) TextFormatting.ORANGE.id, Items.REPEATER.getDefaultStack());
	public static int role_reload(CommandContext<CommandSource> context) {
		Data.Roles.reload();
		FeedbackHandler.success(context, "Reloaded %" + Data.Roles.roleDataHashMap.size() + "% Role(s)!");
		CommandRole.buildRoleSyntax();
		FeedbackHandler.success(context, "Built Role Syntax!");
		Data.MainConfig.reload();
		FeedbackHandler.success(context, "Reloaded Config!");
		return Command.SINGLE_SUCCESS;
	}

	static Icon roleListIcon = new Icon("[List]", (byte) TextFormatting.LIGHT_GRAY.id, Items.PAPER.getDefaultStack());
	public static int role_list(CommandContext<CommandSource> context) {
		CommandSource source = context.getSource();

		if (Data.Roles.roleDataHashMap.isEmpty()) {
			source.sendMessage(TextFormatting.GRAY + "< " + TextFormatting.LIGHT_GRAY + "Roles: " + TextFormatting.GRAY + " >");
			source.sendMessage(TextFormatting.GRAY + "  -No Roles Created-");
			return Command.SINGLE_SUCCESS;
		}

		source.sendMessage(TextFormatting.GRAY + "< " + TextFormatting.LIGHT_GRAY + "Roles: " + TextFormatting.GRAY + " >");

		for (Role role : Data.Roles.roleDataHashMap.values()) {
			source.sendMessage(TextFormatting.GRAY + "  > " + TextFormatting.LIGHT_GRAY + "Role ID: " + TextFormatting.GRAY + "[" + TextFormatting.LIGHT_GRAY + role.roleID + TextFormatting.GRAY + "]" + TextFormatting.LIGHT_GRAY + " - Priority: " + TextFormatting.GRAY + "[" + TextFormatting.LIGHT_GRAY + role.priority + TextFormatting.GRAY + "]");
			source.sendMessage(TextFormatting.GRAY + "    > " + RoleBuilder.buildRoleDisplay(role)
				+ RoleBuilder.buildRoleUsername(role, source.getSender().getDisplayName())
				+ RoleBuilder.buildRoleTextFormat(role) + "text"
			);
		}

		FeedbackHandler.success(context, TextFormatting.GRAY + "<>");
		return Command.SINGLE_SUCCESS;
	}

	static Icon roleRevokeIcon = new Icon("[Revoke]", (byte) TextFormatting.RED.id, Items.DUST_REDSTONE.getDefaultStack());
	public static int role_revoke(CommandContext<CommandSource> context) throws CommandSyntaxException {
		CommandSource source = context.getSource();
		Role role = context.getArgument("role", Role.class);

		EntitySelector entitySelector = context.getArgument("target", EntitySelector.class);
		Player target = (Player)entitySelector.get(source).get(0);

		if (role.playersGrantedRole.contains(target.uuid)) {
			role.playersGrantedRole.remove(target.uuid);
			role.save();
			FeedbackHandler.destructive(context, "Revoked Role %" + role.roleID + "% from Player %" + target.getDisplayName());
		} else {
			FeedbackHandler.error(context, "Failed to Revoke Role %" + role.roleID + "% from Player %" + target.getDisplayName());
			FeedbackHandler.error(context, "(Player does not have Role!)");
		}

		return Command.SINGLE_SUCCESS;
	}

	static Icon roleGrantIcon = new Icon("[Grant]", (byte) TextFormatting.LIME.id, Items.OLIVINE.getDefaultStack());
	public static void role_grant(Role role, String targetUsername, Player sender){
		Pair<UUID, String> profile;
		try {
			profile = MUtil.getProfileFromUsername(targetUsername);
		} catch (NullPointerException e) {
			FeedbackHandler.error(sender, "Failed to Trust %" + targetUsername + "% to Container! (%" + targetUsername + "% Does not Exist)");
			return;
		}

		String targetUsernameOrDisplayName = profile.getRight();
		UUID targetUUID = profile.getLeft();

		if (!role.playersGrantedRole.contains(targetUUID)){
			role.playersGrantedRole.add(targetUUID);
			role.save();
			FeedbackHandler.success(sender, "Granted Role %" + role.roleID + "% to Player %" + targetUsernameOrDisplayName);
		} else {
			FeedbackHandler.error(sender, "Failed to Grant Role %" + role.roleID + "% to Player %" + targetUsernameOrDisplayName);
			FeedbackHandler.error(sender, "(Player already has Role!)");
		}
	}

	public static int role_grant(Role role, Player target, Player sender){
		if (!role.playersGrantedRole.contains(target.uuid)){
			role.playersGrantedRole.add(target.uuid);
			role.save();
			FeedbackHandler.success(sender, "Granted Role %" + role.roleID + "% to Player %" + target.getDisplayName());
		} else {
			FeedbackHandler.error(sender, "Failed to Grant Role %" + role.roleID + "% to Player %" + target.getDisplayName());
			FeedbackHandler.error(sender, "(Player already has Role!)");
			return 0;
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int role_create(CommandContext<CommandSource> context) {
		String roleID = context.getArgument("roleID", String.class);
		int rolePriority = context.getArgument("priorityValue", Integer.class);

		if (Data.Roles.roleDataHashMap.containsKey(roleID)) {
			FeedbackHandler.error(context, "Failed to Create Role with RoleID %" + roleID + "% (Role Already Exists)");
			return Command.SINGLE_SUCCESS;
		}

		Role role = Data.Roles.create(roleID);
		role.displayName = roleID;
		role.priority = rolePriority;
		role.save();

		FeedbackHandler.success(context, "Created Role %" + role.roleID + "% with Priority %" + role.priority);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_delete(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);

		FeedbackHandler.destructive(context, "Deleted Role %" + role.roleID);
		role.delete();
		return Command.SINGLE_SUCCESS;
	}

	public static int role_set_defaultrole_ROLEID(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);

		Data.MainConfig.config.defaultRole = role.roleID;
		Data.MainConfig.save();
		FeedbackHandler.success(context, "Set Default Role to %" + role.roleID);
		return Command.SINGLE_SUCCESS;

	}

	public static int role_set_defaultrole_none(CommandContext<CommandSource> context) {
		Data.MainConfig.config.defaultRole = null;
		Data.MainConfig.save();
		FeedbackHandler.destructive(context, "Removed Default Role");
		return Command.SINGLE_SUCCESS;
	}

	public static int role_set_displaymode_single(CommandContext<CommandSource> context) {
		Data.MainConfig.config.displayMode = "single";
		Data.MainConfig.save();
		FeedbackHandler.success(context, "Set Display Mode to %single%");
		return Command.SINGLE_SUCCESS;
	}

	public static int role_set_displaymode_multi(CommandContext<CommandSource> context) {
		Data.MainConfig.config.displayMode = "multi";
		Data.MainConfig.save();
		FeedbackHandler.success(context, "Set Display Mode to %multi%");
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_priority(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Role role = context.getArgument("role", Role.class);
		int priorityValue = context.getArgument("priorityValue", Integer.class);

		role.priority = priorityValue;
		role.save();
		FeedbackHandler.success(context, "Set Priority for Role %" + role.roleID + "% to %" + priorityValue);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_name(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String displayName = context.getArgument("displayName", String.class);

		FeedbackHandler.success(context, "Set Display Name for Role %" + role.roleID + "% to %" + displayName);
		role.displayName = displayName;
		role.save();
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_color_COLOR(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String color = context.getArgument("color", String.class);

		role.displayColor = color;
		role.save();
		FeedbackHandler.success(context, "Set Display Color for Role %" + role.roleID + "% to %" + MUtil.colorSectionMap.get(color) + color);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_color_HEX(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));

		role.displayColor = hex;
		role.save();
		FeedbackHandler.success(context, "Set Display Color for Role %" + role.roleID + "% to %§<" + hex + ">" + hex);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_underline(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		boolean value = context.getArgument("value", Boolean.class);

		role.isDisplayUnderlined = value;
		role.save();
		FeedbackHandler.success(context, "Set Display Underline for Role %" + role.roleID + "% to %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_bold(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		boolean value = context.getArgument("value", Boolean.class);

		role.isDisplayBold = value;
		role.save();
		FeedbackHandler.success(context, "Set Display Bold for Role %" + role.roleID + "% to %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_italics(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		boolean value = context.getArgument("value", Boolean.class);

		role.isDisplayItalics = value;
		role.save();
		FeedbackHandler.success(context, "Set Display Italics for Role %" + role.roleID + "% to %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_color_COLOR(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String color = context.getArgument("color", String.class);

		role.displayBorderColor = color;
		role.save();
		FeedbackHandler.success(context, "Set Display Border Color for Role %" + role.roleID + "% to %" + MUtil.colorSectionMap.get(color) + color);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_color_HEX(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));

		role.displayBorderColor = hex;
		role.save();
		FeedbackHandler.success(context, "Set Display Border Color for Role %" + role.roleID + "% to %§<" + hex + ">" + hex);
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
		FeedbackHandler.success(context, "Set Display Border to % □None□ % for Role %" + role.roleID);
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
		FeedbackHandler.success(context, "Set Display Border to % [Bracket] % for Role %" + role.roleID);
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
		FeedbackHandler.success(context, "Set Display Border to % {Curly} % for Role %" + role.roleID);
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
		FeedbackHandler.success(context, "Set Display Border to % <Caret> % for Role %" + role.roleID);
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
		FeedbackHandler.success(context, "Set Display Border Prefix to % " + customAffix + " % for Role %" + role.roleID);
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
		FeedbackHandler.success(context, "Set Display Border Suffix to % " + customAffix + " % for Role %" + role.roleID);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_color_COLOR(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String color = context.getArgument("color", String.class);

		role.usernameBorderColor = color;
		role.save();
		FeedbackHandler.success(context, "Set Username Border Color for Role %" + role.roleID + "% to %" + MUtil.colorSectionMap.get(color) + color);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_color_HEX(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));

		role.usernameBorderColor = hex;
		role.save();
		FeedbackHandler.success(context, "Set Username Border Color for Role %" + role.roleID + "% to %§<" + hex + ">" + hex);
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
		FeedbackHandler.success(context, "Set Username Border to % □None□ % for Role %" + role.roleID);
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
		FeedbackHandler.success(context, "Set Username Border to % [Bracket] % for Role %" + role.roleID);
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
		FeedbackHandler.success(context, "Set Username Border to % {Curly} % for Role %" + role.roleID);
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
		FeedbackHandler.success(context, "Set Username Border to % <Caret> % for Role %" + role.roleID);
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
		FeedbackHandler.success(context, "Set Username Border Prefix to % " + customAffix + " % for Role %" + role.roleID);
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
		FeedbackHandler.success(context, "Set Username Border Suffix to % " + customAffix + " % for Role %" + role.roleID);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_color_COLOR(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String color = context.getArgument("color", String.class);

		role.textColor = color;
		role.save();
		FeedbackHandler.success(context, "Set Text Color for Role %" + role.roleID + "% to %" + MUtil.colorSectionMap.get(color) + color);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_color_HEX(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));

		role.textColor = hex;
		role.save();
		FeedbackHandler.success(context, "Set Text Color for Role %" + role.roleID + "% to %§<" + hex + ">" + hex);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_underline(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		boolean value = context.getArgument("value", Boolean.class);

		role.isTextUnderlined = value;
		role.save();
		FeedbackHandler.success(context, "Set Text Underline for Role %" + role.roleID + "% to %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_bold(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		boolean value = context.getArgument("value", Boolean.class);

		role.isTextBold = value;
		role.save();
		FeedbackHandler.success(context, "Set Text Bold for Role %" + role.roleID + "% to %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_italics(CommandContext<CommandSource> context) {
		Role role = context.getArgument("role", Role.class);
		boolean value = context.getArgument("value", Boolean.class);

		role.isTextItalics = value;
		role.save();
		FeedbackHandler.success(context, "Set Text Italics for Role %" + role.roleID + "% to %" + value);
		return Command.SINGLE_SUCCESS;
	}
}
