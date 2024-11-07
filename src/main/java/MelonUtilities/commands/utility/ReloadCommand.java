package MelonUtilities.commands.utility;

import MelonUtilities.utility.FeedbackHandler;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;

public class ReloadCommand implements CommandManager.CommandRegistry{

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		CommandNode<CommandSource> command = dispatcher
		.register((LiteralArgumentBuilder.<CommandSource>literal("reload").requires(CommandSource::hasAdmin))
			.executes(
				c -> {
					c.getSource().getWorld().getCommandManager().init();
					FeedbackHandler.success(c.getSource(), "Reloaded Commands!");
					return Command.SINGLE_SUCCESS;
				}
			)
		);
	}
}
