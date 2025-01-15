package MelonUtilities.command.commands;

import MelonUtilities.command.arguments.ArgumentTypeUsername;
import MelonUtilities.command.commandlogics.CommandLogicLock;
import MelonUtilities.utility.syntax.SyntaxBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.entity.player.PlayerServer;

@SuppressWarnings("UnusedReturnValue")
public class CommandLock implements CommandManager.CommandRegistry{
	public static SyntaxBuilder syntax = new SyntaxBuilder();
	public static void buildSyntax(){
		syntax.clear();
		syntax.append("title",                                                    TextFormatting.LIGHT_GRAY + "< Command Syntax > ([] = optional, <> = variable, / = or)");
		syntax.append("lock", "title",                                     TextFormatting.LIGHT_GRAY + "  > /lock [<mode>]");
		syntax.append("lockOnBlockPlaced", "lock",                         TextFormatting.LIGHT_GRAY + "    > onBlockPlaced true/false");
		syntax.append("lockOnBlockPunched", "lock",                        TextFormatting.LIGHT_GRAY + "    > onBlockPunched true/false");
		syntax.append("lockTrust", "lock",                                 TextFormatting.LIGHT_GRAY + "    > trust <player>");
		syntax.append("lockTrustAll", "lock",                              TextFormatting.LIGHT_GRAY + "    > trustall <player>");
		syntax.append("lockTrustCommunity", "lock",                        TextFormatting.LIGHT_GRAY + "    > trustcommunity");
		syntax.append("lockUntrust", "lock",                               TextFormatting.LIGHT_GRAY + "    > untrust <player>");
		syntax.append("lockUntrustAll", "lock",                            TextFormatting.LIGHT_GRAY + "    > untrustall <player>");
		syntax.append("lockUntrustCommunity", "lock",                      TextFormatting.LIGHT_GRAY + "    > untrustcommunity");
		syntax.append("lockBypass", "lock", true,                      TextFormatting.LIGHT_GRAY + "    > bypass true/false");
	}




	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockOnBlockPlaced(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("onblockplaced")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicLock.lockOnBlockPlaced(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockOnBlockPunched(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("onblockpunched")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicLock.lockOnBlockPunched(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockTrust(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("trust")
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("username", ArgumentTypeUsername.string())
				.executes(context ->
					{
						PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
						String targetUsername = context.getArgument("username", String.class).toLowerCase();
						return CommandLogicLock.lockTrust(sender, targetUsername);
					}
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockTrustAll(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("trustall")
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("username", ArgumentTypeUsername.string())
				.executes(context ->
					{
						PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
						String targetUsername = context.getArgument("username", String.class).toLowerCase();
						return CommandLogicLock.lockTrustAll(sender, targetUsername);
					}
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockTrustCommunity(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("trustcommunity")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicLock.lockTrustCommunity(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockUntrust(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("untrust")
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("username", ArgumentTypeUsername.string())
				.executes(context ->
					{
						PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
						String targetUsername = context.getArgument("username", String.class).toLowerCase();
						return CommandLogicLock.lockUntrust(sender, targetUsername);
					}
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockUntrustAll(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("untrustall")
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("username", ArgumentTypeUsername.string())
				.executes(context ->
					{
						PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
						String targetUsername = context.getArgument("username", String.class).toLowerCase();
						return CommandLogicLock.lockUntrustAll(sender, targetUsername);
					}
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockUntrustCommunity(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("untrustcommunity")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicLock.lockUntrustCommunity(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockBypass(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("bypass").requires(CommandSource::hasAdmin)
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicLock.lockBypass(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockInfo(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("info")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicLock.lockInfo(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lock(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.executes(context ->
			{
				PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
				return CommandLogicLock.lock(sender);
			}
		);
		return builder;
	}


	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> builder = LiteralArgumentBuilder.<CommandSource>literal("lock");

		lock(builder);
		lockOnBlockPlaced(builder);
		lockOnBlockPunched(builder);
		lockTrust(builder);
		lockTrustAll(builder);
		lockTrustCommunity(builder);
		lockUntrust(builder);
		lockUntrustAll(builder);
		lockUntrustCommunity(builder);
		lockBypass(builder);
		lockInfo(builder);

		dispatcher.register(builder);
	}
}
