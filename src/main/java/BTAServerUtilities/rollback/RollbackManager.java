package BTAServerUtilities.rollback;

import BTAServerUtilities.BTAServerUtilities;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.NbtIo;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkLoaderLegacy;
import net.minecraft.core.world.save.LevelData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class RollbackManager {

	static Set<Chunk> ModifiedChunkQueue = new HashSet<>();

	public static boolean skipModifiedQueuing = false;

	File backupsDir = new File("./rollbackdata/fullbackups");
	static File snapshotsDir = new File("./rollbackdata/modifiedchunksnapshots");
	static boolean createIfNecessary = true;

	public static void saveChunk(World world, Chunk chunk) throws IOException {
		world.checkSessionLock();

		File chunkDir = new File(snapshotsDir + "/c[x." + chunk.xPosition + "-z." + chunk.zPosition + "]");
		chunkDir.mkdirs();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy_HH.mm.ss");
		Date resultdate = new Date(System.currentTimeMillis());
		File chunkFile = new File(chunkDir, System.currentTimeMillis() + ".[" + sdf.format(resultdate) + "].dat");

		if (chunkFile.exists()) {
			LevelData levelData = world.getLevelData();
			levelData.setSizeOnDisk(levelData.getSizeOnDisk() - chunkFile.length());
		}

		try {
			File tmpChunkFile = new File(chunkDir, "tmp_chunk.dat");
			FileOutputStream fileStream = new FileOutputStream(tmpChunkFile);
			CompoundTag levelTag = new CompoundTag();
			CompoundTag chunkDataTag = new CompoundTag();
			levelTag.put("Level", chunkDataTag);
			ChunkLoaderLegacy.storeChunkInCompound(chunk, world, chunkDataTag);
			NbtIo.writeCompressed(levelTag, fileStream);
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
					BTAServerUtilities.LOGGER.error("Chunk [x:{}, z:{}] Failed to Save During Snapshot!", c.xPosition, c.zPosition);
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
