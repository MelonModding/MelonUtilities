package BTAServerUtilities.config.datatypes;

import BTAServerUtilities.config.custom.classes.Home;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlayerData {

	@SerializedName(value = "Homes:")
	@Expose public ArrayList<Home> homes = new ArrayList<>();

}
