package MelonUtilities.mixins;

import MelonUtilities.sqlite.DatabaseManager;
import MelonUtilities.sqlite.log_events.LogEventPlace;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.server.world.ServerPlayerController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerPlayerController.class, remap = false)
public class ServerPlayerControllerMixin {

	@Inject(at = @At(ordinal = 1, shift = At.Shift.AFTER, value = "INVOKE", target = "Lnet/minecraft/core/entity/player/Player;setHeldObject(Lnet/minecraft/core/world/ICarriable;)V"), method = "activateBlockOrUseItem", cancellable = true)
	private void activateBlockOrUseItemInject(Player entityplayer, World world, ItemStack itemstack, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir){
		System.out.println("placed block");
		DatabaseManager.connect((conn) -> LogEventPlace.insert(conn, entityplayer.uuid.toString(), itemstack.getItemKey(), blockX, blockY, blockZ));
	}
}
