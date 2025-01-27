package MelonUtilities.command.commands;

import MelonUtilities.command.arguments.ArgumentTypeHome;
import MelonUtilities.command.commandlogic.CommandLogicHome;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.data.Home;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.server.entity.player.PlayerServer;

public class CommandHome implements CommandManager.CommandRegistry {
	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> homeTP(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("tp")
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("home", ArgumentTypeHome.home())
				.executes(context ->
					{
						PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
						String targetHomeName = context.getArgument("home", String.class);
						for(Home home : Data.Users.getOrCreate(sender.uuid).homeData){
							if(targetHomeName.equals(home.name)){
								return CommandLogicHome.homeTP(sender, home);
							}
						}
						return 0;
					}
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> homeDelete(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("delete")
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("home", ArgumentTypeHome.home())
				.executes(context ->
					{
						PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
						String targetHomeName = context.getArgument("home", String.class);
						for(Home home : Data.Users.getOrCreate(sender.uuid).homeData){
							if(targetHomeName.equals(home.name)){
								return CommandLogicHome.homeDelete(sender, home);
							}
						}
						return 0;
					}
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> homeCreate(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("create")
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("name", StringArgumentType.string())
				.executes(context ->
					{
						PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
						String name = context.getArgument("name", String.class);
						return CommandLogicHome.homeCreate(sender, name);
					}
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> homeRename(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("rename")
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("home", ArgumentTypeHome.home())
				.then(RequiredArgumentBuilder.<CommandSource, String>argument("name", StringArgumentType.string())
					.executes(context ->
						{
							PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
							String targetHomeName = context.getArgument("home", String.class);
							String name = context.getArgument("name", String.class);
							for(Home home : Data.Users.getOrCreate(sender.uuid).homeData){
								if(targetHomeName.equals(home.name)){
									return CommandLogicHome.homeRename(sender, home, name);
								}
							}
							return 0;
						}
					)
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> homeList(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("list")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicHome.homeList(sender);
				}
			)
		);
		return builder;
	}

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> builder = LiteralArgumentBuilder.<CommandSource>literal("home");

		homeTP(builder);
		homeDelete(builder);
		homeRename(builder);
		homeCreate(builder);
		homeList(builder);

		dispatcher.register(builder);
	}

}
