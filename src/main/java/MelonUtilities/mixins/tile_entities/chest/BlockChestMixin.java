package MelonUtilities.mixins.tile_entities.chest;

import MelonUtilities.interfaces.TileEntityContainerInterface;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.feedback.FeedbackHandler;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.block.BlockChest;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockChest.class, remap = false)
public class BlockChestMixin {
	@Inject(at = @At("HEAD"), method = "onBlockRightClicked", cancellable = true)
	public void onBlockRightClickedInject(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {

		TileEntityContainerInterface iContainer = (TileEntityContainerInterface) world.getBlockEntity(x, y, z);
		if(!MUtil.canInteractWithLock(iContainer.getIsLocked(), iContainer.getIsCommunityContainer(), iContainer.getLockOwner(), iContainer.getTrustedPlayers(), player)){
			FeedbackHandler.error(player, "Chest is Locked!");
			cir.setReturnValue(false);
		}
	}

	@Inject(at = @At("TAIL"), method = "onBlockPlacedByMob", cancellable = true)
	public void onBlockPlacedInject(World world, int x, int y, int z, Side placeSide, Mob mob, double xPlaced, double yPlaced, CallbackInfo ci, @Local(name = "type") BlockChest.Type type) {
		TileEntityChest existingChest = MUtil.getOtherChest(world, (TileEntityChest) world.getBlockEntity(x, y, z));
		TileEntityChest placedChest = (TileEntityChest) world.getBlockEntity(x, y, z);

		TileEntityContainerInterface existingIContainer = (TileEntityContainerInterface) existingChest;
		TileEntityContainerInterface placedIContainer = (TileEntityContainerInterface) placedChest;

		if(existingIContainer != null) {
			placedIContainer.setLockOwner(existingIContainer.getLockOwner());
			placedIContainer.setIsLocked(existingIContainer.getIsLocked());
			placedIContainer.setTrustedPlayers(existingIContainer.getTrustedPlayers());
		}
	}
}
