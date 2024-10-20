package MelonUtilities.commands.rollback;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.ConfigData;
import MelonUtilities.utility.RollbackManager;
import MelonUtilities.utility.FeedbackHandler;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.SyntaxBuilder;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.NbtIo;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.Packet51MapChunk;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.useless.serverlibe.api.gui.GuiHelper;
import org.useless.serverlibe.api.gui.ServerGuiBuilder;
import org.useless.serverlibe.api.gui.slot.ServerSlotButton;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

import static MelonUtilities.utility.RollbackManager.*;

public class RollbackCommand extends Command {

	private final static String COMMAND = "rollback";
	private final static String NAME = "RollbackCommand";

	public RollbackCommand(){super(COMMAND, "rb");}

	static SyntaxBuilder syntax = new SyntaxBuilder();
	public static void buildSyntax(){
		syntax.clear();
		syntax.append("title",                                               TextFormatting.LIGHT_GRAY + "< Command Syntax > ([] = optional, <> = variable, / = or)");
		syntax.append("rollback", "title",                             TextFormatting.LIGHT_GRAY + "  > /rollback [<x,z> <x,z>] / [<mode>]");
		syntax.append("takeSnapshot", "rollback",                      TextFormatting.LIGHT_GRAY + "    > takeSnapshot");
		syntax.append("takeBackup", "rollback",                        TextFormatting.LIGHT_GRAY + "    > takeBackup");
		syntax.append("pruneSnapshots", "rollback",                    TextFormatting.LIGHT_GRAY + "    > pruneSnapshots");
		syntax.append("pruneBackups", "rollback",                      TextFormatting.LIGHT_GRAY + "    > pruneBackups");
		syntax.append("help", "rollback",                              TextFormatting.LIGHT_GRAY + "    > help");

	}

	private boolean takeSnapshot(CommandHandler handler, CommandSender sender, String[] args){
		RollbackManager.takeSnapshot();
		FeedbackHandler.success(sender, "Taking a Snapshot!");
		return true;
	}

	private boolean takeBackup(CommandHandler handler, CommandSender sender, String[] args){
		RollbackManager.takeBackup();
		FeedbackHandler.success(sender, "Backing Up World!");
		return true;
	}

	private boolean pruneSnapshots(CommandHandler handler, CommandSender sender, String[] args){
		RollbackManager.pruneSnapshots();
		FeedbackHandler.destructive(sender, "Pruning Snapshots");
		return true;
	}

	private boolean pruneBackups(CommandHandler handler, CommandSender sender, String[] args){
		RollbackManager.pruneBackups();
		FeedbackHandler.destructive(sender, "Pruning Backups");
		return true;
	}

	private boolean toggleAutoSnapshots(CommandHandler handler, CommandSender sender, String[] args){
		if(Data.configs.getOrCreate("config", ConfigData.class).snapshotsEnabled){
			Data.configs.getOrCreate("config", ConfigData.class).snapshotsEnabled = false;
			Data.configs.saveAll();
			FeedbackHandler.destructive(sender, "Automatic Snapshots Disabled.");
			return true;
		} else {
			Data.configs.getOrCreate("config", ConfigData.class).snapshotsEnabled = true;
			Data.configs.saveAll();
			FeedbackHandler.success(sender, "Automatic Snapshots Enabled!");
			return true;
		}
	}

	private boolean toggleAutoBackups(CommandHandler handler, CommandSender sender, String[] args){
		if(Data.configs.getOrCreate("config", ConfigData.class).backupsEnabled){
			Data.configs.getOrCreate("config", ConfigData.class).backupsEnabled = false;
			Data.configs.saveAll();
			FeedbackHandler.destructive(sender, "Automatic Backups Disabled.");
			return true;
		} else {
			Data.configs.getOrCreate("config", ConfigData.class).backupsEnabled = true;
			Data.configs.saveAll();
			FeedbackHandler.success(sender, "Automatic Backups Enabled!");
			return true;
		}
	}


	private boolean rollback(CommandHandler handler, CommandSender sender, String[] args){

		if(args.length == 0){
			File chunkDir = new File("./rollbackdata/snapshots/" + sender.getWorld().dimension.id + "/c[x." + sender.getPlayer().chunkCoordX + "-z." + sender.getPlayer().chunkCoordZ + "]");
			chunkDir.mkdirs();
			if (chunkDir.isDirectory()) {

				File[] snapshots = chunkDir.listFiles();
				if(snapshots == null){
					FeedbackHandler.error(sender, "Chunk does not have any Snapshots!");
					return true;
				}

				HashMap<Long, File> snapshotsHashmap = new HashMap<>();
				for (File snapshot : snapshots) {
					if (snapshot.isFile()) {
						snapshotsHashmap.putIfAbsent(Long.parseLong(snapshot.getName().split(" ")[0]), snapshot);
					}
				}



				File[] backups = backupsDir.listFiles();
				if(backups != null){
					for (File backup : backups){
						snapshotsHashmap.putIfAbsent(Long.parseLong(backup.getName().split(" ")[0]), getRegionFileFromCoords(new File(backup.getPath(), String.valueOf(sender.getWorld().dimension.id)), sender.getPlayer().chunkCoordX, sender.getPlayer().chunkCoordZ));
					}
				}

				snapshotsHashmap = MUtil.sortByKey(snapshotsHashmap);


				ServerGuiBuilder rollbackGui = new ServerGuiBuilder();
				rollbackGui.setSize((int)Math.ceil((snapshots.length + 1) / 9.0F));
				int i = 0;
				for(Map.Entry<Long, File> snapshot : snapshotsHashmap.entrySet()){
					int finalI = i;

					if(snapshot.getValue().getName().contains(".dat")){
					rollbackGui
						.setContainerSlot(i, (inventory ->
						{
							ItemStack snapshotIcon = Item.paper.getDefaultStack();
							SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss");
							snapshotIcon.setCustomName("Snapshot: [" + sdf.format(snapshot.getKey()) + "]");
							snapshotIcon.setCustomColor((byte) TextFormatting.LIGHT_BLUE.id);
							try {
								CompoundTag tag = NbtIo.readCompressed(Files.newInputStream(snapshot.getValue().toPath()));
								int chunkX = tag.getInteger("xPos");
								int chunkZ = tag.getInteger("zPos");

								return new ServerSlotButton(snapshotIcon, inventory, finalI, () -> {

									for(Entity entity : sender.getWorld().loadedEntityList){
										if(entity.chunkCoordX == chunkX && entity.chunkCoordZ == chunkZ){
											if(!(entity instanceof EntityPlayer)){
												entity.remove();
											}
										}
									}

									RollbackManager.rollbackChunk(sender.getWorld().getChunkFromChunkCoords(chunkX, chunkZ), tag);
									MinecraftServer.getInstance().playerList.sendPacketToAllPlayersInDimension(new Packet51MapChunk(chunkX * 16, 0, chunkZ * 16, 16, 256, 16, sender.getWorld()), sender.getWorld().dimension.id);
									((EntityPlayerMP) sender.getPlayer()).usePersonalCraftingInventory();
								});
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
						}));
					} else if(snapshot.getValue().getName().contains(".mcr")){
						rollbackGui.setContainerSlot(i, (inventory ->
						{
							ItemStack snapshotIcon = Item.book.getDefaultStack();
							SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss");
							snapshotIcon.setCustomName("Backup: [" + sdf.format(snapshot.getKey()) + "]");
							snapshotIcon.setCustomColor((byte) TextFormatting.CYAN.id);
							return new ServerSlotButton(snapshotIcon, inventory, finalI, () -> {

								for(Entity entity : sender.getWorld().loadedEntityList){
									if(entity.chunkCoordX == sender.getPlayer().chunkCoordX && entity.chunkCoordZ == sender.getPlayer().chunkCoordZ){
										if(!(entity instanceof EntityPlayer)){
											entity.remove();
										}
									}
								}

								File backupDir = snapshot.getValue().getParentFile().getParentFile().getParentFile();
								Chunk chunk = sender.getWorld().getChunkFromChunkCoords(sender.getPlayer().chunkCoordX, sender.getPlayer().chunkCoordZ);

								try {
									RollbackManager.rollbackChunkFromBackup(chunk, backupDir);
								} catch (IOException e) {
									MelonUtilities.LOGGER.error("Exception while trying to rollback chunk {} from backup {}!", chunk, backupDir);
								}
								MinecraftServer.getInstance().playerList.sendPacketToAllPlayersInDimension(new Packet51MapChunk(chunk.xPosition * 16, 0, chunk.zPosition * 16, 16, 256, 16, sender.getWorld()), sender.getWorld().dimension.id);
								((EntityPlayerMP) sender.getPlayer()).usePersonalCraftingInventory();
							});
						}));
					}


					i++;
				}

				GuiHelper.openCustomServerGui((EntityPlayerMP) sender.getPlayer(), rollbackGui.build((EntityPlayer) sender.getPlayer(), "Captures | Snapshots & Backups"));

				FeedbackHandler.success(sender, "Opened Rollback GUI!");
				return true;

			} else {
				FeedbackHandler.error(sender, "Chunk has never been Modified!");
				return true;
			}
		}


		return false;
	}


	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
		if (args.length == 0) {
			return rollback(handler, sender, args);
		}

		switch(args[0].toLowerCase()){
			case "takesnapshot":
			case "ts":
				return takeSnapshot(handler, sender, args);
			case "takebackup":
			case "tb":
				return takeBackup(handler, sender, args);
			case "prunesnapshots":
			case "ps":
				return pruneSnapshots(handler, sender, args);
			case "prunebackups":
			case "pb":
				return pruneBackups(handler, sender, args);
			case "toggleautosnapshots":
			case "tas":
				return toggleAutoSnapshots(handler, sender, args);
			case "toggleautobackups":
			case "tab":
				return toggleAutoBackups(handler, sender, args);
			case "help":
				return false;
		}

		FeedbackHandler.error(sender, " " + NAME + " Error: (Invalid Syntax)");
		return false;
	}

	@Override
	public boolean opRequired(String[] args) {
		return true;
	}

	@Override
	public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
		syntax.printAllLines(sender);
	}
}
