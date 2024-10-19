package MelonUtilities.mixins.tile_entities.golden_mesh;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.PlayerData;
import MelonUtilities.interfaces.TileEntityContainerInterface;
import MelonUtilities.utility.UUIDHelper;
import net.minecraft.core.block.BlockMeshGold;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockMeshGold.class, remap = false)
public class BlockMeshGoldMixin {
	@Inject(at = @At("HEAD"), method = "onBlockRightClicked", cancellable = true)
	public void onBlockRightClickedInject(World world, int x, int y, int z, EntityPlayer player, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {

		TileEntityContainerInterface iContainer = (TileEntityContainerInterface) world.getBlockTileEntity(x, y, z);

		if(iContainer.getIsLocked()){
			if(iContainer.getLockOwner() != null) {
				if (!iContainer.getLockOwner().equals(UUIDHelper.getUUIDFromName(player.username))
					&& !iContainer.getTrustedPlayers().contains(UUIDHelper.getUUIDFromName(player.username))
					&& !Data.playerData.getOrCreate(iContainer.getLockOwner().toString(), PlayerData.class).playersTrustedToAllContainers.contains(UUIDHelper.getUUIDFromName(player.username))
					&& !iContainer.getIsCommunityContainer()
					&& !Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(player.username).toString(), PlayerData.class).lockBypass){
					player.sendMessage(TextFormatting.RED + "Golden Mesh is Locked!");
					cir.setReturnValue(false);
					return;
				}
			}
		}
	}
}
