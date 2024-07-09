package BTAServerUtilities.utility;

import BTAServerUtilities.mixins.interfaces.JSONable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.Serializable;

public class Position implements Serializable {
	public int x;
	public int y;
	public int z;
	public int dimension;

	public Position(int x, int y, int z, int dimension) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimension = dimension;
	}
}
