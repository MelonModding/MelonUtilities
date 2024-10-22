package MelonUtilities.commands.rollback;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.ConfigData;
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
import org.apache.commons.lang3.math.NumberUtils;
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
		syntax.append("rollback", "title",                             TextFormatting.LIGHT_GRAY + "  > /rollback [<x> <z> <x> <z>] / [<mode>]");
		syntax.append("takeSnapshot", "rollback",                      TextFormatting.LIGHT_GRAY + "    > takeSnapshot");
		syntax.append("takeBackup", "rollback",                        TextFormatting.LIGHT_GRAY + "    > takeBackup");
		syntax.append("pruneSnapshots", "rollback",                    TextFormatting.LIGHT_GRAY + "    > pruneSnapshots");
		syntax.append("pruneBackups", "rollback",                      TextFormatting.LIGHT_GRAY + "    > pruneBackups");
		syntax.append("help", "rollback",                              TextFormatting.LIGHT_GRAY + "    > help");

	}

	private boolean takeSnapshotCommand(CommandHandler handler, CommandSender sender, String[] args){
		takeSnapshot();
		FeedbackHandler.success(sender, "Taking a Snapshot!");
		return true;
	}

	private boolean takeBackupCommand(CommandHandler handler, CommandSender sender, String[] args){
		takeBackup();
		FeedbackHandler.success(sender, "Backing Up World!");
		return true;
	}

	private boolean pruneSnapshotsCommand(CommandHandler handler, CommandSender sender, String[] args){
		pruneSnapshots();
		FeedbackHandler.destructive(sender, "Pruning Snapshots");
		return true;
	}

	private boolean pruneBackupsCommand(CommandHandler handler, CommandSender sender, String[] args){
		pruneBackups();
		FeedbackHandler.destructive(sender, "Pruning Backups");
		return true;
	}

	private boolean toggleAutoSnapshotsCommand(CommandHandler handler, CommandSender sender, String[] args){
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

	private boolean toggleAutoBackupsCommand(CommandHandler handler, CommandSender sender, String[] args){
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

	private HashMap<Long, File> getSortedCaptures(CommandSender sender, File chunkDir){

		String[] segments = chunkDir.getName().split("\\.");
		//"c[x"   "0-z"   "0]"

		String xString = segments[1].substring(0, segments[1].length() - 2);
		String zString = segments[1].substring(0, segments[1].length() - 1);

		int x = Integer.parseInt(xString);
		int z = Integer.parseInt(zString);




		HashMap<Long, File> capturesHashmap = new HashMap<>();

		//Add Snapshots To Hashmap
		File[] snapshots = chunkDir.listFiles();
		if(snapshots != null) {
			for (File snapshot : snapshots) {
				if (snapshot.isFile()) {
					capturesHashmap.putIfAbsent(Long.parseLong(snapshot.getName().split(" ")[0]), snapshot);
				}
			}
		}

		//Add Backups To Hashmap
		File[] backups = backupsDir.listFiles();
		if(backups != null){
			for (File backup : backups) {
				if (backup.isDirectory()) {
					capturesHashmap.putIfAbsent(Long.parseLong(backup.getName().split(" ")[0]), getRegionFileFromCoords(new File(backup.getPath(), String.valueOf(sender.getWorld().dimension.id)), x, z));
				}
			}
		}

		//Return Sorted Hashmap of all Captures (Both Backups and Snapshots)
		return MUtil.sortByKey(capturesHashmap);
	}

	private HashMap<Long, File> getSortedBackups(CommandSender sender, File chunkDir){
		String[] segments = chunkDir.getName().split("\\.");
		//"c[x"   "0-z"   "0]"
		String xString = segments[1].substring(0, segments[1].length() - 2);
		String zString = segments[1].substring(0, segments[1].length() - 1);

		int x = Integer.parseInt(xString);
		int z = Integer.parseInt(zString);

		HashMap<Long, File> backupsHashmap = new HashMap<>();

		//Add Backups To Hashmap
		File[] backups = backupsDir.listFiles();
		if(backups != null){
			for (File backup : backups) {
				if (backup.isDirectory()) {
					backupsHashmap.putIfAbsent(Long.parseLong(backup.getName().split(" ")[0]), getRegionFileFromCoords(new File(backup.getPath(), String.valueOf(sender.getWorld().dimension.id)), x, z));
				}
			}
		}

		return MUtil.sortByKey(backupsHashmap);
	}

	private HashMap<Long, File> getSortedSnapshots(File chunkDir){

		HashMap<Long, File> snapshotsHashmap = new HashMap<>();

		File[] snapshots = chunkDir.listFiles();
		if(snapshots != null) {
			for (File snapshot : snapshots) {
				if (snapshot.isFile()) {
					snapshotsHashmap.putIfAbsent(Long.parseLong(snapshot.getName().split(" ")[0]), snapshot);
				}
			}
		}

		return MUtil.sortByKey(snapshotsHashmap);
	}



	private boolean rollbackGui(CommandSender sender, int x1, int z1){
		File chunkDir = new File("./rollbackdata/snapshots/" + sender.getWorld().dimension.id + "/c[x." + x1 + "-z." + z1 + "]");
		chunkDir.mkdirs();
		if (chunkDir.isDirectory()) {

			HashMap<Long, File> captures = getSortedCaptures(sender, chunkDir);

			ServerGuiBuilder rollbackGui = new ServerGuiBuilder();
			rollbackGui.setSize((int)Math.ceil((captures.size() + 1) / 9.0F));
			int i = 0;
			for(Map.Entry<Long, File> capture : captures.entrySet()){
				int finalI = i;
				if(capture.getValue().getName().contains(".dat")){
					rollbackGui.setContainerSlot(i, (inventory ->
					{
						ItemStack snapshotIcon = Item.paper.getDefaultStack();
						SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss");
						snapshotIcon.setCustomName("Snapshot: [" + sdf.format(capture.getKey()) + "]");
						snapshotIcon.setCustomColor((byte) TextFormatting.LIGHT_BLUE.id);
						return new ServerSlotButton(snapshotIcon, inventory, finalI, () -> {
							for(Entity entity : sender.getWorld().loadedEntityList){
								if(entity.chunkCoordX == x1 && entity.chunkCoordZ == z1){
									if(!(entity instanceof EntityPlayer)){
										entity.remove();
									}
								}
							}
							try {
								CompoundTag tag = NbtIo.readCompressed(Files.newInputStream(capture.getValue().toPath()));
								rollbackChunk(sender.getWorld().getChunkFromChunkCoords(x1, z1), tag);
								MinecraftServer.getInstance().playerList.sendPacketToAllPlayersInDimension(new Packet51MapChunk(x1 * 16, 0, z1 * 16, 16, 256, 16, sender.getWorld()), sender.getWorld().dimension.id);
								((EntityPlayerMP) sender.getPlayer()).usePersonalCraftingInventory();
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
						});
					}));
				} else if(capture.getValue().getName().contains(".mcr")){
					rollbackGui.setContainerSlot(i, (inventory ->
					{
						ItemStack backupIcon = Item.book.getDefaultStack();
						SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss");
						backupIcon.setCustomName("Backup: [" + sdf.format(capture.getKey()) + "]");
						backupIcon.setCustomColor((byte) TextFormatting.CYAN.id);
						return new ServerSlotButton(backupIcon, inventory, finalI, () -> {
							for(Entity entity : sender.getWorld().loadedEntityList){
								if(entity.chunkCoordX == x1 && entity.chunkCoordZ == z1){
									if(!(entity instanceof EntityPlayer)){
										entity.remove();
									}
								}
							}
							File backupDir = capture.getValue().getParentFile().getParentFile().getParentFile();
							Chunk chunk1 = sender.getWorld().getChunkFromChunkCoords(x1, z1);
							rollbackChunkFromBackup(chunk1, backupDir);
							MinecraftServer.getInstance().playerList.sendPacketToAllPlayersInDimension(new Packet51MapChunk(chunk1.xPosition * 16, 0, chunk1.zPosition * 16, 16, 256, 16, sender.getWorld()), sender.getWorld().dimension.id);
							((EntityPlayerMP) sender.getPlayer()).usePersonalCraftingInventory();
						});
					}));
				}
				i++;
			}
			GuiHelper.openCustomServerGui((EntityPlayerMP) sender.getPlayer(), rollbackGui.build((EntityPlayer) sender.getPlayer(), "Captures:"));
			FeedbackHandler.success(sender, "Opened Rollback GUI!");
			return true;
		} else {
			FeedbackHandler.error(sender, "Chunk has never been Modified!");
			return true;
		}
	}

	private boolean rollbackAreaGui(CommandSender sender, int x1, int z1, int x2, int z2){
		File chunkDir = new File("./rollbackdata/snapshots/" + sender.getWorld().dimension.id + "/c[x." + x1 + "-z." + z1 + "]");
		chunkDir.mkdirs();
		if (chunkDir.isDirectory()) {
			HashMap<Long, File> captures = getSortedCaptures(sender, chunkDir);
			ServerGuiBuilder rollbackGui = new ServerGuiBuilder();
			rollbackGui.setSize((int)Math.ceil((captures.size() + 1) / 9.0F));
			int i = 0;
			for(Map.Entry<Long, File> capture : captures.entrySet()){
				int finalI = i;
				if(capture.getValue().getName().contains(".dat")){
					rollbackGui.setContainerSlot(i, (inventory ->
					{
						ItemStack snapshotIcon = Item.paper.getDefaultStack();
						SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss");
						snapshotIcon.setCustomName("Snapshot: [" + sdf.format(capture.getKey()) + "]");
						snapshotIcon.setCustomColor((byte) TextFormatting.LIME.id);
						return new ServerSlotButton(snapshotIcon, inventory, finalI, () -> rollbackChunkArea(sender, MUtil.getChunkGridFromCorners(sender, x1, z1, x2, z2), capture));
					}));
				} else if(capture.getValue().getName().contains(".mcr")){
					rollbackGui.setContainerSlot(i, (inventory ->
					{
						ItemStack backupIcon = Item.book.getDefaultStack();
						SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss");
						backupIcon.setCustomName("Backup: [" + sdf.format(capture.getKey()) + "]");
						backupIcon.setCustomColor((byte) TextFormatting.GREEN.id);
						return new ServerSlotButton(backupIcon, inventory, finalI, () -> rollbackChunkArea(sender, MUtil.getChunkGridFromCorners(sender, x1, z1, x2, z2), capture));
					}));
				}
				i++;
			}
			GuiHelper.openCustomServerGui((EntityPlayerMP) sender.getPlayer(), rollbackGui.build((EntityPlayer) sender.getPlayer(), "Captures:"));
			FeedbackHandler.success(sender, "Opened Rollback GUI!");
			return true;
		} else {
			FeedbackHandler.error(sender, "Chunk has never been Modified!");
			return true;
		}
	}

	private boolean rollbackCommand(CommandHandler handler, CommandSender sender, String[] args){
		rollbackGui(sender, sender.getPlayer().chunkCoordX, sender.getPlayer().chunkCoordZ);
		return true;
	}

	private boolean rollbackAreaCommand(CommandHandler handler, CommandSender sender, String[] args){
		int x1;
		int z1;
		int x2;
		int z2;

		if (MUtil.isNumeric(args[0])) {
			x1 = Integer.parseInt(args[0]);
		} else {
			FeedbackHandler.error(sender, "Failed to Rollback Chunk Area! (Invalid Chunks)");
			return true;
		}
		if (MUtil.isNumeric(args[1])) {
			z1 = Integer.parseInt(args[1]);
		} else {
			FeedbackHandler.error(sender, "Failed to Rollback Chunk Area! (Invalid Chunks)");
			return true;
		}
		if (MUtil.isNumeric(args[2])) {
			x2 = Integer.parseInt(args[2]);
		} else {
			FeedbackHandler.error(sender, "Failed to Rollback Chunk Area! (Invalid Chunks)");
			return true;
		}
		if (MUtil.isNumeric(args[3])) {
			z2 = Integer.parseInt(args[3]);
		} else {
			FeedbackHandler.error(sender, "Failed to Rollback Chunk Area! (Invalid Chunks)");
			return true;
		}

		rollbackAreaGui(sender, x1, z1, x2, z2);

		return true;
	}

	private void rollbackChunkArea(CommandSender sender, List<File> chunkGrid, Map.Entry<Long, File> primaryCapture){
		for(File chunkDir : chunkGrid) {
			String path = chunkDir.getName();
			String[] segments = path.split("\\.");
			//"c[x"   "0-z"   "0]"

			String xString = segments[1].substring(0, segments[1].length() - 2);
			String zString = segments[1].substring(0, segments[1].length() - 1);

			int x = Integer.parseInt(xString);
			int z = Integer.parseInt(zString);


			HashMap<Long, File> captures = getSortedCaptures(sender, chunkDir);


			long timeOfPrimaryCapture = primaryCapture.getKey();
			long lowestDifference = Long.MAX_VALUE;
			Map.Entry<Long, File> oldestClosestCapture = null;

			for(Map.Entry<Long, File> capture : captures.entrySet()){
				long timeOfCapture = capture.getKey();
				if(timeOfCapture <= timeOfPrimaryCapture){
					long difference = Math.abs(timeOfPrimaryCapture - timeOfCapture);
					if(difference < lowestDifference){
						lowestDifference = difference;
						oldestClosestCapture = capture;
					}

				}
			}


			if(oldestClosestCapture != null) {
				if (oldestClosestCapture.getValue().getName().contains(".dat")) {
					for (Entity entity : sender.getWorld().loadedEntityList) {
						if (entity.chunkCoordX == x && entity.chunkCoordZ == z) {
							if (!(entity instanceof EntityPlayer)) {
								entity.remove();
							}
						}
					}
					try {
						Chunk chunk = sender.getWorld().getChunkFromChunkCoords(x, z);
						CompoundTag tag = NbtIo.readCompressed(Files.newInputStream(oldestClosestCapture.getValue().toPath()));
						rollbackChunk(chunk, tag);
						MinecraftServer.getInstance().playerList.sendPacketToAllPlayersInDimension(new Packet51MapChunk(x * 16, 0, z * 16, 16, 256, 16, sender.getWorld()), sender.getWorld().dimension.id);
					} catch (IOException e) {
						MelonUtilities.LOGGER.error("IOException occurred trying to read compressed data from Chunk File: {}", oldestClosestCapture.getValue());
					}
				}

				if (oldestClosestCapture.getValue().getName().contains(".mcr")) {
					for (Entity entity : sender.getWorld().loadedEntityList) {
						if (entity.chunkCoordX == x && entity.chunkCoordZ == z) {
							if (!(entity instanceof EntityPlayer)) {
								entity.remove();
							}
						}
					}
					File backupDir = oldestClosestCapture.getValue().getParentFile().getParentFile().getParentFile();
					rollbackChunkFromBackup(sender.getWorld().getChunkFromChunkCoords(x, z), backupDir);
					MinecraftServer.getInstance().playerList.sendPacketToAllPlayersInDimension(new Packet51MapChunk(x * 16, 0, z * 16, 16, 256, 16, sender.getWorld()), sender.getWorld().dimension.id);
				}
			}
		}
		((EntityPlayerMP) sender.getPlayer()).usePersonalCraftingInventory();
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
		if (args.length == 0) {
			return rollbackCommand(handler, sender, args);
		} else if(args.length == 4) {
			return rollbackAreaCommand(handler, sender, args);
		}

		switch(args[0].toLowerCase()){
			case "takesnapshot":
			case "ts":
				return takeSnapshotCommand(handler, sender, args);
			case "takebackup":
			case "tb":
				return takeBackupCommand(handler, sender, args);
			case "prunesnapshots":
			case "ps":
				return pruneSnapshotsCommand(handler, sender, args);
			case "prunebackups":
			case "pb":
				return pruneBackupsCommand(handler, sender, args);
			case "toggleautosnapshots":
			case "tas":
				return toggleAutoSnapshotsCommand(handler, sender, args);
			case "toggleautobackups":
			case "tab":
				return toggleAutoBackupsCommand(handler, sender, args);
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
