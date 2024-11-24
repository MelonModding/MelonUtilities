package MelonUtilities.commands.lock;

import MelonUtilities.config.Data;
import MelonUtilities.interfaces.TileEntityContainerInterface;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.feedback.FeedbackHandler;
import com.mojang.brigadier.Command;
import net.minecraft.core.block.entity.*;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.util.helper.UUIDHelper;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;

import java.util.UUID;

public class CommandLogicLock {
	/*
	 Naming Scheme for methods in this class is:

	 (arg = command argument/literal)
	 Ex: [ arg_arg_arg ]

	 Naming can also include arguments in all caps:
	 Ex: [ arg.arg.ARG ]

	 !!!Only use capitalized arguments when necessary!!!
	 Capitalized arguments should only be used for arguments that are NOT literals, and are variable.
	 Specifically when two methods share the same base command, and need to be differentiated from each-other

	 Ex: [ role_set_defaultrole_ROLEID ]
	 	 [ role_set_defaultrole_none ]

	 * Note that both methods share the same parent argument (defaultrole), and that none is a literal (so it is not capitalized)

	 PS. Arguments inside the method name should match their registered name/literal in the ArgumentBuilder for their respective command
	*/

	public static int lock(Player sender){
		HitResult rayCastResult = MUtil.rayCastFromPlayer(sender);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandler.error(sender, "Failed to Lock Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = sender.world.getBlockEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);
		if(container != null){
			if (container instanceof TileEntityContainerInterface) {
				TileEntityContainerInterface containerInterface = ((TileEntityContainerInterface) container);
				if (!containerInterface.getIsLocked()) {
					if (container instanceof TileEntityChest) {
						TileEntityContainerInterface otherContainerInterface = (TileEntityContainerInterface) MUtil.getOtherChest(sender.world, (TileEntityChest) container);
						if (otherContainerInterface != null) {
							containerInterface.setIsLocked(true);
							otherContainerInterface.setIsLocked(true);
							containerInterface.setLockOwner(sender.uuid);
							otherContainerInterface.setLockOwner(sender.uuid);
							FeedbackHandler.success(sender, "Locked Double Chest!");
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandler.success(sender, "Locked Chest!");
					} else if (container instanceof TileEntityFurnaceBlast) {
						FeedbackHandler.success(sender, "Locked Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandler.success(sender, "Locked Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandler.success(sender, "Locked Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandler.success(sender, "Locked Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandler.success(sender, "Locked Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandler.success(sender, "Locked Basket!");
					}

					containerInterface.setIsLocked(true);
					containerInterface.setLockOwner(sender.uuid);
					return Command.SINGLE_SUCCESS;

				} else if (containerInterface.getIsLocked() && !containerInterface.getLockOwner().equals(sender.uuid)) {
					FeedbackHandler.error(sender, "Failed to Lock Container! (Not Owned By You)");
					return Command.SINGLE_SUCCESS;
				}
				FeedbackHandler.error(sender, "Failed to Lock Container! (Already Locked)");
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandler.error(sender, "Failed to Lock Container! (Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_onblockplaced(Player sender){
		UUID senderUUID = sender.uuid;

		if(Data.Users.getOrCreate(senderUUID).lockOnBlockPlaced){
			Data.Users.getOrCreate(senderUUID).lockOnBlockPlaced = false;
			Data.Users.save(senderUUID);
			FeedbackHandler.destructive(sender, "Locking on Block Placed Disabled");
		} else {
			Data.Users.getOrCreate(senderUUID).lockOnBlockPlaced = true;
			Data.Users.save(senderUUID);
			FeedbackHandler.success(sender, "Locking on Block Placed Enabled!");
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_onblockpunched(Player sender){
		UUID senderUUID = sender.uuid;

		if(Data.Users.getOrCreate(senderUUID).lockOnBlockPunched){
			Data.Users.getOrCreate(senderUUID).lockOnBlockPunched = false;
			Data.Users.save(senderUUID);
			FeedbackHandler.destructive(sender, "Locking on Block Punched Disabled");
		} else {
			Data.Users.getOrCreate(senderUUID).lockOnBlockPunched = true;
			Data.Users.save(senderUUID);
			FeedbackHandler.success(sender, "Locking on Block Punched Enabled!");
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_trust(Player sender, String targetUsername){

		Pair<UUID, String> profile;
		try {
			profile = MUtil.getProfileFromUsername(targetUsername);
		} catch (NullPointerException e) {
			FeedbackHandler.error(sender, "Failed to Trust %" + targetUsername + "% to Container! (%" + targetUsername + "% Does not Exist)");
			return 0;
		}
		String targetUsernameOrDisplayName = profile.getRight();
		UUID targetUUID = profile.getLeft();

		HitResult rayCastResult = MUtil.rayCastFromPlayer(sender);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandler.error(sender, "Failed to Trust %" + targetUsernameOrDisplayName + "% to Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = sender.world.getBlockEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if(container != null) {
			if (container instanceof TileEntityContainerInterface) {
				TileEntityContainerInterface containerInterface = ((TileEntityContainerInterface) container);
				if (containerInterface.getIsLocked()) {

					if (!containerInterface.getLockOwner().equals(sender.uuid)) {
						FeedbackHandler.error(sender, "Failed to Trust %" + targetUsernameOrDisplayName + "% to Container! (Not Owned By You)");
						return Command.SINGLE_SUCCESS;
					}

					if (containerInterface.getTrustedPlayers().contains(targetUUID)) {
						FeedbackHandler.error(sender, "Failed to Trust %" + targetUsernameOrDisplayName + "% to Container! (Player already Trusted)");
						return Command.SINGLE_SUCCESS;
					}

					if (container instanceof TileEntityChest) {
						TileEntityContainerInterface otherContainerInterface = (TileEntityContainerInterface) MUtil.getOtherChest(sender.world, (TileEntityChest) container);
						if (otherContainerInterface != null) {
							containerInterface.addTrustedPlayer(targetUUID);
							otherContainerInterface.addTrustedPlayer(targetUUID);
							FeedbackHandler.success(sender, "Trusted %" + targetUsernameOrDisplayName + "% to this Double Chest!");
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandler.success(sender, "Trusted %" + targetUsernameOrDisplayName + "% to this Chest!");
					} else if (container instanceof TileEntityFurnaceBlast) {
						FeedbackHandler.success(sender, "Trusted %" + targetUsernameOrDisplayName + "% to this Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandler.success(sender, "Trusted %" + targetUsernameOrDisplayName + "% to this Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandler.success(sender, "Trusted %" + targetUsernameOrDisplayName + "% to this Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandler.success(sender, "Trusted %" + targetUsernameOrDisplayName + "% to this Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandler.success(sender, "Trusted %" + targetUsernameOrDisplayName + "% to this Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandler.success(sender, "Trusted %" + targetUsernameOrDisplayName + "% to this Basket!");
					}
					containerInterface.addTrustedPlayer(targetUUID);
				} else {
					FeedbackHandler.error(sender, "Failed to Trust %" + targetUsernameOrDisplayName + "% to Container! (Container not Locked)");
				}
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandler.error(sender, "Failed to Trust %" + targetUsernameOrDisplayName + "% to Container! (Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_trustall(Player sender, String targetUsername){
		PlayerServer target = MinecraftServer.getInstance().playerList.getPlayerEntity(targetUsername);
		UUID targetUUID;

		if(target != null){
			targetUUID = target.uuid;
			String targetDisplayName = target.getDisplayName();
			if(!Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.containsKey(targetUUID)){

				Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.put(targetUUID, targetUsername);
				Data.Users.save(sender.uuid);
				FeedbackHandler.success(sender, "Trusted %" + targetDisplayName + "% to all Containers!");
				return Command.SINGLE_SUCCESS;
			}
			FeedbackHandler.error(sender, "Failed to Trust %" + targetDisplayName + "% to all Containers! (Player is Already Trusted)");
			return Command.SINGLE_SUCCESS;
		} else {
			UUIDHelper.runConversionAction(targetUsername, targetuuid ->
				{
					if(!Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.containsKey(targetuuid)){

						Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.put(targetuuid, targetUsername);
						Data.Users.save(sender.uuid);
						FeedbackHandler.success(sender, "Trusted %" + targetUsername + "% to all Containers!");
						return;
					}
					FeedbackHandler.error(sender, "Failed to Trust %" + targetUsername + "% to all Containers! (Player is Already Trusted)");
				},
				username -> FeedbackHandler.error(sender, "Failed to Trust %" + targetUsername + "% to all Containers! (%" + targetUsername + "% Does not Exist)")
			);
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_trustcommunity(Player sender) {
		HitResult rayCastResult = MUtil.rayCastFromPlayer(sender);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandler.error(sender, "Failed to Trust Community to Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = sender.world.getBlockEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if(container != null) {
			if (container instanceof TileEntityContainerInterface) {
				TileEntityContainerInterface containerInterface = ((TileEntityContainerInterface) container);
				if (containerInterface.getIsLocked()) {

					if (!containerInterface.getLockOwner().equals(sender.uuid)) {
						FeedbackHandler.error(sender, "Failed to Trust Community to Container! (Not Owned By You)");
						return Command.SINGLE_SUCCESS;
					}

					if (containerInterface.getIsCommunityContainer()){
						FeedbackHandler.error(sender, "Failed to Trust Community to Container! (Community already Trusted)");
						return Command.SINGLE_SUCCESS;
					}

					if (container instanceof TileEntityChest) {
						TileEntityContainerInterface otherContainerInterface = (TileEntityContainerInterface) MUtil.getOtherChest(sender.world, (TileEntityChest) container);
						if (otherContainerInterface != null) {
							containerInterface.setIsCommunityContainer(true);
							otherContainerInterface.setIsCommunityContainer(true);
							FeedbackHandler.success(sender, "Trusted Community to this Double Chest!");
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandler.success(sender, "Trusted Community to this Chest!");
					} else if (container instanceof TileEntityFurnaceBlast) {
						FeedbackHandler.success(sender, "Trusted Community to this Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandler.success(sender, "Trusted Community to this Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandler.success(sender, "Trusted Community to this Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandler.success(sender, "Trusted Community to this Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandler.success(sender, "Trusted Community to this Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandler.success(sender, "Trusted Community to this Basket!");
					}
					containerInterface.setIsCommunityContainer(true);
				} else {
					FeedbackHandler.error(sender, "Failed to Trust Community to Container! (Container not Locked)");
				}
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandler.error(sender, "Failed to Trust Community to Container! (Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_untrust(Player sender, String targetUsername){
		PlayerServer target = MinecraftServer.getInstance().playerList.getPlayerEntity(targetUsername);
		UUID targetUUID;
		String targetDisplayName;

		if(target != null){
			targetUUID = target.uuid;
			targetDisplayName = target.getDisplayName();
		} else {
			targetUUID = UUIDHelper.getUUIDFromName(targetUsername);
			if(targetUUID == null){
				FeedbackHandler.error(sender, "Failed to Untrust %" + targetUsername + "% from Container! (%" + targetUsername + "% Does not Exist)");
				return 0;
			}
			targetDisplayName = targetUsername;
		}

		HitResult rayCastResult = MUtil.rayCastFromPlayer(sender);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandler.error(sender, "Failed to Untrust %" + targetDisplayName + "% from Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = sender.world.getBlockEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if(container != null) {
			if (container instanceof TileEntityContainerInterface) {
				TileEntityContainerInterface containerInterface = ((TileEntityContainerInterface) container);
				if (containerInterface.getIsLocked()) {

					if (!containerInterface.getLockOwner().equals(sender.uuid)) {
						FeedbackHandler.error(sender, "Failed to Untrust %" + targetDisplayName + "% from Container! (Not Owned By You)");
						return Command.SINGLE_SUCCESS;
					}

					if (!containerInterface.getTrustedPlayers().contains(targetUUID)) {
						FeedbackHandler.error(sender, "Failed to Untrust %" + targetDisplayName + "% from Container! (Player not Trusted)");
						return Command.SINGLE_SUCCESS;
					}

					if (container instanceof TileEntityChest) {
						TileEntityContainerInterface otherContainerInterface = (TileEntityContainerInterface) MUtil.getOtherChest(sender.world, (TileEntityChest) container);
						if (otherContainerInterface != null) {
							containerInterface.removeTrustedPlayer(targetUUID);
							otherContainerInterface.removeTrustedPlayer(targetUUID);
							FeedbackHandler.destructive(sender, "Untrusted %" + targetDisplayName + "% from this Double Chest!");
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandler.destructive(sender, "Untrusted %" + targetDisplayName + "% from this Chest!");
					} else if (container instanceof TileEntityFurnaceBlast) {
						FeedbackHandler.destructive(sender, "Untrusted %" + targetDisplayName + "% from this Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandler.destructive(sender, "Untrusted %" + targetDisplayName + "% from this Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandler.destructive(sender, "Untrusted %" + targetDisplayName + "% from this Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandler.destructive(sender, "Untrusted %" + targetDisplayName + "% from this Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandler.destructive(sender, "Untrusted %" + targetDisplayName + "% from this Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandler.destructive(sender, "Untrusted %" + targetDisplayName + "% from this Basket!");
					}
					containerInterface.removeTrustedPlayer(targetUUID);
				} else {
					FeedbackHandler.error(sender, "Failed to Untrust %" + targetDisplayName + "% from Container! (Container not Locked)");
				}
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandler.error(sender, "Failed to Untrust %" + targetDisplayName + "% from Container! (Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_untrustall(Player sender, String targetUsername){
		PlayerServer target = MinecraftServer.getInstance().playerList.getPlayerEntity(targetUsername);
		UUID targetUUID;

		if(target != null){
			targetUUID = target.uuid;
			String targetDisplayName = target.getDisplayName();
			if(Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.containsKey(targetUUID)){
				Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.remove(targetUUID);
				Data.Users.save(sender.uuid);
				FeedbackHandler.destructive(sender, "Untrusted %" + targetDisplayName + "% from all Containers!");
				return Command.SINGLE_SUCCESS;
			}
			FeedbackHandler.error(sender, "Failed to Untrust %" + targetDisplayName + "% from all Containers! (Player is Not Trusted)");
			return Command.SINGLE_SUCCESS;
		} else {
			UUIDHelper.runConversionAction(targetUsername, targetuuid -> {
				if(Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.containsKey(targetuuid)){

					Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.remove(targetuuid, targetUsername);
					Data.Users.save(sender.uuid);
					FeedbackHandler.destructive(sender, "Untrusted %" + targetUsername + "% from all Containers!");
					return;
				}
				FeedbackHandler.error(sender, "Failed to Untrust %" + targetUsername + "% from all Containers! (Player is Not Trusted)");
			}, username -> FeedbackHandler.error(sender, "Failed to Untrust %" + targetUsername + "% from all Containers! (%" + targetUsername + "% Does not Exist)"));
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_untrustcommunity(Player sender){
		HitResult rayCastResult = MUtil.rayCastFromPlayer(sender);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandler.error(sender, "Failed to Untrust Community from Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = sender.world.getBlockEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if(container != null) {
			if (container instanceof TileEntityContainerInterface) {
				TileEntityContainerInterface containerInterface = ((TileEntityContainerInterface) container);
				if (containerInterface.getIsLocked()) {

					if (!containerInterface.getLockOwner().equals(sender.uuid)) {
						FeedbackHandler.error(sender, "Failed to Untrust Community from Container! (Not Owned By You)");
						return Command.SINGLE_SUCCESS;
					}

					if (!containerInterface.getIsCommunityContainer()){
						FeedbackHandler.error(sender, "Failed to Untrust Community from Container! (Community not Trusted)");
						return Command.SINGLE_SUCCESS;
					}

					if (container instanceof TileEntityChest) {
						TileEntityContainerInterface otherContainerInterface = (TileEntityContainerInterface) MUtil.getOtherChest(sender.world, (TileEntityChest) container);
						if (otherContainerInterface != null) {
							containerInterface.setIsCommunityContainer(false);
							otherContainerInterface.setIsCommunityContainer(false);
							FeedbackHandler.destructive(sender, "Untrusted Community from this Double Chest!");
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandler.destructive(sender, "Untrusted Community from this Chest!");
					} else if (container instanceof TileEntityFurnaceBlast) {
						FeedbackHandler.destructive(sender, "Untrusted Community from this Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandler.destructive(sender, "Untrusted Community from this Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandler.destructive(sender, "Untrusted Community from this Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandler.destructive(sender, "Untrusted Community from this Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandler.destructive(sender, "Untrusted Community from this Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandler.destructive(sender, "Untrusted Community from this Basket!");
					}
					containerInterface.setIsCommunityContainer(false);
				} else {
					FeedbackHandler.error(sender, "Failed to Untrust Community from Container!");
					FeedbackHandler.error(sender, "(Container not Locked)");
				}
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandler.error(sender, "Failed to Untrust Community from Container!");
		FeedbackHandler.error(sender, "(Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_bypass(Player sender){
		UUID senderUUID = sender.uuid;

		if(Data.Users.getOrCreate(senderUUID).lockBypass){
			Data.Users.getOrCreate(senderUUID).lockBypass = false;
			Data.Users.save(senderUUID);
			FeedbackHandler.destructive(sender, "Lock Bypass Disabled");
		} else {
			Data.Users.getOrCreate(senderUUID).lockBypass = true;
			Data.Users.save(senderUUID);
			FeedbackHandler.success(sender, "Lock Bypass Enabled!");
		}
		return Command.SINGLE_SUCCESS;
	}
}
