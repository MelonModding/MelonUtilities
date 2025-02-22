package MelonUtilities.command.commandlogic;

import MelonUtilities.config.Data;
import MelonUtilities.utility.MUtil;
import MelonUtilities.utility.feedback.FeedbackArg;
import MelonUtilities.utility.feedback.FeedbackHandlerServer;
import MelonUtilities.utility.feedback.FeedbackType;
import MelonUtilities.utility.managers.RollbackManager;
import com.mojang.brigadier.Command;
import com.mojang.nbt.NbtIo;
import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.PacketBlockRegionUpdate;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import org.useless.serverlibe.api.gui.GuiHelper;
import org.useless.serverlibe.api.gui.ServerGuiBuilder;
import org.useless.serverlibe.api.gui.slot.ServerSlotButton;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static MelonUtilities.utility.managers.RollbackManager.*;

public class CommandLogicRollback {
	public static int rollback(PlayerServer sender) {
		int x1 = sender.chunkCoordX;
		int z1 = sender.chunkCoordZ;

		File chunkDir = new File("./rollbackdata/snapshots/" + sender.world.dimension.id + "/c[x." + x1 + "-z." + z1 + "]");
		chunkDir.mkdirs();

		if (!chunkDir.isDirectory()){
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Chunk has never been Modified!");
			return 0;
		}

		HashMap<Long, File> captures = RollbackManager.getSortedCaptures(sender.world, chunkDir);

		ServerGuiBuilder rollbackGui = new ServerGuiBuilder();
		rollbackGui.setSize((int)Math.floor((captures.size() + 1) / 9.0F));
		int i = 0;
		for(Map.Entry<Long, File> capture : captures.entrySet()){
			int finalI = i;
			if(capture.getValue().getName().contains(".dat")){
				rollbackGui.setContainerSlot(i, (inventory ->
				{
					ItemStack snapshotIcon = Items.PAPER.getDefaultStack();
					SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss");
					snapshotIcon.setCustomName("Snapshot: [" + sdf.format(capture.getKey()) + "]");
					snapshotIcon.setCustomColor((byte) TextFormatting.LIGHT_BLUE.id);
					return new ServerSlotButton(snapshotIcon, inventory, finalI, () -> {
						for(Entity entity : sender.world.loadedEntityList){
							if(entity.chunkCoordX == x1 && entity.chunkCoordZ == z1){
								if(!(entity instanceof Player)){
									entity.remove();
								}
							}
						}
						try {
							CompoundTag tag = NbtIo.readCompressed(Files.newInputStream(capture.getValue().toPath()));
							rollbackChunk(sender.world.getChunkFromChunkCoords(x1, z1), tag);
							MinecraftServer.getInstance().playerList.sendPacketToAllPlayersInDimension(new PacketBlockRegionUpdate(x1 * 16, 0, z1 * 16, 16, 256, 16, sender.world), sender.world.dimension.id);
							FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "%s Rolled Back to %s", new FeedbackArg(x1, z1), new FeedbackArg(sdf.format(capture.getKey())));
							sender.usePersonalCraftingInventory();
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					});
				}));
			} else if(capture.getValue().getName().contains(".mcr")){
				rollbackGui.setContainerSlot(i, (inventory ->
				{
					ItemStack backupIcon = Items.BOOK.getDefaultStack();
					SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss");
					backupIcon.setCustomName("Backup: [" + sdf.format(capture.getKey()) + "]");
					backupIcon.setCustomColor((byte) TextFormatting.CYAN.id);
					return new ServerSlotButton(backupIcon, inventory, finalI, () -> {
						for(Entity entity : sender.world.loadedEntityList){
							if(entity.chunkCoordX == x1 && entity.chunkCoordZ == z1){
								if(!(entity instanceof Player)){
									entity.remove();
								}
							}
						}
						File backupDir = capture.getValue().getParentFile().getParentFile().getParentFile();
						Chunk chunk1 = sender.world.getChunkFromChunkCoords(x1, z1);
						rollbackChunkFromBackup(chunk1, backupDir);
						MinecraftServer.getInstance().playerList.sendPacketToAllPlayersInDimension(new PacketBlockRegionUpdate(chunk1.xPosition * 16, 0, chunk1.zPosition * 16, 16, 256, 16, sender.world), sender.world.dimension.id);
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "%s Rolled Back to %s", new FeedbackArg(x1, z1), new FeedbackArg(sdf.format(capture.getKey())));
						sender.usePersonalCraftingInventory();
					});
				}));
			}
			i++;
		}
		GuiHelper.openCustomServerGui(sender, rollbackGui.build(sender, "Captures:"));
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Opened Rollback GUI!");
		return Command.SINGLE_SUCCESS;
	}

	public static int rollbackArea(PlayerServer sender, int x1, int z1, int x2, int z2) {
		File chunkDir = new File("./rollbackdata/snapshots/" + sender.world.dimension.id + "/c[x." + x1 + "-z." + z1 + "]");
		chunkDir.mkdirs();

		if (!chunkDir.isDirectory()){
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Chunk has never been Modified!");
			return 0;
		}


		HashMap<Long, File> captures = null;
		int minX = Math.min(x1, x2);
		int maxX = Math.max(x1, x2);
		int minZ = Math.min(z1, z2);
		int maxZ = Math.max(z1, z2);
		for (int _x = minX; _x <= maxX; _x++) {
			for (int _z = minZ; _z <= maxZ; _z++) {
				HashMap<Long, File> _captures = RollbackManager.getSortedCaptures(sender.world, new File("./rollbackdata/snapshots/" + sender.world.dimension.id + "/c[x." + _x + "-z." + _z + "]"));
				if (captures == null || captures.size() < _captures.size()) {
					captures = _captures;
				}
			}
		}
		ServerGuiBuilder rollbackGui = new ServerGuiBuilder();
		rollbackGui.setSize((int)Math.ceil((captures.size() + 1) / 9.0F));
		int i = 0;
		for(Map.Entry<Long, File> capture : captures.entrySet()){
			int finalI = i;
			if(capture.getValue().getName().contains(".dat")){
				rollbackGui.setContainerSlot(i, (inventory ->
				{
					ItemStack snapshotIcon = Items.PAPER.getDefaultStack();
					SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss");
					snapshotIcon.setCustomName("Snapshot: [" + sdf.format(capture.getKey()) + "]");
					snapshotIcon.setCustomColor((byte) TextFormatting.LIME.id);
					return new ServerSlotButton(snapshotIcon, inventory, finalI, () -> {
						RollbackManager.rollbackChunkArea(sender, MUtil.getChunkGridFromCorners(sender.world, x1, z1, x2, z2), capture);
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "%s "+ TextFormatting.ORANGE + "- %s Rolled Back to " + TextFormatting.ORANGE + "~%s", new FeedbackArg(x1,z1), new FeedbackArg(x2,z2), new FeedbackArg(sdf.format(capture.getKey())));
					});
				}));
			} else if(capture.getValue().getName().contains(".mcr")){
				rollbackGui.setContainerSlot(i, (inventory ->
				{
					ItemStack backupIcon = Items.BOOK.getDefaultStack();
					SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss");
					backupIcon.setCustomName("Backup: [" + sdf.format(capture.getKey()) + "]");
					backupIcon.setCustomColor((byte) TextFormatting.GREEN.id);
					return new ServerSlotButton(backupIcon, inventory, finalI, () -> {
						RollbackManager.rollbackChunkArea(sender, MUtil.getChunkGridFromCorners(sender.world, x1, z1, x2, z2), capture);
						FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "%s "+ TextFormatting.ORANGE + "- %s Rolled Back to " + TextFormatting.ORANGE + "~%s", new FeedbackArg(x1,z1), new FeedbackArg(x2,z2), new FeedbackArg(sdf.format(capture.getKey())));
					});
				}));
			}
			i++;
		}
		GuiHelper.openCustomServerGui(sender, rollbackGui.build(sender, "Captures:"));
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Opened Rollback GUI!");
		return Command.SINGLE_SUCCESS;
	}

	public static int rollbackTakeSnapshot(PlayerServer sender) {
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Taking a Snapshot!");
		takeSnapshot();
		return Command.SINGLE_SUCCESS;
	}

	public static int rollbackTakeBackup(PlayerServer sender) {
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Taking a Backup!");
		takeBackup();
		return Command.SINGLE_SUCCESS;
	}

	public static int rollbackPruneSnapshots(PlayerServer sender) {
		FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Pruning Snapshots..");
		pruneSnapshots();
		return Command.SINGLE_SUCCESS;
	}

	public static int rollbackPruneBackups(PlayerServer sender) {
		FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Pruning Backups..");
		pruneBackups();
		return Command.SINGLE_SUCCESS;
	}

	public static int rollbackAutoSnapshots(PlayerServer sender) {
		if(Data.MainConfig.config.snapshotsEnabled){
			Data.MainConfig.config.snapshotsEnabled = false;
			Data.MainConfig.save();
			FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Automatic Snapshots Disabled");
		} else {
			Data.MainConfig.config.snapshotsEnabled = true;
			Data.MainConfig.save();
			FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Automatic Snapshots Enabled!");
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int rollbackAutoBackups(PlayerServer sender) {
		if(Data.MainConfig.config.backupsEnabled){
			Data.MainConfig.config.backupsEnabled = false;
			Data.MainConfig.save();
			FeedbackHandlerServer.sendFeedback(FeedbackType.destructive, sender, "Automatic Backups Disabled");
		} else {
			Data.MainConfig.config.backupsEnabled = true;
			Data.MainConfig.save();
			FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "Automatic Backups Enabled!");
		}
		return Command.SINGLE_SUCCESS;
	}
}
