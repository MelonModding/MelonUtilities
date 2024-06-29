package goldenage.omnimod.misc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import goldenage.omnimod.interfaces.JSONable;

public class Position implements JSONable {
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

	@Override
	public JsonElement toJson() {
		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("x", x);
		jsonObject.addProperty("y", y);
		jsonObject.addProperty("z", z);
		jsonObject.addProperty("dimension", dimension);

		return jsonObject;
	}
}
