package MelonUtilities.commands.utility;

import MelonUtilities.commands.role.CommandRole;
import MelonUtilities.config.Data;
import MelonUtilities.utility.feedback.FeedbackHandler;
import com.mojang.brigadier.Command;
import net.minecraft.core.entity.player.Player;

import static net.minecraft.server.player.PlayerListBox.updateList;

public class CommandLogicMelonUtilities {

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

	public static int melonutilities_reload(Player sender){
		FeedbackHandler.success(sender, "Reloading MelonUtilities...");

		FeedbackHandler.destructive(sender, "Reloading Player Data...");
		Data.Users.reload();
		FeedbackHandler.success(sender, "Reloaded " + Data.Users.userDataHashMap.size() + " Player(s)!");

		//TODO FeedbackHandler.destructive(source, "Building Helper Syntax...");
		//TODO HelperCommand.buildHelperSyntax();
		//TODO FeedbackHandler.success(source, "Helper Syntax Built!");

		FeedbackHandler.destructive(sender, "Reloading Kit Data...");
		Data.Kits.reload();
		FeedbackHandler.success(sender, "Reloaded " + Data.Kits.kitDataHashMap.size() + " Kit(s)!");

		//TODO FeedbackHandler.destructive(source, "Building Kit Syntax...");
		//TODO KitCommand.buildKitSyntax();
		//TODO FeedbackHandler.success(source, "Kit Syntax Built!");

		FeedbackHandler.destructive(sender, "Reloading Role Data...");
		Data.Roles.reload();
		FeedbackHandler.success(sender, "Reloaded " + Data.Roles.roleDataHashMap.size() + " Role(s)!");

		FeedbackHandler.destructive(sender, "Building Role Syntax...");
		CommandRole.buildSyntax();
		FeedbackHandler.success(sender, "Role Syntax Built!");

		//TODO FeedbackHandler.destructive(source, "Building Rollback Syntax...");
		//TODO RollbackCommand.buildSyntax();
		//TODO FeedbackHandler.success(source, "Rollback Syntax Built!");

		FeedbackHandler.destructive(sender, "Reloading General Configs...");
		Data.MainConfig.reload();
		FeedbackHandler.success(sender, "Reloaded Configs!");

		FeedbackHandler.destructive(sender, "Updating Player List...");
		updateList();
		FeedbackHandler.success(sender, "Updated List!");
		return Command.SINGLE_SUCCESS;
	}
}
