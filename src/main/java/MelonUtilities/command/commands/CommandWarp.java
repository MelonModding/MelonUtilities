package MelonUtilities.command.commands;

import MelonUtilities.command.arguments.ArgumentTypeWarp;
import MelonUtilities.command.commandlogic.CommandLogicWarp;
import MelonUtilities.config.datatypes.data.Warp;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.server.entity.player.PlayerServer;

@SuppressWarnings("UnusedReturnValue")
public class CommandWarp implements CommandManager.CommandRegistry{
	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> warpTP(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("tp")
			.then(RequiredArgumentBuilder.<CommandSource, Warp>argument("warp", ArgumentTypeWarp.warp())
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

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> warpDelete(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("delete").requires(CommandSource::hasAdmin)
			.then(RequiredArgumentBuilder.<CommandSource, Warp>argument("warp", ArgumentTypeWarp.warp())
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

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> warpCreate(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("create").requires(CommandSource::hasAdmin)
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("name", StringArgumentType.string())
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

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> warpRename(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("rename").requires(CommandSource::hasAdmin)
			.then(RequiredArgumentBuilder.<CommandSource, Warp>argument("warp", ArgumentTypeWarp.warp())
				.then(RequiredArgumentBuilder.<CommandSource, String>argument("name", StringArgumentType.string())
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

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> warpList(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("list")
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
		LiteralArgumentBuilder<CommandSource> builder = LiteralArgumentBuilder.<CommandSource>literal("warp");

		warpTP(builder);
		warpDelete(builder);
		warpRename(builder);
		warpCreate(builder);
		warpList(builder);

		dispatcher.register(builder);
	}

}
