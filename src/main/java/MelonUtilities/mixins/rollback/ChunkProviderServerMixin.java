package MelonUtilities.mixins.rollback;

import MelonUtilities.utility.managers.RollbackManager;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.server.world.chunk.provider.ChunkProviderServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ChunkProviderServer.class, remap = false)
public class ChunkProviderServerMixin {
	@Redirect(method = "populate", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/chunk/Chunk;setChunkModified()V"))
	private void populateRedirect(Chunk instance){
		RollbackManager.skipModifiedQueuing = true;
		instance.setChunkModified();
		RollbackManager.skipModifiedQueuing = false;
	}
}
