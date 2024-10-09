package BTAServerUtilities.mixins.tile_entities.golden_mesh;

import BTAServerUtilities.config.Data;
import BTAServerUtilities.config.datatypes.PlayerData;
import BTAServerUtilities.interfaces.TileEntityContainerInterface;
import BTAServerUtilities.utility.UUIDHelper;
import net.minecraft.core.block.BlockMeshGold;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.packet.Packet53BlockChange;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.server.net.handler.NetServerHandler;
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
					&& !Data.playerData.getOrCreate(iContainer.getLockOwner().toString(), PlayerData.class).playersTrustedToAllContainers.contains(UUIDHelper.getUUIDFromName(player.username))){
					player.sendMessage("§eGolden Mesh is Locked!");
					cir.setReturnValue(false);
					return;
				}
			}
		}
	}
}
