package MelonUtilities.rollback;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public class RegionFile {
	private static final byte[] emptySector = new byte[4096];
	private final File regionFile;
	private RandomAccessFile dataFile;
	private final int[] chunkLocations = new int[1024];
	private final int[] chunkTimestamps = new int[1024];
	private ArrayList<Boolean> freeSectors;
	private int sizeDeltaBytes;

	public RegionFile(File regionFile) {
		this.regionFile = regionFile;
		this.println("REGION LOAD " + this.regionFile);
		this.sizeDeltaBytes = 0;
		try {
			int i;
			int i2;
			this.dataFile = new RandomAccessFile(regionFile, "rw");
			if (this.dataFile.length() < 4096L) {
				for (i2 = 0; i2 < 1024; ++i2) {
					this.dataFile.writeInt(0);
				}
				for (i2 = 0; i2 < 1024; ++i2) {
					this.dataFile.writeInt(0);
				}
				this.sizeDeltaBytes += 8192;
			}
			if ((this.dataFile.length() & 0xFFFL) != 0L) {
				i2 = 0;
				while ((long)i2 < (this.dataFile.length() & 0xFFFL)) {
					this.dataFile.write(0);
					++i2;
				}
			}
			int lengthSectors = (int)this.dataFile.length() / 4096;
			this.freeSectors = new ArrayList(lengthSectors);
			for (i = 0; i < lengthSectors; ++i) {
				this.freeSectors.add(Boolean.TRUE);
			}
			this.freeSectors.set(0, Boolean.FALSE);
			this.freeSectors.set(1, Boolean.FALSE);
			this.dataFile.seek(0L);
			for (i = 0; i < 1024; ++i) {
				int location;
				this.chunkLocations[i] = location = this.dataFile.readInt();
				if (location == 0 || (location >> 8) + (location & 0xFF) > this.freeSectors.size()) continue;
				for (int j = 0; j < (location & 0xFF); ++j) {
					this.freeSectors.set((location >> 8) + j, Boolean.FALSE);
				}
			}
			for (i = 0; i < 1024; ++i) {
				int timestamp;
				this.chunkTimestamps[i] = timestamp = this.dataFile.readInt();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized int getSizeDeltaBytes() {
		int delta = this.sizeDeltaBytes;
		this.sizeDeltaBytes = 0;
		return delta;
	}

	private void print(String message) {
	}

	private void println(String message) {
		this.print(message + "\n");
	}

	private void debug(String op, int x, int z, String message) {
		this.print("REGION " + op + " " + this.regionFile.getName() + "[" + x + "," + z + "] = " + message);
	}

	private void debug(String op, int x, int z, int sizeBytes, String message) {
		this.print("REGION " + op + " " + this.regionFile.getName() + "[" + x + "," + z + "] " + sizeBytes + "B = " + message);
	}

	private void debugln(String op, int x, int z, String message) {
		this.debug(op, x, z, message + "\n");
	}

	public synchronized DataInputStream getChunkDataInputStream(int x, int z) {
		if (this.isChunkOutOfBounds(x, z)) {
			this.debugln("READ", x, z, "out of bounds");
			return null;
		}
		try {
			int chunkLocation = this.getChunkLocation(x, z);
			if (chunkLocation == 0) {
				return null;
			}
			int chunkOffset = chunkLocation >> 8;
			int chunkSectorCount = chunkLocation & 0xFF;
			if (chunkOffset + chunkSectorCount > this.freeSectors.size()) {
				this.debugln("READ", x, z, "invalid sector");
				return null;
			}
			this.dataFile.seek((long)chunkOffset * 4096L);
			int chunkLengthBytes = this.dataFile.readInt();
			if (chunkLengthBytes > 4096 * chunkSectorCount) {
				this.debugln("READ", x, z, "invalid length: " + chunkLengthBytes + " > 4096 * " + chunkSectorCount);
				return null;
			}
			byte compressionType = this.dataFile.readByte();
			if (compressionType == 1) {
				byte[] dataBuffer = new byte[chunkLengthBytes - 1];
				this.dataFile.read(dataBuffer);
				return new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(dataBuffer)));
			}
			if (compressionType == 2) {
				byte[] dataBuffer = new byte[chunkLengthBytes - 1];
				this.dataFile.read(dataBuffer);
				return new DataInputStream(new InflaterInputStream(new ByteArrayInputStream(dataBuffer)));
			}
			if (compressionType == 3) {
				byte[] dataBuffer = new byte[chunkLengthBytes - 1];
				this.dataFile.read(dataBuffer);
				return new DataInputStream(new ByteArrayInputStream(dataBuffer));
			}
			this.debugln("READ", x, z, "unknown version " + compressionType);
			return null;
		} catch (IOException ioexception) {
			this.debugln("READ", x, z, "exception");
			return null;
		}
	}

	public DataOutputStream getChunkDataOutputStream(int x, int z) {
		if (this.isChunkOutOfBounds(x, z)) {
			return null;
		}
		return new DataOutputStream(new DeflaterOutputStream(new RegionFileChunkBuffer(this, x, z)));
	}

	protected synchronized void write(int x, int z, byte[] chunkData, int size) {
		try {
			int chunkLocation = this.getChunkLocation(x, z);
			int chunkOffset = chunkLocation >> 8;
			int chunkSectorCount = chunkLocation & 0xFF;
			int newSectorCount = (size + 5) / 4096 + 1;
			if (newSectorCount >= 256) {
				return;
			}
			if (chunkOffset != 0 && chunkSectorCount == newSectorCount) {
				this.debug("SAVE", x, z, size, "rewrite");
				this.write(chunkOffset, chunkData, size);
			} else {
				for (int i = 0; i < chunkSectorCount; ++i) {
					this.freeSectors.set(chunkOffset + i, Boolean.TRUE);
				}
				int firstEmptySector = this.freeSectors.indexOf(Boolean.TRUE);
				int contiguousFreeSectors = 0;
				if (firstEmptySector != -1) {
					for (int sector = firstEmptySector; sector < this.freeSectors.size(); ++sector) {
						if (contiguousFreeSectors != 0) {
							contiguousFreeSectors = this.freeSectors.get(sector).booleanValue() ? ++contiguousFreeSectors : 0;
						} else if (this.freeSectors.get(sector).booleanValue()) {
							firstEmptySector = sector;
							contiguousFreeSectors = 1;
						}
						if (contiguousFreeSectors >= newSectorCount) break;
					}
				}
				if (contiguousFreeSectors >= newSectorCount) {
					this.debug("SAVE", x, z, size, "reuse");
					this.setChunkLocation(x, z, firstEmptySector << 8 | newSectorCount);
					for (int i = 0; i < newSectorCount; ++i) {
						this.freeSectors.set(firstEmptySector + i, Boolean.FALSE);
					}
					this.write(firstEmptySector, chunkData, size);
				} else {
					this.debug("SAVE", x, z, size, "grow");
					this.dataFile.seek(this.dataFile.length());
					int numSectors = this.freeSectors.size();
					for (int i = 0; i < newSectorCount; ++i) {
						this.dataFile.write(emptySector);
						this.freeSectors.add(Boolean.FALSE);
					}
					this.sizeDeltaBytes += 4096 * newSectorCount;
					this.write(numSectors, chunkData, size);
					this.setChunkLocation(x, z, numSectors << 8 | newSectorCount);
				}
			}
			this.setChunkTimestamp(x, z, (int)(System.currentTimeMillis() / 1000L));
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
	}

	private void write(int sectorOffset, byte[] chunkData, int size) throws IOException {
		this.println(" " + sectorOffset);
		this.dataFile.seek((long)sectorOffset * 4096L);
		this.dataFile.writeInt(size + 1);
		this.dataFile.writeByte(2);
		this.dataFile.write(chunkData, 0, size);
	}

	private boolean isChunkOutOfBounds(int x, int z) {
		return x < 0 || x >= 32 || z < 0 || z >= 32;
	}

	private int getChunkLocation(int x, int z) {
		return this.chunkLocations[x + z * 32];
	}

	public boolean chunkExists(int x, int z) {
		return this.getChunkLocation(x, z) != 0;
	}

	private void setChunkLocation(int x, int z, int location) throws IOException {
		this.chunkLocations[x + z * 32] = location;
		this.dataFile.seek(((long)x + (long)z * 32L) * 4L);
		this.dataFile.writeInt(location);
	}

	private void setChunkTimestamp(int x, int z, int timestamp) throws IOException {
		this.chunkTimestamps[x + z * 32] = timestamp;
		this.dataFile.seek(4096L + ((long)x + (long)z * 32L) * 4L);
		this.dataFile.writeInt(timestamp);
	}

	public void close() throws IOException {
		this.dataFile.close();
	}
}

