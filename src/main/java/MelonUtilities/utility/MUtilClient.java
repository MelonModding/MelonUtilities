package MelonUtilities.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.WorldClient;
import net.minecraft.core.achievement.Achievements;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.chunk.ChunkCoordinates;

public class MUtilClient {

	public static void teleportToDimension(Dimension dimension) {
		Minecraft mc = Minecraft.getMinecraft();
		Dimension lastDim = Dimension.getDimensionList().get(mc.thePlayer.dimension);
		Minecraft.LOGGER.info("Switching to dimension \"{}\"!!", dimension.getTranslatedName());
		mc.thePlayer.dimension = dimension.id;
		mc.currentWorld.setEntityDead(mc.thePlayer);
		mc.thePlayer.removed = false;
		double x = mc.thePlayer.x;
		double z = mc.thePlayer.z;
		double y = mc.thePlayer.y;
		x *= Dimension.getCoordScale(lastDim, dimension);
		z *= Dimension.getCoordScale(lastDim, dimension);
		mc.thePlayer.moveTo(x, y, z, mc.thePlayer.yRot, mc.thePlayer.xRot);
		ChunkCoordinates newCoordinates = new ChunkCoordinates(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
		if (mc.thePlayer.isAlive() && mc.thePlayer.dimensionEnterCoordinate != null) {
			double dx = (double)mc.thePlayer.dimensionEnterCoordinate.x - x;
			double dy = (double)mc.thePlayer.dimensionEnterCoordinate.y - y;
			double dz = (double)mc.thePlayer.dimensionEnterCoordinate.z - z;
			double distSqr = dx * dx + dy * dy + dz * dz;
			if (distSqr > 6.4E7) {
				mc.thePlayer.addStat(Achievements.FAST_TRAVEL, 1);
			}
		}

		mc.thePlayer.dimensionEnterCoordinate = newCoordinates;
		if (mc.thePlayer.isAlive()) {
			mc.currentWorld.updateEntityWithOptionalForce(mc.thePlayer, false);
		}

		WorldClient world = new WorldClient(mc.currentWorld, dimension);
		I18n i18n = I18n.getInstance();
		if (dimension == lastDim.homeDim) {
			mc.changeWorld(world, i18n.translateKeyAndFormat("gui.loading.label.leaving", lastDim.getTranslatedName()), mc.thePlayer);
		} else {
			mc.changeWorld(world, i18n.translateKeyAndFormat("gui.loading.label.entering", dimension.getTranslatedName()), mc.thePlayer);
		}

		mc.thePlayer.world = mc.currentWorld;
		if (mc.thePlayer.isAlive()) {
			mc.thePlayer.moveTo(x, y, z, mc.thePlayer.yRot, mc.thePlayer.xRot);
			mc.currentWorld.updateEntityWithOptionalForce(mc.thePlayer, false);
		}
	}

}
