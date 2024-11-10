package MelonUtilities.commands.utility;

import MelonUtilities.commands.ExecuteMethods;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;

public class CommandMelonUtilities implements CommandManager.CommandRegistry{
	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		CommandNode<CommandSource> command = dispatcher
			.register((LiteralArgumentBuilder.<CommandSource>literal("melonutilities").requires(CommandSource::hasAdmin))
				.then(LiteralArgumentBuilder.<CommandSource>literal("reload")
					.executes(
						ExecuteMethods::melonutilities_reload
					)
				)
			);
	}
}
