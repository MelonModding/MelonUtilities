package MelonUtilities.rollback;

import MelonUtilities.MelonUtilities;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;


public class RollbackManager {

	static Set<Chunk> ModifiedChunkQueue = new HashSet<>();

	public static boolean skipModifiedQueuing = false;

	File backupsDir = new File("./rollbackdata/fullbackups");
	static File snapshotsDir = new File("./rollbackdata/modifiedchunksnapshots");
	static boolean createIfNecessary = true;
	public static SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy_HH.mm.ss");

	public static void saveChunk(World world, Chunk chunk) throws IOException {
		world.checkSessionLock();

		File chunkDir = new File(snapshotsDir + "/c[x." + chunk.xPosition + "-z." + chunk.zPosition + "]");
		chunkDir.mkdirs();
		Date resultdate = new Date(System.currentTimeMillis());
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
		int x = reader.getX();
		int z = reader.getZ();
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

	public static void OnInit(){
		new File("./rollbackdata").mkdirs();
		new File("./rollbackdata/fullbackups").mkdirs();
		new File("./rollbackdata/modifiedchunksnapshots").mkdirs();
	}

	public static void CreateFullBackup(){
		new Thread(() -> {
			//do the thing, put made thing into thing properly :)
		}).start();
	}

	public static void QueueModifiedChunk(Chunk chunk){
		ModifiedChunkQueue.add(chunk);
	}

	public static void TakeModifiedChunkSnapshot(){
		new Thread(() -> {
			Iterator<Chunk> chunkIterator = ModifiedChunkQueue.iterator();
			while(chunkIterator.hasNext()){
				Chunk c = chunkIterator.next();
				try {
					saveChunk(c.world, c);
				} catch (IOException e) {
					MelonUtilities.LOGGER.error("Chunk [x:{}, z:{}] Failed to Save During Snapshot!", c.xPosition, c.zPosition);
					continue;
				}
				chunkIterator.remove();
			}
		}).start();
	}


	public static void PruneFullBackups(){

	}

	public static void Tick(){
		long currentTime = System.currentTimeMillis();

	}

}
