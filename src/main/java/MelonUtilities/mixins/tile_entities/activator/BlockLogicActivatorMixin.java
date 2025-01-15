package MelonUtilities.mixins.tile_entities.activator;

import MelonUtilities.interfaces.Lockable;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicActivator;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockLogicActivator.class, remap = false)
public abstract class BlockLogicActivatorMixin extends BlockLogic {
	public BlockLogicActivatorMixin(Block<?> block, Material material) {
		super(block, material);
	}

	@Override
	public int getPistonPushReaction(World world, int x, int y, int z) {
		Lockable iContainer = (Lockable) world.getTileEntity(x, y, z);
		if(iContainer.getIsLocked()){
			return Material.PISTON_CANT_PUSH;
		}
		return super.getPistonPushReaction(world, x, y, z);
	}

	@Inject(at = @At("HEAD"), method = "onBlockRightClicked", cancellable = true)
	public void onBlockRightClickedInject(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {

		Lockable iContainer = (Lockable) world.getTileEntity(x, y, z);
		if(!MUtil.canInteractWithLock(iContainer.getIsLocked(), iContainer.getIsCommunityContainer(), iContainer.getLockOwner(), iContainer.getTrustedPlayers(), player)){
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, (PlayerServer) player, "Activator is Locked!");
			cir.setReturnValue(false);
		} else {
			//LogManager.logEventOpenContainer(player.uuid, world.getBlock(x, y, z), x, y, z);
		}
	}
}
