package MelonUtilities.command.commandlogics;

import MelonUtilities.config.Data;
import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import com.mojang.brigadier.Command;
import net.minecraft.server.entity.player.PlayerServer;

public class CommandLogicElevator {
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

	public static int elevator_allowobstructions(PlayerServer sender){
		if(Data.MainConfig.config.allowObstructions){
			Data.MainConfig.config.allowObstructions = false;
			Data.MainConfig.save();
			FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Allow Obstructions Disabled");
		} else {
			Data.MainConfig.config.allowObstructions = true;
			Data.MainConfig.save();
			FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Allow Obstructions Enabled!");
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int elevator_cooldown(PlayerServer sender, int cooldownValue) {
		Data.MainConfig.config.elevatorCooldown = cooldownValue;
		Data.MainConfig.save();
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Elevator Cooldown Set to %" + cooldownValue);
		return Command.SINGLE_SUCCESS;
	}
}
