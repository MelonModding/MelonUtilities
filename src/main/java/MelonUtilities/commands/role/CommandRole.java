package MelonUtilities.commands.role;

import MelonUtilities.commands.command_arguments.ArgumentTypeColor;
import MelonUtilities.commands.command_arguments.ArgumentTypeRole;
import MelonUtilities.config.datatypes.data.Role;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.syntax.SyntaxBuilder;
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
import net.minecraft.core.net.command.TextFormatting;
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
							Player sender = context.getSource().getSender(); if(sender == null){return 0;}
							String roleID = context.getArgument("roleID", String.class);
							int rolePriority = context.getArgument("priorityvalue", Integer.class);
							return CommandLogicRole.role_create(sender, roleID, rolePriority);
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
						Player sender = context.getSource().getSender(); if(sender == null){return 0;}
						Role role = context.getArgument("role", Role.class);
						return CommandLogicRole.role_delete(sender, role);
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
								Player sender = context.getSource().getSender(); if(sender == null){return 0;}
								Role role = context.getArgument("role", Role.class);
								int priorityValue = context.getArgument("priorityvalue", Integer.class);
								return CommandLogicRole.role_edit_priority(sender, role, priorityValue);
							}
						)
					)
				)
				.then(LiteralArgumentBuilder.<CommandSource>literal("display")
					.then(LiteralArgumentBuilder.<CommandSource>literal("name")
						.then(RequiredArgumentBuilder.<CommandSource, String>argument("displayname", StringArgumentType.greedyString())
							.executes(context ->
								{
									Player sender = context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									String displayName = context.getArgument("displayName", String.class);
									return CommandLogicRole.role_edit_display_name(sender, role, displayName);
								}
							)
						)
					)
					.then(LiteralArgumentBuilder.<CommandSource>literal("color")
						.then(RequiredArgumentBuilder.<CommandSource, String>argument("color", ArgumentTypeColor.color())
							.executes(context ->
								{
									Player sender = context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									String color = context.getArgument("color", String.class);
									return CommandLogicRole.role_edit_display_color_COLOR(sender, role, color);
								}
							)
						)
						.then(RequiredArgumentBuilder.<CommandSource, String>argument("hex", StringArgumentType.string())
							.executes(context ->
								{
									Player sender = context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));
									return CommandLogicRole.role_edit_display_color_HEX(sender, role, hex);
								}
							)
						)
					)
					.then(LiteralArgumentBuilder.<CommandSource>literal("underline")
						.then(RequiredArgumentBuilder.<CommandSource, Boolean>argument("value", BoolArgumentType.bool())
							.executes(context ->
								{
									Player sender = context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									boolean value = context.getArgument("value", Boolean.class);
									return CommandLogicRole.role_edit_display_underline(sender, role, value);
								}
							)
						)
					)
					.then(LiteralArgumentBuilder.<CommandSource>literal("bold")
						.then(RequiredArgumentBuilder.<CommandSource, Boolean>argument("value", BoolArgumentType.bool())
							.executes(context ->
								{
									Player sender = context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									boolean value = context.getArgument("value", Boolean.class);
									return CommandLogicRole.role_edit_display_bold(sender, role, value);
								}
							)
						)
					)
					.then(LiteralArgumentBuilder.<CommandSource>literal("italics")
						.then(RequiredArgumentBuilder.<CommandSource, Boolean>argument("value", BoolArgumentType.bool())
							.executes(context ->
								{
									Player sender = context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									boolean value = context.getArgument("value", Boolean.class);
									return CommandLogicRole.role_edit_display_italics(sender, role, value);
								}
							)
						)
					)
					.then(LiteralArgumentBuilder.<CommandSource>literal("border")
						.then(LiteralArgumentBuilder.<CommandSource>literal("color")
							.then(RequiredArgumentBuilder.<CommandSource, String>argument("color", ArgumentTypeColor.color())
								.executes(context ->
									{
										Player sender = context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										String color = context.getArgument("color", String.class);
										return CommandLogicRole.role_edit_display_border_color_COLOR(sender, role, color);
									}
								)
							)
							.then(RequiredArgumentBuilder.<CommandSource, String>argument("hex", StringArgumentType.string())
								.executes(context ->
									{
										Player sender = context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));
										return CommandLogicRole.role_edit_display_border_color_HEX(sender, role, hex);
									}
								)
							)
						)
						.then(LiteralArgumentBuilder.<CommandSource>literal("style")
							.then(LiteralArgumentBuilder.<CommandSource>literal("none")
								.executes(context ->
									{
										Player sender = context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.role_edit_display_border_style_none(sender, role);
									}
								)
							)
							.then(LiteralArgumentBuilder.<CommandSource>literal("bracket")
								.executes(context ->
									{
										Player sender = context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.role_edit_display_border_style_bracket(sender, role);
									}
								)
							)
							.then(LiteralArgumentBuilder.<CommandSource>literal("curly")
								.executes(context ->
									{
										Player sender = context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.role_edit_display_border_style_curly(sender, role);
									}
								)
							)
							.then(LiteralArgumentBuilder.<CommandSource>literal("caret")
								.executes(context ->
									{
										Player sender = context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.role_edit_display_border_style_caret(sender, role);
									}
								)
							)
							.then(LiteralArgumentBuilder.<CommandSource>literal("custom")
								.then(LiteralArgumentBuilder.<CommandSource>literal("prefix")
									.then(RequiredArgumentBuilder.<CommandSource, String>argument("customaffix", StringArgumentType.greedyString())
										.executes(context ->
											{
												Player sender = context.getSource().getSender(); if(sender == null){return 0;}
												Role role = context.getArgument("role", Role.class);
												String customAffix = context.getArgument("customaffix", String.class);
												return CommandLogicRole.role_edit_display_border_style_custom_prefix(sender, role, customAffix);
											}
										)
									)
								)
								.then(LiteralArgumentBuilder.<CommandSource>literal("suffix")
									.then(RequiredArgumentBuilder.<CommandSource, String>argument("customaffix", StringArgumentType.greedyString())
										.executes(context ->
											{
												Player sender = context.getSource().getSender(); if(sender == null){return 0;}
												Role role = context.getArgument("role", Role.class);
												String customAffix = context.getArgument("customaffix", String.class);
												return CommandLogicRole.role_edit_display_border_style_custom_suffix(sender, role, customAffix);
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
										Player sender = context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										String color = context.getArgument("color", String.class);
										return CommandLogicRole.role_edit_username_border_color_COLOR(sender, role, color);
									}
								)
							)
							.then(RequiredArgumentBuilder.<CommandSource, String>argument("hex", StringArgumentType.string())
								.executes(context ->
									{
										Player sender = context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));
										return CommandLogicRole.role_edit_username_border_color_HEX(sender, role, hex);
									}
								)
							)
						)
						.then(LiteralArgumentBuilder.<CommandSource>literal("style")
							.then(LiteralArgumentBuilder.<CommandSource>literal("none")
								.executes(context ->
									{
										Player sender = context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.role_edit_username_border_style_none(sender, role);
									}
								)
							)
							.then(LiteralArgumentBuilder.<CommandSource>literal("bracket")
								.executes(context ->
									{
										Player sender = context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.role_edit_username_border_style_bracket(sender, role);
									}
								)
							)
							.then(LiteralArgumentBuilder.<CommandSource>literal("curly")
								.executes(context ->
									{
										Player sender = context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.role_edit_username_border_style_curly(sender, role);
									}
								)
							)
							.then(LiteralArgumentBuilder.<CommandSource>literal("caret")
								.executes(context ->
									{
										Player sender = context.getSource().getSender(); if(sender == null){return 0;}
										Role role = context.getArgument("role", Role.class);
										return CommandLogicRole.role_edit_username_border_style_caret(sender, role);
									}
								)
							)
							.then(LiteralArgumentBuilder.<CommandSource>literal("custom")
								.then(LiteralArgumentBuilder.<CommandSource>literal("prefix")
									.then(RequiredArgumentBuilder.<CommandSource, String>argument("customaffix", StringArgumentType.greedyString())
										.executes(context ->
											{
												Player sender = context.getSource().getSender(); if(sender == null){return 0;}
												Role role = context.getArgument("role", Role.class);
												String customAffix = context.getArgument("customaffix", String.class);
												return CommandLogicRole.role_edit_username_border_style_custom_prefix(sender, role, customAffix);
											}
										)
									)
								)
								.then(LiteralArgumentBuilder.<CommandSource>literal("suffix")
									.then(RequiredArgumentBuilder.<CommandSource, String>argument("customaffix", StringArgumentType.greedyString())
										.executes(context ->
											{
												Player sender = context.getSource().getSender(); if(sender == null){return 0;}
												Role role = context.getArgument("role", Role.class);
												String customAffix = context.getArgument("customaffix", String.class);
												return CommandLogicRole.role_edit_username_border_style_custom_suffix(sender, role, customAffix);
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
									Player sender = context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									String color = context.getArgument("color", String.class);
									return CommandLogicRole.role_edit_text_color_COLOR(sender, role, color);
								}
							)
						)
						.then(RequiredArgumentBuilder.<CommandSource, String>argument("hex", StringArgumentType.string())
							.executes(context ->
								{
									Player sender = context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									String hex = MUtil.breakDownHex(context.getArgument("hex", String.class));
									return CommandLogicRole.role_edit_text_color_HEX(sender, role, hex);
								}
							)
						)
					)
					.then(LiteralArgumentBuilder.<CommandSource>literal("underline")
						.then(RequiredArgumentBuilder.<CommandSource, Boolean>argument("value", BoolArgumentType.bool())
							.executes(context ->
								{
									Player sender = context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									boolean value = context.getArgument("value", Boolean.class);
									return CommandLogicRole.role_edit_text_underline(sender, role, value);
								}
							)
						)
					)
					.then(LiteralArgumentBuilder.<CommandSource>literal("bold")
						.then(RequiredArgumentBuilder.<CommandSource, Boolean>argument("value", BoolArgumentType.bool())
							.executes(context ->
								{
									Player sender = context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									boolean value = context.getArgument("value", Boolean.class);
									return CommandLogicRole.role_edit_text_bold(sender, role, value);
								}
							)
						)
					)
					.then(LiteralArgumentBuilder.<CommandSource>literal("italics")
						.then(RequiredArgumentBuilder.<CommandSource, Boolean>argument("value", BoolArgumentType.bool())
							.executes(context ->
								{
									Player sender = context.getSource().getSender(); if(sender == null){return 0;}
									Role role = context.getArgument("role", Role.class);
									boolean value = context.getArgument("value", Boolean.class);
									return CommandLogicRole.role_edit_text_italics(sender, role, value);
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
							Player sender = context.getSource().getSender(); if(sender == null){return 0;}
							return CommandLogicRole.role_grant(sender, target, role);
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
							Player sender = context.getSource().getSender(); if(sender == null){return 0;}
							return CommandLogicRole.role_revoke(sender, target, role);
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
							Player sender = context.getSource().getSender(); if(sender == null){return 0;}
							Role role = context.getArgument("role", Role.class);
							return CommandLogicRole.role_set_defaultrole_ROLEID(sender, role);
						}
					)
				)
				.then(LiteralArgumentBuilder.<CommandSource>literal("none")
					.executes(context ->
						{
							Player sender = context.getSource().getSender(); if(sender == null){return 0;}
							return CommandLogicRole.role_set_defaultrole_none(sender);
						}
					)
				)
			)
			.then(LiteralArgumentBuilder.<CommandSource>literal("displaymode")
				.then(LiteralArgumentBuilder.<CommandSource>literal("single")
					.executes(context ->
						{
							Player sender = context.getSource().getSender(); if(sender == null){return 0;}
							return CommandLogicRole.role_set_displaymode_single(sender);
						}
					)
				)
				.then(LiteralArgumentBuilder.<CommandSource>literal("multi")
					.executes(context ->
						{
							Player sender = context.getSource().getSender(); if(sender == null){return 0;}
							return CommandLogicRole.role_set_displaymode_multi(sender);
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
					Player sender = context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicRole.role_list(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleReload(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("reload")
			.executes(context ->
				{
					Player sender = context.getSource().getSender(); if(sender == null){return 0;}
					return CommandLogicRole.role_reload(sender);
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> role(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.executes(
			context -> CommandLogicRole.role((PlayerServer) context.getSource().getSender())
		);
		return builder;
	}

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> commandBuilder = LiteralArgumentBuilder.<CommandSource>literal("role").requires(CommandSource::hasAdmin);

		role(commandBuilder);
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

	public static SyntaxBuilder syntax = new SyntaxBuilder();
	public static void buildSyntax(){
		syntax.clear();
		syntax.append("title",                                                  TextFormatting.LIGHT_GRAY + "< Command Syntax > ([] = optional, <> = variable, / = or)");
		syntax.append("create", "title",                                  TextFormatting.LIGHT_GRAY + "  > /role create <role id> [<priority>]");
		syntax.append("delete", "title",                                  TextFormatting.LIGHT_GRAY + "  > /role delete <role id>");
		syntax.append("edit", "title",                                    TextFormatting.LIGHT_GRAY + "  > /role edit <role id> <mode>");
		syntax.append("priority", "edit",                                 TextFormatting.LIGHT_GRAY + "    > priority <priority value>");
		/*		syntax.append("perms", "edit",                                    TextFormatting.LIGHT_GRAY + "    > perms <permission>");*/
		syntax.append("display", "edit",                                  TextFormatting.LIGHT_GRAY + "    > display <style>");
		syntax.append("displayName", "display",                           TextFormatting.LIGHT_GRAY + "      > name <display name>");
		syntax.append("displayColor", "display",                          TextFormatting.LIGHT_GRAY + "      > color <color/hex>");
		syntax.append("displayUnderline", "display",                      TextFormatting.LIGHT_GRAY + "      > underline true/false");
		syntax.append("displayBold", "display",                           TextFormatting.LIGHT_GRAY + "      > bold true/false");
		syntax.append("displayItalics", "display",                        TextFormatting.LIGHT_GRAY + "      > italics true/false");
		syntax.append("displayBorder", "display",                         TextFormatting.LIGHT_GRAY + "      > border <style>");
		syntax.append("displayBorderColor", "displayBorder",              TextFormatting.LIGHT_GRAY + "        > color <color/hex>");
		syntax.append("displayBorderType", "displayBorder",               TextFormatting.LIGHT_GRAY + "        > none/bracket/caret/curly");
		syntax.append("displayBorderCustom", "displayBorder",             TextFormatting.LIGHT_GRAY + "        > custom [<affix>]");
		syntax.append("displayBorderCustomAffix", "displayBorderCustom",  TextFormatting.LIGHT_GRAY + "          > prefix/suffix <custom affix>");
		syntax.append("username", "edit",                                 TextFormatting.LIGHT_GRAY + "    > username <style>");
		/*syntax.append("usernameColor", "username",                      TextFormatting.LIGHT_GRAY + "      > color <color/hex>");
		syntax.append("usernameUnderline", "username",                    TextFormatting.LIGHT_GRAY + "      > underline true/false");
		syntax.append("usernameBold", "username",                         TextFormatting.LIGHT_GRAY + "      > bold true/false");
		syntax.append("usernameItalics", "username",                      TextFormatting.LIGHT_GRAY + "      > italics true/false");*/
		syntax.append("usernameBorder", "username",                       TextFormatting.LIGHT_GRAY + "      > border <style>");
		syntax.append("usernameBorderColor", "usernameBorder",            TextFormatting.LIGHT_GRAY + "        > color <color/hex>");
		syntax.append("usernameBorderType", "usernameBorder",             TextFormatting.LIGHT_GRAY + "        > none/bracket/caret/curly");
		syntax.append("usernameBorderCustom", "usernameBorder",           TextFormatting.LIGHT_GRAY + "        > custom [<affix>]");
		syntax.append("usernameBorderCustomAffix", "usernameBorderCustom",TextFormatting.LIGHT_GRAY + "          > prefix/suffix <custom affix>");
		syntax.append("text", "edit",                                     TextFormatting.LIGHT_GRAY + "    > text <style>");
		syntax.append("textColor", "text",                                TextFormatting.LIGHT_GRAY + "      > color <color/hex> (*bug: hex won't wrap!)");
		syntax.append("textUnderline", "text",                            TextFormatting.LIGHT_GRAY + "      > underline true/false");
		syntax.append("textBold", "text",                                 TextFormatting.LIGHT_GRAY + "      > bold true/false");
		syntax.append("textItalics", "text",                              TextFormatting.LIGHT_GRAY + "      > italics true/false");
		syntax.append("grant", "title",                                   TextFormatting.LIGHT_GRAY + "  > /role grant <role id> [<username>]");
		syntax.append("revoke", "title",                                  TextFormatting.LIGHT_GRAY + "  > /role revoke <role id> [<username>]");
		syntax.append("set", "title",                                     TextFormatting.LIGHT_GRAY + "  > /role set <mode>");
		syntax.append("setDefaultRole", "set",                            TextFormatting.LIGHT_GRAY + "    > defaultRole <role id>/none");
		syntax.append("setDisplayMode", "set",                            TextFormatting.LIGHT_GRAY + "    > displayMode single/multi");
		syntax.append("list", "title",                                    TextFormatting.LIGHT_GRAY + "  > /role list");
		syntax.append("reload", "title",                                  TextFormatting.LIGHT_GRAY + "  > /role reload");
	}
}
