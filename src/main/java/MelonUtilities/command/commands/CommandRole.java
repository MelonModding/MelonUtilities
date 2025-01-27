package MelonUtilities.command.commands;

import MelonUtilities.command.arguments.ArgumentTypeColor;
import MelonUtilities.command.arguments.ArgumentTypeRole;
import MelonUtilities.command.commandlogic.CommandLogicRole;
import MelonUtilities.config.datatypes.data.Role;
import MelonUtilities.utility.MUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.arguments.ArgumentTypeEntity;
import net.minecraft.core.net.command.helpers.EntitySelector;
import net.minecraft.server.entity.player.PlayerServer;

@SuppressWarnings("UnusedReturnValue")
public class CommandRole implements CommandManager.CommandRegistry{

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleCreate(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("create")
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("roleID", StringArgumentType.string())
				.then(RequiredArgumentBuilder.<CommandSource, Integer>argument("priorityvalue", IntegerArgumentType.integer(0, 99))
					.executes(context ->
						{
							PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
							String roleID = context.getArgument("roleID", String.class);
							int rolePriority = context.getArgument("priorityvalue", Integer.class);
							return CommandLogicRole.roleCreate(sender, roleID, rolePriority);
						}
					)
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleDelete(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("delete")
			.then(RequiredArgumentBuilder.<CommandSource, Role>argument("role", ArgumentTypeRole.role())
				.executes(context ->
					{
						PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
						Role role = context.getArgument("role", Role.class);
						return CommandLogicRole.roleDelete(sender, role);
					}
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleEdit(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("edit")
			.then(RequiredArgumentBuilder.<CommandSource, Role>argument("role", ArgumentTypeRole.role())
				.then(LiteralArgumentBuilder.<CommandSource>literal("priority")
					.then(RequiredArgumentBuilder.<CommandSource, Integer>argument("priorityvalue", IntegerArgumentType.integer(0, 99))
						.executes(context ->
							{
								PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
								Role role = context.getArgument("role", Role.class);
								int priorityValue = context.getArgument("priorityvalue", Integer.class);
								return CommandLogicRole.roleEditPriority(sender, role, priorityValue);
							}
						)
					)
				)
				.then(LiteralArgumentBuilder.<CommandSource>literal("display")
					.then(LiteralArgumentBuilder.<CommandSource>literal("name")
						.then(RequiredArgumentBuilder.<CommandSource, String>argument("displayname", StringArgumentType.greedyString())
							.executes(context ->
								{
									PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									String displayName = context.getArgument("displayName", String.class);
									return CommandLogicRole.roleEditDisplayName(sender, role, displayName);
								}
							)
						)
					)
					.then(LiteralArgumentBuilder.<CommandSource>literal("color")
						.then(RequiredArgumentBuilder.<CommandSource, String>argument("color", ArgumentTypeColor.color())
							.executes(context ->
								{
									PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									String color = context.getArgument("color", String.class);
									return CommandLogicRole.roleEditDisplayColorCOLOR(sender, role, color);
								}
							)
						)
						.then(RequiredArgumentBuilder.<CommandSource, String>argument("hex", StringArgumentType.string())
							.executes(context ->
								{
									PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));
									return CommandLogicRole.roleEditDisplayColorHEX(sender, role, hex);
								}
							)
						)
					)
					.then(LiteralArgumentBuilder.<CommandSource>literal("underline")
						.then(RequiredArgumentBuilder.<CommandSource, Boolean>argument("value", BoolArgumentType.bool())
							.executes(context ->
								{
									PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									boolean value = context.getArgument("value", Boolean.class);
									return CommandLogicRole.roleEditDisplayUnderline(sender, role, value);
								}
							)
						)
					)
					.then(LiteralArgumentBuilder.<CommandSource>literal("bold")
						.then(RequiredArgumentBuilder.<CommandSource, Boolean>argument("value", BoolArgumentType.bool())
							.executes(context ->
								{
									PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									boolean value = context.getArgument("value", Boolean.class);
									return CommandLogicRole.roleEditDisplayBold(sender, role, value);
								}
							)
						)
					)
					.then(LiteralArgumentBuilder.<CommandSource>literal("italics")
						.then(RequiredArgumentBuilder.<CommandSource, Boolean>argument("value", BoolArgumentType.bool())
							.executes(context ->
								{
									PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									boolean value = context.getArgument("value", Boolean.class);
									return CommandLogicRole.roleEditDisplayItalics(sender, role, value);
								}
							)
						)
					)
					.then(LiteralArgumentBuilder.<CommandSource>literal("border")
						.then(LiteralArgumentBuilder.<CommandSource>literal("color")
							.then(RequiredArgumentBuilder.<CommandSource, String>argument("color", ArgumentTypeColor.color())
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										String color = context.getArgument("color", String.class);
										return CommandLogicRole.roleEditDisplayBorderColorCOLOR(sender, role, color);
									}
								)
							)
							.then(RequiredArgumentBuilder.<CommandSource, String>argument("hex", StringArgumentType.string())
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));
										return CommandLogicRole.roleEditDisplayBorderColorHEX(sender, role, hex);
									}
								)
							)
						)
						.then(LiteralArgumentBuilder.<CommandSource>literal("style")
							.then(LiteralArgumentBuilder.<CommandSource>literal("none")
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.roleEditDisplayBorderStyleNone(sender, role);
									}
								)
							)
							.then(LiteralArgumentBuilder.<CommandSource>literal("bracket")
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.roleEditDisplayBorderStyleBracket(sender, role);
									}
								)
							)
							.then(LiteralArgumentBuilder.<CommandSource>literal("curly")
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.roleEditDisplayBorderStyleCurly(sender, role);
									}
								)
							)
							.then(LiteralArgumentBuilder.<CommandSource>literal("caret")
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.roleEditDisplayBorderStyleCaret(sender, role);
									}
								)
							)
							.then(LiteralArgumentBuilder.<CommandSource>literal("custom")
								.then(LiteralArgumentBuilder.<CommandSource>literal("prefix")
									.then(RequiredArgumentBuilder.<CommandSource, String>argument("customaffix", StringArgumentType.greedyString())
										.executes(context ->
											{
												PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
												Role role = context.getArgument("role", Role.class);
												String customAffix = context.getArgument("customaffix", String.class);
												return CommandLogicRole.roleEditDisplayBorderStyleCustomPrefix(sender, role, customAffix);
											}
										)
									)
								)
								.then(LiteralArgumentBuilder.<CommandSource>literal("suffix")
									.then(RequiredArgumentBuilder.<CommandSource, String>argument("customaffix", StringArgumentType.greedyString())
										.executes(context ->
											{
												PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
												Role role = context.getArgument("role", Role.class);
												String customAffix = context.getArgument("customaffix", String.class);
												return CommandLogicRole.roleEditDisplayBorderStyleCustomSuffix(sender, role, customAffix);
											}
										)
									)
								)
							)
						)
					)
				)
				.then(LiteralArgumentBuilder.<CommandSource>literal("username")
					.then(LiteralArgumentBuilder.<CommandSource>literal("border")
						.then(LiteralArgumentBuilder.<CommandSource>literal("color")
							.then(RequiredArgumentBuilder.<CommandSource, String>argument("color", ArgumentTypeColor.color())
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										String color = context.getArgument("color", String.class);
										return CommandLogicRole.roleEditUsernameBorderColorCOLOR(sender, role, color);
									}
								)
							)
							.then(RequiredArgumentBuilder.<CommandSource, String>argument("hex", StringArgumentType.string())
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));
										return CommandLogicRole.roleEditUsernameBorderColorHEX(sender, role, hex);
									}
								)
							)
						)
						.then(LiteralArgumentBuilder.<CommandSource>literal("style")
							.then(LiteralArgumentBuilder.<CommandSource>literal("none")
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.roleEditUsernameBorderStyleNone(sender, role);
									}
								)
							)
							.then(LiteralArgumentBuilder.<CommandSource>literal("bracket")
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.roleEditUsernameBorderStyleBracket(sender, role);
									}
								)
							)
							.then(LiteralArgumentBuilder.<CommandSource>literal("curly")
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.roleEditUsernameBorderStyleCurly(sender, role);
									}
								)
							)
							.then(LiteralArgumentBuilder.<CommandSource>literal("caret")
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.roleEditUsernameBorderStyleCaret(sender, role);
									}
								)
							)
							.then(LiteralArgumentBuilder.<CommandSource>literal("custom")
								.then(LiteralArgumentBuilder.<CommandSource>literal("prefix")
									.then(RequiredArgumentBuilder.<CommandSource, String>argument("customaffix", StringArgumentType.greedyString())
										.executes(context ->
											{
												PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
												Role role = context.getArgument("role", Role.class);
												String customAffix = context.getArgument("customaffix", String.class);
												return CommandLogicRole.roleEditUsernameBorderStyleCustomPrefix(sender, role, customAffix);
											}
										)
									)
								)
								.then(LiteralArgumentBuilder.<CommandSource>literal("suffix")
									.then(RequiredArgumentBuilder.<CommandSource, String>argument("customaffix", StringArgumentType.greedyString())
										.executes(context ->
											{
												PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
												Role role = context.getArgument("role", Role.class);
												String customAffix = context.getArgument("customaffix", String.class);
												return CommandLogicRole.roleEditUsernameBorderStyleCustomSuffix(sender, role, customAffix);
											}
										)
									)
								)
							)
						)
					)
				)
				.then(LiteralArgumentBuilder.<CommandSource>literal("text")
					.then(LiteralArgumentBuilder.<CommandSource>literal("color")
						.then(RequiredArgumentBuilder.<CommandSource, String>argument("color", ArgumentTypeColor.color())
							.executes(context ->
								{
									PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									String color = context.getArgument("color", String.class);
									return CommandLogicRole.roleEditTextColorCOLOR(sender, role, color);
								}
							)
						)
						.then(RequiredArgumentBuilder.<CommandSource, String>argument("hex", StringArgumentType.string())
							.executes(context ->
								{
									PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));
									return CommandLogicRole.roleEditTextColorHEX(sender, role, hex);
								}
							)
						)
					)
					.then(LiteralArgumentBuilder.<CommandSource>literal("underline")
						.then(RequiredArgumentBuilder.<CommandSource, Boolean>argument("value", BoolArgumentType.bool())
							.executes(context ->
								{
									PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									boolean value = context.getArgument("value", Boolean.class);
									return CommandLogicRole.roleEditTextUnderline(sender, role, value);
								}
							)
						)
					)
					.then(LiteralArgumentBuilder.<CommandSource>literal("bold")
						.then(RequiredArgumentBuilder.<CommandSource, Boolean>argument("value", BoolArgumentType.bool())
							.executes(context ->
								{
									PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									boolean value = context.getArgument("value", Boolean.class);
									return CommandLogicRole.roleEditTextBold(sender, role, value);
								}
							)
						)
					)
					.then(LiteralArgumentBuilder.<CommandSource>literal("italics")
						.then(RequiredArgumentBuilder.<CommandSource, Boolean>argument("value", BoolArgumentType.bool())
							.executes(context ->
								{
									PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									boolean value = context.getArgument("value", Boolean.class);
									return CommandLogicRole.roleEditTextItalics(sender, role, value);
								}
							)
						)
					)
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleGrant(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("grant")
			.then(RequiredArgumentBuilder.<CommandSource, Role>argument("role", ArgumentTypeRole.role())
				.then(RequiredArgumentBuilder.<CommandSource, EntitySelector>argument("target", ArgumentTypeEntity.player())
					.executes(
						context ->
						{
							Role role = context.getArgument("role", Role.class);
							Player target = (Player)context.getArgument("target", EntitySelector.class).get(context.getSource()).get(0);
							PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
							return CommandLogicRole.roleGrant(sender, target, role);
						}
					)
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleRevoke(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("revoke")
			.then(RequiredArgumentBuilder.<CommandSource, Role>argument("role", ArgumentTypeRole.role())
				.then(RequiredArgumentBuilder.<CommandSource, EntitySelector>argument("target", ArgumentTypeEntity.player())
					.executes(context ->
						{
							Role role = context.getArgument("role", Role.class);
							Player target = (Player)context.getArgument("target", EntitySelector.class).get(context.getSource()).get(0);
							PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
							return CommandLogicRole.roleRevoke(sender, target, role);
						}
					)
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleSet(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("set")
			.then(LiteralArgumentBuilder.<CommandSource>literal("defaultrole")
				.then(RequiredArgumentBuilder.<CommandSource, Role>argument("role", ArgumentTypeRole.role())
					.executes(context ->
						{
							PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
							Role role = context.getArgument("role", Role.class);
							return CommandLogicRole.roleSetDefaultroleROLEID(sender, role);
						}
					)
				)
				.then(LiteralArgumentBuilder.<CommandSource>literal("none")
					.executes(context ->
						{
							PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
							return CommandLogicRole.roleSetDefaultroleNone(sender);
						}
					)
				)
			)
			.then(LiteralArgumentBuilder.<CommandSource>literal("displaymode")
				.then(LiteralArgumentBuilder.<CommandSource>literal("single")
					.executes(context ->
						{
							PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
							return CommandLogicRole.roleSetDisplaymodeSingle(sender);
						}
					)
				)
				.then(LiteralArgumentBuilder.<CommandSource>literal("multi")
					.executes(context ->
						{
							PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
							return CommandLogicRole.roleSetDisplaymodeMulti(sender);
						}
					)
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleList(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("list")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicRole.roleList(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleReload(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("reload")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicRole.roleReload(sender);
				}
			)
		);
		return builder;
	}

/*	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> role(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.executes(
			context -> CommandLogicRole.role((PlayerServer) context.getSource().getSender())
		);
		return builder;
	}*/

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> commandBuilder = LiteralArgumentBuilder.<CommandSource>literal("role").requires(CommandSource::hasAdmin);

		//role(commandBuilder);
		roleCreate(commandBuilder);
		roleDelete(commandBuilder);
		roleGrant(commandBuilder);
		roleRevoke(commandBuilder);
		roleEdit(commandBuilder);
		roleList(commandBuilder);
		roleReload(commandBuilder);
		roleSet(commandBuilder);

		dispatcher.register(commandBuilder);
	}
}
