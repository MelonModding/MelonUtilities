package MelonUtilities.commands.misc;

import MelonUtilities.utility.FeedbackHandler;
import net.minecraft.core.entity.EntityLightningBolt;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.LocationTarget;

public class SmiteCommand extends Command {
	private final static String COMMAND = "smite";

	public SmiteCommand() {
		super(COMMAND);
	}

	@Override
	public boolean execute(CommandHandler commandHandler, CommandSource commandSource, String[] strings) {
		LocationTarget location = new LocationTarget(commandHandler, commandSource);
		location.getWorld().addWeatherEffect(new EntityLightningBolt(location.getWorld(), location.getX(), location.getY()-1, location.getZ()));
		return true;
	}

	@Override
	public boolean opRequired(String[] strings) {
		return true;
	}

	@Override
	public void sendCommandSyntax(CommandHandler commandHandler, CommandSource commandSource) {
		FeedbackHandler.syntax(commandSource, "smite");
	}
}
