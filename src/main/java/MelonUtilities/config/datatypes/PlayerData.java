package MelonUtilities.config.datatypes;

import MelonUtilities.config.custom.classes.Home;
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

	@SerializedName(value = "Lock-On-Block-Placed:")
	@Expose public boolean lockOnBlockPlaced = false;

	@SerializedName(value = "Lock-On-Block-Punched:")
	@Expose public boolean lockOnBlockPunched = false;

	@SerializedName(value = "Lock-Bypass:")
	@Expose public boolean lockBypass = false;

	@SerializedName(value = "Players Trusted to all Containers:")
	@Expose public List<UUID> playersTrustedToAllContainers = new ArrayList<>();
}
