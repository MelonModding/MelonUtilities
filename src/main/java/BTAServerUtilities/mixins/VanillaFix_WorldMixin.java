package BTAServerUtilities.mixins;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;


@Mixin(value = World.class, remap = false)
public class VanillaFix_WorldMixin {
	@Shadow
	public List<EntityPlayer> players;

	/**
	 * @author MelonCommands
	 * @reason fixing bug in vanilla method for 7.2pre2
	 */
	@Overwrite
	public EntityPlayer getClosestPlayer(double x, double y, double z, double radius) {
		double closestDistance = Double.POSITIVE_INFINITY;
		EntityPlayer entityplayer = null;
		if (radius < 0) {

			for (EntityPlayer entityPlayer1 : players) {
				double currentDistance = entityPlayer1.distanceToSqr(x, y, z);
				if ((currentDistance < closestDistance)) {
					closestDistance = currentDistance;
					entityplayer = entityPlayer1;
				}
			}
		} else {
			double rSquared = radius * radius;
			for (EntityPlayer entityPlayer1 : players) {
				double currentDistance = entityPlayer1.distanceToSqr(x, y, z);
				if (currentDistance < rSquared && (currentDistance < closestDistance)) {
					closestDistance = currentDistance;
					entityplayer = entityPlayer1;
				}
			}
		}
		return entityplayer;
	}
}

