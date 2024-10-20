package MelonUtilities.commands.lock;

import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.PlayerData;
import MelonUtilities.interfaces.TileEntityContainerInterface;
import MelonUtilities.utility.FeedbackHandler;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.SyntaxBuilder;
import MelonUtilities.utility.UUIDHelper;
import net.minecraft.core.HitResult;
import net.minecraft.core.block.entity.*;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;

public class LockCommand extends Command {

	private final static String COMMAND = "lock";

	public LockCommand() {
		super(COMMAND, "l");
	}

	public static SyntaxBuilder syntax = new SyntaxBuilder();

	public static void buildLockSyntax(){
		syntax.clear();
		syntax.append("title",                                                    TextFormatting.LIGHT_GRAY + "< Command Syntax > ([] = optional, <> = variable, / = or)");
		syntax.append("lock", "title",                                     TextFormatting.LIGHT_GRAY + "  > /lock [<mode>]");
		syntax.append("lockOnBlockPlaced", "lock",                         TextFormatting.LIGHT_GRAY + "    > onBlockPlaced true/false");
		syntax.append("lockOnBlockPunched", "lock",                        TextFormatting.LIGHT_GRAY + "    > onBlockPunched true/false");
		syntax.append("lockTrust", "lock",                                 TextFormatting.LIGHT_GRAY + "    > trust <player>");
		syntax.append("lockTrustAll", "lock",                              TextFormatting.LIGHT_GRAY + "    > trustall <player>");
		syntax.append("lockTrustCommunity", "lock",                        TextFormatting.LIGHT_GRAY + "    > trustcommunity");
		syntax.append("lockUntrust", "lock",                               TextFormatting.LIGHT_GRAY + "    > untrust <player>");
		syntax.append("lockUntrustAll", "lock",                            TextFormatting.LIGHT_GRAY + "    > untrustall <player>");
		syntax.append("lockUntrustCommunity", "lock",                      TextFormatting.LIGHT_GRAY + "    > untrustcommunity");
		syntax.append("lockBypass", "lock", true,                      TextFormatting.LIGHT_GRAY + "    > bypass true/false");
	}

	private boolean onBlockPlaced(CommandHandler handler, CommandSender sender, String[] args){
		if(args[1].equals("true")) {
			if(!Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).lockOnBlockPlaced){
				Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).lockOnBlockPlaced = true;
				FeedbackHandler.success(sender, "Lock-On-Block-Placed is now On!");
				return true;
			}
			FeedbackHandler.error(sender, "Failed to turn Lock-On-Block-Placed On! (Already On)");
			return false;
		}
		if(args[1].equals("false")) {
			if(Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).lockOnBlockPlaced) {
				Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).lockOnBlockPlaced = false;
				FeedbackHandler.success(sender, "Lock on Block Placed is now Off!");
				return true;
			}
			FeedbackHandler.error(sender, "Failed to turn Lock-On-Block-Placed Off! (Already Off)");
			return false;
		}
		FeedbackHandler.error(sender, "Failed to set Lock Mode! (Invalid Syntax)");
		return false;
	}

	private boolean onBlockPunched(CommandHandler handler, CommandSender sender, String[] args){
		if(args[1].equals("true")) {
			if(!Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).lockOnBlockPunched){
				Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).lockOnBlockPunched = true;
				FeedbackHandler.success(sender, "Lock-On-Block-Punched is now On!");
				return true;
			}
			FeedbackHandler.error(sender, "Failed to turn Lock-On-Block-Punched On! (Already On)");
			return true;
		}
		if(args[1].equals("false")) {
			if(Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).lockOnBlockPunched) {
				Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).lockOnBlockPunched = false;
				FeedbackHandler.success(sender, "Lock-On-Block-Punched is now Off!");
				return true;
			}
			FeedbackHandler.error(sender, "Failed to turn Lock-On-Block-Punched Off! (Already Off)");
			return true;
		}
		FeedbackHandler.error(sender, "Failed to set Lock Mode! (Invalid Syntax)");
		return false;
	}

	private boolean trust(CommandHandler handler, CommandSender sender, String[] args){
		if(UUIDHelper.getUUIDFromName(args[1]) != null) {

			HitResult rayCastResult = MUtil.rayCastFromPlayer(sender);

			if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
				FeedbackHandler.error(sender, "Failed to Trust Player to Container! (Not Looking at Container)");
				return true;
			}

			TileEntity container = sender.getWorld().getBlockTileEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

			if(container != null) {
				if (container instanceof TileEntityContainerInterface) {
					TileEntityContainerInterface iContainer = ((TileEntityContainerInterface) container);
					if (iContainer.getIsLocked()) {

						if (!iContainer.getLockOwner().equals(UUIDHelper.getUUIDFromName(sender.getPlayer().username))) {
							FeedbackHandler.error(sender, "Failed to Trust Player to Container! (Not Owned By You)");
							return true;
						}

						if (iContainer.getTrustedPlayers().contains(UUIDHelper.getUUIDFromName(args[1]))) {
							FeedbackHandler.error(sender, "Failed to Trust Player to Container! (Player already Trusted)");
							return true;
						}

						if (container instanceof TileEntityChest) {
							TileEntityContainerInterface iOtherContainer = (TileEntityContainerInterface) MUtil.getOtherChest(sender.getWorld(), (TileEntityChest) container);
							if (iOtherContainer != null) {
								iContainer.addTrustedPlayer(UUIDHelper.getUUIDFromName(args[1]));
								iOtherContainer.addTrustedPlayer(UUIDHelper.getUUIDFromName(args[1]));
								FeedbackHandler.success(sender, "Trusted " + TextFormatting.GRAY + args[1] + TextFormatting.LIME + " to this Double Chest!");
								return true;
							}
							FeedbackHandler.success(sender, "Trusted " + TextFormatting.GRAY + args[1] + TextFormatting.LIME + " to this Chest!");
						} else if (container instanceof TileEntityBlastFurnace) {
							FeedbackHandler.success(sender, "Trusted " + TextFormatting.GRAY + args[1] + TextFormatting.LIME + " to this Blast Furnace!");
						} else if (container instanceof TileEntityFurnace) {
							FeedbackHandler.success(sender, "Trusted " + TextFormatting.GRAY + args[1] + TextFormatting.LIME + " to this Furnace!");
						} else if (container instanceof TileEntityDispenser) {
							FeedbackHandler.success(sender, "Trusted " + TextFormatting.GRAY + args[1] + TextFormatting.LIME + " to this Dispenser!");
						} else if (container instanceof TileEntityMeshGold) {
							FeedbackHandler.success(sender, "Trusted " + TextFormatting.GRAY + args[1] + TextFormatting.LIME + " to this Golden Mesh!");
						} else if (container instanceof TileEntityTrommel) {
							FeedbackHandler.success(sender, "Trusted " + TextFormatting.GRAY + args[1] + TextFormatting.LIME + " to this Trommel!");
						} else if (container instanceof TileEntityBasket) {
							FeedbackHandler.success(sender, "Trusted " + TextFormatting.GRAY + args[1] + TextFormatting.LIME + " to this Basket!");
						}
						iContainer.addTrustedPlayer(UUIDHelper.getUUIDFromName(args[1]));
						return true;
					}
				}
			}
			if(container != null) {
				FeedbackHandler.error(sender, "Failed to Trust Player to Container! (Container not Locked)");
				return true;
			}
			FeedbackHandler.error(sender, "Failed to Trust Player to Container! (Not Looking at Container)");
			return true;
		}
		FeedbackHandler.error(sender, "Failed to Trust " + TextFormatting.GRAY + args[1] + TextFormatting.RED + " to Container!");
		FeedbackHandler.error(sender, "(Player Doesn't Exist)");
		return true;
	}

	private boolean trustAll(CommandHandler handler, CommandSender sender, String[] args){
		if(UUIDHelper.getUUIDFromName(args[1]) != null) {
			if(!Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).playersTrustedToAllContainers.contains(UUIDHelper.getUUIDFromName(args[1]))){
				Data.playerData.loadAll(PlayerData.class);
				Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).playersTrustedToAllContainers.add(UUIDHelper.getUUIDFromName(args[1]));
				Data.playerData.saveAll();
				FeedbackHandler.success(sender, "Trusted " + TextFormatting.GRAY + args[1] + TextFormatting.LIME + " to all Containers!");
				return true;
			}
			FeedbackHandler.error(sender, "Failed to Trust " + TextFormatting.GRAY + args[1] + TextFormatting.RED + " to all Containers!");
			FeedbackHandler.error(sender, "(Player is Already Trusted)");
			return true;
		}
		FeedbackHandler.error(sender, "Failed to Trust " + TextFormatting.GRAY + args[1] + TextFormatting.RED + " to all Containers!");
		FeedbackHandler.error(sender, "(Player Doesn't Exist)");
		return true;
	}

	private boolean trustCommunity(CommandHandler handler, CommandSender sender, String[] args){

		HitResult rayCastResult = MUtil.rayCastFromPlayer(sender);

		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandler.error(sender, "Failed to Trust Community to Container! (Not Looking at Container)");
			return true;
		}

		TileEntity container = sender.getWorld().getBlockTileEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if (container instanceof TileEntityContainerInterface) {
			TileEntityContainerInterface iContainer = ((TileEntityContainerInterface) container);
			if (iContainer.getIsLocked()) {

				if (!iContainer.getLockOwner().equals(UUIDHelper.getUUIDFromName(sender.getPlayer().username))) {
					FeedbackHandler.error(sender, "Failed to Trust Community to Container! (Not Owned By You)");
					return true;
				}

				if (container instanceof TileEntityChest) {
					TileEntityContainerInterface iOtherContainer = (TileEntityContainerInterface) MUtil.getOtherChest(sender.getWorld(), (TileEntityChest) container);
					if (iOtherContainer != null) {
						iContainer.setIsCommunityContainer(true);
						iOtherContainer.setIsCommunityContainer(true);
						FeedbackHandler.destructive(sender, "Trusted Community to this Double Chest!");
						return true;
					}
					FeedbackHandler.destructive(sender, "Trusted Community to this Chest!");
				} else if (container instanceof TileEntityBlastFurnace) {
					FeedbackHandler.destructive(sender, "Trusted Community to this Blast Furnace!");
				} else if (container instanceof TileEntityFurnace) {
					FeedbackHandler.destructive(sender, "Trusted Community to this Furnace!");
				} else if (container instanceof TileEntityDispenser) {
					FeedbackHandler.destructive(sender, "Trusted Community to this Dispenser!");
				} else if (container instanceof TileEntityMeshGold) {
					FeedbackHandler.destructive(sender, "Trusted Community to this Golden Mesh!");
				} else if (container instanceof TileEntityTrommel) {
					FeedbackHandler.destructive(sender, "Trusted Community to this Trommel!");
				} else if (container instanceof TileEntityBasket) {
					FeedbackHandler.destructive(sender, "Trusted Community to this Basket!");
				}
				iContainer.setIsCommunityContainer(true);
				return true;
			}
		}
		if(container != null) {
			FeedbackHandler.error(sender, "Failed to Trust Community to Container! (Container not Locked)");
			return true;
		}
		FeedbackHandler.error(sender, "Failed to Trust Community to Container! (Not Looking at Container)");
		return true;
	}

	private boolean untrust(CommandHandler handler, CommandSender sender, String[] args){
		if(UUIDHelper.getUUIDFromName(args[1]) != null) {

			HitResult rayCastResult = MUtil.rayCastFromPlayer(sender);

			if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
				FeedbackHandler.error(sender, "Failed to Untrust Player from Container! (Not Looking at Container)");
				return true;
			}

			TileEntity container = sender.getWorld().getBlockTileEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

			if (container instanceof TileEntityContainerInterface) {
				TileEntityContainerInterface iContainer = ((TileEntityContainerInterface) container);
				if (iContainer.getIsLocked()) {

					if (!iContainer.getLockOwner().equals(UUIDHelper.getUUIDFromName(sender.getPlayer().username))) {
						FeedbackHandler.error(sender, "Failed to Untrust Player from Container! (Not Owned By You)");
						return true;
					}

					if (!iContainer.getTrustedPlayers().contains(UUIDHelper.getUUIDFromName(args[1]))) {
						FeedbackHandler.error(sender, "Failed to Untrust Player from Container! (Player not Trusted)");
						return true;
					}

					if (container instanceof TileEntityChest) {
						TileEntityContainerInterface iOtherContainer = (TileEntityContainerInterface) MUtil.getOtherChest(sender.getWorld(), (TileEntityChest) container);
						if (iOtherContainer != null) {
							iContainer.removeTrustedPlayer(UUIDHelper.getUUIDFromName(args[1]));
							iOtherContainer.removeTrustedPlayer(UUIDHelper.getUUIDFromName(args[1]));
							FeedbackHandler.destructive(sender, "Untrusted " + TextFormatting.GRAY + args[1] + TextFormatting.ORANGE + " from this Double Chest!");
							return true;
						}
						FeedbackHandler.destructive(sender, "Untrusted " + TextFormatting.GRAY + args[1] + TextFormatting.ORANGE + " from this Chest!");
					} else if (container instanceof TileEntityBlastFurnace) {
						FeedbackHandler.destructive(sender, "Untrusted " + TextFormatting.GRAY + args[1] + TextFormatting.ORANGE + " from this Blast Furnace!");
					} else if (container instanceof TileEntityFurnace) {
						FeedbackHandler.destructive(sender, "Untrusted " + TextFormatting.GRAY + args[1] + TextFormatting.ORANGE + " from this Furnace!");
					} else if (container instanceof TileEntityDispenser) {
						FeedbackHandler.destructive(sender, "Untrusted " + TextFormatting.GRAY + args[1] + TextFormatting.ORANGE + " from this Dispenser!");
					} else if (container instanceof TileEntityMeshGold) {
						FeedbackHandler.destructive(sender, "Untrusted " + TextFormatting.GRAY + args[1] + TextFormatting.ORANGE + " from this Golden Mesh!");
					} else if (container instanceof TileEntityTrommel) {
						FeedbackHandler.destructive(sender, "Untrusted " + TextFormatting.GRAY + args[1] + TextFormatting.ORANGE + " from this Trommel!");
					} else if (container instanceof TileEntityBasket) {
						FeedbackHandler.destructive(sender, "Untrusted " + TextFormatting.GRAY + args[1] + TextFormatting.ORANGE + " from this Basket!");
					}
					iContainer.removeTrustedPlayer(UUIDHelper.getUUIDFromName(args[1]));
					return true;
				}
			}
			if(container != null) {
				FeedbackHandler.error(sender, "Failed to Untrust Player from Container! (Container not Locked)");
				return true;
			}
			FeedbackHandler.error(sender, "Failed to Untrust Player from Container! (Not Looking at Container)");
			return true;
		}
		FeedbackHandler.error(sender, "Failed to Untrust " + TextFormatting.GRAY + args[1] + TextFormatting.RED + " from Container!");
		FeedbackHandler.error(sender, "(Player Doesn't Exist)");
		return true;
	}

	private boolean untrustAll(CommandHandler handler, CommandSender sender, String[] args){
		if(UUIDHelper.getUUIDFromName(args[1]) != null) {
			if(Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).playersTrustedToAllContainers.contains(UUIDHelper.getUUIDFromName(args[1]))){
				Data.playerData.loadAll(PlayerData.class);
				Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).playersTrustedToAllContainers.remove(UUIDHelper.getUUIDFromName(args[1]));
				Data.playerData.saveAll();
				FeedbackHandler.destructive(sender, "Untrusted " + TextFormatting.GRAY + args[1] + TextFormatting.ORANGE + " from all Containers!");
				return true;
			}
			FeedbackHandler.error(sender, "Failed to Untrust " + TextFormatting.GRAY + args[1] + TextFormatting.RED + " from all Containers!");
			FeedbackHandler.error(sender, "(Player isn't Trusted)");
			return true;
		}
		FeedbackHandler.error(sender, "Failed to Untrust " + TextFormatting.GRAY + args[1] + TextFormatting.RED + " from all Containers!");
		FeedbackHandler.error(sender, "(Player Doesn't Exist)");
		return true;
	}

	private boolean untrustCommunity(CommandHandler handler, CommandSender sender, String[] args){

		HitResult rayCastResult = MUtil.rayCastFromPlayer(sender);

		if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
			FeedbackHandler.error(sender, "Failed to Untrust Community from Container! (Not Looking at Container)");
			return true;
		}

		TileEntity container = sender.getWorld().getBlockTileEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);

		if(container != null) {
			if (container instanceof TileEntityContainerInterface) {
				TileEntityContainerInterface iContainer = ((TileEntityContainerInterface) container);
				if (iContainer.getIsLocked()) {

					if (!iContainer.getLockOwner().equals(UUIDHelper.getUUIDFromName(sender.getPlayer().username))) {
						FeedbackHandler.error(sender, "Failed to Untrust Community from Container! (Not Owned By You)");
						return true;
					}

					if (container instanceof TileEntityChest) {
						TileEntityContainerInterface iOtherContainer = (TileEntityContainerInterface) MUtil.getOtherChest(sender.getWorld(), (TileEntityChest) container);
						if (iOtherContainer != null) {
							iContainer.setIsCommunityContainer(false);
							iOtherContainer.setIsCommunityContainer(false);
							FeedbackHandler.destructive(sender, "Untrusted Community from this Double Chest!");
							return true;
						}
						FeedbackHandler.destructive(sender, "Untrusted Community from this Chest!");
					} else if (container instanceof TileEntityBlastFurnace) {
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
					iContainer.setIsCommunityContainer(false);
					return true;
				}
			}
		}
		FeedbackHandler.error(sender, "Failed to Untrust Community from Container! (Not Looking at Container)");
		return true;
	}


	private boolean bypass(CommandHandler handler, CommandSender sender, String[] args) {
		if(sender.isAdmin()) {
			if (args[1].equals("true")) {
				if (!Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).lockBypass) {
					Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).lockBypass = true;
					FeedbackHandler.success(sender, "Lock-Bypass is now On!");
					return true;
				}
				FeedbackHandler.error(sender, "Failed to turn Lock-Bypass On! (Already On)");
				return true;
			}
			if (args[1].equals("false")) {
				if (Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).lockBypass) {
					Data.playerData.getOrCreate(UUIDHelper.getUUIDFromName(sender.getPlayer().username).toString(), PlayerData.class).lockBypass = false;
					FeedbackHandler.success(sender, "Lock-Bypass is now Off!");
					return true;
				}
				FeedbackHandler.error(sender, "Failed to turn Lock-Bypass Off! (Already Off)");
				return true;
			}
			FeedbackHandler.error(sender, "Failed to set Lock-Bypass! (Invalid Syntax)");
			return false;
		}
		FeedbackHandler.error(sender, "You don't have permission to use this command!");
		return true;
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {

		if (args.length == 0) {
			HitResult rayCastResult = MUtil.rayCastFromPlayer(sender);
			if (rayCastResult == null || rayCastResult.hitType != HitResult.HitType.TILE) {
				FeedbackHandler.error(sender, "Failed to Lock Container! (Not Looking at Container)");
				return true;
			}

			TileEntity container = sender.getWorld().getBlockTileEntity(rayCastResult.x, rayCastResult.y, rayCastResult.z);
			if(container != null){
				if (container instanceof TileEntityContainerInterface) {
					TileEntityContainerInterface iContainer = ((TileEntityContainerInterface) container);
					if (!iContainer.getIsLocked()) {
						if (container instanceof TileEntityChest) {
							TileEntityContainerInterface iOtherContainer = (TileEntityContainerInterface) MUtil.getOtherChest(sender.getWorld(), (TileEntityChest) container);
							if (iOtherContainer != null) {
								iContainer.setIsLocked(true);
								iOtherContainer.setIsLocked(true);
								iContainer.setLockOwner(sender.getPlayer().username);
								iOtherContainer.setLockOwner(sender.getPlayer().username);
								FeedbackHandler.success(sender, "Locked Double Chest!");
								return true;
							}
							FeedbackHandler.success(sender, "Locked Chest!");
						} else if (container instanceof TileEntityBlastFurnace) {
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

						iContainer.setIsLocked(true);
						iContainer.setLockOwner(sender.getPlayer().username);
						return true;

					} else if (iContainer.getIsLocked() && !iContainer.getLockOwner().equals(UUIDHelper.getUUIDFromName(sender.getPlayer().username))) {
						FeedbackHandler.error(sender, "Failed to Lock Container! (Not Owned By You)");
						return true;
					}
					FeedbackHandler.error(sender, "Failed to Lock Container! (Already Locked)");
					return true;
				}
			}
			FeedbackHandler.error(sender, "Failed to Lock Container! (Not Looking at Container)");
			return true;
		}

		switch(args[0]){
			case "onBlockPlaced":
				return onBlockPlaced(handler, sender, args);
			case "onBlockPunched":
				return onBlockPunched(handler, sender, args);
			case "trust":
				return trust(handler, sender, args);
			case "trustall":
				return trustAll(handler, sender, args);
			case "trustcommunity":
				return trustCommunity(handler, sender, args);
			case "untrust":
				return untrust(handler, sender, args);
			case "untrustall":
				return untrustAll(handler, sender, args);
			case "untrustcommunity":
				return untrustCommunity(handler, sender, args);
			case "bypass":
				return bypass(handler, sender, args);
			case "help":
				return false;
		}

		FeedbackHandler.error(sender, "Lock Command Failed (Invalid Syntax)");
		syntax.printAllLines(sender);
		return true;
	}

	@Override
	public boolean opRequired(String[] strings) {
		return false;
	}

	@Override
	public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
		syntax.printAllLines(sender);
	}
}
