package MelonUtilities.command.commands;

import MelonUtilities.command.arguments.ArgumentTypeColor;
import MelonUtilities.command.arguments.ArgumentTypeRole;
import MelonUtilities.command.commandlogic.CommandLogicRole;
import MelonUtilities.config.datatypes.data.Role;
import MelonUtilities.utility.MUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeBool;
import com.mojang.brigadier.arguments.ArgumentTypeInteger;
import com.mojang.brigadier.arguments.ArgumentTypeString;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.arguments.ArgumentTypeEntity;
import net.minecraft.core.net.command.helpers.EntitySelector;
import net.minecraft.server.entity.player.PlayerServer;

@SuppressWarnings("UnusedReturnValue")
public class CommandRole implements CommandManager.CommandRegistry{

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> roleCreate(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("create")
			.then(ArgumentBuilderRequired.<CommandSource, String>argument("roleID", ArgumentTypeString.string())
				.then(ArgumentBuilderRequired.<CommandSource, Integer>argument("priorityvalue", ArgumentTypeInteger.integer(0, 99))
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

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> roleDelete(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("delete")
			.then(ArgumentBuilderRequired.<CommandSource, Role>argument("role", ArgumentTypeRole.role())
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

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> roleEdit(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("edit")
			.then(ArgumentBuilderRequired.<CommandSource, Role>argument("role", ArgumentTypeRole.role())
				.then(ArgumentBuilderLiteral.<CommandSource>literal("priority")
					.then(ArgumentBuilderRequired.<CommandSource, Integer>argument("priorityvalue", ArgumentTypeInteger.integer(0, 99))
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
				.then(ArgumentBuilderLiteral.<CommandSource>literal("display")
					.then(ArgumentBuilderLiteral.<CommandSource>literal("name")
						.then(ArgumentBuilderRequired.<CommandSource, String>argument("displayname", ArgumentTypeString.greedyString())
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
					.then(ArgumentBuilderLiteral.<CommandSource>literal("color")
						.then(ArgumentBuilderRequired.<CommandSource, String>argument("color", ArgumentTypeColor.color())
							.executes(context ->
								{
									PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									String color = context.getArgument("color", String.class);
									return CommandLogicRole.roleEditDisplayColorCOLOR(sender, role, color);
								}
							)
						)
						.then(ArgumentBuilderRequired.<CommandSource, String>argument("hex", ArgumentTypeString.string())
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
					.then(ArgumentBuilderLiteral.<CommandSource>literal("underline")
						.then(ArgumentBuilderRequired.<CommandSource, Boolean>argument("value", ArgumentTypeBool.bool())
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
					.then(ArgumentBuilderLiteral.<CommandSource>literal("bold")
						.then(ArgumentBuilderRequired.<CommandSource, Boolean>argument("value", ArgumentTypeBool.bool())
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
					.then(ArgumentBuilderLiteral.<CommandSource>literal("italics")
						.then(ArgumentBuilderRequired.<CommandSource, Boolean>argument("value", ArgumentTypeBool.bool())
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
					.then(ArgumentBuilderLiteral.<CommandSource>literal("border")
						.then(ArgumentBuilderLiteral.<CommandSource>literal("color")
							.then(ArgumentBuilderRequired.<CommandSource, String>argument("color", ArgumentTypeColor.color())
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										String color = context.getArgument("color", String.class);
										return CommandLogicRole.roleEditDisplayBorderColorCOLOR(sender, role, color);
									}
								)
							)
							.then(ArgumentBuilderRequired.<CommandSource, String>argument("hex", ArgumentTypeString.string())
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
						.then(ArgumentBuilderLiteral.<CommandSource>literal("style")
							.then(ArgumentBuilderLiteral.<CommandSource>literal("none")
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.roleEditDisplayBorderStyleNone(sender, role);
									}
								)
							)
							.then(ArgumentBuilderLiteral.<CommandSource>literal("bracket")
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.roleEditDisplayBorderStyleBracket(sender, role);
									}
								)
							)
							.then(ArgumentBuilderLiteral.<CommandSource>literal("curly")
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.roleEditDisplayBorderStyleCurly(sender, role);
									}
								)
							)
							.then(ArgumentBuilderLiteral.<CommandSource>literal("caret")
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.roleEditDisplayBorderStyleCaret(sender, role);
									}
								)
							)
							.then(ArgumentBuilderLiteral.<CommandSource>literal("custom")
								.then(ArgumentBuilderLiteral.<CommandSource>literal("prefix")
									.then(ArgumentBuilderRequired.<CommandSource, String>argument("customaffix", ArgumentTypeString.greedyString())
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
								.then(ArgumentBuilderLiteral.<CommandSource>literal("suffix")
									.then(ArgumentBuilderRequired.<CommandSource, String>argument("customaffix", ArgumentTypeString.greedyString())
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
				.then(ArgumentBuilderLiteral.<CommandSource>literal("username")
					.then(ArgumentBuilderLiteral.<CommandSource>literal("border")
						.then(ArgumentBuilderLiteral.<CommandSource>literal("color")
							.then(ArgumentBuilderRequired.<CommandSource, String>argument("color", ArgumentTypeColor.color())
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										String color = context.getArgument("color", String.class);
										return CommandLogicRole.roleEditUsernameBorderColorCOLOR(sender, role, color);
									}
								)
							)
							.then(ArgumentBuilderRequired.<CommandSource, String>argument("hex", ArgumentTypeString.string())
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
						.then(ArgumentBuilderLiteral.<CommandSource>literal("style")
							.then(ArgumentBuilderLiteral.<CommandSource>literal("none")
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.roleEditUsernameBorderStyleNone(sender, role);
									}
								)
							)
							.then(ArgumentBuilderLiteral.<CommandSource>literal("bracket")
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.roleEditUsernameBorderStyleBracket(sender, role);
									}
								)
							)
							.then(ArgumentBuilderLiteral.<CommandSource>literal("curly")
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.roleEditUsernameBorderStyleCurly(sender, role);
									}
								)
							)
							.then(ArgumentBuilderLiteral.<CommandSource>literal("caret")
								.executes(context ->
									{
										PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.roleEditUsernameBorderStyleCaret(sender, role);
									}
								)
							)
							.then(ArgumentBuilderLiteral.<CommandSource>literal("custom")
								.then(ArgumentBuilderLiteral.<CommandSource>literal("prefix")
									.then(ArgumentBuilderRequired.<CommandSource, String>argument("customaffix", ArgumentTypeString.greedyString())
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
								.then(ArgumentBuilderLiteral.<CommandSource>literal("suffix")
									.then(ArgumentBuilderRequired.<CommandSource, String>argument("customaffix", ArgumentTypeString.greedyString())
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
				.then(ArgumentBuilderLiteral.<CommandSource>literal("text")
					.then(ArgumentBuilderLiteral.<CommandSource>literal("color")
						.then(ArgumentBuilderRequired.<CommandSource, String>argument("color", ArgumentTypeColor.color())
							.executes(context ->
								{
									PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									String color = context.getArgument("color", String.class);
									return CommandLogicRole.roleEditTextColorCOLOR(sender, role, color);
								}
							)
						)
						.then(ArgumentBuilderRequired.<CommandSource, String>argument("hex", ArgumentTypeString.string())
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
					.then(ArgumentBuilderLiteral.<CommandSource>literal("underline")
						.then(ArgumentBuilderRequired.<CommandSource, Boolean>argument("value", ArgumentTypeBool.bool())
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
					.then(ArgumentBuilderLiteral.<CommandSource>literal("bold")
						.then(ArgumentBuilderRequired.<CommandSource, Boolean>argument("value", ArgumentTypeBool.bool())
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
					.then(ArgumentBuilderLiteral.<CommandSource>literal("italics")
						.then(ArgumentBuilderRequired.<CommandSource, Boolean>argument("value", ArgumentTypeBool.bool())
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

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> roleGrant(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("grant")
			.then(ArgumentBuilderRequired.<CommandSource, Role>argument("role", ArgumentTypeRole.role())
				.then(ArgumentBuilderRequired.<CommandSource, EntitySelector>argument("target", ArgumentTypeEntity.username())
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

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> roleRevoke(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("revoke")
			.then(ArgumentBuilderRequired.<CommandSource, Role>argument("role", ArgumentTypeRole.role())
				.then(ArgumentBuilderRequired.<CommandSource, EntitySelector>argument("target", ArgumentTypeEntity.username())
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

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> roleSet(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("set")
			.then(ArgumentBuilderLiteral.<CommandSource>literal("defaultrole")
				.then(ArgumentBuilderRequired.<CommandSource, Role>argument("role", ArgumentTypeRole.role())
					.executes(context ->
						{
							PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
							Role role = context.getArgument("role", Role.class);
							return CommandLogicRole.roleSetDefaultroleROLEID(sender, role);
						}
					)
				)
				.then(ArgumentBuilderLiteral.<CommandSource>literal("none")
					.executes(context ->
						{
							PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
							return CommandLogicRole.roleSetDefaultroleNone(sender);
						}
					)
				)
			)
			.then(ArgumentBuilderLiteral.<CommandSource>literal("displaymode")
				.then(ArgumentBuilderLiteral.<CommandSource>literal("single")
					.executes(context ->
						{
							PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
							return CommandLogicRole.roleSetDisplaymodeSingle(sender);
						}
					)
				)
				.then(ArgumentBuilderLiteral.<CommandSource>literal("multi")
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

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> roleList(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("list")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicRole.roleList(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> roleReload(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("reload")
			.executes(context ->
				{
					PlayerServer sender = (PlayerServer) context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicRole.roleReload(sender);
				}
			)
		);
		return builder;
	}

/*	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> role(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.executes(
			context -> CommandLogicRole.role((PlayerServer) context.getSource().getSender())
		);
		return builder;
	}*/

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		ArgumentBuilderLiteral<CommandSource> commandBuilder = ArgumentBuilderLiteral.<CommandSource>literal("role").requires(CommandSource::hasAdmin);

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
