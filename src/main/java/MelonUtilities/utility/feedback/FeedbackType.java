package MelonUtilities.utility.feedback;

import MelonUtilities.utility.MUtil;
import net.minecraft.core.net.command.TextFormatting;

public class FeedbackType {

	private final String type;
	private final String color;
	private final String soundPath;

	public FeedbackType(String type, String color, String soundPath){
		this.type = type;
		this.color = color;
		this.soundPath = soundPath;
	}

	public FeedbackType(String type, String color){
		this.type = type;
		this.color = color;
		this.soundPath = null;
	}

	public String getType() {
		return type;
	}

	public String getColorString() {
		return color;
	}

	public String getColorSection() {
		return TextFormatting.getColorFormatting(color).toString();
	}

	public TextFormatting getColorFormat() {
		return TextFormatting.getColorFormatting(color);
	}

	public String getSoundPath() {
		if(soundPath != null){
			return soundPath;
		}
		return "NO_SOUND";
	}
}
