package MelonUtilities.utility.feedback;

import MelonUtilities.utility.MUtil;
import net.minecraft.core.net.command.TextFormatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FeedbackArg {

	public static final String defaultBorderOpener = "[";
	public static final String defaultBorderCloser = "]";
    public static final String defaultArgColor = TextFormatting.LIGHT_GRAY.toString();
    public static final String defaultBorderColor = TextFormatting.GRAY.toString();

	private @Nullable String borderOpener;
	private @Nullable String borderCloser;
	private @NotNull String borderColor;

	private final @NotNull List<String> arguments = new ArrayList<>();
    private @NotNull String argColor;
    private @Nullable String argSpecial;

	//Constructors

	public FeedbackArg(@Nullable Object argument){
		this.borderOpener = defaultBorderOpener;
		this.borderCloser = defaultBorderCloser;
		this.borderColor = defaultBorderColor;

		this.arguments.add(String.valueOf(argument));
		this.argColor = defaultArgColor;
		this.argSpecial = null;
	}

	public FeedbackArg(@Nullable Object... arguments){
		this.borderOpener = defaultBorderOpener;
		this.borderCloser = defaultBorderCloser;
		this.borderColor = defaultBorderColor;

		for(Object arg : arguments){
			this.arguments.add(String.valueOf(arg));
		}
		this.argColor = defaultArgColor;
		this.argSpecial = null;
	}

	//Setters

		//Border

		public FeedbackArg caretBorder(){
			this.borderOpener = "<";
			this.borderCloser = ">";
			return this;
		}

		public FeedbackArg curlyBorder(){
			this.borderOpener = "{";
			this.borderCloser = "}";
			return this;
		}

		public FeedbackArg bracketBorder(){
			this.borderOpener = "[";
			this.borderCloser = "]";
			return this;
		}

		public FeedbackArg parenthesesBorder(){
			this.borderOpener = "(";
			this.borderCloser = ")";
			return this;
		}

		public FeedbackArg noBorder(){
			this.borderOpener = null;
			this.borderCloser = null;
			return this;
		}

		public FeedbackArg borderOpener(String opener){
			this.borderOpener = opener;
			return this;
		}

		public FeedbackArg borderCloser(String closer){
			this.borderCloser = closer;
			return this;
		}

		public FeedbackArg borderColor(TextFormatting color){
			this.borderColor = color.toString();
			return this;
		}

		public FeedbackArg borderColor(String hex){
			this.borderColor = MUtil.formatHexString(hex);
			return this;
		}

		//Arg

		public FeedbackArg argColor(TextFormatting color){
			this.argColor = color.toString();
			return this;
		}

		public FeedbackArg argColor(String hex){
			this.argColor = MUtil.formatHexString(hex);
			return this;
		}

		public FeedbackArg argSpecial(TextFormatting special){
			this.argSpecial = special.toString();
			return this;
		}

	//Getters

    public @NotNull String getFirstArg() {
        return arguments.get(0);
    }

	public @NotNull String getAllArgs() {
		StringBuilder allArgs = new StringBuilder();
		for (int i = 0; i < arguments.size(); i++) {
			allArgs.append(arguments.get(i));
			if(i < arguments.size() - 1){
				allArgs.append(", ");
			}
		}
		return allArgs.toString();
	}

    public @NotNull List<String> getArgs(){
        return arguments;
    }

    public @NotNull String getArgColor() {
        return argColor;
    }

    public @Nullable String getArgSpecial() {
		if(argSpecial == null){
			return "";
		}
		return argSpecial;
    }

	public @NotNull String getBorderColor() {
		return borderColor;
	}

	public @Nullable String getBorderOpener() {
		if(borderOpener == null){
			return "";
		}
		return borderOpener;
	}

	public @Nullable String getBorderCloser() {
		if(borderCloser == null){
			return "";
		}
		return borderCloser;
	}

}
