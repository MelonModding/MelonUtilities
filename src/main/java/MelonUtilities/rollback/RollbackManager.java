package MelonUtilities.rollback;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.Data;
import MelonUtilities.config.datatypes.ConfigData;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import com.mojang.nbt.NbtIo;
import com.mojang.nbt.Tag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkLoaderLegacy;
import net.minecraft.core.world.chunk.reader.ChunkReader;
import net.minecraft.core.world.chunk.reader.ChunkReaderLegacy;
import net.minecraft.core.world.chunk.reader.ChunkReaderVersion1;
import net.minecraft.core.world.chunk.reader.ChunkReaderVersion2;
import net.minecraft.core.world.save.LevelData;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;


public class RollbackManager {

	static final Set<Chunk> modifiedChunkQueue = new HashSet<>();

	public static boolean skipModifiedQueuing = false;

	static File backupsDir = new File("./rollbackdata/backups");
	static File snapshotsDir = new File("./rollbackdata/snapshots");
	static File dimDir = new File("./world/dimensions");

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
			File thisBackupDir = new File(backupsDir, System.currentTimeMillis() + " [" + sdf.format(new Date(System.currentTimeMillis())) + "].dat");
			File[] dimFiles = dimDir.listFiles();
			if(dimFiles != null){
				for(File dim : dimFiles){
					File thisBackupRegionDir = new File(thisBackupDir,dim.getName() + "/region");
					thisBackupRegionDir.mkdirs();
					File regionDir = new File(dimDir,dim.getName() + "/region");
					if(regionDir.exists()){
						File[] regionFiles = regionDir.listFiles();
						if(regionFiles != null) {
							for (File regionFile : regionFiles) {
								try {
									Files.copy(regionFile.toPath(), thisBackupRegionDir.toPath());
								} catch (IOException e) {
									continue;
								}
							}
						}
					}
				}
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
				for (Chunk chunk : tempModifiedChunkQueue){
					try {
						saveChunk(chunk.world, chunk);
					} catch (IOException e) {
						MelonUtilities.LOGGER.error("Chunk [x:{}, z:{}] Failed to Save During Snapshot!", chunk.xPosition, chunk.zPosition);
						continue;
					}
				}
			}
		}).start();
	}

	public static void pruneBackups(){

	}

	static float timeBetweenSnapshots = 0;
	static float timeBetweenBackups = 0;
	public static void tick(){
		timeBetweenSnapshots += 0.05f;
		if(timeBetweenSnapshots >= Data.configs.getOrCreate("config", ConfigData.class).timeBetweenSnapshots){
			takeSnapshot();
			timeBetweenSnapshots = 0;
		}
	}

}
