package MelonUtilities.commands.lock;

import MelonUtilities.command_arguments.ArgumentTypeUsername;
import MelonUtilities.commands.ExecuteMethods;
import MelonUtilities.utility.syntax.SyntaxBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.command.arguments.ArgumentTypeEntity;
import net.minecraft.core.net.command.helpers.EntitySelector;

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
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("username", ArgumentTypeUsername.string())
				.executes(
					ExecuteMethods::lock_trust
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockTrustAll(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("trustall")
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("username", ArgumentTypeUsername.string())
				.executes(
					ExecuteMethods::lock_trustall
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockTrustCommunity(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("trustcommunity")
			.executes(
				ExecuteMethods::lock_trustcommunity
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockUntrust(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("untrust")
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("username", ArgumentTypeUsername.string())
				.executes(
					ExecuteMethods::lock_untrust
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockUntrustAll(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("untrustall")
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("username", ArgumentTypeUsername.string())
				.executes(
					ExecuteMethods::lock_untrustall
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lockUntrustCommunity(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("untrustcommunity")
			.executes(
				ExecuteMethods::lock_untrustcommunity
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

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> lock(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.executes(
			ExecuteMethods::lock
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

		dispatcher.register(builder);
	}
}
