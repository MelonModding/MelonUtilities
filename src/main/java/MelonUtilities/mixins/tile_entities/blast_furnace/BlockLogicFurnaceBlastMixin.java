package MelonUtilities.mixins.tile_entities.blast_furnace;

import MelonUtilities.interfaces.TileEntityContainerInterface;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import net.minecraft.core.block.BlockLogicFurnaceBlast;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockLogicFurnaceBlast.class, remap = false)
public class BlockLogicFurnaceBlastMixin {
	@Inject(at = @At("HEAD"), method = "onBlockRightClicked", cancellable = true)
	public void onBlockRightClickedInject(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {

		TileEntityContainerInterface iContainer = (TileEntityContainerInterface) world.getTileEntity(x, y, z);
		if(!MUtil.canInteractWithLock(iContainer.getIsLocked(), iContainer.getIsCommunityContainer(), iContainer.getLockOwner(), iContainer.getTrustedPlayers(), player)){
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, (PlayerServer) player, "Blast Furnace is Locked!");
			cir.setReturnValue(false);
		}
	}
}
