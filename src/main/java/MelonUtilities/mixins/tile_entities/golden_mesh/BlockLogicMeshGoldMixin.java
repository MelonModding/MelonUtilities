package MelonUtilities.mixins.tile_entities.golden_mesh;

import MelonUtilities.interfaces.TileEntityContainerInterface;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.feedback.FeedbackHandler;
import net.minecraft.core.block.BlockLogicMeshGold;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockLogicMeshGold.class, remap = false)
public class BlockLogicMeshGoldMixin {
	@Inject(at = @At("HEAD"), method = "onBlockRightClicked", cancellable = true)
	public void onBlockRightClickedInject(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {

		TileEntityContainerInterface iContainer = (TileEntityContainerInterface) world.getBlockEntity(x, y, z);

		if(!MUtil.canInteractWithLock(iContainer.getIsLocked(), iContainer.getIsCommunityContainer(), iContainer.getLockOwner(), iContainer.getTrustedPlayers(), player)){
			FeedbackHandler.error(player, "Golden Mesh is Locked!");
			cir.setReturnValue(false);
		}

	}
}
