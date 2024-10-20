package MelonUtilities.mixins.rollback;

import MelonUtilities.utility.RollbackManager;
import net.minecraft.core.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Chunk.class, remap = false)
public class ChunkMixin {

	@Shadow
	public boolean isModified;

	@Shadow
	public boolean isLoaded;

	@Unique
	private void ModifiedQueueCheck(Chunk chunk){
		if(this.isModified && this.isLoaded && !RollbackManager.skipModifiedQueuing){
			RollbackManager.queueModifiedChunk(chunk);
		}
	}

	@Inject(at = @At("TAIL"), method = "setBlockIDWithMetadata")
	private void ModifiedQueue0(int x, int y, int z, int id, int data, CallbackInfoReturnable<Boolean> cir) {ModifiedQueueCheck((Chunk) (Object) this);}

	@Inject(at = @At("TAIL"), method = "setBlockIDRaw")
	private void ModifiedQueue1(int x, int y, int z, int id, CallbackInfoReturnable<Boolean> cir) {ModifiedQueueCheck((Chunk) (Object) this);}

	@Inject(at = @At("TAIL"), method = "setBlockMetadata")
	private void ModifiedQueue2(CallbackInfo ci) {ModifiedQueueCheck((Chunk) (Object) this);}

	@Inject(at = @At("TAIL"), method = "setBrightness")
	private void ModifiedQueue3(CallbackInfo ci) {ModifiedQueueCheck((Chunk) (Object) this);}

	@Inject(at = @At("TAIL"), method = "setChunkModified")
	private void ModifiedQueue4(CallbackInfo ci) {ModifiedQueueCheck((Chunk) (Object) this);}

}
