package BTAServerUtilities.config.datatypes;

import BTAServerUtilities.utility.Position;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerData {

	@SerializedName(value = "Homes:")
	@Expose public List<HashMap<String, Position>> homes = new ArrayList<>();

}
