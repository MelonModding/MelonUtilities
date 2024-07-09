package BTAServerSolutions.BTAServerUtilities.commands.misc;

import net.minecraft.core.entity.EntityLightningBolt;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.LocationTarget;

public class SmiteCommand extends Command {
	private final static String COMMAND = "smite";

	public SmiteCommand() {
		super(COMMAND);
	}

	@Override
	public boolean execute(CommandHandler commandHandler, CommandSender commandSender, String[] strings) {
		LocationTarget location = new LocationTarget(commandHandler, commandSender);
		location.getWorld().addWeatherEffect(new EntityLightningBolt(location.getWorld(), location.getX(), location.getY()-1, location.getZ()));
		return true;
	}

	@Override
	public boolean opRequired(String[] strings) {
		return true;
	}

	@Override
	public void sendCommandSyntax(CommandHandler commandHandler, CommandSender commandSender) {
		commandSender.sendMessage("smite");
	}
}
