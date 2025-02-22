package MelonUtilities.utility.managers;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.data.Config;
import MelonUtilities.utility.MUtil;
import com.mojang.nbt.NbtIo;
import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityDispatcher;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.PacketBlockRegionUpdate;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkLoaderLegacy;
import net.minecraft.core.world.chunk.reader.ChunkReader;
import net.minecraft.core.world.chunk.reader.ChunkReaderLegacy;
import net.minecraft.core.world.chunk.reader.ChunkReaderVersion1;
import net.minecraft.core.world.chunk.reader.ChunkReaderVersion2;
import net.minecraft.core.world.save.LevelData;
import net.minecraft.core.world.save.mcregion.RegionFileCache;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;


public class RollbackManager {

	static final Set<Chunk> modifiedChunkQueue = new HashSet<>();

	public static boolean skipModifiedQueuing = false;

	public static boolean lock = false;

	public static File backupsDir = new File("./rollbackdata/backups");
	public static File snapshotsDir = new File("./rollbackdata/snapshots");

	public static File getDimensionsDir() {
		return new File(MinecraftServer.getInstance().getMinecraftDir(), MinecraftServer.getInstance().propertyManager.getStringProperty("level-name", "world") + "/dimensions");
	}

	public static File getRegionFileFromCoords(File worldDir, int x, int z) {
		File regionDir = new File(worldDir, "region");
		File regionFile = new File(regionDir, "r." + (x >> 5) + "." + (z >> 5) + ".mcr");
		return regionFile;
	}

	public static void rollbackChunkFromBackup(Chunk chunk, File backupDir) {
		DataInputStream regionStream = RegionFileCache.getChunkInputStream(new File(backupDir, String.valueOf(chunk.world.dimension.id)), chunk.xPosition, chunk.zPosition);

		if (regionStream == null) {return;}

		CompoundTag tag = null;
		try {
			tag = NbtIo.read(regionStream);
		} catch (IOException e) {
			MelonUtilities.LOGGER.error("Failed to read NBT Data while reading Chunk {} from Backup {}", chunk, backupDir);
		}
		if(tag != null){
			rollbackChunk(chunk, tag.getCompound("Level"));
		}
	}


	public static void saveChunk(World world, Chunk chunk) throws IOException {
		world.checkSessionLock();

		File chunkDir = new File(snapshotsDir, chunk.world.dimension.id + "/c[x." + chunk.xPosition + "-z." + chunk.zPosition + "]");
		chunkDir.mkdirs();
		Date resultdate = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy_HH.mm.ss");
		File chunkFile = new File(chunkDir, System.currentTimeMillis() + " [" + sdf.format(resultdate) + "].dat");

		if (chunkFile.exists()) {
			LevelData levelData = world.getLevelData();
			levelData.setSizeOnDisk(levelData.getSizeOnDisk() - chunkFile.length());
		}

		try {
			File tmpChunkFile = new File(chunkDir, "tmp_chunk.dat");
			CompoundTag chunkDataTag = new CompoundTag();
			ChunkLoaderLegacy.storeChunkInCompound(chunk, world, chunkDataTag);
			OutputStream fileStream = Files.newOutputStream(tmpChunkFile.toPath());
			NbtIo.writeCompressed(chunkDataTag, fileStream);
			fileStream.close();
			if (chunkFile.exists()) {
				chunkFile.delete();
			}
			tmpChunkFile.renameTo(chunkFile);
			LevelData levelData = world.getLevelData();
			levelData.setSizeOnDisk(levelData.getSizeOnDisk() + chunkFile.length());
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static ChunkReader getChunkReaderByVersion(World world, CompoundTag tag, int version) {
		switch (version) {
			case 1: {
				return new ChunkReaderVersion1(world, tag);
			}
			case 2: {
				return new ChunkReaderVersion2(world, tag);
			}
		}
		return new ChunkReaderLegacy(world, tag);
	}

	public static void rollbackChunk(Chunk chunk, CompoundTag tag){
		ListTag tileEntityListTag;
		ListTag entityListTag;
		int version = tag.getIntegerOrDefault("Version", -1);
		ChunkReader reader = getChunkReaderByVersion(chunk.world, tag, version);
		chunk.heightMap = reader.getHeightMap();
		chunk.averageBlockHeight = reader.getAverageBlockHeight();
		chunk.isTerrainPopulated = reader.getIsTerrainPopulated();
		chunk.temperature = reader.getTemperatureMap();
		chunk.humidity = reader.getHumidityMap();
		Map<Integer, String> biomeRegistry = reader.getBiomeRegistry();
		for (int i = 0; i < 16; ++i) {
			ChunkLoaderLegacy.loadChunkSectionFromCompound(chunk.getSection(i), reader, biomeRegistry);
		}
		if (chunk.heightMap == null) {
			chunk.heightMap = new short[256];
			chunk.recalcHeightmap();
		}
		if (chunk.temperature == null || chunk.temperature.length == 0) {
			chunk.temperature = new double[256];
			Arrays.fill(chunk.temperature, Double.NEGATIVE_INFINITY);
		}
		if (chunk.humidity == null || chunk.humidity.length == 0) {
			chunk.humidity = new double[256];
			Arrays.fill(chunk.humidity, Double.NEGATIVE_INFINITY);
		}
		if ((entityListTag = tag.getList("Entities")) != null) {
			for (Tag<?> entityTagBase : entityListTag) {
				if (!(entityTagBase instanceof CompoundTag)) continue;
				CompoundTag entityTag = (CompoundTag)entityTagBase;
				Entity entity = EntityDispatcher.createEntityFromNBT(entityTag, chunk.world);
				chunk.hasEntities = true;
				if (entity == null) continue;
				chunk.addEntity(entity);
				chunk.world.entityJoinedWorld(entity);
			}
		}
		if ((tileEntityListTag = tag.getList("TileEntities")) != null) {
			for (Tag<?> tileEntityTagBase : tileEntityListTag) {
				TileEntity tileEntity;
				if (!(tileEntityTagBase instanceof CompoundTag) || (tileEntity = TileEntityDispatcher.createAndLoadEntity((CompoundTag)tileEntityTagBase)) == null) continue;
				chunk.addTileEntity(tileEntity);
			}
		}
	}

	public static void onInit(){
		new File("./rollbackdata").mkdirs();
		new File("./rollbackdata/backups").mkdirs();
		new File("./rollbackdata/snapshots").mkdirs();
	}

	public static void takeBackup(){
		if(lock){
			return;
		}
		lock = true;
		new Thread(() -> {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy_HH.mm.ss");
				File thisBackupDir = new File(backupsDir, System.currentTimeMillis() + " [" + sdf.format(new Date(System.currentTimeMillis())) + "]");
				File[] dimFiles = getDimensionsDir().listFiles();
				if(dimFiles != null){
					int totalFiles = 0;
					int completedFiles = 0;
					long startTime, lastMessage;
					startTime = lastMessage = System.currentTimeMillis();

					for(File dim : dimFiles){
						File regionDir = new File(getDimensionsDir(),dim.getName() + "/region");
						if(regionDir.exists()){
							File[] regionFiles = regionDir.listFiles();
							if(regionFiles != null) {
								totalFiles += regionFiles.length;
							}
						}
					}

					MinecraftServer.getInstance().playerList.sendChatMessageToAllOps(String.format("%sStarting Backup of all %s files!", TextFormatting.GRAY, totalFiles));
					for(File dim : dimFiles){
						File thisBackupRegionDir = new File(thisBackupDir,dim.getName() + "/region");
						thisBackupRegionDir.mkdirs();
						File regionDir = new File(getDimensionsDir(),dim.getName() + "/region");
						if(regionDir.exists()){
							File[] regionFiles = regionDir.listFiles();
							if(regionFiles != null) {
								for (File regionFile : regionFiles) {
									try {
										Files.copy(regionFile.toPath(), new File(thisBackupRegionDir, regionFile.getName()).toPath());
									} catch (IOException e) {
										MelonUtilities.LOGGER.error("Exception while trying to copy file {} to {}!", regionFile, thisBackupRegionDir, e);
										continue;
									}
									completedFiles++;

									if (System.currentTimeMillis() - lastMessage > 500) {
										lastMessage = System.currentTimeMillis();
										MinecraftServer.getInstance().playerList.sendChatMessageToAllOps( String.format("%s%.2f%% complete", TextFormatting.GRAY, ((float)completedFiles/totalFiles) * 100f));
									}
								}
							}
						}
					}
					MinecraftServer.getInstance().playerList.sendChatMessageToAllOps(String.format("%sFinished Backup of all %s files in %.3f seconds", TextFormatting.GRAY, totalFiles, (System.currentTimeMillis() - startTime)/1000f));
				}
			} finally {
				lock = false;
			}
		}).start();
	}

	public static void queueModifiedChunk(Chunk chunk){
		modifiedChunkQueue.add(chunk);
	}

	public static void takeSnapshot(){
		if(lock){
			return;
		}
		lock = true;
		List<Chunk> tempModifiedChunkQueue = new ArrayList<>(modifiedChunkQueue);
		modifiedChunkQueue.clear();

		new Thread(() -> {
			try {
				synchronized (tempModifiedChunkQueue){
					int completedFiles = 0;
					long startTime, lastMessage;
					startTime = lastMessage = System.currentTimeMillis();
					MinecraftServer.getInstance().playerList.sendChatMessageToAllOps(String.format("%sStarting Snapshot of all %s modified chunks!", TextFormatting.GRAY, tempModifiedChunkQueue.size()));
					for (Chunk chunk : tempModifiedChunkQueue){
						try {
							saveChunk(chunk.world, chunk);
						} catch (IOException e) {
							MelonUtilities.LOGGER.error("Chunk [x:{}, z:{}] Failed to Save During Snapshot!", chunk.xPosition, chunk.zPosition);
						}

						completedFiles++;
						if (System.currentTimeMillis() - lastMessage > 500) {
							lastMessage = System.currentTimeMillis();
							MinecraftServer.getInstance().playerList.sendChatMessageToAllOps( String.format("%s%.2f%% complete", TextFormatting.GRAY, ((float)completedFiles/tempModifiedChunkQueue.size()) * 100f));
						}
					}
					MinecraftServer.getInstance().playerList.sendChatMessageToAllOps(String.format("%sFinished Snapshot of all %s modified chunks in %.3f seconds", TextFormatting.GRAY, tempModifiedChunkQueue.size(), (System.currentTimeMillis() - startTime)/1000f));
				}
			} finally {
				lock = false;
			}
		}).start();
	}

	public static void prune(List<File> fileList) throws IOException {
		 if((fileList.size()) % 2 == 0){
			 for(int i = 1; i < (fileList.size()); i += 2){
				 File file = fileList.get(i);
				 if(!file.getName().contains("archived")){
					 deleteDirectory(file);
				 }
			 }
		 } else {
			 for(int i = 0; i < (fileList.size()); i += 2){
				 File file = fileList.get(i);
				 if(!file.getName().contains("archived")){
					 deleteDirectory(file);
				 }
			 }
		 }
	}

	public static void deleteDirectory(File file) throws IOException {
		if(file.isDirectory()){
			Path dir = Paths.get(file.getPath()); //path to the directory
			Files
				.walk(dir) // Traverse the file tree in depth-first order
				.sorted(Comparator.reverseOrder())
				.forEach(path -> {
					try {
						Files.delete(path);  //delete each file or directory
					} catch (IOException e) {
						MelonUtilities.LOGGER.error("Could not delete file at Path: [{}]!", dir, e);
					}
				});
		} else {
			file.delete();
		}
	}

	public static void pruneSnapshots(){
		if(lock){
			return;
		}
		lock = true;
		new Thread(() -> {
			try {
				File[] dimensions = snapshotsDir.listFiles();

				if (dimensions == null) {return;}
				for (File dimension : dimensions) {

					if (!dimension.isDirectory()) {return;}
					File chunksDir = new File(snapshotsDir, dimension.getName());
					File[] chunks = chunksDir.listFiles();

					if (chunks == null) {return;}
					for (File chunk : chunks) {

						if (!chunk.isDirectory()) {return;}
						File[] snapshots = chunk.listFiles();

						if (snapshots == null) {return;}
						List<File> snapshotList = Arrays.asList(snapshots);
						snapshotList.sort((o1, o2) -> {
							long l1 = Long.parseLong(o1.getName().split(" ")[0]);
							long l2 = Long.parseLong(o2.getName().split(" ")[0]);
							return Long.compare(l2, l1);
						});

						List<File> toPrune = snapshotList.subList(snapshotList.size() / 2, snapshotList.size());
						try {
							prune(toPrune);
						} catch (IOException e) {
							MelonUtilities.LOGGER.error("Failed to Prune Snapshot files in {}!", toPrune);
						}
					}
				}
			} finally {
				lock = false;
			}
		}).start();
	}

	public static void pruneBackups(){
		if(lock){
			return;
		}
		lock = true;
		new Thread(() -> {
			try {
				File[] backups = backupsDir.listFiles();

				if (backups == null) {return;}
				List<File> backupList = Arrays.asList(backups);
				backupList.sort((o1, o2) -> {
					long l1 = Long.parseLong(o1.getName().split(" ")[0]);
					long l2 = Long.parseLong(o2.getName().split(" ")[0]);
					return Long.compare(l2, l1);
				});

				List<File> toPrune = backupList.subList(backupList.size() / 2, backupList.size());
				try {
					prune(toPrune);
				} catch (IOException e) {
					MelonUtilities.LOGGER.error("Failed to Prune Backup files in {}!", toPrune);
				}
			} finally {
				lock = false;
			}
		}).start();
	}

	public static int[] parseCoordsFromChunkDir(File chunkDir) {
		int xIndex = chunkDir.getName().indexOf('x');
		int zIndex = chunkDir.getName().indexOf('z');

		StringBuilder xStringBuilder = new StringBuilder();
		StringBuilder zStringBuilder = new StringBuilder();

		String s = chunkDir.getName();

		boolean firstMinus = true;
		for (int i = xIndex + 2; i < s.length(); i++) {
			char c = s.charAt(i);
			if (Character.isDigit(c) || (c == '-' && firstMinus)) {
				xStringBuilder.append(c);
				firstMinus = false;
			} else {
				break;
			}
		}

		firstMinus = true;
		for (int i = zIndex + 2; i < s.length(); i++) {
			char c = s.charAt(i);
			if (Character.isDigit(c) || (c == '-' && firstMinus)) {
				zStringBuilder.append(c);
				firstMinus = false;
			} else {
				break;
			}
		}

		String xString = xStringBuilder.toString();
		String zString = zStringBuilder.toString();

		int x = Integer.parseInt(xString);
		int z = Integer.parseInt(zString);
		return new int[]{x, z};
	}

	public static HashMap<Long, File> getSortedCaptures(World world, File chunkDir){
		int[] chunkCoords = parseCoordsFromChunkDir(chunkDir);

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
					capturesHashmap.putIfAbsent(Long.parseLong(backup.getName().split(" ")[0]), getRegionFileFromCoords(new File(backup.getPath(), String.valueOf(world.dimension.id)), chunkCoords[0], chunkCoords[1]));
				}
			}
		}

		//Return Sorted Hashmap of all Captures (Both Backups and Snapshots)
		return MUtil.sortByKey(capturesHashmap);
	}

	public HashMap<Long, File> getSortedBackups(World world, File chunkDir){
		int[] chunkCoords = parseCoordsFromChunkDir(chunkDir);

		HashMap<Long, File> backupsHashmap = new HashMap<>();

		//Add Backups To Hashmap
		File[] backups = backupsDir.listFiles();
		if(backups != null){
			for (File backup : backups) {
				if (backup.isDirectory()) {
					backupsHashmap.putIfAbsent(Long.parseLong(backup.getName().split(" ")[0]), getRegionFileFromCoords(new File(backup.getPath(), String.valueOf(world.dimension.id)), chunkCoords[0], chunkCoords[1]));
				}
			}
		}

		return MUtil.sortByKey(backupsHashmap);
	}

	public HashMap<Long, File> getSortedSnapshots(File chunkDir){

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

	public static void rollbackChunkArea(PlayerServer sender, List<File> chunkGrid, Map.Entry<Long, File> primaryCapture){
		for(File chunkDir : chunkGrid) {
			int[] chunkCoords = parseCoordsFromChunkDir(chunkDir);

			HashMap<Long, File> captures = getSortedCaptures(sender.world, chunkDir);
			Map.Entry<Long, File> closestCapture = getClosestCapture(primaryCapture, captures);

			if (closestCapture.getValue().getName().contains(".dat")) {
				for (Entity entity : sender.world.loadedEntityList) {
					if (entity.chunkCoordX == chunkCoords[0] && entity.chunkCoordZ == chunkCoords[1]) {
						if (!(entity instanceof Player)) {
							entity.remove();
						}
					}
				}
				try {
					Chunk chunk = sender.world.getChunkFromChunkCoords(chunkCoords[0], chunkCoords[1]);
					CompoundTag tag = NbtIo.readCompressed(Files.newInputStream(closestCapture.getValue().toPath()));
					rollbackChunk(chunk, tag);
					MinecraftServer.getInstance().playerList.sendPacketToAllPlayersInDimension(new PacketBlockRegionUpdate(chunkCoords[0] * 16, 0, chunkCoords[1] * 16, 16, 256, 16, sender.world), sender.world.dimension.id);
				} catch (IOException e) {
					MelonUtilities.LOGGER.error("IOException occurred trying to read compressed data from Chunk File: {}", closestCapture.getValue());
				}
			}

			if (closestCapture.getValue().getName().contains(".mcr")) {
				for (Entity entity : sender.world.loadedEntityList) {
					if (entity.chunkCoordX == chunkCoords[0] && entity.chunkCoordZ == chunkCoords[1]) {
						if (!(entity instanceof Player)) {
							entity.remove();
						}
					}
				}
				File backupDir = closestCapture.getValue().getParentFile().getParentFile().getParentFile();
				rollbackChunkFromBackup(sender.world.getChunkFromChunkCoords(chunkCoords[0], chunkCoords[1]), backupDir);
				MinecraftServer.getInstance().playerList.sendPacketToAllPlayersInDimension(new PacketBlockRegionUpdate(chunkCoords[0] * 16, 0, chunkCoords[1] * 16, 16, 256, 16, sender.world), sender.world.dimension.id);
			}
		}
		sender.usePersonalCraftingInventory();
	}

	private static Map.@Nullable Entry<Long, File> getClosestCapture(Map.Entry<Long, File> primaryCapture, HashMap<Long, File> captures) {
		long timeOfPrimaryCapture = primaryCapture.getKey();
		long oldestLowestDifference = Long.MAX_VALUE;
		long lowestDifference = Long.MAX_VALUE;
		Map.Entry<Long, File> closestCapture = null;
		Map.Entry<Long, File> oldestClosestCapture = null;

		for(Map.Entry<Long, File> capture : captures.entrySet()){
			long timeOfCapture = capture.getKey();
			long difference = Math.abs(timeOfPrimaryCapture - timeOfCapture);
			if(timeOfCapture <= timeOfPrimaryCapture){
				if(difference < oldestLowestDifference){
					oldestLowestDifference = difference;
					oldestClosestCapture = capture;
				}
			} else {
				if(difference < lowestDifference){
					lowestDifference = difference;
					closestCapture = capture;
				}
			}
		}

		if(oldestClosestCapture != null) {
			closestCapture = oldestClosestCapture;
		}
		return closestCapture;
	}

	//TODO Hard Backup Size Limit in Config

	static int configLoadCounter = 0;

	public static void tick() {
		Config config = Data.MainConfig.config;

		double systemTime = System.currentTimeMillis();
		double difference = (systemTime - config.lastSnapshot);
		if(!lock && config.snapshotsEnabled && difference >= config.timeBetweenSnapshots * 1000){
			takeSnapshot();
			config.lastSnapshot = System.currentTimeMillis();
			Data.MainConfig.save();
		}

		if(!lock && config.backupsEnabled && System.currentTimeMillis() - config.lastBackup >= config.timeBetweenBackups * 60 * 60 * 1000){
			takeBackup();
			config.lastBackup = System.currentTimeMillis();
			Data.MainConfig.save();
		}

		if(!lock && config.backupsEnabled && System.currentTimeMillis() - config.lastBackupPrune >= config.timeBetweenBackupPruning * 60 * 60 * 1000){
			pruneBackups();
			config.lastBackupPrune = System.currentTimeMillis();
			Data.MainConfig.save();
		}

		if(!lock && config.snapshotsEnabled && System.currentTimeMillis() - config.lastSnapshotPrune >= config.timeBetweenSnapshotPruning * 60 * 60 * 1000){
			pruneSnapshots();
			config.lastSnapshotPrune = System.currentTimeMillis();
			Data.MainConfig.save();
		}


		configLoadCounter++;
	}
}
