package MelonUtilities.commands;

import MelonUtilities.commands.role.CommandRole;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.data.Role;
import MelonUtilities.interfaces.TileEntityContainerInterface;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.feedback.FeedbackHandler;
import MelonUtilities.utility.builders.RoleBuilder;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.block.entity.*;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.command.helpers.EntitySelector;
import net.minecraft.core.util.helper.UUIDHelper;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;

import java.util.UUID;

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
		Data.Users.reload();
		FeedbackHandler.success(context, "Reloaded " + Data.Users.userDataHashMap.size() + " Player(s)!");

		//TODO FeedbackHandler.destructive(source, "Building Helper Syntax...");
		//TODO HelperCommand.buildHelperSyntax();
		//TODO FeedbackHandler.success(source, "Helper Syntax Built!");

		FeedbackHandler.destructive(context, "Reloading Kit Data...");
		Data.Kits.reload();
		FeedbackHandler.success(context, "Reloaded " + Data.Kits.kitDataHashMap.size() + " Kit(s)!");

		//TODO FeedbackHandler.destructive(source, "Building Kit Syntax...");
		//TODO KitCommand.buildKitSyntax();
		//TODO FeedbackHandler.success(source, "Kit Syntax Built!");

		FeedbackHandler.destructive(context, "Reloading Role Data...");
		Data.Roles.reload();
		FeedbackHandler.success(context, "Reloaded " + Data.Roles.roleDataHashMap.size() + " Role(s)!");

		FeedbackHandler.destructive(context, "Building Role Syntax...");
		CommandRole.buildRoleSyntax();
		FeedbackHandler.success(context, "Role Syntax Built!");

		//TODO FeedbackHandler.destructive(source, "Building Rollback Syntax...");
		//TODO RollbackCommand.buildSyntax();
		//TODO FeedbackHandler.success(source, "Rollback Syntax Built!");

		FeedbackHandler.destructive(context, "Reloading General Configs...");
		Data.MainConfig.reload();
		FeedbackHandler.success(context, "Reloaded Configs!");

		FeedbackHandler.destructive(context, "Updating Player List...");
		updateList();
		FeedbackHandler.success(context, "Updated List!");
		return Command.SINGLE_SUCCESS;
	}

	public static int role_reload(CommandContext<CommandSource> context) {
		Data.Roles.reload();
		FeedbackHandler.success(context, "Reloaded %" + Data.Roles.roleDataHashMap.size() + "% Role(s)!");
		CommandRole.buildRoleSyntax();
		FeedbackHandler.success(context, "Built Role Syntax!");
		Data.MainConfig.reload();
		FeedbackHandler.success(context, "Reloaded Config!");
		return Command.SINGLE_SUCCESS;
	}

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

		return Command.SINGLE_SUCCESS;
	}

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

	public static int role_grant(CommandContext<CommandSource> context) throws CommandSyntaxException {
		CommandSource source = context.getSource();
		Role role = context.getArgument("role", Role.class);

		EntitySelector entitySelector = context.getArgument("target", EntitySelector.class);
		Player target = ((Player)entitySelector.get(source).get(0));

		if (!role.playersGrantedRole.contains(target.uuid)){
			role.playersGrantedRole.add(target.uuid);
			role.save();
			FeedbackHandler.success(context, "Granted Role %" + role.roleID + "% to Player %" + target.getDisplayName());
		} else {
			FeedbackHandler.error(context, "Failed to Grant Role %" + role.roleID + "% to Player %" + target.getDisplayName());
			FeedbackHandler.error(context, "(Player already has Role!)");
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

	public static int lock(CommandContext<CommandSource> context){
		CommandSource source = context.getSource();
		Player sender = source.getSender();

		HitResult rayCastResult = MUtil.rayCastFromPlayer(context);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandler.error(context, "Failed to Lock Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = source.getWorld().getBlockEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);
		if(container != null){
			if (container instanceof TileEntityContainerInterface) {
				TileEntityContainerInterface containerInterface = ((TileEntityContainerInterface) container);
				if (!containerInterface.getIsLocked()) {
					if (container instanceof TileEntityChest) {
						TileEntityContainerInterface otherContainerInterface = (TileEntityContainerInterface) MUtil.getOtherChest(source.getWorld(), (TileEntityChest) container);
						if (otherContainerInterface != null) {
							containerInterface.setIsLocked(true);
							otherContainerInterface.setIsLocked(true);
							containerInterface.setLockOwner(sender.uuid);
							otherContainerInterface.setLockOwner(sender.uuid);
							FeedbackHandler.success(context, "Locked Double Chest!");
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandler.success(context, "Locked Chest!");
					} else if (container instanceof TileEntityFurnaceBlastFurnace) {
						FeedbackHandler.success(context, "Locked Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandler.success(context, "Locked Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandler.success(context, "Locked Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandler.success(context, "Locked Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandler.success(context, "Locked Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandler.success(context, "Locked Basket!");
					}

					containerInterface.setIsLocked(true);
					containerInterface.setLockOwner(sender.uuid);
					return Command.SINGLE_SUCCESS;

				} else if (containerInterface.getIsLocked() && !containerInterface.getLockOwner().equals(sender.uuid)) {
					FeedbackHandler.error(context, "Failed to Lock Container! (Not Owned By You)");
					return Command.SINGLE_SUCCESS;
				}
				FeedbackHandler.error(context, "Failed to Lock Container! (Already Locked)");
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandler.error(context, "Failed to Lock Container! (Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_onblockplaced(CommandContext<CommandSource> context){
		CommandSource source = context.getSource();
		boolean value = context.getArgument("value", Boolean.class);
		UUID senderUUID = source.getSender().uuid;

		if(value) {
			if(!Data.Users.getOrCreate(senderUUID).lockOnBlockPlaced){
				Data.Users.getOrCreate(senderUUID).lockOnBlockPlaced = true;
				Data.Users.save(senderUUID);
				FeedbackHandler.success(context, "Locking on Block Placed set to %" + true);
				return Command.SINGLE_SUCCESS;
			}
			FeedbackHandler.error(context, "Failed to set Locking on Block Placed.. (Already %true%)");
		}
		else {
			if(Data.Users.getOrCreate(senderUUID).lockOnBlockPlaced) {
				Data.Users.getOrCreate(senderUUID).lockOnBlockPlaced = false;
				Data.Users.save(senderUUID);
				FeedbackHandler.success(context, "Locking on Block Placed set to %" + false);
				return Command.SINGLE_SUCCESS;
			}
			FeedbackHandler.error(context, "Failed to set Locking on Block Placed.. (Already %false%)");
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_onblockpunched(CommandContext<CommandSource> context){
		CommandSource source = context.getSource();
		boolean value = context.getArgument("value", Boolean.class);
		UUID senderUUID = source.getSender().uuid;

		if(value) {
			if(!Data.Users.getOrCreate(senderUUID).lockOnBlockPunched){
				Data.Users.getOrCreate(senderUUID).lockOnBlockPunched = true;
				Data.Users.save(senderUUID);
				FeedbackHandler.success(context, "Locking on Block Punched set to %" + true);
				return Command.SINGLE_SUCCESS;
			}
			FeedbackHandler.error(context, "Failed to set Locking on Block Punched.. (Already %true%)");
		}
		else {
			if(Data.Users.getOrCreate(senderUUID).lockOnBlockPunched) {
				Data.Users.getOrCreate(senderUUID).lockOnBlockPunched = false;
				Data.Users.save(senderUUID);
				FeedbackHandler.success(context, "Locking on Block Punched set to %" + false);
				return Command.SINGLE_SUCCESS;
			}
			FeedbackHandler.error(context, "Failed to set Locking on Block Punched.. (Already %false%)");
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_trust(CommandContext<CommandSource> context){
		CommandSource source = context.getSource();
		Player sender = source.getSender();

		String targetUsername = context.getArgument("username", String.class).toLowerCase();
		PlayerServer target = MinecraftServer.getInstance().playerList.getPlayerEntity(targetUsername);
		UUID targetUUID;
		String targetDisplayName;

		if(target != null){
			targetUUID = target.uuid;
			targetDisplayName = target.getDisplayName();
		} else {
			targetUUID = UUIDHelper.getUUIDFromName(targetUsername);
			if(targetUUID == null){
				FeedbackHandler.error(context, "Failed to Trust %" + targetUsername + "% to Container! (%" + targetUsername + "% Does not Exist)");
				return 0;
			}
			targetDisplayName = targetUsername;
		}


		HitResult rayCastResult = MUtil.rayCastFromPlayer(context);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandler.error(context, "Failed to Trust %" + targetDisplayName + "% to Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = source.getWorld().getBlockEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if(container != null) {
			if (container instanceof TileEntityContainerInterface) {
				TileEntityContainerInterface containerInterface = ((TileEntityContainerInterface) container);
				if (containerInterface.getIsLocked()) {

					if (!containerInterface.getLockOwner().equals(sender.uuid)) {
						FeedbackHandler.error(context, "Failed to Trust %" + targetDisplayName + "% to Container! (Not Owned By You)");
						return Command.SINGLE_SUCCESS;
					}

					if (containerInterface.getTrustedPlayers().contains(targetUUID)) {
						FeedbackHandler.error(context, "Failed to Trust %" + targetDisplayName + "% to Container! (Player already Trusted)");
						return Command.SINGLE_SUCCESS;
					}

					if (container instanceof TileEntityChest) {
						TileEntityContainerInterface otherContainerInterface = (TileEntityContainerInterface) MUtil.getOtherChest(source.getWorld(), (TileEntityChest) container);
						if (otherContainerInterface != null) {
							containerInterface.addTrustedPlayer(targetUUID);
							otherContainerInterface.addTrustedPlayer(targetUUID);
							FeedbackHandler.success(context, "Trusted %" + targetDisplayName + "% to this Double Chest!");
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandler.success(context, "Trusted %" + targetDisplayName + "% to this Chest!");
					} else if (container instanceof TileEntityFurnaceBlastFurnace) {
						FeedbackHandler.success(context, "Trusted %" + targetDisplayName + "% to this Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandler.success(context, "Trusted %" + targetDisplayName + "% to this Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandler.success(context, "Trusted %" + targetDisplayName + "% to this Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandler.success(context, "Trusted %" + targetDisplayName + "% to this Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandler.success(context, "Trusted %" + targetDisplayName + "% to this Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandler.success(context, "Trusted %" + targetDisplayName + "% to this Basket!");
					}
					containerInterface.addTrustedPlayer(targetUUID);
				} else {
					FeedbackHandler.error(context, "Failed to Trust %" + targetDisplayName + "% to Container! (Container not Locked)");
				}
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandler.error(context, "Failed to Trust %" + targetDisplayName + "% to Container! (Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_trustall(CommandContext<CommandSource> context){
		CommandSource source = context.getSource();
		Player sender = source.getSender();

		String targetUsername = context.getArgument("username", String.class).toLowerCase();
		PlayerServer target = MinecraftServer.getInstance().playerList.getPlayerEntity(targetUsername);
		UUID targetUUID;

		if(target != null){
			targetUUID = target.uuid;
			String targetDisplayName = target.getDisplayName();
			if(!Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.containsKey(targetUUID)){

				Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.put(targetUUID, targetUsername);
				Data.Users.save(sender.uuid);
				FeedbackHandler.success(context, "Trusted %" + targetDisplayName + "% to all Containers!");
				return Command.SINGLE_SUCCESS;
			}
			FeedbackHandler.error(context, "Failed to Trust %" + targetDisplayName + "% to all Containers! (Player is Already Trusted)");
			return Command.SINGLE_SUCCESS;
		} else {
			UUIDHelper.runConversionAction(targetUsername, targetuuid -> {
				if(!Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.containsKey(targetuuid)){

					Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.put(targetuuid, targetUsername);
					Data.Users.save(sender.uuid);
					FeedbackHandler.success(context, "Trusted %" + targetUsername + "% to all Containers!");
					return;
				}
				FeedbackHandler.error(context, "Failed to Trust %" + targetUsername + "% to all Containers! (Player is Already Trusted)");
			}, username -> FeedbackHandler.error(context, "Failed to Trust %" + targetUsername + "% to all Containers! (%" + targetUsername + "% Does not Exist)"));
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_trustcommunity(CommandContext<CommandSource> context) {
		CommandSource source = context.getSource();
		Player sender = source.getSender();

		HitResult rayCastResult = MUtil.rayCastFromPlayer(context);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandler.error(context, "Failed to Trust Community to Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = source.getWorld().getBlockEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if(container != null) {
			if (container instanceof TileEntityContainerInterface) {
				TileEntityContainerInterface containerInterface = ((TileEntityContainerInterface) container);
				if (containerInterface.getIsLocked()) {

					if (!containerInterface.getLockOwner().equals(sender.uuid)) {
						FeedbackHandler.error(context, "Failed to Trust Community to Container! (Not Owned By You)");
						return Command.SINGLE_SUCCESS;
					}

					if (containerInterface.getIsCommunityContainer()){
						FeedbackHandler.error(context, "Failed to Trust Community to Container! (Community already Trusted)");
						return Command.SINGLE_SUCCESS;
					}

					if (container instanceof TileEntityChest) {
						TileEntityContainerInterface otherContainerInterface = (TileEntityContainerInterface) MUtil.getOtherChest(source.getWorld(), (TileEntityChest) container);
						if (otherContainerInterface != null) {
							containerInterface.setIsCommunityContainer(true);
							otherContainerInterface.setIsCommunityContainer(true);
							FeedbackHandler.success(context, "Trusted Community to this Double Chest!");
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandler.success(context, "Trusted Community to this Chest!");
					} else if (container instanceof TileEntityFurnaceBlastFurnace) {
						FeedbackHandler.success(context, "Trusted Community to this Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandler.success(context, "Trusted Community to this Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandler.success(context, "Trusted Community to this Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandler.success(context, "Trusted Community to this Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandler.success(context, "Trusted Community to this Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandler.success(context, "Trusted Community to this Basket!");
					}
					containerInterface.setIsCommunityContainer(true);
				} else {
					FeedbackHandler.error(context, "Failed to Trust Community to Container! (Container not Locked)");
				}
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandler.error(context, "Failed to Trust Community to Container! (Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_untrust(CommandContext<CommandSource> context){
		CommandSource source = context.getSource();
		Player sender = source.getSender();

		String targetUsername = context.getArgument("username", String.class).toLowerCase();
		PlayerServer target = MinecraftServer.getInstance().playerList.getPlayerEntity(targetUsername);
		UUID targetUUID;
		String targetDisplayName;

		if(target != null){
			targetUUID = target.uuid;
			targetDisplayName = target.getDisplayName();
		} else {
			targetUUID = UUIDHelper.getUUIDFromName(targetUsername);
			if(targetUUID == null){
				FeedbackHandler.error(context, "Failed to Untrust %" + targetUsername + "% from Container! (%" + targetUsername + "% Does not Exist)");
				return 0;
			}
			targetDisplayName = targetUsername;
		}

		HitResult rayCastResult = MUtil.rayCastFromPlayer(context);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandler.error(context, "Failed to Untrust %" + targetDisplayName + "% from Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = source.getWorld().getBlockEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if(container != null) {
			if (container instanceof TileEntityContainerInterface) {
				TileEntityContainerInterface containerInterface = ((TileEntityContainerInterface) container);
				if (containerInterface.getIsLocked()) {

					if (!containerInterface.getLockOwner().equals(sender.uuid)) {
						FeedbackHandler.error(context, "Failed to Untrust %" + targetDisplayName + "% from Container! (Not Owned By You)");
						return Command.SINGLE_SUCCESS;
					}

					if (!containerInterface.getTrustedPlayers().contains(targetUUID)) {
						FeedbackHandler.error(context, "Failed to Untrust %" + targetDisplayName + "% from Container! (Player not Trusted)");
						return Command.SINGLE_SUCCESS;
					}

					if (container instanceof TileEntityChest) {
						TileEntityContainerInterface otherContainerInterface = (TileEntityContainerInterface) MUtil.getOtherChest(source.getWorld(), (TileEntityChest) container);
						if (otherContainerInterface != null) {
							containerInterface.removeTrustedPlayer(targetUUID);
							otherContainerInterface.removeTrustedPlayer(targetUUID);
							FeedbackHandler.destructive(context, "Untrusted %" + targetDisplayName + "% from this Double Chest!");
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandler.destructive(context, "Untrusted %" + targetDisplayName + "% from this Chest!");
					} else if (container instanceof TileEntityFurnaceBlastFurnace) {
						FeedbackHandler.destructive(context, "Untrusted %" + targetDisplayName + "% from this Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandler.destructive(context, "Untrusted %" + targetDisplayName + "% from this Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandler.destructive(context, "Untrusted %" + targetDisplayName + "% from this Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandler.destructive(context, "Untrusted %" + targetDisplayName + "% from this Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandler.destructive(context, "Untrusted %" + targetDisplayName + "% from this Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandler.destructive(context, "Untrusted %" + targetDisplayName + "% from this Basket!");
					}
					containerInterface.removeTrustedPlayer(targetUUID);
				} else {
					FeedbackHandler.error(context, "Failed to Untrust %" + targetDisplayName + "% from Container! (Container not Locked)");
				}
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandler.error(context, "Failed to Untrust %" + targetDisplayName + "% from Container! (Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_untrustall(CommandContext<CommandSource> context){
		CommandSource source = context.getSource();
		Player sender = source.getSender();

		String targetUsername = context.getArgument("username", String.class).toLowerCase();
		PlayerServer target = MinecraftServer.getInstance().playerList.getPlayerEntity(targetUsername);
		UUID targetUUID;

		if(target != null){
			targetUUID = target.uuid;
			String targetDisplayName = target.getDisplayName();
			if(Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.containsKey(targetUUID)){
				Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.remove(targetUUID);
				Data.Users.save(sender.uuid);
				FeedbackHandler.destructive(context, "Untrusted %" + targetDisplayName + "% from all Containers!");
				return Command.SINGLE_SUCCESS;
			}
			FeedbackHandler.error(context, "Failed to Untrust %" + targetDisplayName + "% from all Containers! (Player is Not Trusted)");
			return Command.SINGLE_SUCCESS;
		} else {
			UUIDHelper.runConversionAction(targetUsername, targetuuid -> {
				if(Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.containsKey(targetuuid)){

					Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.remove(targetuuid, targetUsername);
					Data.Users.save(sender.uuid);
					FeedbackHandler.destructive(context, "Untrusted %" + targetUsername + "% from all Containers!");
					return;
				}
				FeedbackHandler.error(context, "Failed to Untrust %" + targetUsername + "% from all Containers! (Player is Not Trusted)");
			}, username -> FeedbackHandler.error(context, "Failed to Untrust %" + targetUsername + "% from all Containers! (%" + targetUsername + "% Does not Exist)"));
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_untrustcommunity(CommandContext<CommandSource> context){
		CommandSource source = context.getSource();
		Player sender = source.getSender();

		HitResult rayCastResult = MUtil.rayCastFromPlayer(context);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandler.error(context, "Failed to Untrust Community from Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = source.getWorld().getBlockEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if(container != null) {
			if (container instanceof TileEntityContainerInterface) {
				TileEntityContainerInterface containerInterface = ((TileEntityContainerInterface) container);
				if (containerInterface.getIsLocked()) {

					if (!containerInterface.getLockOwner().equals(sender.uuid)) {
						FeedbackHandler.error(context, "Failed to Untrust Community from Container! (Not Owned By You)");
						return Command.SINGLE_SUCCESS;
					}

					if (!containerInterface.getIsCommunityContainer()){
						FeedbackHandler.error(context, "Failed to Untrust Community from Container! (Community not Trusted)");
						return Command.SINGLE_SUCCESS;
					}

					if (container instanceof TileEntityChest) {
						TileEntityContainerInterface otherContainerInterface = (TileEntityContainerInterface) MUtil.getOtherChest(source.getWorld(), (TileEntityChest) container);
						if (otherContainerInterface != null) {
							containerInterface.setIsCommunityContainer(false);
							otherContainerInterface.setIsCommunityContainer(false);
							FeedbackHandler.destructive(context, "Untrusted Community from this Double Chest!");
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandler.destructive(context, "Untrusted Community from this Chest!");
					} else if (container instanceof TileEntityFurnaceBlastFurnace) {
						FeedbackHandler.destructive(context, "Untrusted Community from this Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandler.destructive(context, "Untrusted Community from this Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandler.destructive(context, "Untrusted Community from this Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandler.destructive(context, "Untrusted Community from this Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandler.destructive(context, "Untrusted Community from this Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandler.destructive(context, "Untrusted Community from this Basket!");
					}
					containerInterface.setIsCommunityContainer(false);
				} else {
					FeedbackHandler.error(context, "Failed to Untrust Community from Container!");
					FeedbackHandler.error(context, "(Container not Locked)");
				}
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandler.error(context, "Failed to Untrust Community from Container!");
		FeedbackHandler.error(context, "(Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_bypass(CommandContext<CommandSource> context){
		CommandSource source = context.getSource();
		Player sender = source.getSender();
		boolean value = context.getArgument("value", Boolean.class);

		if(Data.Users.getOrCreate(sender.uuid).lockBypass == value){
			FeedbackHandler.error(context, "Failed to set Lock Bypass to %" + value + "% (Already %" + value + "%)");
			return Command.SINGLE_SUCCESS;
		}

		Data.Users.getOrCreate(sender.uuid).lockBypass = value;
		FeedbackHandler.success(context, "Lock Bypass set to %" + value);
		return Command.SINGLE_SUCCESS;
	}
}
