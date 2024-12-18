package MelonUtilities.command.commandlogics;

import MelonUtilities.command.commands.CommandRole;
import MelonUtilities.config.Data;
import MelonUtilities.utility.feedback.FeedbackArg;
import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import com.mojang.brigadier.Command;
import net.minecraft.server.entity.player.PlayerServer;

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

	public static int melonutilities_reload(PlayerServer sender){
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Reloading MelonUtilities...");

		FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Reloading Player Data...");
		Data.Users.reload();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Reloaded %s Player(s)!", new FeedbackArg(Data.Users.userDataHashMap.size()));

		//TODO FeedbackHandler.sendFeedback(FeedbackType.destructive, source, "Building Helper Syntax...");
		//TODO HelperCommand.buildHelperSyntax();
		//TODO FeedbackHandler.sendFeedback(FeedbackType.success, source, "Helper Syntax Built!");

		FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Reloading Kit Data...");
		Data.Kits.reload();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Reloaded %s Kit(s)!", new FeedbackArg(Data.Kits.kitDataHashMap.size()));

		//TODO FeedbackHandler.sendFeedback(FeedbackType.destructive, source, "Building Kit Syntax...");
		//TODO KitCommand.buildKitSyntax();
		//TODO FeedbackHandler.sendFeedback(FeedbackType.success, source, "Kit Syntax Built!");

		FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Reloading Role Data...");
		Data.Roles.reload();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Reloaded %s Role(s)!", new FeedbackArg(Data.Roles.roleDataHashMap.size()));

		FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Building Role Syntax...");
		CommandRole.buildSyntax();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Role Syntax Built!");

		//TODO FeedbackHandler.sendFeedback(FeedbackType.destructive, source, "Building Rollback Syntax...");
		//TODO RollbackCommand.buildSyntax();
		//TODO FeedbackHandler.sendFeedback(FeedbackType.success, source, "Rollback Syntax Built!");

		FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Reloading General Configs...");
		Data.MainConfig.reload();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Reloaded Configs!");

		FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Updating Player List...");
		updateList();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Updated List!");
		return Command.SINGLE_SUCCESS;
	}
}
