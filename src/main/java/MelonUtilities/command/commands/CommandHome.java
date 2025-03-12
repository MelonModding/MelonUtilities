package MelonUtilities.command.commands;

import MelonUtilities.command.arguments.ArgumentTypeHome;
import MelonUtilities.command.commandlogic.CommandLogicHome;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.data.Home;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeString;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.server.entity.player.PlayerServer;

public class CommandHome implements CommandManager.CommandRegistry {
	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> homeTP(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("tp")
			.then(ArgumentBuilderRequired.<CommandSource, String>argument("home", ArgumentTypeHome.home())
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

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> homeDelete(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("delete")
			.then(ArgumentBuilderRequired.<CommandSource, String>argument("home", ArgumentTypeHome.home())
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

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> homeCreate(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("create")
			.then(ArgumentBuilderRequired.<CommandSource, String>argument("name", ArgumentTypeString.string())
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

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> homeRename(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("rename")
			.then(ArgumentBuilderRequired.<CommandSource, String>argument("home", ArgumentTypeHome.home())
				.then(ArgumentBuilderRequired.<CommandSource, String>argument("name", ArgumentTypeString.string())
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

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> homeList(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("list")
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
		ArgumentBuilderLiteral<CommandSource> builder = ArgumentBuilderLiteral.<CommandSource>literal("home");

		homeTP(builder);
		homeDelete(builder);
		homeRename(builder);
		homeCreate(builder);
		homeList(builder);

		dispatcher.register(builder);
	}

}
