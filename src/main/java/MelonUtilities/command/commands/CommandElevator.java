package MelonUtilities.command.commands;

import MelonUtilities.command.commandlogic.CommandLogicElevator;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.server.entity.player.PlayerServer;

public class CommandElevator implements CommandManager.CommandRegistry{

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> elevatorAllowObstructions(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("allowobstructions")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicElevator.elevatorAllowobstructions(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> elevatorCooldown(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("cooldown")
			.then(RequiredArgumentBuilder.<CommandSource, Integer>argument("cooldownvalue", IntegerArgumentType.integer(0, 256))
				.executes(context ->
					{
						PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
						int cooldownValue = context.getArgument("cooldownvalue", Integer.class);
						return CommandLogicElevator.elevatorCooldown(sender, cooldownValue);
					}
				)
			)
		);
		return builder;
	}

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> builder = LiteralArgumentBuilder.<CommandSource>literal("elevator").requires(CommandSource::hasAdmin);

		elevatorAllowObstructions(builder);
		elevatorCooldown(builder);

		dispatcher.register(builder);
	}
}
