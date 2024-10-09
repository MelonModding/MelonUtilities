package BTAServerUtilities.config.datatypes;

import BTAServerUtilities.config.custom.classes.Home;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {

	@SerializedName(value = "Homes:")
	@Expose public ArrayList<Home> homes = new ArrayList<>();

	@SerializedName(value = "Helper:")
	@Expose public boolean isHelper = false;

	@SerializedName(value = "Players Trusted to all Containers:")
	@Expose public List<UUID> playersTrustedToAllContainers = new ArrayList<>();
}
