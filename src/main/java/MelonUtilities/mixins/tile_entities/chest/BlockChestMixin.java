package MelonUtilities.mixins.tile_entities.chest;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.PlayerData;
import MelonUtilities.interfaces.BlockEntityContainerInterface;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.UUIDHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.block.ChestBlock;
import net.minecraft.core.block.entity.ChestBlockEntity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChestBlock.class, remap = false)
public class ChestBlockMixin {
	@Inject(at = @At("HEAD"), method = "onBlockRightClicked", cancellable = true)
	public void onBlockRightClickedInject(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {

		BlockEntityContainerInterface iContainer = (BlockEntityContainerInterface) world.getBlockEntity(x, y, z);

		if(iContainer.getIsLocked()){
			if(iContainer.getLockOwner() != null) {
				if (!iContainer.getLockOwner().equals(UUIDHelper.getUUIDFromName(player.username))
					&& !iContainer.getTrustedPlayers().contains(UUIDHelper.getUUIDFromName(player.username))
					&& !Data.playerData.getOrCreate(iContainer.getLockOwner().toString(), PlayerData.class).playersTrustedToAllContainers.contains(UUIDHelper.getUUIDFromName(player.username))
					&& !iContainer.getIsCommunityContainer()
					&& !Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(player.username).toString(), PlayerData.class).lockBypass){
					player.sendMessage(TextFormatting.RED + "Chest is Locked!");
					cir.setReturnValue(false);
					return;
				}
			}
		}
	}

	@Inject(at = @At("TAIL"), method = "onBlockPlacedByMob", cancellable = true)
	public void onBlockPlacedInject(World world, int x, int y, int z, Side placeSide, Mob mob, double xPlaced, double yPlaced, CallbackInfo ci, @Local(name = "type") ChestBlock.Type type) {
		ChestBlockEntity existingChest = MUtil.getOtherChest(world, (ChestBlockEntity) world.getBlockEntity(x, y, z));
		ChestBlockEntity placedChest = (ChestBlockEntity) world.getBlockEntity(x, y, z);

		BlockEntityContainerInterface existingIContainer = (BlockEntityContainerInterface) existingChest;
		BlockEntityContainerInterface placedIContainer = (BlockEntityContainerInterface) placedChest;

		if(existingIContainer != null) {
			placedIContainer.setLockOwner(existingIContainer.getLockOwner());
			placedIContainer.setIsLocked(existingIContainer.getIsLocked());
			placedIContainer.setTrustedPlayers(existingIContainer.getTrustedPlayers());
		}
	}
}
