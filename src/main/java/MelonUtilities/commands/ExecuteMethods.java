package MelonUtilities.commands;

import MelonUtilities.commands.role.RoleCommand;
import MelonUtilities.config.Data;
import MelonUtilities.config.DataBank;
import MelonUtilities.config.datatypes.ConfigData;
import MelonUtilities.config.datatypes.KitData;
import MelonUtilities.config.datatypes.PlayerData;
import MelonUtilities.config.datatypes.RoleData;
import MelonUtilities.utility.FeedbackHandler;
import MelonUtilities.utility.RoleBuilder;
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
	 Naming Scheme for methods in this class is [  (arg = command argument/literal) arg_arg_arg ...etc ]
	 Arguments in method name should match their registered name/literal in the ArgumentBuilder for their respective command
	 Use ctrl+f to search for the specific command you want to edit/view. Same as in-game except no '/' and replace spaces with '_'
	*/

	public static int melonutilities_reload(CommandContext<CommandSource> command){
		CommandSource source = command.getSource();
		FeedbackHandler.success(source, "Reloading MelonUtilities...");

		FeedbackHandler.destructive(source, "Reloading Player Data...");
		Data.playerData.loadAll(PlayerData.class);
		FeedbackHandler.success(source, "Reloaded " + Data.playerData.dataHashMap.size() + " Player(s)!");

		//TODO FeedbackHandler.destructive(source, "Building Helper Syntax...");
		//TODO HelperCommand.buildHelperSyntax();
		//TODO FeedbackHandler.success(source, "Helper Syntax Built!");

		FeedbackHandler.destructive(source, "Reloading Kit Data...");
		Data.kits.loadAll(KitData.class);
		FeedbackHandler.success(source, "Reloaded " + Data.kits.dataHashMap.size() + " Kit(s)!");

		//TODO FeedbackHandler.destructive(source, "Building Kit Syntax...");
		//TODO KitCommand.buildKitSyntax();
		//TODO FeedbackHandler.success(source, "Kit Syntax Built!");

		FeedbackHandler.destructive(source, "Reloading Role Data...");
		Data.roles.loadAll(RoleData.class);
		FeedbackHandler.success(source, "Reloaded " + Data.roles.dataHashMap.size() + " Role(s)!");

		FeedbackHandler.destructive(source, "Building Role Syntax...");
		RoleCommand.buildRoleSyntax();
		FeedbackHandler.success(source, "Role Syntax Built!");

		//TODO FeedbackHandler.destructive(source, "Building Rollback Syntax...");
		//TODO RollbackCommand.buildSyntax();
		//TODO FeedbackHandler.success(source, "Rollback Syntax Built!");

		FeedbackHandler.destructive(source, "Reloading General Configs...");
		Data.configs.loadAll(ConfigData.class);
		FeedbackHandler.success(source, "Reloaded Configs!");

		FeedbackHandler.destructive(source, "Updating Player List...");
		updateList();
		FeedbackHandler.success(source, "Updated List!");
		return Command.SINGLE_SUCCESS;
	}

	public static int role_reload(CommandContext<CommandSource> command) {
		CommandSource source = command.getSource();
		Data.roles.loadAll(RoleData.class);
		FeedbackHandler.success(source, "Reloaded " + Data.roles.dataHashMap.size() + " Role(s)!");
		RoleCommand.buildRoleSyntax();
		FeedbackHandler.success(source, "Built Role Syntax!");
		Data.configs.loadAll(ConfigData.class);
		FeedbackHandler.success(source, "Reloaded Config!");
		return Command.SINGLE_SUCCESS;
	}

	public static int role_list(CommandContext<CommandSource> command) {
		CommandSource source = command.getSource();
		if (Data.roles.dataHashMap.isEmpty()) {
			source.sendMessage(TextFormatting.LIGHT_GRAY + "< Roles: >");
			source.sendMessage(TextFormatting.LIGHT_GRAY + "  -No Roles Created-");
			return Command.SINGLE_SUCCESS;
		}

		source.sendMessage(TextFormatting.LIGHT_GRAY + "< Roles: >");

		for (String role : Data.roles.dataHashMap.keySet()) {
			source.sendMessage(TextFormatting.LIGHT_GRAY + "  > Role ID: " + TextFormatting.WHITE + TextFormatting.ITALIC + role + TextFormatting.LIGHT_GRAY + " - Priority: " + TextFormatting.WHITE + RoleCommand.getRoleDataFromRoleID(role).priority);
			source.sendMessage(TextFormatting.LIGHT_GRAY + "    > " + RoleBuilder.buildRoleDisplay(Data.roles.dataHashMap.get(role))
				+ RoleBuilder.buildRoleUsername(Data.roles.dataHashMap.get(role), source.getSender().getDisplayName())
				+ RoleBuilder.buildRoleTextFormat(Data.roles.dataHashMap.get(role)) + "text");
		}

		return Command.SINGLE_SUCCESS;
	}

	public static int role_revoke(CommandContext<CommandSource> command) throws CommandSyntaxException {
		CommandSource source = command.getSource();
		String role = command.getArgument("roleID", String.class);
		EntitySelector entitySelector = (EntitySelector)command.getArgument("target", EntitySelector.class);
		String target = ((Player)entitySelector.get(source).get(0)).username;

		if(!Data.roles.dataHashMap.containsKey(role)){
			FeedbackHandler.error(source, "Failed to Revoke Role (Role doesn't exist!)");
			RoleCommand.syntax.printLayerAndSubLayers("revoke", source);
			return Command.SINGLE_SUCCESS;
		}

		RoleData roleData = RoleCommand.getRoleDataFromRoleID(role);

		if (roleData.playersGrantedRole.contains(target)) {
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleDataFromRoleID(role).playersGrantedRole.remove(target);
			Data.roles.saveAll();
			FeedbackHandler.destructive(source, "Revoked Role: " + role + " from player: " + TextFormatting.LIGHT_GRAY + target);
			return Command.SINGLE_SUCCESS;
		} else if (roleData.playersGrantedRole.contains(source.getSender().username)){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleDataFromRoleID(role).playersGrantedRole.remove(source.getSender().username);
			Data.roles.saveAll();
			FeedbackHandler.destructive(source, "Revoked Role: " + role + " from player: " + TextFormatting.LIGHT_GRAY + source.getSender().username);
			return Command.SINGLE_SUCCESS;
		} else if (!roleData.playersGrantedRole.contains(source.getSender().username) || !roleData.playersGrantedRole.contains(target)) {
			FeedbackHandler.error(source, "Failed to Revoke Role (Player does not have Role!)");
			return Command.SINGLE_SUCCESS;
		}

		FeedbackHandler.error(source, "Failed to Revoke Role (Default Error) (Invalid Syntax?)");
		RoleCommand.syntax.printLayerAndSubLayers("revoke", source);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_grant(CommandContext<CommandSource> command) throws CommandSyntaxException {
		CommandSource source = command.getSource();
		String role = command.getArgument("roleID", String.class);
		EntitySelector entitySelector = (EntitySelector)command.getArgument("target", EntitySelector.class);
		String target = ((Player)entitySelector.get(source).get(0)).username;

		if(!Data.roles.dataHashMap.containsKey(role)){
			FeedbackHandler.error(source, "Failed to Grant Role (Role doesn't exist!)");
			RoleCommand.syntax.printLayerAndSubLayers("grant", source);
			return Command.SINGLE_SUCCESS;
		}

		RoleData roleData = RoleCommand.getRoleDataFromRoleID(role);

		if(!roleData.playersGrantedRole.contains(source.getSender().username)){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleDataFromRoleID(role).playersGrantedRole.add(source.getSender().username);
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Granted Role: " + role + " to player: " + TextFormatting.LIGHT_GRAY + source.getSender().username);
			return Command.SINGLE_SUCCESS;
		} else if (!roleData.playersGrantedRole.contains(target)){
			Data.roles.loadAll(RoleData.class);
			RoleCommand.getRoleDataFromRoleID(role).playersGrantedRole.add(target);
			Data.roles.saveAll();
			FeedbackHandler.success(source, "Granted Role: " + role + " to player: " + TextFormatting.LIGHT_GRAY + target);
			return Command.SINGLE_SUCCESS;
		} else if (roleData.playersGrantedRole.contains(source.getSender().username) || roleData.playersGrantedRole.contains(target)) {
			FeedbackHandler.error(source, "Failed to Grant Role (Player already has Role!)");
			return Command.SINGLE_SUCCESS;
		}


		FeedbackHandler.error(source, "Failed to Grant Role (Default Error) (Invalid Syntax?)");
		RoleCommand.syntax.printLayerAndSubLayers("grant", source);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_create(CommandContext<CommandSource> command) {
		CommandSource source = command.getSource();
		String roleID = command.getArgument("roleID", String.class);
		String rolePriority = command.getArgument("rolePriority", String.class);


		if (Data.roles.dataHashMap.containsKey(roleID)) {
			FeedbackHandler.error(source, "Failed to Create Role: " + roleID + " (Role Already Exists)");
			return Command.SINGLE_SUCCESS;
		}

		Data.roles.getOrCreate(roleID, RoleData.class);
		Data.roles.loadAll(RoleData.class);
		RoleCommand.getRoleDataFromRoleID(roleID).displayName = roleID;
		RoleCommand.getRoleDataFromRoleID(roleID).priority = Integer.parseInt(rolePriority);
		Data.roles.saveAll();

		FeedbackHandler.success(source, "Created Role: " + RoleCommand.getRoleDataFromRoleID(roleID).displayName + " with Priority: " + RoleCommand.getRoleDataFromRoleID(roleID).priority);
		return Command.SINGLE_SUCCESS;
	}

	public static int role_delete(CommandContext<CommandSource> command) {
		CommandSource source = command.getSource();
		String role = command.getArgument("roleID", String.class);

		switch (Data.roles.remove(role)) {
			case DataBank.NO_ERROR:
				FeedbackHandler.destructive(source, "Deleted Role: " + role);
				return Command.SINGLE_SUCCESS;
			case DataBank.ROLE_DOESNT_EXIST:
				FeedbackHandler.error(source, "Failed to Delete Role: " + role + " (Role Doesn't Exist)");
				RoleCommand.syntax.printLayerAndSubLayers("delete", source);
				return Command.SINGLE_SUCCESS;
			case DataBank.IO_ERROR:
				FeedbackHandler.error(source, "Failed to Delete Role: " + role + " (IO Error)");
				return Command.SINGLE_SUCCESS;
		}
		return Command.SINGLE_SUCCESS;
	}

}
