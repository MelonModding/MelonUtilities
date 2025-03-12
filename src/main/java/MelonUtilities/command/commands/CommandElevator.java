package MelonUtilities.command.commands;

import MelonUtilities.command.commandlogic.CommandLogicElevator;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeInteger;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.server.entity.player.PlayerServer;

public class CommandElevator implements CommandManager.CommandRegistry{

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> elevatorAllowObstructions(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("allowobstructions")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicElevator.elevatorAllowobstructions(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> elevatorCooldown(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("cooldown")
			.then(ArgumentBuilderRequired.<CommandSource, Integer>argument("cooldownvalue", ArgumentTypeInteger.integer(0, 256))
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
		ArgumentBuilderLiteral<CommandSource> builder = ArgumentBuilderLiteral.<CommandSource>literal("elevator").requires(CommandSource::hasAdmin);

		elevatorAllowObstructions(builder);
		elevatorCooldown(builder);

		dispatcher.register(builder);
	}
}
