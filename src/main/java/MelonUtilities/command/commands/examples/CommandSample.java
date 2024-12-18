package MelonUtilities.command.commands.examples;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.core.net.command.CommandSource;

public class CommandSample {
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> commandBuilder = LiteralArgumentBuilder.<CommandSource>literal("command").requires(CommandSource::hasAdmin);

		subCommandOne(commandBuilder);

		dispatcher.register(commandBuilder);
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> subCommandOne(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("subCommand")
			.executes(c -> {
				return Command.SINGLE_SUCCESS;
			})
		);
		return builder;
	}

}
