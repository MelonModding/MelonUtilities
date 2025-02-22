package MelonUtilities.mixins.tile_entities.dispenser;

import MelonUtilities.interfaces.Lockable;
import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import MelonUtilities.utility.managers.LockManager;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicDispenser;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockLogicDispenser.class, remap = false)
public abstract class BlockLogicDispenserMixin extends BlockLogic {
	public BlockLogicDispenserMixin(Block<?> block, Material material) {
		super(block, material);
	}

	@Override
	public int getPistonPushReaction(World world, int x, int y, int z) {
		Lockable lockable = (Lockable) world.getTileEntity(x, y, z);
		if(lockable.getIsLocked()){
			return Material.PISTON_CANT_PUSH;
		}
		return super.getPistonPushReaction(world, x, y, z);
	}

	@Inject(at = @At("HEAD"), method = "onBlockRightClicked", cancellable = true)
	public void onBlockRightClickedInject(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {

		Lockable lockable = (Lockable) world.getTileEntity(x, y, z);
		if(player instanceof PlayerServer && LockManager.determineAuthStatus(lockable, (PlayerServer) player) <= LockManager.UNTRUSTED && !player.isSneaking()){
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, (PlayerServer) player, "Dispenser is Locked! (Use /lock info for more information)");
			cir.setReturnValue(false);
			return;
		} else if(player instanceof PlayerServer && LockManager.determineAuthStatus(lockable, (PlayerServer) player) <= LockManager.UNTRUSTED && player.isSneaking()){
			cir.setReturnValue(false);
			return;
		}
	}
}
