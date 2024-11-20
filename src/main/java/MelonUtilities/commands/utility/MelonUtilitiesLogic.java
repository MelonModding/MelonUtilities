package MelonUtilities.commands.utility;

import MelonUtilities.commands.role.CommandRole;
import MelonUtilities.config.Data;
import MelonUtilities.utility.feedback.FeedbackHandler;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.core.net.command.CommandSource;

import static net.minecraft.server.player.PlayerListBox.updateList;

public class MelonUtilitiesLogic {

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
}
