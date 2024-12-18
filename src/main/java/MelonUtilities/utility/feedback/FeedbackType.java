package MelonUtilities.utility.feedback;

import net.minecraft.core.net.command.TextFormatting;
import org.jetbrains.annotations.NotNull;

public class FeedbackType {

	public static FeedbackType success = new FeedbackType("success", TextFormatting.LIME, "note.harp");
	public static FeedbackType error = new FeedbackType("error", TextFormatting.RED, "note.bd");
	public static FeedbackType destructive = new FeedbackType("destructive", TextFormatting.ORANGE, "note.snare");
	public static FeedbackType syntax = new FeedbackType("syntax", TextFormatting.LIGHT_GRAY);

	private final @NotNull String name;
	private final @NotNull TextFormatting color;
	private final @NotNull String soundPath;

	public FeedbackType(@NotNull String name, @NotNull TextFormatting color, @NotNull String soundPath){
		this.name = name;
		this.color = color;
		this.soundPath = soundPath;
	}

	public FeedbackType(@NotNull String name, @NotNull TextFormatting color){
		this.name = name;
		this.color = color;
		this.soundPath = "NO_SOUND_PATH";
	}

	public @NotNull String getName() {
		return name;
	}

	public @NotNull TextFormatting getColor() {
		return color;
	}

	public @NotNull String getSoundPath() {
		return soundPath;
	}
}
