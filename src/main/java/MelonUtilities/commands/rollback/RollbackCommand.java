package MelonUtilities.commands.rollback;

import MelonUtilities.rollback.RollbackManager;
import MelonUtilities.utility.SyntaxBuilder;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.NbtIo;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.useless.serverlibe.api.gui.GuiHelper;
import org.useless.serverlibe.api.gui.ServerGuiBase;
import org.useless.serverlibe.api.gui.ServerGuiBuilder;
import org.useless.serverlibe.api.gui.slot.ServerSlotButton;
import org.useless.serverlibe.api.gui.slot.ServerSlotDisplay;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static MelonUtilities.rollback.RollbackManager.sdf;

public class RollbackCommand extends Command {

	private final static String COMMAND = "rollback";
	private final static String NAME = "RollbackCommand";

	public RollbackCommand(){super(COMMAND, "rb");}

	static SyntaxBuilder syntax = new SyntaxBuilder();
	public static void buildSyntax(){
		syntax.clear();
		syntax.append("title",                                                TextFormatting.LIGHT_GRAY + "< Command Syntax >");
	}

	private boolean takeSnapshot(CommandHandler handler, CommandSender sender, String[] args){
		RollbackManager.TakeModifiedChunkSnapshot();
		sender.sendMessage(TextFormatting.LIME + "Snap!");
		return true;
	}

	private boolean loadSnapshot(CommandHandler handler, CommandSender sender, String[] args){

		File chunkDir = new File("./rollbackdata/modifiedchunksnapshots/c[x." + sender.getPlayer().chunkCoordX + "-z." + sender.getPlayer().chunkCoordZ + "]");
		if (chunkDir.isDirectory()) {

			File[] snapshots = chunkDir.listFiles();
			if(snapshots == null){
				sender.sendMessage(TextFormatting.RED + "Chunk does not have any Snapshots!");
				return true;
			}

			HashMap<Long, File> snapshotHashmap = new HashMap<>();
			for (File snapshot : snapshots) {
				if (snapshot.isFile()) {
					snapshotHashmap.putIfAbsent(Long.parseLong(snapshot.getName().split(" ")[0]), snapshot);
				}
			}

			ServerGuiBuilder rollbackGui = new ServerGuiBuilder();
			int i = 0;
			for(Map.Entry<Long, File> snapshot : snapshotHashmap.entrySet()){
				int finalI = i;
				rollbackGui
					.setContainerSlot(i, (inventory -> {
					ItemStack snapshotIcon = Item.label.getDefaultStack();
					snapshotIcon.setCustomName("[" + sdf.format(snapshot.getKey()) + "]");
					snapshotIcon.setCustomColor((byte) TextFormatting.LIGHT_BLUE.id);
						try {
							CompoundTag tag = NbtIo.readCompressed(Files.newInputStream(snapshot.getValue().toPath()));
							return new ServerSlotButton(snapshotIcon, inventory, finalI, () -> RollbackManager.rollbackChunk(sender.getWorld().getChunkFromChunkCoords(sender.getPlayer().chunkCoordX, sender.getPlayer().chunkCoordZ), tag));
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
				}));
				i++;
			}

			GuiHelper.openCustomServerGui((EntityPlayerMP) sender.getPlayer(), rollbackGui.build((EntityPlayer) sender.getPlayer(), "Snapshots:"));

			sender.sendMessage(TextFormatting.LIME + "Opened Rollback GUI!");
			return true;

		} else {
			sender.sendMessage(TextFormatting.RED + "Chunk has never been Modified!");
			return true;
		}
	}


	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
		if (args.length == 0) {
			return false;
		}

		switch(args[0].toLowerCase()){
			case "takesnapshot":
			case "ts":
				return takeSnapshot(handler, sender, args);
			case "loadsnapshot":
			case "ls":
				return loadSnapshot(handler, sender,args);
		}

		sender.sendMessage(TextFormatting.RED + " " + NAME + " Error: (Invalid Syntax)");
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
