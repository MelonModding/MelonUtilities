package MelonUtilities.command.commands;

import MelonUtilities.command.arguments.ArgumentTypeWarp;
import MelonUtilities.command.commandlogic.CommandLogicWarp;
import MelonUtilities.config.datatypes.data.Warp;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeString;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.server.entity.player.PlayerServer;

@SuppressWarnings("UnusedReturnValue")
public class CommandWarp implements CommandManager.CommandRegistry{
	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> warpTP(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("tp")
			.then(ArgumentBuilderRequired.<CommandSource, Warp>argument("warp", ArgumentTypeWarp.warp())
				.executes(context ->
					{
						PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
						Warp warp = context.getArgument("warp", Warp.class);
						return CommandLogicWarp.warpTP(sender, warp);
					}
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> warpDelete(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("delete").requires(CommandSource::hasAdmin)
			.then(ArgumentBuilderRequired.<CommandSource, Warp>argument("warp", ArgumentTypeWarp.warp())
				.executes(context ->
					{
						PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
						Warp warp = context.getArgument("warp", Warp.class);
						return CommandLogicWarp.warpDelete(sender, warp);
					}
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> warpCreate(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("create").requires(CommandSource::hasAdmin)
			.then(ArgumentBuilderRequired.<CommandSource, String>argument("name", ArgumentTypeString.string())
				.executes(context ->
					{
						PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
						String name = context.getArgument("name", String.class);
						return CommandLogicWarp.warpCreate(sender, name);
					}
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> warpRename(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("rename").requires(CommandSource::hasAdmin)
			.then(ArgumentBuilderRequired.<CommandSource, Warp>argument("warp", ArgumentTypeWarp.warp())
				.then(ArgumentBuilderRequired.<CommandSource, String>argument("name", ArgumentTypeString.string())
					.executes(context ->
						{
							PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
							Warp warp = context.getArgument("warp", Warp.class);
							String name = context.getArgument("name", String.class);
							return CommandLogicWarp.warpRename(sender, warp, name);
						}
					)
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> warpList(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("list")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicWarp.warpList(sender);
				}
			)
		);
		return builder;
	}

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		ArgumentBuilderLiteral<CommandSource> builder = ArgumentBuilderLiteral.<CommandSource>literal("warp");

		warpTP(builder);
		warpDelete(builder);
		warpRename(builder);
		warpCreate(builder);
		warpList(builder);

		dispatcher.register(builder);
	}

}
