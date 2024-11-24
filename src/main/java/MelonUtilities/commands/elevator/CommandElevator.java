package MelonUtilities.commands.elevator;

import MelonUtilities.commands.lock.CommandLogicLock;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;

public class CommandElevator implements CommandManager.CommandRegistry{

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> elevatorAllowObstructions(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("allowobstructions").requires(CommandSource::hasAdmin)
			.executes(context ->
				{
					Player sender = context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicLock.lock_bypass(sender);
				}
			)
		);
		return builder;
	}

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> builder = LiteralArgumentBuilder.<CommandSource>literal("elevator");

		elevatorAllowObstructions(builder);

		dispatcher.register(builder);
	}
}
