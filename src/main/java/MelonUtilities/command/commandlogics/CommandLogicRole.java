package MelonUtilities.command.commandlogics;

import MelonUtilities.command.commands.CommandRole;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.data.Role;
import MelonUtilities.interfaces.PlayerCustomInputFunctionInterface;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.builders.RoleBuilder;
import MelonUtilities.utility.classes.Icon;
import MelonUtilities.utility.feedback.FeedbackArg;
import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
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
							roleGrant(sender, target, role);
							sender.usePersonalCraftingInventory();
						})));
						//-------------------------------------
						j++;
					}
					int finalJ1 = j;
					roleGrantToGui.setContainerSlot(j+1, (roleGrantToGuiInventory -> new ServerSlotButton(inputUsernameIcon.icon, roleGrantToGuiInventory, finalJ1+1, () -> {
						((PlayerCustomInputFunctionInterface) sender).melonutilities$setCustomInputFunction(customInput -> roleGrant(sender, customInput, role));
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
			roleReload(sender);
			sender.usePersonalCraftingInventory();
		})));
		//-------------------------------------
		roleGui.setContainerSlot(5, (inventory -> new ServerSlotButton(roleListIcon.icon, inventory, 5, () -> {
			roleList(sender);
			sender.usePersonalCraftingInventory();
		})));
		//-------------------------------------
		roleGui.setContainerSlot(6, (inventory -> new ServerSlotButton(roleRevokeIcon.icon, inventory, 6, () -> {
			ServerGuiBuilder roleRevokeGui = new ServerGuiBuilder();
			roleRevokeGui.setSize(0);

		})));
		//-------------------------------------
		GuiHelper.openCustomServerGui(sender, roleGui.build(sender, "Role Command:"));

		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Opened Role GUI!");
		return Command.SINGLE_SUCCESS;
	}

	static Icon roleReloadIcon = new Icon("[Reload]", (byte) TextFormatting.ORANGE.id, Items.REPEATER.getDefaultStack());
	public static int roleReload(PlayerServer sender) {
		Data.Roles.reload();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Reloaded %s Role(s)!", new FeedbackArg(Data.Roles.roleDataHashMap.size()));
		CommandRole.buildSyntax();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Built Role Syntax!");
		Data.MainConfig.reload();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Reloaded Config!");
		return Command.SINGLE_SUCCESS;
	}

	static Icon roleListIcon = new Icon("[List]", (byte) TextFormatting.LIGHT_GRAY.id, Items.PAPER.getDefaultStack());
	public static int roleList(PlayerServer sender) {
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
		sender.sendMessage(TextFormatting.GRAY + "<>");
		return Command.SINGLE_SUCCESS;
	}

	static Icon roleRevokeIcon = new Icon("[Revoke]", (byte) TextFormatting.RED.id, Items.DUST_REDSTONE.getDefaultStack());
	public static int roleRevoke(PlayerServer sender, Player target, Role role){

		if (role.playersGrantedRole.contains(target.uuid)) {
			role.playersGrantedRole.remove(target.uuid);
			role.save();
			FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Revoked Role %s from Player %s", new FeedbackArg(role.roleID), new FeedbackArg(target.getDisplayName()));
		} else {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Revoke Role %s from Player %s", new FeedbackArg(role.roleID), new FeedbackArg(target.getDisplayName()));
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "(Player does not have Role!)");
		}

		return Command.SINGLE_SUCCESS;
	}

	static Icon roleGrantIcon = new Icon("[Grant]", (byte) TextFormatting.LIME.id, Items.OLIVINE.getDefaultStack());
	public static void roleGrant(PlayerServer sender, String targetUsername, Role role) {
		Pair<UUID, String> profile;
		try {
			profile = MUtil.getProfileFromUsername(targetUsername);
		} catch (NullPointerException e) {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Grant Role %s to Player %s (Player Does not Exist)", new FeedbackArg(role.roleID), new FeedbackArg(targetUsername));
			return;
		}

		String targetUsernameOrDisplayName = profile.getRight();
		UUID targetUUID = profile.getLeft();

		if (!role.playersGrantedRole.contains(targetUUID)) {
			role.playersGrantedRole.add(targetUUID);
			role.save();
			FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Granted Role %s to Player %s", new FeedbackArg(role.roleID), new FeedbackArg(targetUsernameOrDisplayName));
		} else {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Grant Role %s to Player %s", new FeedbackArg(role.roleID), new FeedbackArg(targetUsernameOrDisplayName));
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "(Player already has Role!)");
		}
	}

	public static int roleGrant(PlayerServer sender, Player target, Role role){
		if (!role.playersGrantedRole.contains(target.uuid)){
			role.playersGrantedRole.add(target.uuid);
			role.save();
			FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Granted Role %s to Player %s", new FeedbackArg(role.roleID), new FeedbackArg(target.getDisplayName()));
		} else {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Grant Role %s to Player %s", new FeedbackArg(role.roleID), new FeedbackArg(target.getDisplayName()));
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "(Player already has Role!)");
			return 0;
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int roleCreate(PlayerServer sender, String roleID, int rolePriority) {
		if (Data.Roles.roleDataHashMap.containsKey(roleID)) {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Create Role with RoleID %s (Role Already Exists)", new FeedbackArg(roleID));
			return Command.SINGLE_SUCCESS;
		}
		Role role = Data.Roles.create(roleID);
		role.displayName = roleID;
		role.priority = rolePriority;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Created Role %s with Priority %s", new FeedbackArg(role.roleID), new FeedbackArg(role.priority));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleDelete(PlayerServer sender, Role role) {
		FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Deleted Role %s", new FeedbackArg(role.roleID));
		role.delete();
		return Command.SINGLE_SUCCESS;
	}

	public static int roleSetDefaultroleROLEID(PlayerServer sender, Role role) {
		Data.MainConfig.config.defaultRole = role.roleID;
		Data.MainConfig.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Default Role to %s", new FeedbackArg(role.roleID));
		return Command.SINGLE_SUCCESS;

	}

	public static int roleSetDefaultroleNone(PlayerServer sender) {
		Data.MainConfig.config.defaultRole = null;
		Data.MainConfig.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Removed Default Role");
		return Command.SINGLE_SUCCESS;
	}

	public static int roleSetDisplaymodeSingle(PlayerServer sender) {
		Data.MainConfig.config.displayMode = "single";
		Data.MainConfig.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Display Mode to %s", new FeedbackArg("single"));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleSetDisplaymodeMulti(PlayerServer sender) {
		Data.MainConfig.config.displayMode = "multi";
		Data.MainConfig.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Display Mode to %s", new FeedbackArg("multi"));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditPriority(PlayerServer sender, Role role, int priorityValue){
		role.priority = priorityValue;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Priority for Role %s to %s", new FeedbackArg(role.roleID), new FeedbackArg(priorityValue));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditDisplayName(PlayerServer sender, Role role, String displayName) {
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Display Name for Role %s to %s", new FeedbackArg(role.roleID), new FeedbackArg(displayName));
		role.displayName = displayName;
		role.save();
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditDisplayColorCOLOR(PlayerServer sender, Role role, String color) {
		role.displayColor = color;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Display Color for Role %s to %s", new FeedbackArg(role.roleID), new FeedbackArg().argColor(TextFormatting.getColorFormatting(color)));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditDisplayColorHEX(PlayerServer sender, Role role, String hex) {
		role.displayColor = hex;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Display Color for Role %s to %s", new FeedbackArg(role.roleID), new FeedbackArg("#" + hex).argColor(hex));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditDisplayUnderline(PlayerServer sender, Role role, boolean value) {
		role.isDisplayUnderlined = value;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Display Underline for Role %s to %s", new FeedbackArg(role.roleID), new FeedbackArg(value));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditDisplayBold(PlayerServer sender, Role role, boolean value) {
		role.isDisplayBold = value;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Display Bold for Role %s to %s", new FeedbackArg(role.roleID), new FeedbackArg(value));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditDisplayItalics(PlayerServer sender, Role role, boolean value) {
		role.isDisplayItalics = value;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Display Italics for Role %s to %s", new FeedbackArg(role.roleID), new FeedbackArg(value));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditDisplayBorderColorCOLOR(PlayerServer sender, Role role, String color) {
		role.displayBorderColor = color;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Display Border Color for Role %s to %s", new FeedbackArg(role.roleID), new FeedbackArg(color).argColor(TextFormatting.getColorFormatting(color)));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditDisplayBorderColorHEX(PlayerServer sender, Role role, String hex) {
		role.displayBorderColor = hex;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Display Border Color for Role %s to %s", new FeedbackArg(role.roleID), new FeedbackArg("#" + hex).argColor(hex));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditDisplayBorderStyleNone(PlayerServer sender, Role role) {
		role.isUsernameBorderBracket = false;
		role.isUsernameBorderNone = true;
		role.isUsernameBorderCaret = false;
		role.isUsernameBorderCurly = false;
		role.isUsernameBorderCustom = false;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Display Border to %s for Role %s", new FeedbackArg("□None□").noBorder(), new FeedbackArg(role.roleID));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditDisplayBorderStyleBracket(PlayerServer sender, Role role) {
		role.isDisplayBorderBracket = true;
		role.isDisplayBorderNone = false;
		role.isDisplayBorderCaret = false;
		role.isDisplayBorderCurly = false;
		role.isDisplayBorderCustom = false;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Display Border to %s for Role %s", new FeedbackArg("Bracket").bracketBorder(), new FeedbackArg(role.roleID));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditDisplayBorderStyleCurly(PlayerServer sender, Role role) {
		role.isDisplayBorderBracket = false;
		role.isDisplayBorderNone = false;
		role.isDisplayBorderCaret = false;
		role.isDisplayBorderCurly = true;
		role.isDisplayBorderCustom = false;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Display Border to %s for Role %s", new FeedbackArg("Curly").curlyBorder(), new FeedbackArg(role.roleID));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditDisplayBorderStyleCaret(PlayerServer sender, Role role) {
		role.isDisplayBorderBracket = false;
		role.isDisplayBorderNone = false;
		role.isDisplayBorderCaret = true;
		role.isDisplayBorderCurly = false;
		role.isDisplayBorderCustom = false;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Display Border to %s for Role %s", new FeedbackArg("Caret").caretBorder(), new FeedbackArg(role.roleID));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditDisplayBorderStyleCustomPrefix(PlayerServer sender, Role role, String customAffix) {
		role.isDisplayBorderBracket = false;
		role.isDisplayBorderNone = false;
		role.isDisplayBorderCaret = false;
		role.isDisplayBorderCurly = false;
		role.isDisplayBorderCustom = true;
		role.customDisplayBorderPrefix = customAffix;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Display Border Prefix to %s for Role %s", new FeedbackArg(customAffix), new FeedbackArg(role.roleID));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditDisplayBorderStyleCustomSuffix(PlayerServer sender, Role role, String customAffix) {
		role.isDisplayBorderBracket = false;
		role.isDisplayBorderNone = false;
		role.isDisplayBorderCaret = false;
		role.isDisplayBorderCurly = false;
		role.isDisplayBorderCustom = true;
		role.customDisplayBorderSuffix = customAffix;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Display Border Suffix to %s for Role %s", new FeedbackArg(customAffix), new FeedbackArg(role.roleID));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditUsernameBorderColorCOLOR(PlayerServer sender, Role role, String color) {
		role.usernameBorderColor = color;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Username Border Color for Role %s to %s", new FeedbackArg(role.roleID), new FeedbackArg(color).argColor(TextFormatting.getColorFormatting(color)));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditUsernameBorderColorHEX(PlayerServer sender, Role role, String hex) {
		role.usernameBorderColor = hex;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Username Border Color for Role %s to %s", new FeedbackArg(role.roleID), new FeedbackArg("#" + hex).argColor(hex));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditUsernameBorderStyleNone(PlayerServer sender, Role role) {
		role.isUsernameBorderBracket = false;
		role.isUsernameBorderNone = true;
		role.isUsernameBorderCaret = false;
		role.isUsernameBorderCurly = false;
		role.isUsernameBorderCustom = false;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Username Border to %s for Role %s", new FeedbackArg("□None□").noBorder(), new FeedbackArg(role.roleID));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditUsernameBorderStyleBracket(PlayerServer sender, Role role) {
		role.isUsernameBorderBracket = true;
		role.isUsernameBorderNone = false;
		role.isUsernameBorderCaret = false;
		role.isUsernameBorderCurly = false;
		role.isUsernameBorderCustom = false;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Username Border to %s for Role %s", new FeedbackArg("Bracket").bracketBorder(), new FeedbackArg(role.roleID));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditUsernameBorderStyleCurly(PlayerServer sender, Role role) {
		role.isUsernameBorderBracket = false;
		role.isUsernameBorderNone = false;
		role.isUsernameBorderCaret = false;
		role.isUsernameBorderCurly = true;
		role.isUsernameBorderCustom = false;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Username Border to %s for Role %s", new FeedbackArg("Curly").curlyBorder(), new FeedbackArg(role.roleID));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditUsernameBorderStyleCaret(PlayerServer sender, Role role) {
		role.isUsernameBorderBracket = false;
		role.isUsernameBorderNone = false;
		role.isUsernameBorderCaret = true;
		role.isUsernameBorderCurly = false;
		role.isUsernameBorderCustom = false;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Username Border to %s for Role %s", new FeedbackArg("Caret").caretBorder(), new FeedbackArg(role.roleID));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditUsernameBorderStyleCustomPrefix(PlayerServer sender, Role role, String customAffix) {
		role.isUsernameBorderBracket = false;
		role.isUsernameBorderNone = false;
		role.isUsernameBorderCaret = false;
		role.isUsernameBorderCurly = false;
		role.isUsernameBorderCustom = true;
		role.customUsernameBorderPrefix = customAffix;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Username Border Prefix to %s for Role %s", new FeedbackArg(customAffix), new FeedbackArg(role.roleID));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditUsernameBorderStyleCustomSuffix(PlayerServer sender, Role role, String customAffix) {
		role.isUsernameBorderBracket = false;
		role.isUsernameBorderNone = false;
		role.isUsernameBorderCaret = false;
		role.isUsernameBorderCurly = false;
		role.isUsernameBorderCustom = true;
		role.customUsernameBorderSuffix = customAffix;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Username Border Suffix to %s for Role %s", new FeedbackArg(customAffix), new FeedbackArg(role.roleID));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditTextColorCOLOR(PlayerServer sender, Role role, String color) {
		role.textColor = color;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Text Color for Role %s to %s", new FeedbackArg(role.roleID), new FeedbackArg(color).argColor(TextFormatting.getColorFormatting(color)));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditTextColorHEX(PlayerServer sender, Role role, String hex) {
		role.textColor = hex;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Text Color for Role %s to %s", new FeedbackArg(role.roleID), new FeedbackArg("#" + hex).argColor(hex));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditTextUnderline(PlayerServer sender, Role role, boolean value) {
		role.isTextUnderlined = value;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Text Underline for Role %s to %s", new FeedbackArg(role.roleID), new FeedbackArg(value));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditTextBold(PlayerServer sender, Role role, boolean value) {
		role.isTextBold = value;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Text Bold for Role %s to %s", new FeedbackArg(role.roleID), new FeedbackArg(value));
		return Command.SINGLE_SUCCESS;
	}

	public static int roleEditTextItalics(PlayerServer sender, Role role, boolean value) {
		role.isTextItalics = value;
		role.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Set Text Italics for Role %s to %s", new FeedbackArg(role.roleID), new FeedbackArg(value));
		return Command.SINGLE_SUCCESS;
	}
}
