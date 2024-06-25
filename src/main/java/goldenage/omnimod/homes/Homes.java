package goldenage.omnimod.homes;

import goldenage.omnimod.homes.commands.CommandDelHome;
import goldenage.omnimod.homes.commands.CommandHome;
import goldenage.omnimod.homes.commands.CommandSetHome;
import goldenage.omnimod.interfaces.Initializable;
import goldenage.omnimod.interfaces.Saveable;
import turniplabs.halplibe.helper.CommandHelper;

public class Homes implements Initializable, Saveable {
	@Override
	public void initialize() {
		CommandHelper.Server.createCommand(new CommandHome());
		CommandHelper.Server.createCommand(new CommandSetHome());
		CommandHelper.Server.createCommand(new CommandDelHome());
	}

	@Override
	public void save() {

	}

	@Override
	public void load() {

	}
}
