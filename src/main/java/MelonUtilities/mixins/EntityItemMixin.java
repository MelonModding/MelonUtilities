package MelonUtilities.mixins;

import MelonUtilities.config.Data;
import MelonUtilities.utility.MUtil;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityItem.class, remap = false)
public abstract class EntityItemMixin extends Entity {

	//ignore, constructor is useless
	public EntityItemMixin(@Nullable World world) {
		super(world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	void tick(CallbackInfo ci){
		if(Data.MainConfig.config.enableMagnets){
			if((Entity) this instanceof EntityItem){
				Player closestPlayer = MUtil.closestPlayerWithMagnetToItem(this.world, this);
				if(closestPlayer != null && closestPlayer.distanceTo(this) < 15){
					//magnet stuff
					Vec3 item = Vec3.getTempVec3(this.x, this.y, this.z);
					Vec3 target = Vec3.getTempVec3(closestPlayer.x, closestPlayer.y + closestPlayer.getHeadHeight() - 1.05, closestPlayer.z);
					double strength = 0.06;

					Vec3 normal = item.vectorTo(target).normalize().scale(strength).add(0,0.032,0);
					this.xd += normal.x;
					this.yd += normal.y;
					this.zd += normal.z;
				}
			}
		}
	}


}
