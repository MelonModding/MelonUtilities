package BTAServerUtilities.mixins.tile_entities.trommel;

import BTAServerUtilities.config.Data;
import BTAServerUtilities.config.datatypes.PlayerData;
import BTAServerUtilities.interfaces.TileEntityContainerInterface;
import BTAServerUtilities.utility.UUIDHelper;
import net.minecraft.core.block.BlockTrommel;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockTrommel.class, remap = false)
public class BlockTrommelMixin {
	@Inject(at = @At("HEAD"), method = "onBlockRightClicked", cancellable = true)
	public void onBlockRightClickedInject(World world, int x, int y, int z, EntityPlayer player, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {

		TileEntityContainerInterface iContainer = (TileEntityContainerInterface) world.getBlockTileEntity(x, y, z);

		if(iContainer.getIsLocked()){
			if(iContainer.getLockOwner() != null) {
				if (!iContainer.getLockOwner().equals(UUIDHelper.getUUIDFromName(player.username))
					&& !iContainer.getTrustedPlayers().contains(UUIDHelper.getUUIDFromName(player.username))
					&& !Data.playerData.getOrCreate(iContainer.getLockOwner().toString(), PlayerData.class).playersTrustedToAllContainers.contains(UUIDHelper.getUUIDFromName(player.username))){
					player.sendMessage("§eTrommel is Locked!");
					cir.setReturnValue(false);
					return;
				}
			}
		}
	}
}
