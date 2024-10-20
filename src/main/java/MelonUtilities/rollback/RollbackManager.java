package MelonUtilities.rollback;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.ConfigData;
import com.b100.utils.FileUtils;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import com.mojang.nbt.NbtIo;
import com.mojang.nbt.Tag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.net.command.TextFormatting;
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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;


public class RollbackManager {

	static final Set<Chunk> modifiedChunkQueue = new HashSet<>();

	public static boolean skipModifiedQueuing = false;

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

	public static void rollbackChunkFromBackup(Chunk chunk, File backupDir) throws IOException {
		DataInputStream regionStream = RegionFileCache.getChunkInputStream(new File(backupDir, String.valueOf(chunk.world.dimension.id)), chunk.xPosition, chunk.zPosition);

		if (regionStream == null) {return;}

		CompoundTag tag = NbtIo.read(regionStream);
		rollbackChunk(chunk, tag.getCompound("Level"));
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
				CompoundTag tileEntityTag;
				TileEntity tileEntity;
				if (!(tileEntityTagBase instanceof CompoundTag) || (tileEntity = TileEntity.createAndLoadEntity(tileEntityTag = (CompoundTag)tileEntityTagBase)) == null) continue;
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
		new Thread(() -> {
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
		}).start();
	}

	public static void queueModifiedChunk(Chunk chunk){
		modifiedChunkQueue.add(chunk);
	}

	public static void takeSnapshot(){
		List<Chunk> tempModifiedChunkQueue = new ArrayList<>(modifiedChunkQueue);
		modifiedChunkQueue.clear();

		new Thread(() -> {
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
		}).start();
	}

	public static void prune(List<File> fileList) throws IOException {
		 if(fileList.size() % 2 == 0){
			 for(int i = 1; i<fileList.size(); i += 2){
				 File file = fileList.get(i);
				 if(!file.getName().contains("archived")){
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
			 }
		 } else{
			 for(int i = 0; i<fileList.size(); i += 2){
				 File file = fileList.get(i);
				 if(!file.getName().contains("archived")){
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
			 }
		 }
	}

	public static void pruneSnapshots(){
		File[] dimensions = snapshotsDir.listFiles();
		if(dimensions != null) {
			for (File dimension : dimensions) {
				if (dimension.isDirectory()) {
					File chunksDir = new File(snapshotsDir, dimension.getName());
					File[] chunks = chunksDir.listFiles();
					if (chunks != null) {
						for (File chunk : chunks) {
							if (chunk.isDirectory()) {
								File[] snapshots = chunk.listFiles();
								if (snapshots != null) {
									long newestSnapshot = Long.MIN_VALUE;
									long oldestSnapshot = Long.MAX_VALUE;
									for (File snapshot : snapshots) {
										long snapshotTime = Long.parseLong(snapshot.getName().split(" ")[0]);
										if (snapshotTime > newestSnapshot) {
											newestSnapshot = snapshotTime;
										} else if (snapshotTime < oldestSnapshot) {
											oldestSnapshot = snapshotTime;
										}
									}
									long middleMostSnapshotTime = (newestSnapshot + oldestSnapshot) / 2;
									HashMap<Long, File> snapshotDifferences = new HashMap<>();
									for (File snapshot : snapshots) {
										snapshotDifferences.putIfAbsent(Math.abs(middleMostSnapshotTime - Long.parseLong(snapshot.getName().split(" ")[0])), snapshot);
									}
									long lowestDifference = Long.MAX_VALUE;
									for (Long difference : snapshotDifferences.keySet()) {
										if (difference < lowestDifference) {
											lowestDifference = difference;
										}
									}

									File middleMostSnapshot = snapshotDifferences.get(lowestDifference);

									List<File> toStay = new ArrayList<>();
									List<File> toPrune = new ArrayList<>();

									for (File snapshot : snapshots) {
										if (Long.parseLong(snapshot.getName().split(" ")[0]) > Long.parseLong(middleMostSnapshot.getName().split(" ")[0])) {
											toStay.add(snapshot);
										} else {
											toPrune.add(snapshot);
										}
									}
									try {
										prune(toPrune);
									} catch (IOException e) {
										MelonUtilities.LOGGER.error("Failed to Prune Snapshot files in {}!", toPrune);
									}
									toStay.addAll(toPrune);
								}
							}
						}
					}
				}
			}
		}
	}

	public static void pruneBackups(){
		File[] backups = backupsDir.listFiles();
		if(backups != null){
			List<File> backupList = Arrays.asList(backups);
			backupList.sort((o1, o2) -> {
				long l1 = Long.parseLong(o1.getName().split(" ")[0]);
				long l2 = Long.parseLong(o2.getName().split(" ")[0]);
				return Long.compare(l1, l2);
			});

			List<File> toPrune = backupList.subList(backupList.size()/2, backupList.size());
			try {
				prune(toPrune);
			} catch (IOException e) {
				MelonUtilities.LOGGER.error("Failed to Prune Backup files in {}!", toPrune);
			}
		}
	}

	//TODO Hard Backup Size Limit in Config

	static ConfigData config = Data.configs.getOrCreate("config", ConfigData.class);

	public static void tick(){
		if(System.currentTimeMillis() <= config.lastSnapshot + config.timeBetweenSnapshots){
			takeSnapshot();
			config.lastSnapshot = System.currentTimeMillis() / 1000f;
		}

		if(System.currentTimeMillis() <= config.lastBackup + config.timeBetweenBackups * 120){
			takeBackup();
			config.lastBackup = System.currentTimeMillis() / 1000f;
		}

		if(System.currentTimeMillis() <= config.lastBackupPrune + config.timeBetweenBackupPruning * 120){
			pruneBackups();
			config.lastBackupPrune = System.currentTimeMillis() / 1000f;
		}

		if(System.currentTimeMillis() <= config.lastSnapshotPrune + config.timeBetweenSnapshotPruning * 120){
			pruneSnapshots();
			config.lastSnapshotPrune = System.currentTimeMillis() / 1000f;
		}

	}
}
