package BTAServerUtilities.config.custom.classes;

public class Home {
	public String name;
	public double x;
	public double y;
	public double z;
	public int dimID;

	public Home(String name, double x, double y, double z, int dimID) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimID = dimID;
	}

	@Override
	public String toString(){
		return 	"[Name: " + name + "]" + "[DimensionID: " + dimID + "]" + "[x: " + x + " y: " + y + " z: " + z + "]";
	}
}
