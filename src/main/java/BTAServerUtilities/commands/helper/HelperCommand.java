package BTAServerUtilities.commands.helper;

import BTAServerUtilities.config.Data;
import BTAServerUtilities.config.datatypes.PlayerData;
import BTAServerUtilities.utility.SyntaxBuilder;
import BTAServerUtilities.utility.UUIDHelper;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.entity.player.EntityPlayerMP;

public class HelperCommand extends Command {

	private final static String COMMAND = "helper";

	public HelperCommand() {super(COMMAND, "h");}

	public static SyntaxBuilder syntax = new SyntaxBuilder();

	public static void buildHelperSyntax(){
		syntax.clear();
		syntax.append("title",                                                  TextFormatting.LIGHT_GRAY + "< Command Syntax >");
		syntax.append("helper", "title",                                  TextFormatting.LIGHT_GRAY + "  > /helper <mode>");
		syntax.append("helperAdd", "helper",                              TextFormatting.LIGHT_GRAY + "    > add <username>");
		syntax.append("helperRemove", "helper",                           TextFormatting.LIGHT_GRAY + "    > remove <username>");
	}

	private boolean add(CommandHandler handler, CommandSender sender, String[] args){
		String username = args[1];
		EntityPlayerMP player = (EntityPlayerMP) handler.getPlayer(username);

		if (player != null && !Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(username).toString(), PlayerData.class).isHelper) {
			handler.sendMessageToPlayer(player, TextFormatting.LIME + "You are now a Helper!");
			handler.sendCommandFeedback(sender, TextFormatting.LIME + "Setting " + TextFormatting.GRAY + username + TextFormatting.LIME + " to Helper.");

			Data.playerData.loadAll(PlayerData.class);
			Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(username).toString(), PlayerData.class).isHelper = true;
			Data.playerData.saveAll();

			return true;
		} else if(Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(username).toString(), PlayerData.class).isHelper){
			handler.sendCommandFeedback(sender, TextFormatting.GRAY + username + TextFormatting.RED + " is already a helper!");
			return true;
		} else if(player == null){
			handler.sendCommandFeedback(sender, TextFormatting.LIME + "Setting " + TextFormatting.GRAY + username + TextFormatting.LIME + " to Helper.");

			Data.playerData.loadAll(PlayerData.class);
			Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(username).toString(), PlayerData.class).isHelper = true;
			Data.playerData.saveAll();

			return true;
		}
		return true;
	}

	private boolean remove(CommandHandler handler, CommandSender sender, String[] args){
		String username = args[1];
		EntityPlayerMP player = (EntityPlayerMP) handler.getPlayer(username);

		if (player != null && Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(username).toString(), PlayerData.class).isHelper) {
			handler.sendMessageToPlayer(player, TextFormatting.RED + "You are no longer a Helper!");
			handler.sendCommandFeedback(sender, TextFormatting.LIME + "Removing " + TextFormatting.GRAY + username + TextFormatting.LIME + " from Helper List.");

			Data.playerData.loadAll(PlayerData.class);
			Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(username).toString(), PlayerData.class).isHelper = false;
			Data.playerData.saveAll();

			return true;
		} else if(!Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(username).toString(), PlayerData.class).isHelper){
			handler.sendCommandFeedback(sender, TextFormatting.GRAY + username + TextFormatting.RED + " isn't a helper!");
			return true;
		} else if(player == null){
			handler.sendCommandFeedback(sender, TextFormatting.LIME + "Removing " + TextFormatting.GRAY + username + TextFormatting.LIME + " from Helper List.");

			Data.playerData.loadAll(PlayerData.class);
			Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(username).toString(), PlayerData.class).isHelper = false;
			Data.playerData.saveAll();

			return true;
		}
		return true;
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {

		if (args.length == 0) {
			return false;
		}

		switch(args[0]){
			case "add":
				return add(handler, sender, args);
			case "remove":
				return remove(handler, sender, args);
		}

		sender.sendMessage(TextFormatting.RED + "Helper Command Failed (Invalid Syntax)");
		syntax.printAllLines(sender);
		return true;
	}

	@Override
	public boolean opRequired(String[] args) {
		return true;
	}

	@Override
	public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
		syntax.printAllLines(sender);
	}
}
