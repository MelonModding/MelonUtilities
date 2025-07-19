package MelonUtilities.config.datatypes.data;

import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.world.Dimension;

public class Spawn {
	public double x;
	public double y;
	public double z;
	public int dimID;

	public Spawn(double x, double y, double z, int dimID) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimID = dimID;
	}

	@Override
	public String toString(){
		return String.format("%d %d %d", (int) x, (int) y, (int) z);
	}

	public String toDescriptiveString(){
		String dimensionName = Dimension.getDimensionList().get(dimID).languageKey;
		dimensionName = Character.toUpperCase(dimensionName.charAt(0)) + dimensionName.substring(1);
		TextFormatting dimColor = TextFormatting.LIGHT_GRAY;
		if(dimID == Dimension.OVERWORLD.id){
			dimColor = TextFormatting.LIME;
		} else if(dimID == Dimension.NETHER.id){
			dimColor = TextFormatting.RED;
		} else if(dimID == Dimension.PARADISE.id){
			dimColor = TextFormatting.LIGHT_BLUE;
		}

		return String.format(
			TextFormatting.GRAY + "[SPAWN] [" +
				dimColor + "%s" +
				TextFormatting.GRAY + "] [" +
				TextFormatting.LIGHT_GRAY + "x: %.1f y: %.1f z: %.1f" +
				TextFormatting.GRAY + "]",
			dimensionName, x, y, z);
	}
}
