package MelonUtilities.commands.command_arguments;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.data.Role;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ArgumentTypeRole implements ArgumentType<Role>{
	private static final List<String> EXAMPLES = Arrays.asList("owner", "noob", "supporter");

	public ArgumentTypeRole() {
	}

	public static ArgumentType<Role> role() {
		return new ArgumentTypeRole();
	}

	public Role parse(StringReader reader) throws CommandSyntaxException {
		final String string = reader.readString();

		for (Role role : roles()) {
			if (role.roleID.equalsIgnoreCase(string)) {
				return role;
			}
		}
		throw new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument(), () -> "Failed to find Role: " + string + " (Role Doesn't Exist)");
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		for(Role role : roles()){
			if (role.roleID.startsWith(builder.getRemaining())) {
				builder.suggest(role.roleID);
			}
		}

		return builder.buildFuture();
	}

	public Collection<Role> roles() {
		return new ArrayList<>(Data.Roles.roleDataHashMap.values());
	}

	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
