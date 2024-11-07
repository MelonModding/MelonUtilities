package MelonUtilities.commands.role;

import MelonUtilities.command_arguments.RoleIDArgumentType;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;

public class RoleCommand implements CommandManager.CommandRegistry{

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleCreate(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(RequiredArgumentBuilder.<CommandSource, String>argument("roleid", RoleIDArgumentType.roleID())
			.executes(c -> {

				return Command.SINGLE_SUCCESS;
			})
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleDelete(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {


	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleEdit(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {


	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleGrant(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {


	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleRevoke(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {


	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleSet(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {


	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleList(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {


	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleReload(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {


	}

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> command = dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("role").requires(CommandSource::hasAdmin);

		roleCreate(command);

		dispatcher.register(command);
	}
}
