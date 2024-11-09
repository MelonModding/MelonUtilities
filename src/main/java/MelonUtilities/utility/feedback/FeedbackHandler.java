package MelonUtilities.utility.feedback;

import MelonUtilities.utility.MUtil;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandSource;
import org.jetbrains.annotations.NotNull;

public class FeedbackHandler {

	static FeedbackType success = new FeedbackType("success", "lime", "note.harp");
	static FeedbackType error = new FeedbackType("error", "red", "note.bd");
	static FeedbackType destructive = new FeedbackType("destructive", "orange", "note.snare");
	static FeedbackType syntax = new FeedbackType("syntax", "light_grey");

	private static final char variableStarter = '%';
	private static final char variableOpener = '[';
	private static final char variableCloser = ']';

	private static void info(@NotNull CommandSource source, @NotNull FeedbackType feedbackType, @NotNull String msg) {
		StringBuilder tempmsg = new StringBuilder();
		for (int i = 0; i < msg.length(); i++){
			char c = msg.charAt(i);
			if(c != variableStarter){
				tempmsg.append(c);
			} else{
				tempmsg.append(MUtil.SECTION_GRAY);
				tempmsg.append(variableOpener);
				tempmsg.append(MUtil.SECTION_LIGHT_GRAY);
				for(int j = i+1; j < msg.length(); j++){
					i = j;
					c = msg.charAt(j);
					if(Character.isSpaceChar(c)){
						tempmsg.append(MUtil.SECTION_GRAY);
						tempmsg.append(variableCloser);
						tempmsg.append(feedbackType.getColorSection());
						tempmsg.append(' ');
						break;
					} else {
						tempmsg.append(c);
						if(msg.length()-1 == j){
							tempmsg.append(MUtil.SECTION_GRAY);
							tempmsg.append(variableCloser);
						}
					}
				}
			}
		}

		msg = String.valueOf(tempmsg);

		source.sendMessage(feedbackType.getColorFormat() + msg);
		if(!feedbackType.getSoundPath().equals("NO_SOUND")){
			Player player = source.getSender();
			if (player == null) return;
			player.world.playSoundAtEntity(null, player, feedbackType.getSoundPath(), 1f, 1f);
		}
	}

	public static void success(@NotNull CommandSource source, @NotNull String msg) {
		info(source, success, msg);
	}

	public static void error(@NotNull CommandSource source, @NotNull String msg) {
		info(source, error, msg);
	}

	public static void destructive(@NotNull CommandSource source, @NotNull String msg) {
		info(source, destructive, msg);
	}

	public static void syntax(@NotNull CommandSource source, @NotNull String msg) {
		info(source, syntax, msg);
	}
}
