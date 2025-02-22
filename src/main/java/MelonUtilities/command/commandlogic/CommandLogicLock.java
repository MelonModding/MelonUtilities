package MelonUtilities.command.commandlogic;

import MelonUtilities.config.Data;
import MelonUtilities.interfaces.Lockable;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.feedback.FeedbackArg;
import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import com.mojang.brigadier.Command;
import net.minecraft.core.block.entity.*;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.util.helper.UUIDHelper;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;

import java.util.UUID;

public class CommandLogicLock {

	public static int lock(PlayerServer sender){
		HitResult rayCastResult = MUtil.rayCastFromPlayer(sender);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Lock Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = sender.world.getTileEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);
		if(container != null){
			if (container instanceof Lockable) {
				Lockable lockable = ((Lockable) container);
				if (!lockable.getIsLocked()) {
					if (container instanceof TileEntityChest) {
						Lockable otherLockable = (Lockable) MUtil.getOtherChest(sender.world, (TileEntityChest) container);
						if (otherLockable != null) {
							lockable.setIsLocked(true);
							otherLockable.setIsLocked(true);
							lockable.setLockOwner(sender.uuid);
							otherLockable.setLockOwner(sender.uuid);
							FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Locked Double Chest!");
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Locked Chest!");
					} else if (container instanceof TileEntityFurnaceBlast) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Locked Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Locked Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Locked Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Locked Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Locked Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Locked Basket!");
					} else if (container instanceof TileEntityActivator) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Locked Activator!");
					}

					lockable.setIsLocked(true);
					lockable.setLockOwner(sender.uuid);
					return Command.SINGLE_SUCCESS;

				} else if (lockable.getIsLocked() && !lockable.getLockOwner().equals(sender.uuid)) {
					FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Lock Container! (Not Owned By You)");
					return Command.SINGLE_SUCCESS;
				}
				FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Lock Container! (Already Locked)");
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Lock Container! (Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}

	public static int lockOnBlockPlaced(PlayerServer sender){
		UUID senderUUID = sender.uuid;

		if(Data.Users.getOrCreate(senderUUID).lockOnBlockPlaced){
			Data.Users.getOrCreate(senderUUID).lockOnBlockPlaced = false;
			Data.Users.save(senderUUID);
			FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Locking on Block Placed Disabled");
		} else {
			Data.Users.getOrCreate(senderUUID).lockOnBlockPlaced = true;
			Data.Users.save(senderUUID);
			FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Locking on Block Placed Enabled!");
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int lockOnBlockPunched(PlayerServer sender){
		UUID senderUUID = sender.uuid;

		if(Data.Users.getOrCreate(senderUUID).lockOnBlockPunched){
			Data.Users.getOrCreate(senderUUID).lockOnBlockPunched = false;
			Data.Users.save(senderUUID);
			FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Locking on Block Punched Disabled");
		} else {
			Data.Users.getOrCreate(senderUUID).lockOnBlockPunched = true;
			Data.Users.save(senderUUID);
			FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Locking on Block Punched Enabled!");
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int lockTrust(PlayerServer sender, String targetUsername){

		Pair<UUID, String> profile;
		try {
			profile = MUtil.getProfileFromUsername(targetUsername);
		} catch (NullPointerException e) {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Trust %s to Container! (Player Does not Exist)", new FeedbackArg(targetUsername));
			return 0;
		}
		String targetUsernameOrDisplayName = profile.getRight();
		UUID targetUUID = profile.getLeft();

		HitResult rayCastResult = MUtil.rayCastFromPlayer(sender);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Trust %s to Container! (Not Looking at Container)", new FeedbackArg(targetUsernameOrDisplayName));
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = sender.world.getTileEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if(container != null) {
			if (container instanceof Lockable) {
				Lockable lockable = ((Lockable) container);
				if (lockable.getIsLocked()) {

					if (!lockable.getLockOwner().equals(sender.uuid)) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Trust %s to Container! (Not Owned By You)", new FeedbackArg(targetUsernameOrDisplayName));
						return Command.SINGLE_SUCCESS;
					}

					if (lockable.getTrustedPlayers().contains(targetUUID)) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Trust %s to Container! (Player already Trusted)", new FeedbackArg(targetUsernameOrDisplayName));
						return Command.SINGLE_SUCCESS;
					}

					if (container instanceof TileEntityChest) {
						Lockable otherLockable = (Lockable) MUtil.getOtherChest(sender.world, (TileEntityChest) container);
						if (otherLockable != null) {
							lockable.addTrustedPlayer(targetUUID);
							otherLockable.addTrustedPlayer(targetUUID);
							FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted %s to this Double Chest!", new FeedbackArg(targetUsernameOrDisplayName));
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted %s to this Chest!", new FeedbackArg(targetUsernameOrDisplayName));
					} else if (container instanceof TileEntityFurnaceBlast) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted %s to this Blast Furnace!", new FeedbackArg(targetUsernameOrDisplayName));
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted %s to this Furnace!", new FeedbackArg(targetUsernameOrDisplayName));
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted %s to this Dispenser!", new FeedbackArg(targetUsernameOrDisplayName));
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted %s to this Golden Mesh!", new FeedbackArg(targetUsernameOrDisplayName));
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted %s to this Trommel!", new FeedbackArg(targetUsernameOrDisplayName));
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted %s to this Basket!", new FeedbackArg(targetUsernameOrDisplayName));
					} else if (container instanceof TileEntityActivator) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted %s to this Activator!", new FeedbackArg(targetUsernameOrDisplayName));
					}
					lockable.addTrustedPlayer(targetUUID);
				} else {
					FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Trust %s to Container! (Container not Locked)", new FeedbackArg(targetUsernameOrDisplayName));
				}
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Trust %s to Container! (Not Looking at Container)", new FeedbackArg(targetUsernameOrDisplayName));
		return Command.SINGLE_SUCCESS;
	}

	public static int lockTrustAll(PlayerServer sender, String targetUsername){
		PlayerServer target = MinecraftServer.getInstance().playerList.getPlayerEntity(targetUsername);
		UUID targetUUID;

		if(target != null){
			targetUUID = target.uuid;
			String targetDisplayName = target.getDisplayName();
			if(!Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.containsKey(targetUUID)){

				Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.put(targetUUID, targetUsername);
				Data.Users.save(sender.uuid);
				FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted %s to all Containers!", new FeedbackArg(targetDisplayName));
				return Command.SINGLE_SUCCESS;
			}
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Trust %s to all Containers! (Player is Already Trusted)", new FeedbackArg(targetDisplayName));
			return Command.SINGLE_SUCCESS;
		} else {
			UUIDHelper.runConversionAction(targetUsername, targetuuid ->
				{
					if(!Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.containsKey(targetuuid)){

						Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.put(targetuuid, targetUsername);
						Data.Users.save(sender.uuid);
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted %s to all Containers!", new FeedbackArg(targetUsername));
						return;
					}
					FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Trust %s to all Containers! (Player is Already Trusted)", new FeedbackArg(targetUsername));
				},
				username -> FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Trust %s to all Containers! (Player Does not Exist)", new FeedbackArg(targetUsername))
			);
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int lockTrustCommunity(PlayerServer sender) {
		HitResult rayCastResult = MUtil.rayCastFromPlayer(sender);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Trust Community to Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = sender.world.getTileEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if(container != null) {
			if (container instanceof Lockable) {
				Lockable lockable = ((Lockable) container);
				if (lockable.getIsLocked()) {

					if (!lockable.getLockOwner().equals(sender.uuid)) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Trust Community to Container! (Not Owned By You)");
						return Command.SINGLE_SUCCESS;
					}

					if (lockable.getIsCommunityContainer()){
						FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Trust Community to Container! (Community already Trusted)");
						return Command.SINGLE_SUCCESS;
					}

					if (container instanceof TileEntityChest) {
						Lockable otherLockable = (Lockable) MUtil.getOtherChest(sender.world, (TileEntityChest) container);
						if (otherLockable != null) {
							lockable.setIsCommunityContainer(true);
							otherLockable.setIsCommunityContainer(true);
							FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted Community to this Double Chest!");
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted Community to this Chest!");
					} else if (container instanceof TileEntityFurnaceBlast) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted Community to this Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted Community to this Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted Community to this Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted Community to this Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted Community to this Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted Community to this Basket!");
					} else if (container instanceof TileEntityActivator) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Trusted Community to this Activator!");
					}
					lockable.setIsCommunityContainer(true);
				} else {
					FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Trust Community to Container! (Container not Locked)");
				}
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Trust Community to Container! (Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}

	public static int lockUntrust(PlayerServer sender, String targetUsername){
		PlayerServer target = MinecraftServer.getInstance().playerList.getPlayerEntity(targetUsername);
		UUID targetUUID;
		String targetDisplayName;

		if(target != null){
			targetUUID = target.uuid;
			targetDisplayName = target.getDisplayName();
		} else {
			targetUUID = UUIDHelper.getUUIDFromName(targetUsername);
			if(targetUUID == null){
				FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Untrust %s from Container! (Player Does not Exist)", new FeedbackArg(targetUsername));
				return 0;
			}
			targetDisplayName = targetUsername;
		}

		HitResult rayCastResult = MUtil.rayCastFromPlayer(sender);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Untrust %s from Container! (Not Looking at Container)", new FeedbackArg(targetDisplayName));
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = sender.world.getTileEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if(container != null) {
			if (container instanceof Lockable) {
				Lockable lockable = ((Lockable) container);
				if (lockable.getIsLocked()) {

					if (!lockable.getLockOwner().equals(sender.uuid)) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Untrust %s from Container! (Not Owned By You)", new FeedbackArg(targetDisplayName));
						return Command.SINGLE_SUCCESS;
					}

					if (!lockable.getTrustedPlayers().contains(targetUUID)) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Untrust %s from Container! (Player not Trusted)", new FeedbackArg(targetDisplayName));
						return Command.SINGLE_SUCCESS;
					}

					if (container instanceof TileEntityChest) {
						Lockable otherLockable = (Lockable) MUtil.getOtherChest(sender.world, (TileEntityChest) container);
						if (otherLockable != null) {
							lockable.removeTrustedPlayer(targetUUID);
							otherLockable.removeTrustedPlayer(targetUUID);
							FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted %s from this Double Chest!", new FeedbackArg(targetDisplayName));
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted %s from this Chest!", new FeedbackArg(targetDisplayName));
					} else if (container instanceof TileEntityFurnaceBlast) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted %s from this Blast Furnace!", new FeedbackArg(targetDisplayName));
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted %s from this Furnace!", new FeedbackArg(targetDisplayName));
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted %s from this Dispenser!", new FeedbackArg(targetDisplayName));
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted %s from this Golden Mesh!", new FeedbackArg(targetDisplayName));
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted %s from this Trommel!", new FeedbackArg(targetDisplayName));
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted %s from this Basket!", new FeedbackArg(targetDisplayName));
					} else if (container instanceof TileEntityActivator) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted %s from this Activator!", new FeedbackArg(targetDisplayName));
					}
					lockable.removeTrustedPlayer(targetUUID);
				} else {
					FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Untrust %s from Container! (Container not Locked)", new FeedbackArg(targetDisplayName));
				}
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Untrust %s from Container! (Not Looking at Container)", new FeedbackArg(targetDisplayName));
		return Command.SINGLE_SUCCESS;
	}

	public static int lockUntrustAll(PlayerServer sender, String targetUsername){
		PlayerServer target = MinecraftServer.getInstance().playerList.getPlayerEntity(targetUsername);
		UUID targetUUID;

		if(target != null){
			targetUUID = target.uuid;
			String targetDisplayName = target.getDisplayName();
			if(Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.containsKey(targetUUID)){
				Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.remove(targetUUID);
				Data.Users.save(sender.uuid);
				FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted %s from all Containers!",  new FeedbackArg(targetDisplayName));
				return Command.SINGLE_SUCCESS;
			}
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Untrust %s from all Containers! (Player is Not Trusted)", new FeedbackArg(targetDisplayName));
			return Command.SINGLE_SUCCESS;
		} else {
			UUIDHelper.runConversionAction(targetUsername, targetuuid -> {
				if(Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.containsKey(targetuuid)){

					Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.remove(targetuuid, targetUsername);
					Data.Users.save(sender.uuid);
					FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted %s from all Containers!", new FeedbackArg(targetUsername));
					return;
				}
				FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Untrust %s from all Containers! (Player is Not Trusted)", new FeedbackArg(targetUsername));
			}, username -> FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Untrust %s from all Containers! (Player Does not Exist)", new FeedbackArg(targetUsername)));
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int lockUntrustCommunity(PlayerServer sender){
		HitResult rayCastResult = MUtil.rayCastFromPlayer(sender);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Untrust Community from Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = sender.world.getTileEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if(container != null) {
			if (container instanceof Lockable) {
				Lockable lockable = ((Lockable) container);
				if (lockable.getIsLocked()) {

					if (!lockable.getLockOwner().equals(sender.uuid)) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Untrust Community from Container! (Not Owned By You)");
						return Command.SINGLE_SUCCESS;
					}

					if (!lockable.getIsCommunityContainer()){
						FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Untrust Community from Container! (Community not Trusted)");
						return Command.SINGLE_SUCCESS;
					}

					if (container instanceof TileEntityChest) {
						Lockable otherLockable = (Lockable) MUtil.getOtherChest(sender.world, (TileEntityChest) container);
						if (otherLockable != null) {
							lockable.setIsCommunityContainer(false);
							otherLockable.setIsCommunityContainer(false);
							FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted Community from this Double Chest!");
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted Community from this Chest!");
					} else if (container instanceof TileEntityFurnaceBlast) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted Community from this Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted Community from this Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted Community from this Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted Community from this Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted Community from this Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted Community from this Basket!");
					} else if (container instanceof TileEntityActivator) {
						FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Untrusted Community from this Activator!");
					}
					lockable.setIsCommunityContainer(false);
				} else {
					FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Untrust Community from Container!");
					FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "(Container not Locked)");
				}
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to Untrust Community from Container!");
		FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "(Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}

	public static int lockBypass(PlayerServer sender){
		UUID senderUUID = sender.uuid;

		if(Data.Users.getOrCreate(senderUUID).lockBypass){
			Data.Users.getOrCreate(senderUUID).lockBypass = false;
			Data.Users.save(senderUUID);
			FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Lock Bypass Disabled");
		} else {
			Data.Users.getOrCreate(senderUUID).lockBypass = true;
			Data.Users.save(senderUUID);
			FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Lock Bypass Enabled!");
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int lockInfo(PlayerServer sender){
		HitResult rayCastResult = MUtil.rayCastFromPlayer(sender);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to get info from Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = sender.world.getTileEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if(container != null) {
			if (container instanceof Lockable) {
				Lockable lockable = ((Lockable) container);
					if (container instanceof TileEntityChest) {
						Lockable otherLockable = (Lockable) MUtil.getOtherChest(sender.world, (TileEntityChest) container);
						if (otherLockable != null) {
							MUtil.sendContainerLockInfo(sender, lockable, "Double Chest");
							return Command.SINGLE_SUCCESS;
						}
						MUtil.sendContainerLockInfo(sender, lockable, "Chest");
					} else if (container instanceof TileEntityFurnaceBlast) {
						MUtil.sendContainerLockInfo(sender, lockable, "Blast Furnace");
					} else if (container instanceof TileEntityFurnace) {
						MUtil.sendContainerLockInfo(sender, lockable, "Furnace");
					} else if (container instanceof TileEntityDispenser) {
						MUtil.sendContainerLockInfo(sender, lockable, "Dispenser");
					} else if (container instanceof TileEntityMeshGold) {
						MUtil.sendContainerLockInfo(sender, lockable, "Gold Mesh");
					} else if (container instanceof TileEntityTrommel) {
						MUtil.sendContainerLockInfo(sender, lockable, "Trommel");
					} else if (container instanceof TileEntityBasket) {
						MUtil.sendContainerLockInfo(sender, lockable, "Basket");
					} else if (container instanceof TileEntityActivator) {
						MUtil.sendContainerLockInfo(sender, lockable, "Activator");
					}
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Failed to get info from Container! (Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}
}
