package MelonUtilities.commands.role;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.data.Role;
import MelonUtilities.interfaces.PlayerCustomInputFunctionInterface;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.classes.Icon;
import MelonUtilities.utility.feedback.FeedbackHandler;
import MelonUtilities.utility.builders.RoleBuilder;
import com.mojang.brigadier.Command;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Items;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import org.useless.serverlibe.api.gui.GuiHelper;
import org.useless.serverlibe.api.gui.ServerGuiBuilder;
import org.useless.serverlibe.api.gui.slot.ServerSlotButton;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("SameReturnValue")
public class CommandLogicRole {

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
							role_grant(sender, target, role);
							sender.usePersonalCraftingInventory();
						})));
						//-------------------------------------
						j++;
					}
					int finalJ1 = j;
					roleGrantToGui.setContainerSlot(j+1, (roleGrantToGuiInventory -> new ServerSlotButton(inputUsernameIcon.icon, roleGrantToGuiInventory, finalJ1+1, () -> {
						((PlayerCustomInputFunctionInterface) sender).melonutilities$setCustomInputFunction(customInput -> role_grant(sender, customInput, role));
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
			role_reload(sender);
			sender.usePersonalCraftingInventory();
		})));
		//-------------------------------------
		roleGui.setContainerSlot(5, (inventory -> new ServerSlotButton(roleListIcon.icon, inventory, 5, () -> {
			role_list(sender);
			sender.usePersonalCraftingInventory();
		})));
		//-------------------------------------
		roleGui.setContainerSlot(6, (inventory -> new ServerSlotButton(roleRevokeIcon.icon, inventory, 6, () -> {
			ServerGuiBuilder roleRevokeGui = new ServerGuiBuilder();
			roleRevokeGui.setSize(0);

		})));
		//-------------------------------------
		GuiHelper.openCustomServerGui(sender, roleGui.build(sender, "Role Command:"));

		FeedbackHandler.success(sender, "Opened Role GUI!");
		return Command.SINGLE_SUCCESS;
	}

	static Icon roleReloadIcon = new Icon("[Reload]", (byte) TextFormatting.ORANGE.id, Items.REPEATER.getDefaultStack());
	public static int role_reload(Player sender) {
		Data.Roles.reload();
		FeedbackHandler.success(sender, "Reloaded %" + Data.Roles.roleDataHashMap.size() + "% Role(s)!");
		CommandRole.buildSyntax();
		FeedbackHandler.success(sender, "Built Role Syntax!");
		Data.MainConfig.reload();
		FeedbackHandler.success(sender, "Reloaded Config!");
		return Command.SINGLE_SUCCESS;
	}

	static Icon roleListIcon = new Icon("[List]", (byte) TextFormatting.LIGHT_GRAY.id, Items.PAPER.getDefaultStack());
	public static int role_list(Player sender) {
		if (Data.Roles.roleDataHashMap.isEmpty()) {
			sender.sendMessage(TextFormatting.GRAY + "< " + TextFormatting.LIGHT_GRAY + "Roles: " + TextFormatting.GRAY + " >");
			sender.sendMessage(TextFormatting.GRAY + "  -No Roles Created-");
			return Command.SINGLE_SUCCESS;
		}
		sender.sendMessage(TextFormatting.GRAY + "< " + TextFormatting.LIGHT_GRAY + "Roles: " + TextFormatting.GRAY + " >");
		for (Role role : Data.Roles.roleDataHashMap.values()) {
			sender.sendMessage(TextFormatting.GRAY + "  > " + TextFormatting.LIGHT_GRAY + "Role ID: " + TextFormatting.GRAY + "[" + TextFormatting.LIGHT_GRAY + role.roleID + TextFormatting.GRAY + "]" + TextFormatting.LIGHT_GRAY + " - Priority: " + TextFormatting.GRAY + "[" + TextFormatting.LIGHT_GRAY + role.priority + TextFormatting.GRAY + "]");
			sender.sendMessage(TextFormatting.GRAY + "    > " + RoleBuilder.buildRoleDisplay(role)
				+ RoleBuilder.buildRoleUsername(role, sender.getDisplayName())
				+ RoleBuilder.buildRoleTextFormat(role) + "text"
			);
		}
		FeedbackHandler.success(sender, TextFormatting.GRAY + "<>");
		return Command.SINGLE_SUCCESS;
	}

	static Icon roleRevokeIcon = new Icon("[Revoke]", (byte) TextFormatting.RED.id, Items.DUST_REDSTONE.getDefaultStack());
	public static int role_revoke(Player sender, Player target, Role role){

		if (role.playersGrantedRole.contains(target.uuid)) {
			role.playersGrantedRole.remove(target.uuid);
			role.save();
			FeedbackHandler.destructive(sender, "Revoked Role %" + role.roleID + "% from Player %" + target.getDisplayName());
		} else {
			FeedbackHandler.error(sender, "Failed to Revoke Role %" + role.roleID + "% from Player %" + target.getDisplayName());
			FeedbackHandler.error(sender, "(Player does not have Role!)");
		}

		return Command.SINGLE_SUCCESS;
	}

	static Icon roleGrantIcon = new Icon("[Grant]", (byte) TextFormatting.LIME.id, Items.OLIVINE.getDefaultStack());
	public static void role_grant(Player sender, String targetUsername, Role role) {
		Pair<UUID, String> profile;
		try {
			profile = MUtil.getProfileFromUsername(targetUsername);
		} catch (NullPointerException e) {
			FeedbackHandler.error(sender, "Failed to Trust %" + targetUsername + "% to Container! (%" + targetUsername + "% Does not Exist)");
			return;
		}

		String targetUsernameOrDisplayName = profile.getRight();
		UUID targetUUID = profile.getLeft();

		if (!role.playersGrantedRole.contains(targetUUID)) {
			role.playersGrantedRole.add(targetUUID);
			role.save();
			FeedbackHandler.success(sender, "Granted Role %" + role.roleID + "% to Player %" + targetUsernameOrDisplayName);
		} else {
			FeedbackHandler.error(sender, "Failed to Grant Role %" + role.roleID + "% to Player %" + targetUsernameOrDisplayName);
			FeedbackHandler.error(sender, "(Player already has Role!)");
		}
	}

	public static int role_grant(Player sender, Player target, Role role){
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

	public static int role_create(Player sender, String roleID, int rolePriority) {
		if (Data.Roles.roleDataHashMap.containsKey(roleID)) {
			FeedbackHandler.error(sender, "Failed to Create Role with RoleID %" + roleID + "% (Role Already Exists)");
			return Command.SINGLE_SUCCESS;
		}
		Role role = Data.Roles.create(roleID);
		role.displayName = roleID;
		role.priority = rolePriority;
		role.save();
		FeedbackHandler.success(sender, "Created Role %" + role.roleID + "% with Priority %" + role.priority);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_delete(Player sender, Role role) {
		FeedbackHandler.destructive(sender, "Deleted Role %" + role.roleID);
		role.delete();
		return Command.SINGLE_SUCCESS;
	}

	public static int role_set_defaultrole_ROLEID(Player sender, Role role) {
		Data.MainConfig.config.defaultRole = role.roleID;
		Data.MainConfig.save();
		FeedbackHandler.success(sender, "Set Default Role to %" + role.roleID);
		return Command.SINGLE_SUCCESS;

	}

	public static int role_set_defaultrole_none(Player sender) {
		Data.MainConfig.config.defaultRole = null;
		Data.MainConfig.save();
		FeedbackHandler.destructive(sender, "Removed Default Role");
		return Command.SINGLE_SUCCESS;
	}

	public static int role_set_displaymode_single(Player sender) {
		Data.MainConfig.config.displayMode = "single";
		Data.MainConfig.save();
		FeedbackHandler.success(sender, "Set Display Mode to %single%");
		return Command.SINGLE_SUCCESS;
	}

	public static int role_set_displaymode_multi(Player sender) {
		Data.MainConfig.config.displayMode = "multi";
		Data.MainConfig.save();
		FeedbackHandler.success(sender, "Set Display Mode to %multi%");
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_priority(Player sender, Role role, int priorityValue){
		role.priority = priorityValue;
		role.save();
		FeedbackHandler.success(sender, "Set Priority for Role %" + role.roleID + "% to %" + priorityValue);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_name(Player sender, Role role, String displayName) {
		FeedbackHandler.success(sender, "Set Display Name for Role %" + role.roleID + "% to %" + displayName);
		role.displayName = displayName;
		role.save();
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_color_COLOR(Player sender, Role role, String color) {
		role.displayColor = color;
		role.save();
		FeedbackHandler.success(sender, "Set Display Color for Role %" + role.roleID + "% to %" + TextFormatting.getColorFormatting(color) + color);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_color_HEX(Player sender, Role role, String hex) {
		role.displayColor = hex;
		role.save();
		FeedbackHandler.success(sender, "Set Display Color for Role %" + role.roleID + "% to %§<" + hex + ">" + hex);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_underline(Player sender, Role role, boolean value) {
		role.isDisplayUnderlined = value;
		role.save();
		FeedbackHandler.success(sender, "Set Display Underline for Role %" + role.roleID + "% to %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_bold(Player sender, Role role, boolean value) {
		role.isDisplayBold = value;
		role.save();
		FeedbackHandler.success(sender, "Set Display Bold for Role %" + role.roleID + "% to %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_italics(Player sender, Role role, boolean value) {
		role.isDisplayItalics = value;
		role.save();
		FeedbackHandler.success(sender, "Set Display Italics for Role %" + role.roleID + "% to %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_color_COLOR(Player sender, Role role, String color) {
		role.displayBorderColor = color;
		role.save();
		FeedbackHandler.success(sender, "Set Display Border Color for Role %" + role.roleID + "% to %" + TextFormatting.getColorFormatting(color) + color);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_color_HEX(Player sender, Role role, String hex) {
		role.displayBorderColor = hex;
		role.save();
		FeedbackHandler.success(sender, "Set Display Border Color for Role %" + role.roleID + "% to %§<" + hex + ">" + hex);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_style_none(Player sender, Role role) {
		role.isUsernameBorderBracket = false;
		role.isUsernameBorderNone = true;
		role.isUsernameBorderCaret = false;
		role.isUsernameBorderCurly = false;
		role.isUsernameBorderCustom = false;
		role.save();
		FeedbackHandler.success(sender, "Set Display Border to % □None□ % for Role %" + role.roleID);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_style_bracket(Player sender, Role role) {
		role.isDisplayBorderBracket = true;
		role.isDisplayBorderNone = false;
		role.isDisplayBorderCaret = false;
		role.isDisplayBorderCurly = false;
		role.isDisplayBorderCustom = false;
		role.save();
		FeedbackHandler.success(sender, "Set Display Border to % [Bracket] % for Role %" + role.roleID);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_style_curly(Player sender, Role role) {
		role.isDisplayBorderBracket = false;
		role.isDisplayBorderNone = false;
		role.isDisplayBorderCaret = false;
		role.isDisplayBorderCurly = true;
		role.isDisplayBorderCustom = false;
		role.save();
		FeedbackHandler.success(sender, "Set Display Border to % {Curly} % for Role %" + role.roleID);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_style_caret(Player sender, Role role) {
		role.isDisplayBorderBracket = false;
		role.isDisplayBorderNone = false;
		role.isDisplayBorderCaret = true;
		role.isDisplayBorderCurly = false;
		role.isDisplayBorderCustom = false;
		role.save();
		FeedbackHandler.success(sender, "Set Display Border to % <Caret> % for Role %" + role.roleID);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_style_custom_prefix(Player sender, Role role, String customAffix) {
		role.isDisplayBorderBracket = false;
		role.isDisplayBorderNone = false;
		role.isDisplayBorderCaret = false;
		role.isDisplayBorderCurly = false;
		role.isDisplayBorderCustom = true;
		role.customDisplayBorderPrefix = customAffix;
		role.save();
		FeedbackHandler.success(sender, "Set Display Border Prefix to % " + customAffix + " % for Role %" + role.roleID);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_display_border_style_custom_suffix(Player sender, Role role, String customAffix) {
		role.isDisplayBorderBracket = false;
		role.isDisplayBorderNone = false;
		role.isDisplayBorderCaret = false;
		role.isDisplayBorderCurly = false;
		role.isDisplayBorderCustom = true;
		role.customDisplayBorderSuffix = customAffix;
		role.save();
		FeedbackHandler.success(sender, "Set Display Border Suffix to % " + customAffix + " % for Role %" + role.roleID);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_color_COLOR(Player sender, Role role, String color) {
		role.usernameBorderColor = color;
		role.save();
		FeedbackHandler.success(sender, "Set Username Border Color for Role %" + role.roleID + "% to %" + TextFormatting.getColorFormatting(color) + color);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_color_HEX(Player sender, Role role, String hex) {
		role.usernameBorderColor = hex;
		role.save();
		FeedbackHandler.success(sender, "Set Username Border Color for Role %" + role.roleID + "% to %§<" + hex + ">" + hex);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_style_none(Player sender, Role role) {
		role.isUsernameBorderBracket = false;
		role.isUsernameBorderNone = true;
		role.isUsernameBorderCaret = false;
		role.isUsernameBorderCurly = false;
		role.isUsernameBorderCustom = false;
		role.save();
		FeedbackHandler.success(sender, "Set Username Border to % □None□ % for Role %" + role.roleID);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_style_bracket(Player sender, Role role) {
		role.isUsernameBorderBracket = true;
		role.isUsernameBorderNone = false;
		role.isUsernameBorderCaret = false;
		role.isUsernameBorderCurly = false;
		role.isUsernameBorderCustom = false;
		role.save();
		FeedbackHandler.success(sender, "Set Username Border to % [Bracket] % for Role %" + role.roleID);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_style_curly(Player sender, Role role) {
		role.isUsernameBorderBracket = false;
		role.isUsernameBorderNone = false;
		role.isUsernameBorderCaret = false;
		role.isUsernameBorderCurly = true;
		role.isUsernameBorderCustom = false;
		role.save();
		FeedbackHandler.success(sender, "Set Username Border to % {Curly} % for Role %" + role.roleID);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_style_caret(Player sender, Role role) {
		role.isUsernameBorderBracket = false;
		role.isUsernameBorderNone = false;
		role.isUsernameBorderCaret = true;
		role.isUsernameBorderCurly = false;
		role.isUsernameBorderCustom = false;
		role.save();
		FeedbackHandler.success(sender, "Set Username Border to % <Caret> % for Role %" + role.roleID);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_style_custom_prefix(Player sender, Role role, String customAffix) {
		role.isUsernameBorderBracket = false;
		role.isUsernameBorderNone = false;
		role.isUsernameBorderCaret = false;
		role.isUsernameBorderCurly = false;
		role.isUsernameBorderCustom = true;
		role.customUsernameBorderPrefix = customAffix;
		role.save();
		FeedbackHandler.success(sender, "Set Username Border Prefix to % " + customAffix + " % for Role %" + role.roleID);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_username_border_style_custom_suffix(Player sender, Role role, String customAffix) {
		role.isUsernameBorderBracket = false;
		role.isUsernameBorderNone = false;
		role.isUsernameBorderCaret = false;
		role.isUsernameBorderCurly = false;
		role.isUsernameBorderCustom = true;
		role.customUsernameBorderSuffix = customAffix;
		role.save();
		FeedbackHandler.success(sender, "Set Username Border Suffix to % " + customAffix + " % for Role %" + role.roleID);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_color_COLOR(Player sender, Role role, String color) {
		role.textColor = color;
		role.save();
		FeedbackHandler.success(sender, "Set Text Color for Role %" + role.roleID + "% to %" + TextFormatting.getColorFormatting(color) + color);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_color_HEX(Player sender, Role role, String hex) {
		role.textColor = hex;
		role.save();
		FeedbackHandler.success(sender, "Set Text Color for Role %" + role.roleID + "% to %§<" + hex + ">" + hex);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_underline(Player sender, Role role, boolean value) {
		role.isTextUnderlined = value;
		role.save();
		FeedbackHandler.success(sender, "Set Text Underline for Role %" + role.roleID + "% to %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_bold(Player sender, Role role, boolean value) {
		role.isTextBold = value;
		role.save();
		FeedbackHandler.success(sender, "Set Text Bold for Role %" + role.roleID + "% to %" + value);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_edit_text_italics(Player sender, Role role, boolean value) {
		role.isTextItalics = value;
		role.save();
		FeedbackHandler.success(sender, "Set Text Italics for Role %" + role.roleID + "% to %" + value);
		return Command.SINGLE_SUCCESS;
	}
}
