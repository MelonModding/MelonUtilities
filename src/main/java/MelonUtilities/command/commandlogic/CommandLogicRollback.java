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
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.PacketBlockRegionUpdate;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static MelonUtilities.utility.managers.RollbackManager.*;

public class CommandLogicRollback {

	private static class PendingRollback {
		final int dimensionId;
		final int x1, z1, x2, z2;
		final boolean area;
		final List<Map.Entry<Long, File>> captures;

		PendingRollback(int dimensionId, int x1, int z1, int x2, int z2, boolean area, List<Map.Entry<Long, File>> captures) {
			this.dimensionId = dimensionId;
			this.x1 = x1;
			this.z1 = z1;
			this.x2 = x2;
			this.z2 = z2;
			this.area = area;
			this.captures = captures;
		}
	}

	private static final Map<UUID, PendingRollback> pendingRollbacks = new HashMap<>();

	private static @NotNull SimpleDateFormat captureDateFormat() {
		return new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss");
	}

	private static void listCaptures(@NotNull PlayerServer sender, PendingRollback pending) {
		pendingRollbacks.put(sender.uuid, pending);
		SimpleDateFormat sdf = captureDateFormat();

		String header = pending.area
			? "Captures for Chunks [" + pending.x1 + ", " + pending.z1 + "] - [" + pending.x2 + ", " + pending.z2 + "]:"
			: "Captures for Chunk [" + pending.x1 + ", " + pending.z1 + "]:";
		sender.sendMessage(TextFormatting.GRAY + "< " + TextFormatting.LIGHT_GRAY + header + TextFormatting.GRAY + " >" + TextFormatting.LIGHT_GRAY + " (" + ZoneId.systemDefault() + ")");

		int i = 1;
		for (Map.Entry<Long, File> capture : pending.captures) {
			if (capture.getValue().getName().contains(".dat")) {
				sender.sendMessage(TextFormatting.GRAY + "  " + i + ") " + TextFormatting.LIGHT_BLUE + "Snapshot [" + sdf.format(capture.getKey()) + "]");
			} else if (capture.getValue().getName().contains(".mcr")) {
				sender.sendMessage(TextFormatting.GRAY + "  " + i + ") " + TextFormatting.CYAN + "Backup [" + sdf.format(capture.getKey()) + "]");
			}
			i++;
		}
		sender.sendMessage(TextFormatting.GRAY + "Use " + TextFormatting.ORANGE + "/rollback apply <number>" + TextFormatting.GRAY + " to roll back");
	}

	public static int rollback(@NotNull PlayerServer sender) {
		int x1 = sender.chunkCoordX;
		int z1 = sender.chunkCoordZ;

		File chunkDir = new File("./rollbackdata/snapshots/" + sender.world.dimension.id + "/c[x." + x1 + "-z." + z1 + "]");
		chunkDir.mkdirs();

		if (!chunkDir.isDirectory()){
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Chunk has never been Modified!");
			return 0;
		}

		HashMap<Long, File> captures = RollbackManager.getSortedCaptures(sender.world, chunkDir);
		if (captures.isEmpty()) {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "No Captures Found for this Chunk!");
			return 0;
		}

		listCaptures(sender, new PendingRollback(sender.world.dimension.id, x1, z1, x1, z1, false, new ArrayList<>(captures.entrySet())));
		return Command.SINGLE_SUCCESS;
	}

	public static int rollbackArea(@NotNull PlayerServer sender, int x1, int z1, int x2, int z2) {
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
		if (captures == null || captures.isEmpty()) {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "No Captures Found for this Area!");
			return 0;
		}

		listCaptures(sender, new PendingRollback(sender.world.dimension.id, x1, z1, x2, z2, true, new ArrayList<>(captures.entrySet())));
		return Command.SINGLE_SUCCESS;
	}

	public static int rollbackApply(@NotNull PlayerServer sender, int captureNumber) {
		PendingRollback pending = pendingRollbacks.get(sender.uuid);
		if (pending == null) {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "No Capture List! (Use /rollback or /rollback area first)");
			return 0;
		}
		if (pending.dimensionId != sender.world.dimension.id) {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Capture List is for another Dimension! (Re-run /rollback)");
			return 0;
		}
		if (captureNumber < 1 || captureNumber > pending.captures.size()) {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Invalid Capture Number! (1-" + pending.captures.size() + ")");
			return 0;
		}

		Map.Entry<Long, File> capture = pending.captures.get(captureNumber - 1);
		if (!capture.getValue().isFile()) {
			FeedbackHandlerServer.sendFeedback(FeedbackType.error, sender, "Capture no longer exists! (Re-run /rollback)");
			return 0;
		}

		SimpleDateFormat sdf = captureDateFormat();

		if (pending.area) {
			RollbackManager.rollbackChunkArea(sender, MUtil.getChunkGridFromCorners(sender.world, pending.x1, pending.z1, pending.x2, pending.z2), capture);
			FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "%s "+ TextFormatting.ORANGE + "- %s Rolled Back to " + TextFormatting.ORANGE + "~%s", new FeedbackArg(pending.x1, pending.z1), new FeedbackArg(pending.x2, pending.z2), new FeedbackArg(sdf.format(capture.getKey())));
			return Command.SINGLE_SUCCESS;
		}

		int x1 = pending.x1;
		int z1 = pending.z1;
		for (Entity entity : sender.world.getLoadedEntityList()) {
			if (entity.chunkCoordX == x1 && entity.chunkCoordZ == z1) {
				if (!(entity instanceof Player)) {
					entity.remove();
				}
			}
		}
		if (capture.getValue().getName().contains(".dat")) {
			try {
				CompoundTag tag = NbtIo.readCompressed(Files.newInputStream(capture.getValue().toPath()));
				rollbackChunk(sender.world.getChunkFromChunkCoords(x1, z1), tag);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			File backupDir = capture.getValue().getParentFile().getParentFile().getParentFile();
			Chunk chunk = sender.world.getChunkFromChunkCoords(x1, z1);
			rollbackChunkFromBackup(chunk, backupDir);
		}
		MinecraftServer.getInstance().playerList.sendPacketToAllPlayersInDimension(new PacketBlockRegionUpdate(x1 * 16, 0, z1 * 16, 16, 256, 16, sender.world), sender.world.dimension.id);
		FeedbackHandlerServer.sendFeedback(FeedbackType.success, sender, "%s Rolled Back to %s", new FeedbackArg(x1, z1), new FeedbackArg(sdf.format(capture.getKey())));
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
