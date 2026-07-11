package MelonUtilities.mixins;
import MelonUtilities.interfaces.Lockable;
import net.minecraft.core.world.Explosion;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@Mixin(value = Explosion.class, remap = false)
public class ExplosionMixin {
	@Shadow
	@Final
	protected World world;

	@Redirect(method = "calculateBlocksToDestroy()V", at = @At(value = "INVOKE", target = "Ljava/util/Set;add(Ljava/lang/Object;)Z"))
	private boolean preventClaimedDestruction(Set instance, Object e){
		if (world.getTileEntity(((TilePos) e).x, ((TilePos) e).y, ((TilePos) e).z) instanceof Lockable
			&& ((Lockable) world.getTileEntity(((TilePos) e).x, ((TilePos) e).y, ((TilePos) e).z)).getIsLocked()) {
			return false;
		}
		instance.add(e);
		return true;
	}

}
