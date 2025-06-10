package MelonUtilities.mixins;

import MelonUtilities.interfaces.Lockable;
import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import MelonUtilities.utility.managers.LockManager;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemPaintBrush;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemPaintBrush.class, remap = false)
public class PaintBrushMixin {

	@Inject(at = @At("HEAD"), method = "onUseItemOnBlock", cancellable = true)
	public void onPaint(ItemStack itemstack, Player player, World world, int x, int y, int z, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {
		if (player == null) return;

		Lockable lockable = (Lockable) world.getTileEntity(x, y, z);

		if (lockable == null || LockManager.determineAuthStatus(lockable, (PlayerServer) player) > LockManager.UNTRUSTED) return;

		FeedbackHandlerServer.sendFeedback(FeedbackType.error, (PlayerServer) player, "Failed to Paint Container! (Not Authorized)");
		cir.setReturnValue(false);
	}
}
