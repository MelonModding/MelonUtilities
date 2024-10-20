package MelonUtilities.rollback;

import java.io.ByteArrayOutputStream;

class RegionFileChunkBuffer
	extends ByteArrayOutputStream {
	private final int x;
	private final int z;
	final RegionFile regionFile;

	public RegionFileChunkBuffer(RegionFile regionFile, int x, int z) {
		super(8096);
		this.regionFile = regionFile;
		this.x = x;
		this.z = z;
	}

	@Override
	public void close() {
		this.regionFile.write(this.x, this.z, this.buf, this.count);
	}
}
