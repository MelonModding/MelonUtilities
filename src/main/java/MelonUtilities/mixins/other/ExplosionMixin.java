package MelonUtilities.mixins.other;
import MelonUtilities.interfaces.TileEntityContainerInterface;
import net.minecraft.core.world.Explosion;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkPosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@Mixin(value = Explosion.class, remap = false)
public class ExplosionMixin {
	@Shadow
	protected World worldObj;

	@Redirect(method = "calculateBlocksToDestroy()V", at = @At(value = "INVOKE", target = "Ljava/util/Set;add(Ljava/lang/Object;)Z"))
	private boolean preventClaimedDestruction(Set instance, Object e){
		if (worldObj.getBlockEntity(((ChunkPosition) e).x, ((ChunkPosition) e).y, ((ChunkPosition) e).z) instanceof TileEntityContainerInterface
			&& ((TileEntityContainerInterface) worldObj.getBlockEntity(((ChunkPosition) e).x, ((ChunkPosition) e).y, ((ChunkPosition) e).z)).getIsLocked()) {
			return false;
		}
		instance.add(e);
		return true;
	}

}
