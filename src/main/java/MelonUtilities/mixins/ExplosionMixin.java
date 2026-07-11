package MelonUtilities.mixins;
import MelonUtilities.interfaces.Lockable;
import net.minecraft.core.world.Explosion;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkPosition;
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
		if (world.getTileEntity(((ChunkPosition) e).x, ((ChunkPosition) e).y, ((ChunkPosition) e).z) instanceof Lockable
			&& ((Lockable) world.getTileEntity(((ChunkPosition) e).x, ((ChunkPosition) e).y, ((ChunkPosition) e).z)).getIsLocked()) {
			return false;
		}
		instance.add(e);
		return true;
	}

}
