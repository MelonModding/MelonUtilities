package MelonUtilities.mixins.tile_entities.chest;

import MelonUtilities.interfaces.Lockable;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicChest;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockLogicChest.class, remap = false)
public abstract class BlockLogicChestMixin extends BlockLogic {
	public BlockLogicChestMixin(Block<?> block, Material material) {
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
		if(!MUtil.canInteractWithLockable(lockable, player)){
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, (PlayerServer) player, "Chest is Locked!");
			cir.setReturnValue(false);
		}
	}

	@Inject(at = @At("TAIL"), method = "onBlockPlacedByMob", cancellable = true)
	public void onBlockPlacedInject(World world, int x, int y, int z, Side placeSide, Mob mob, double xPlaced, double yPlaced, CallbackInfo ci, @Local(name = "type") BlockLogicChest.Type type) {
		TileEntityChest existingChest = MUtil.getOtherChest(world, (TileEntityChest) world.getTileEntity(x, y, z));
		TileEntityChest placedChest = (TileEntityChest) world.getTileEntity(x, y, z);

		Lockable existingLockable = (Lockable) existingChest;
		Lockable placedLockable = (Lockable) placedChest;

		if(existingLockable != null) {
			placedLockable.setLockOwner(existingLockable.getLockOwner());
			placedLockable.setIsLocked(existingLockable.getIsLocked());
			placedLockable.setTrustedPlayers(existingLockable.getTrustedPlayers());
		}
	}
}
