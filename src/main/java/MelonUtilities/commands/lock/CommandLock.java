package MelonUtilities.commands.lock;

import MelonUtilities.commands.ExecuteMethods;
import MelonUtilities.utility.syntax.SyntaxBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.command.arguments.ArgumentTypeEntity;
import net.minecraft.core.net.command.helpers.EntitySelector;

import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public class CommandLock implements CommandManager.CommandRegistry{
	public static SyntaxBuilder syntax = new SyntaxBuilder();
	public static void buildLockSyntax(){
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
			.then(RequiredArgumentBuilder.<CommandSource, Boolean>argument("value", BoolArgumentType.bool())
				.executes(
					ExecuteMethods::lock_onblockplaced
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockOnBlockPunched(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("onblockpunched")
			.then(RequiredArgumentBuilder.<CommandSource, Boolean>argument("value", BoolArgumentType.bool())
				.executes(
					ExecuteMethods::lock_onblockpunched
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockTrust(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("trust")
			.then(RequiredArgumentBuilder.<CommandSource, EntitySelector>argument("target", ArgumentTypeEntity.player())
				.executes(
					ExecuteMethods::lock_trust_TARGET
				)
			)
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("username", StringArgumentType.string())
				.executes(
					ExecuteMethods::lock_trust_USERNAME
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockTrustAll(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("trustall")
			.then(RequiredArgumentBuilder.<CommandSource, EntitySelector>argument("target", ArgumentTypeEntity.player())
				.executes(
					ExecuteMethods::lock_trustall_TARGET
				)
			)
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("username", StringArgumentType.string())
				.executes(
					ExecuteMethods::lock_trustall_USERNAME
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockTrustCommunity(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("trustcommunity")
			.then(RequiredArgumentBuilder.<CommandSource, EntitySelector>argument("target", ArgumentTypeEntity.player())
				.executes(
					ExecuteMethods::lock_trustcommunity_TARGET
				)
			)
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("username", StringArgumentType.string())
				.executes(
					ExecuteMethods::lock_trustcommunity_USERNAME
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockUntrust(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("untrust")
			.then(RequiredArgumentBuilder.<CommandSource, EntitySelector>argument("target", ArgumentTypeEntity.player())
				.executes(
					ExecuteMethods::lock_untrust_TARGET
				)
			)
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("username", StringArgumentType.string())
				.executes(
					ExecuteMethods::lock_untrust_USERNAME
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockUntrustAll(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("untrustall")
			.then(RequiredArgumentBuilder.<CommandSource, EntitySelector>argument("target", ArgumentTypeEntity.player())
				.executes(
					ExecuteMethods::lock_untrustall_TARGET
				)
			)
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("username", StringArgumentType.string())
				.executes(
					ExecuteMethods::lock_untrustall_USERNAME
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockUntrustCommunity(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("untrustcommunity")
			.then(RequiredArgumentBuilder.<CommandSource, EntitySelector>argument("target", ArgumentTypeEntity.player())
				.executes(
					ExecuteMethods::lock_untrustcommunity_TARGET
				)
			)
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("username", StringArgumentType.string())
				.executes(
					ExecuteMethods::lock_untrustcommunity_USERNAME
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockBypass(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("bypass").requires(CommandSource::hasAdmin)
			.then(RequiredArgumentBuilder.<CommandSource, Boolean>argument("value", BoolArgumentType.bool())
				.executes(
					ExecuteMethods::lock_bypass
				)
			)
		);
		return builder;
	}


	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> builder = LiteralArgumentBuilder.<CommandSource>literal("lock");

		lockOnBlockPlaced(builder);
		lockOnBlockPunched(builder);
		lockTrust(builder);
		lockTrustAll(builder);
		lockTrustCommunity(builder);
		lockUntrust(builder);
		lockUntrustAll(builder);
		lockUntrustCommunity(builder);
		lockBypass(builder);

		dispatcher.register(builder);
	}
}
