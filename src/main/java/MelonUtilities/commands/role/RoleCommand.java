package MelonUtilities.commands.role;

import MelonUtilities.command_arguments.ArgumentTypeRoleID;
import MelonUtilities.config.Data;
import MelonUtilities.config.DataBank;
import MelonUtilities.config.datatypes.ConfigData;
import MelonUtilities.config.datatypes.RoleData;
import MelonUtilities.utility.FeedbackHandler;
import MelonUtilities.utility.RoleBuilder;
import MelonUtilities.utility.SyntaxBuilder;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.command.arguments.ArgumentTypeEntity;
import net.minecraft.core.net.command.helpers.EntitySelector;
import net.minecraft.server.entity.player.PlayerServer;

public class RoleCommand implements CommandManager.CommandRegistry{

	public static SyntaxBuilder syntax = new SyntaxBuilder();
	public static void buildRoleSyntax(){
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

	public static RoleData getRoleDataFromRoleID(String arg){
		return Data.roles.getOrCreate(arg, RoleData.class);
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleCreate(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {

		builder.then(LiteralArgumentBuilder.<CommandSource>literal("create")
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("roleID", StringArgumentType.string())
				.then(RequiredArgumentBuilder.<CommandSource, String>argument("rolePriority", StringArgumentType.string())
					.executes(
						command -> {
							CommandSource source = command.getSource();
							String roleID = command.getArgument("roleID", String.class);
							String rolePriority = command.getArgument("rolePriority", String.class);


							if (Data.roles.dataHashMap.containsKey(roleID)) {
								FeedbackHandler.error(source, "Failed to Create Role: " + roleID + " (Role Already Exists)");
								return Command.SINGLE_SUCCESS;
							}

							Data.roles.getOrCreate(roleID, RoleData.class);
							Data.roles.loadAll(RoleData.class);
							getRoleDataFromRoleID(roleID).displayName = roleID;
							getRoleDataFromRoleID(roleID).priority = Integer.parseInt(rolePriority);
							Data.roles.saveAll();

							FeedbackHandler.success(source, "Created Role: " + getRoleDataFromRoleID(roleID).displayName + " with Priority: " + getRoleDataFromRoleID(roleID).priority);
							return Command.SINGLE_SUCCESS;
						}
					)
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleDelete(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("delete")
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("roleID", ArgumentTypeRoleID.roleID())
				.executes(
					command -> {
						CommandSource source = command.getSource();
						String role = command.getArgument("roleID", String.class);

						switch (Data.roles.remove(role)) {
							case DataBank.NO_ERROR:
								FeedbackHandler.destructive(source, "Deleted Role: " + role);
								return Command.SINGLE_SUCCESS;
							case DataBank.ROLE_DOESNT_EXIST:
								FeedbackHandler.error(source, "Failed to Delete Role: " + role + " (Role Doesn't Exist)");
								syntax.printLayerAndSubLayers("delete", source);
								return Command.SINGLE_SUCCESS;
							case DataBank.IO_ERROR:
								FeedbackHandler.error(source, "Failed to Delete Role: " + role + " (IO Error)");
								return Command.SINGLE_SUCCESS;
						}
						return Command.SINGLE_SUCCESS;
					}
				)
			)
		);
		return builder;
	}

/*	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleEdit(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {


	}*/

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleGrant(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("grant")
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("roleID", ArgumentTypeRoleID.roleID())
				.then(RequiredArgumentBuilder.<CommandSource, EntitySelector>argument("target", ArgumentTypeEntity.player())
					.executes(
						command -> {

							CommandSource source = command.getSource();
							String role = command.getArgument("roleID", String.class);
							EntitySelector entitySelector = (EntitySelector)command.getArgument("target", EntitySelector.class);
							String target = ((Player)entitySelector.get(source).get(0)).username;

							if(!Data.roles.dataHashMap.containsKey(role)){
								FeedbackHandler.error(source, "Failed to Grant Role (Role doesn't exist!)");
								syntax.printLayerAndSubLayers("grant", source);
								return Command.SINGLE_SUCCESS;
							}

							RoleData roleData = getRoleDataFromRoleID(role);

							if(!roleData.playersGrantedRole.contains(source.getSender().username)){
								Data.roles.loadAll(RoleData.class);
								getRoleDataFromRoleID(role).playersGrantedRole.add(source.getSender().username);
								Data.roles.saveAll();
								FeedbackHandler.success(source, "Granted Role: " + role + " to player: " + TextFormatting.LIGHT_GRAY + source.getSender().username);
								return Command.SINGLE_SUCCESS;
							} else if (!roleData.playersGrantedRole.contains(target)){
								Data.roles.loadAll(RoleData.class);
								getRoleDataFromRoleID(role).playersGrantedRole.add(target);
								Data.roles.saveAll();
								FeedbackHandler.success(source, "Granted Role: " + role + " to player: " + TextFormatting.LIGHT_GRAY + target);
								return Command.SINGLE_SUCCESS;
							} else if (roleData.playersGrantedRole.contains(source.getSender().username) || roleData.playersGrantedRole.contains(target)) {
								FeedbackHandler.error(source, "Failed to Grant Role (Player already has Role!)");
								return Command.SINGLE_SUCCESS;
							}


							FeedbackHandler.error(source, "Failed to Grant Role (Default Error) (Invalid Syntax?)");
							syntax.printLayerAndSubLayers("grant", source);
							return Command.SINGLE_SUCCESS;
						}
					)
				)
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleRevoke(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("revoke")
			.then(RequiredArgumentBuilder.<CommandSource, String>argument("roleID", ArgumentTypeRoleID.roleID())
				.then(RequiredArgumentBuilder.<CommandSource, EntitySelector>argument("target", ArgumentTypeEntity.player())
					.executes(
						command -> {

							CommandSource source = command.getSource();
							String role = command.getArgument("roleID", String.class);
							EntitySelector entitySelector = (EntitySelector)command.getArgument("target", EntitySelector.class);
							String target = ((Player)entitySelector.get(source).get(0)).username;

							if(!Data.roles.dataHashMap.containsKey(role)){
								FeedbackHandler.error(source, "Failed to Revoke Role (Role doesn't exist!)");
								syntax.printLayerAndSubLayers("revoke", source);
								return Command.SINGLE_SUCCESS;
							}

							RoleData roleData = getRoleDataFromRoleID(role);

							if (roleData.playersGrantedRole.contains(target)) {
								Data.roles.loadAll(RoleData.class);
								getRoleDataFromRoleID(role).playersGrantedRole.remove(target);
								Data.roles.saveAll();
								FeedbackHandler.destructive(source, "Revoked Role: " + role + " from player: " + TextFormatting.LIGHT_GRAY + target);
								return Command.SINGLE_SUCCESS;
							} else if (roleData.playersGrantedRole.contains(source.getSender().username)){
								Data.roles.loadAll(RoleData.class);
								getRoleDataFromRoleID(role).playersGrantedRole.remove(source.getSender().username);
								Data.roles.saveAll();
								FeedbackHandler.destructive(source, "Revoked Role: " + role + " from player: " + TextFormatting.LIGHT_GRAY + source.getSender().username);
								return Command.SINGLE_SUCCESS;
							} else if (!roleData.playersGrantedRole.contains(source.getSender().username) || !roleData.playersGrantedRole.contains(target)) {
								FeedbackHandler.error(source, "Failed to Revoke Role (Player does not have Role!)");
								return Command.SINGLE_SUCCESS;
							}

							FeedbackHandler.error(source, "Failed to Revoke Role (Default Error) (Invalid Syntax?)");
							syntax.printLayerAndSubLayers("revoke", source);
							return Command.SINGLE_SUCCESS;
						}
					)
				)
			)
		);
		return builder;
	}

/*	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleSet(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {

	}*/

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleList(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("list")
			.executes(
				command -> {
					CommandSource source = command.getSource();
					if (Data.roles.dataHashMap.isEmpty()) {
						source.sendMessage(TextFormatting.LIGHT_GRAY + "< Roles: >");
						source.sendMessage(TextFormatting.LIGHT_GRAY + "  -No Roles Created-");
						return Command.SINGLE_SUCCESS;
					}

					source.sendMessage(TextFormatting.LIGHT_GRAY + "< Roles: >");

					for (String role : Data.roles.dataHashMap.keySet()) {
						source.sendMessage(TextFormatting.LIGHT_GRAY + "  > Role ID: " + TextFormatting.WHITE + TextFormatting.ITALIC + role + TextFormatting.LIGHT_GRAY + " - Priority: " + TextFormatting.WHITE + getRoleDataFromRoleID(role).priority);
						source.sendMessage(TextFormatting.LIGHT_GRAY + "    > " + RoleBuilder.buildRoleDisplay(Data.roles.dataHashMap.get(role))
							+ RoleBuilder.buildRoleUsername(Data.roles.dataHashMap.get(role), source.getSender().getDisplayName())
							+ RoleBuilder.buildRoleTextFormat(Data.roles.dataHashMap.get(role)) + "text");
					}

					return Command.SINGLE_SUCCESS;
				}
			)
		);
		return builder;
	}

	public static ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> roleReload(ArgumentBuilder<CommandSource, LiteralArgumentBuilder<CommandSource>> builder) {
		builder.then(LiteralArgumentBuilder.<CommandSource>literal("reload")
			.executes(
				command -> {
					CommandSource source = command.getSource();
					Data.roles.loadAll(RoleData.class);
					FeedbackHandler.success(source, "Reloaded " + Data.roles.dataHashMap.size() + " Role(s)!");
					buildRoleSyntax();
					FeedbackHandler.success(source, "Built Role Syntax!");
					Data.configs.loadAll(ConfigData.class);
					FeedbackHandler.success(source, "Reloaded Config!");
					return Command.SINGLE_SUCCESS;
				}
			)
		);
		return builder;
	}

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> commandBuilder = LiteralArgumentBuilder.<CommandSource>literal("role").requires(CommandSource::hasAdmin);

		roleCreate(commandBuilder);
		roleDelete(commandBuilder);
		roleGrant(commandBuilder);
		roleRevoke(commandBuilder);
		roleList(commandBuilder);
		roleReload(commandBuilder);

		dispatcher.register(commandBuilder);
	}
}
