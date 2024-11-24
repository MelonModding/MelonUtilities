package MelonUtilities.commands.elevator;

import MelonUtilities.config.Data;
import MelonUtilities.utility.feedback.FeedbackHandler;
import com.mojang.brigadier.Command;
import net.minecraft.core.entity.player.Player;

import java.util.UUID;

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

	public static int elevator_allowobstructions(Player sender){
		UUID senderUUID = sender.uuid;

		if(Data.MainConfig.config.allowObstructions){
			Data.MainConfig.config.allowObstructions = false;
			Data.Users.save(senderUUID);
			FeedbackHandler.destructive(sender, "Lock Bypass Disabled");
		} else {
			Data.MainConfig.config.allowObstructions = true;
			Data.Users.save(senderUUID);
			FeedbackHandler.success(sender, "Lock Bypass Enabled!");
		}
		return Command.SINGLE_SUCCESS;
	}
}
