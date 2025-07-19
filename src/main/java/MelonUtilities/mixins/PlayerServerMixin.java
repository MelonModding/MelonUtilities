package MelonUtilities.mixins;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.data.Spawn;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PlayerServer.class, remap = false)
public class PlayerServerMixin {

	/**
	 * @author ipiepiepie
	 * @reason removing randomized spawn point
	 */
	@Redirect(
		method = "<init>",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/server/entity/player/PlayerServer;moveTo(DDDFF)V")
	)
	private void setSpawn(PlayerServer instance, double x, double y, double z, float yRot, float xRot) {
		Spawn spawn = Data.MainConfig.config.spawnData;
		if (spawn != null && Data.MainConfig.config.enableSpawn)
			instance.moveTo(spawn.x, spawn.y, spawn.z, yRot, xRot);
		else
			instance.moveTo(x, y, z, yRot, xRot);
	}

}
