package MelonUtilities.commands.lock;

import MelonUtilities.config.Data;
import MelonUtilities.interfaces.TileEntityContainerInterface;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.feedback.FeedbackHandler;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.core.block.entity.*;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.util.helper.UUIDHelper;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;

import java.util.UUID;

public class LockLogic {
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

	public static int lock(CommandContext<CommandSource> context){
		CommandSource source = context.getSource();
		Player sender = source.getSender();

		HitResult rayCastResult = MUtil.rayCastFromPlayer(context);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandler.error(context, "Failed to Lock Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = source.getWorld().getBlockEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);
		if(container != null){
			if (container instanceof TileEntityContainerInterface) {
				TileEntityContainerInterface containerInterface = ((TileEntityContainerInterface) container);
				if (!containerInterface.getIsLocked()) {
					if (container instanceof TileEntityChest) {
						TileEntityContainerInterface otherContainerInterface = (TileEntityContainerInterface) MUtil.getOtherChest(source.getWorld(), (TileEntityChest) container);
						if (otherContainerInterface != null) {
							containerInterface.setIsLocked(true);
							otherContainerInterface.setIsLocked(true);
							containerInterface.setLockOwner(sender.uuid);
							otherContainerInterface.setLockOwner(sender.uuid);
							FeedbackHandler.success(context, "Locked Double Chest!");
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandler.success(context, "Locked Chest!");
					} else if (container instanceof TileEntityFurnaceBlast) {
						FeedbackHandler.success(context, "Locked Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandler.success(context, "Locked Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandler.success(context, "Locked Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandler.success(context, "Locked Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandler.success(context, "Locked Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandler.success(context, "Locked Basket!");
					}

					containerInterface.setIsLocked(true);
					containerInterface.setLockOwner(sender.uuid);
					return Command.SINGLE_SUCCESS;

				} else if (containerInterface.getIsLocked() && !containerInterface.getLockOwner().equals(sender.uuid)) {
					FeedbackHandler.error(context, "Failed to Lock Container! (Not Owned By You)");
					return Command.SINGLE_SUCCESS;
				}
				FeedbackHandler.error(context, "Failed to Lock Container! (Already Locked)");
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandler.error(context, "Failed to Lock Container! (Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_onblockplaced(CommandContext<CommandSource> context){
		CommandSource source = context.getSource();
		boolean value = context.getArgument("value", Boolean.class);
		UUID senderUUID = source.getSender().uuid;

		if(value) {
			if(!Data.Users.getOrCreate(senderUUID).lockOnBlockPlaced){
				Data.Users.getOrCreate(senderUUID).lockOnBlockPlaced = true;
				Data.Users.save(senderUUID);
				FeedbackHandler.success(context, "Locking on Block Placed set to %" + true);
				return Command.SINGLE_SUCCESS;
			}
			FeedbackHandler.error(context, "Failed to set Locking on Block Placed.. (Already %true%)");
		}
		else {
			if(Data.Users.getOrCreate(senderUUID).lockOnBlockPlaced) {
				Data.Users.getOrCreate(senderUUID).lockOnBlockPlaced = false;
				Data.Users.save(senderUUID);
				FeedbackHandler.success(context, "Locking on Block Placed set to %" + false);
				return Command.SINGLE_SUCCESS;
			}
			FeedbackHandler.error(context, "Failed to set Locking on Block Placed.. (Already %false%)");
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_onblockpunched(CommandContext<CommandSource> context){
		CommandSource source = context.getSource();
		boolean value = context.getArgument("value", Boolean.class);
		UUID senderUUID = source.getSender().uuid;

		if(value) {
			if(!Data.Users.getOrCreate(senderUUID).lockOnBlockPunched){
				Data.Users.getOrCreate(senderUUID).lockOnBlockPunched = true;
				Data.Users.save(senderUUID);
				FeedbackHandler.success(context, "Locking on Block Punched set to %" + true);
				return Command.SINGLE_SUCCESS;
			}
			FeedbackHandler.error(context, "Failed to set Locking on Block Punched.. (Already %true%)");
		}
		else {
			if(Data.Users.getOrCreate(senderUUID).lockOnBlockPunched) {
				Data.Users.getOrCreate(senderUUID).lockOnBlockPunched = false;
				Data.Users.save(senderUUID);
				FeedbackHandler.success(context, "Locking on Block Punched set to %" + false);
				return Command.SINGLE_SUCCESS;
			}
			FeedbackHandler.error(context, "Failed to set Locking on Block Punched.. (Already %false%)");
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_trust(CommandContext<CommandSource> context){
		CommandSource source = context.getSource();
		Player sender = source.getSender();
		String targetUsername = context.getArgument("username", String.class).toLowerCase();

		//TODO Make all other UUID and DisplayName get -> getProfileFromUsername

		Pair<UUID, String> profile;
		try {
			profile = MUtil.getProfileFromUsername(targetUsername);
		} catch (NullPointerException e) {
			FeedbackHandler.error(sender, "Failed to Trust %" + targetUsername + "% to Container! (%" + targetUsername + "% Does not Exist)");
			return 0;
		}

		String targetUsernameOrDisplayName = profile.getRight();
		UUID targetUUID = profile.getLeft();

		HitResult rayCastResult = MUtil.rayCastFromPlayer(context);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandler.error(context, "Failed to Trust %" + targetUsernameOrDisplayName + "% to Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = source.getWorld().getBlockEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if(container != null) {
			if (container instanceof TileEntityContainerInterface) {
				TileEntityContainerInterface containerInterface = ((TileEntityContainerInterface) container);
				if (containerInterface.getIsLocked()) {

					if (!containerInterface.getLockOwner().equals(sender.uuid)) {
						FeedbackHandler.error(context, "Failed to Trust %" + targetUsernameOrDisplayName + "% to Container! (Not Owned By You)");
						return Command.SINGLE_SUCCESS;
					}

					if (containerInterface.getTrustedPlayers().contains(targetUUID)) {
						FeedbackHandler.error(context, "Failed to Trust %" + targetUsernameOrDisplayName + "% to Container! (Player already Trusted)");
						return Command.SINGLE_SUCCESS;
					}

					if (container instanceof TileEntityChest) {
						TileEntityContainerInterface otherContainerInterface = (TileEntityContainerInterface) MUtil.getOtherChest(source.getWorld(), (TileEntityChest) container);
						if (otherContainerInterface != null) {
							containerInterface.addTrustedPlayer(targetUUID);
							otherContainerInterface.addTrustedPlayer(targetUUID);
							FeedbackHandler.success(context, "Trusted %" + targetUsernameOrDisplayName + "% to this Double Chest!");
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandler.success(context, "Trusted %" + targetUsernameOrDisplayName + "% to this Chest!");
					} else if (container instanceof TileEntityFurnaceBlast) {
						FeedbackHandler.success(context, "Trusted %" + targetUsernameOrDisplayName + "% to this Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandler.success(context, "Trusted %" + targetUsernameOrDisplayName + "% to this Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandler.success(context, "Trusted %" + targetUsernameOrDisplayName + "% to this Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandler.success(context, "Trusted %" + targetUsernameOrDisplayName + "% to this Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandler.success(context, "Trusted %" + targetUsernameOrDisplayName + "% to this Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandler.success(context, "Trusted %" + targetUsernameOrDisplayName + "% to this Basket!");
					}
					containerInterface.addTrustedPlayer(targetUUID);
				} else {
					FeedbackHandler.error(context, "Failed to Trust %" + targetUsernameOrDisplayName + "% to Container! (Container not Locked)");
				}
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandler.error(context, "Failed to Trust %" + targetUsernameOrDisplayName + "% to Container! (Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_trustall(CommandContext<CommandSource> context){
		CommandSource source = context.getSource();
		Player sender = source.getSender();

		String targetUsername = context.getArgument("username", String.class).toLowerCase();
		PlayerServer target = MinecraftServer.getInstance().playerList.getPlayerEntity(targetUsername);
		UUID targetUUID;

		if(target != null){
			targetUUID = target.uuid;
			String targetDisplayName = target.getDisplayName();
			if(!Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.containsKey(targetUUID)){

				Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.put(targetUUID, targetUsername);
				Data.Users.save(sender.uuid);
				FeedbackHandler.success(context, "Trusted %" + targetDisplayName + "% to all Containers!");
				return Command.SINGLE_SUCCESS;
			}
			FeedbackHandler.error(context, "Failed to Trust %" + targetDisplayName + "% to all Containers! (Player is Already Trusted)");
			return Command.SINGLE_SUCCESS;
		} else {
			UUIDHelper.runConversionAction(targetUsername, targetuuid -> {
				if(!Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.containsKey(targetuuid)){

					Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.put(targetuuid, targetUsername);
					Data.Users.save(sender.uuid);
					FeedbackHandler.success(context, "Trusted %" + targetUsername + "% to all Containers!");
					return;
				}
				FeedbackHandler.error(context, "Failed to Trust %" + targetUsername + "% to all Containers! (Player is Already Trusted)");
			}, username -> FeedbackHandler.error(context, "Failed to Trust %" + targetUsername + "% to all Containers! (%" + targetUsername + "% Does not Exist)"));
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_trustcommunity(CommandContext<CommandSource> context) {
		CommandSource source = context.getSource();
		Player sender = source.getSender();

		HitResult rayCastResult = MUtil.rayCastFromPlayer(context);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandler.error(context, "Failed to Trust Community to Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = source.getWorld().getBlockEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if(container != null) {
			if (container instanceof TileEntityContainerInterface) {
				TileEntityContainerInterface containerInterface = ((TileEntityContainerInterface) container);
				if (containerInterface.getIsLocked()) {

					if (!containerInterface.getLockOwner().equals(sender.uuid)) {
						FeedbackHandler.error(context, "Failed to Trust Community to Container! (Not Owned By You)");
						return Command.SINGLE_SUCCESS;
					}

					if (containerInterface.getIsCommunityContainer()){
						FeedbackHandler.error(context, "Failed to Trust Community to Container! (Community already Trusted)");
						return Command.SINGLE_SUCCESS;
					}

					if (container instanceof TileEntityChest) {
						TileEntityContainerInterface otherContainerInterface = (TileEntityContainerInterface) MUtil.getOtherChest(source.getWorld(), (TileEntityChest) container);
						if (otherContainerInterface != null) {
							containerInterface.setIsCommunityContainer(true);
							otherContainerInterface.setIsCommunityContainer(true);
							FeedbackHandler.success(context, "Trusted Community to this Double Chest!");
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandler.success(context, "Trusted Community to this Chest!");
					} else if (container instanceof TileEntityFurnaceBlast) {
						FeedbackHandler.success(context, "Trusted Community to this Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandler.success(context, "Trusted Community to this Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandler.success(context, "Trusted Community to this Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandler.success(context, "Trusted Community to this Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandler.success(context, "Trusted Community to this Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandler.success(context, "Trusted Community to this Basket!");
					}
					containerInterface.setIsCommunityContainer(true);
				} else {
					FeedbackHandler.error(context, "Failed to Trust Community to Container! (Container not Locked)");
				}
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandler.error(context, "Failed to Trust Community to Container! (Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_untrust(CommandContext<CommandSource> context){
		CommandSource source = context.getSource();
		Player sender = source.getSender();

		String targetUsername = context.getArgument("username", String.class).toLowerCase();
		PlayerServer target = MinecraftServer.getInstance().playerList.getPlayerEntity(targetUsername);
		UUID targetUUID;
		String targetDisplayName;

		if(target != null){
			targetUUID = target.uuid;
			targetDisplayName = target.getDisplayName();
		} else {
			targetUUID = UUIDHelper.getUUIDFromName(targetUsername);
			if(targetUUID == null){
				FeedbackHandler.error(context, "Failed to Untrust %" + targetUsername + "% from Container! (%" + targetUsername + "% Does not Exist)");
				return 0;
			}
			targetDisplayName = targetUsername;
		}

		HitResult rayCastResult = MUtil.rayCastFromPlayer(context);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandler.error(context, "Failed to Untrust %" + targetDisplayName + "% from Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = source.getWorld().getBlockEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if(container != null) {
			if (container instanceof TileEntityContainerInterface) {
				TileEntityContainerInterface containerInterface = ((TileEntityContainerInterface) container);
				if (containerInterface.getIsLocked()) {

					if (!containerInterface.getLockOwner().equals(sender.uuid)) {
						FeedbackHandler.error(context, "Failed to Untrust %" + targetDisplayName + "% from Container! (Not Owned By You)");
						return Command.SINGLE_SUCCESS;
					}

					if (!containerInterface.getTrustedPlayers().contains(targetUUID)) {
						FeedbackHandler.error(context, "Failed to Untrust %" + targetDisplayName + "% from Container! (Player not Trusted)");
						return Command.SINGLE_SUCCESS;
					}

					if (container instanceof TileEntityChest) {
						TileEntityContainerInterface otherContainerInterface = (TileEntityContainerInterface) MUtil.getOtherChest(source.getWorld(), (TileEntityChest) container);
						if (otherContainerInterface != null) {
							containerInterface.removeTrustedPlayer(targetUUID);
							otherContainerInterface.removeTrustedPlayer(targetUUID);
							FeedbackHandler.destructive(context, "Untrusted %" + targetDisplayName + "% from this Double Chest!");
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandler.destructive(context, "Untrusted %" + targetDisplayName + "% from this Chest!");
					} else if (container instanceof TileEntityFurnaceBlast) {
						FeedbackHandler.destructive(context, "Untrusted %" + targetDisplayName + "% from this Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandler.destructive(context, "Untrusted %" + targetDisplayName + "% from this Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandler.destructive(context, "Untrusted %" + targetDisplayName + "% from this Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandler.destructive(context, "Untrusted %" + targetDisplayName + "% from this Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandler.destructive(context, "Untrusted %" + targetDisplayName + "% from this Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandler.destructive(context, "Untrusted %" + targetDisplayName + "% from this Basket!");
					}
					containerInterface.removeTrustedPlayer(targetUUID);
				} else {
					FeedbackHandler.error(context, "Failed to Untrust %" + targetDisplayName + "% from Container! (Container not Locked)");
				}
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandler.error(context, "Failed to Untrust %" + targetDisplayName + "% from Container! (Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_untrustall(CommandContext<CommandSource> context){
		CommandSource source = context.getSource();
		Player sender = source.getSender();

		String targetUsername = context.getArgument("username", String.class).toLowerCase();
		PlayerServer target = MinecraftServer.getInstance().playerList.getPlayerEntity(targetUsername);
		UUID targetUUID;

		if(target != null){
			targetUUID = target.uuid;
			String targetDisplayName = target.getDisplayName();
			if(Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.containsKey(targetUUID)){
				Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.remove(targetUUID);
				Data.Users.save(sender.uuid);
				FeedbackHandler.destructive(context, "Untrusted %" + targetDisplayName + "% from all Containers!");
				return Command.SINGLE_SUCCESS;
			}
			FeedbackHandler.error(context, "Failed to Untrust %" + targetDisplayName + "% from all Containers! (Player is Not Trusted)");
			return Command.SINGLE_SUCCESS;
		} else {
			UUIDHelper.runConversionAction(targetUsername, targetuuid -> {
				if(Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.containsKey(targetuuid)){

					Data.Users.getOrCreate(sender.uuid).usersTrustedToAllContainers.remove(targetuuid, targetUsername);
					Data.Users.save(sender.uuid);
					FeedbackHandler.destructive(context, "Untrusted %" + targetUsername + "% from all Containers!");
					return;
				}
				FeedbackHandler.error(context, "Failed to Untrust %" + targetUsername + "% from all Containers! (Player is Not Trusted)");
			}, username -> FeedbackHandler.error(context, "Failed to Untrust %" + targetUsername + "% from all Containers! (%" + targetUsername + "% Does not Exist)"));
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_untrustcommunity(CommandContext<CommandSource> context){
		CommandSource source = context.getSource();
		Player sender = source.getSender();

		HitResult rayCastResult = MUtil.rayCastFromPlayer(context);
		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandler.error(context, "Failed to Untrust Community from Container! (Not Looking at Container)");
			return Command.SINGLE_SUCCESS;
		}

		TileEntity container = source.getWorld().getBlockEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if(container != null) {
			if (container instanceof TileEntityContainerInterface) {
				TileEntityContainerInterface containerInterface = ((TileEntityContainerInterface) container);
				if (containerInterface.getIsLocked()) {

					if (!containerInterface.getLockOwner().equals(sender.uuid)) {
						FeedbackHandler.error(context, "Failed to Untrust Community from Container! (Not Owned By You)");
						return Command.SINGLE_SUCCESS;
					}

					if (!containerInterface.getIsCommunityContainer()){
						FeedbackHandler.error(context, "Failed to Untrust Community from Container! (Community not Trusted)");
						return Command.SINGLE_SUCCESS;
					}

					if (container instanceof TileEntityChest) {
						TileEntityContainerInterface otherContainerInterface = (TileEntityContainerInterface) MUtil.getOtherChest(source.getWorld(), (TileEntityChest) container);
						if (otherContainerInterface != null) {
							containerInterface.setIsCommunityContainer(false);
							otherContainerInterface.setIsCommunityContainer(false);
							FeedbackHandler.destructive(context, "Untrusted Community from this Double Chest!");
							return Command.SINGLE_SUCCESS;
						}
						FeedbackHandler.destructive(context, "Untrusted Community from this Chest!");
					} else if (container instanceof TileEntityFurnaceBlast) {
						FeedbackHandler.destructive(context, "Untrusted Community from this Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandler.destructive(context, "Untrusted Community from this Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandler.destructive(context, "Untrusted Community from this Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandler.destructive(context, "Untrusted Community from this Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandler.destructive(context, "Untrusted Community from this Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandler.destructive(context, "Untrusted Community from this Basket!");
					}
					containerInterface.setIsCommunityContainer(false);
				} else {
					FeedbackHandler.error(context, "Failed to Untrust Community from Container!");
					FeedbackHandler.error(context, "(Container not Locked)");
				}
				return Command.SINGLE_SUCCESS;
			}
		}
		FeedbackHandler.error(context, "Failed to Untrust Community from Container!");
		FeedbackHandler.error(context, "(Not Looking at Container)");
		return Command.SINGLE_SUCCESS;
	}

	public static int lock_bypass(CommandContext<CommandSource> context){
		CommandSource source = context.getSource();
		Player sender = source.getSender();
		boolean value = context.getArgument("value", Boolean.class);

		if(Data.Users.getOrCreate(sender.uuid).lockBypass == value){
			FeedbackHandler.error(context, "Failed to set Lock Bypass to %" + value + "% (Already %" + value + "%)");
			return Command.SINGLE_SUCCESS;
		}

		Data.Users.getOrCreate(sender.uuid).lockBypass = value;
		FeedbackHandler.success(context, "Lock Bypass set to %" + value);
		return Command.SINGLE_SUCCESS;
	}
}
