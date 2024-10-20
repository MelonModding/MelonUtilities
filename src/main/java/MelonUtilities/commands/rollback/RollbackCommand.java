package MelonUtilities.commands.rollback;

import MelonUtilities.rollback.RollbackManager;
import MelonUtilities.utility.FeedbackHandler;
import MelonUtilities.utility.SyntaxBuilder;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.NbtIo;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.Packet51MapChunk;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.useless.serverlibe.api.gui.GuiHelper;
import org.useless.serverlibe.api.gui.ServerGuiBuilder;
import org.useless.serverlibe.api.gui.slot.ServerSlotButton;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
		RollbackManager.takeModifiedChunkSnapshot();
		FeedbackHandler.success(sender, "Snap!");
		return true;
	}

	private boolean loadSnapshot(CommandHandler handler, CommandSender sender, String[] args){

		File chunkDir = new File("./rollbackdata/modifiedchunksnapshots/" + sender.getWorld().dimension.id + "/c[x." + sender.getPlayer().chunkCoordX + "-z." + sender.getPlayer().chunkCoordZ + "]");
		chunkDir.mkdirs();
		if (chunkDir.isDirectory()) {

			File[] snapshots = chunkDir.listFiles();
			if(snapshots == null){
				FeedbackHandler.error(sender, "Chunk does not have any Snapshots!");
				return true;
			}

			HashMap<Long, File> snapshotHashmap = new HashMap<>();
			for (File snapshot : snapshots) {
				if (snapshot.isFile()) {
					snapshotHashmap.putIfAbsent(Long.parseLong(snapshot.getName().split(" ")[0]), snapshot);
				}
			}

			ServerGuiBuilder rollbackGui = new ServerGuiBuilder();
			rollbackGui.setSize((int)Math.ceil(snapshots.length / 9.0F));
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
							return new ServerSlotButton(snapshotIcon, inventory, finalI, () -> {
								RollbackManager.rollbackChunk(sender.getWorld().getChunkFromChunkCoords(sender.getPlayer().chunkCoordX, sender.getPlayer().chunkCoordZ), tag);
								MinecraftServer.getInstance().playerList.sendPacketToAllPlayersInDimension(new Packet51MapChunk(sender.getPlayer().chunkCoordX * 16, 0, sender.getPlayer().chunkCoordZ * 16, 16, 256, 16, sender.getWorld()), sender.getWorld().dimension.id);
								((EntityPlayerMP) sender.getPlayer()).usePersonalCraftingInventory();

							});
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
				}));
				i++;
			}

			GuiHelper.openCustomServerGui((EntityPlayerMP) sender.getPlayer(), rollbackGui.build((EntityPlayer) sender.getPlayer(), "Snapshots:"));

			FeedbackHandler.success(sender, "Opened Rollback GUI!");
			return true;

		} else {
			FeedbackHandler.error(sender, "Chunk has never been Modified!");
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

		FeedbackHandler.error(sender, " " + NAME + " Error: (Invalid Syntax)");
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
