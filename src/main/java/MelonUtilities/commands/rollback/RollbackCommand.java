package MelonUtilities.commands.rollback;

import MelonUtilities.rollback.RollbackManager;
import MelonUtilities.utility.SyntaxBuilder;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.NbtIo;
import net.minecraft.core.entity.Entity;
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
		syntax.append("title",                                               TextFormatting.LIGHT_GRAY + "< Command Syntax > ([] = optional, <> = variable, / = or)");
		syntax.append("rollback", "title",                             TextFormatting.LIGHT_GRAY + "  > /rollback [<x,z> <x,z>] / [<mode>]");
		syntax.append("takeSnapshot", "rollback",                      TextFormatting.LIGHT_GRAY + "    > takeSnapshot");
		syntax.append("takeSnapshot", "rollback",                      TextFormatting.LIGHT_GRAY + "    > help");

	}

	private boolean takeSnapshot(CommandHandler handler, CommandSender sender, String[] args){
		RollbackManager.takeModifiedChunkSnapshot();
		sender.sendMessage(TextFormatting.LIME + "Snap!");
		return true;
	}

	private boolean rollback(CommandHandler handler, CommandSender sender, String[] args){

		if(args.length == 0){
			File chunkDir = new File("./rollbackdata/modifiedchunksnapshots/" + sender.getWorld().dimension.id + "/c[x." + sender.getPlayer().chunkCoordX + "-z." + sender.getPlayer().chunkCoordZ + "]");
			chunkDir.mkdirs();
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
								int chunkX = tag.getInteger("xPos");
								int chunkZ = tag.getInteger("zPos");

								return new ServerSlotButton(snapshotIcon, inventory, finalI, () -> {

									for(Entity entity : sender.getWorld().loadedEntityList){
										if(entity.chunkCoordX == chunkX && entity.chunkCoordZ == chunkZ){
											if(!(entity instanceof EntityPlayer)){
												entity.remove();
											}
										}
									}

									RollbackManager.rollbackChunk(sender.getWorld().getChunkFromChunkCoords(chunkX, chunkZ), tag);
									MinecraftServer.getInstance().playerList.sendPacketToAllPlayersInDimension(new Packet51MapChunk(chunkX * 16, 0, chunkZ * 16, 16, 256, 16, sender.getWorld()), sender.getWorld().dimension.id);
									((EntityPlayerMP) sender.getPlayer()).usePersonalCraftingInventory();
								});
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


		return false;
	}


	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
		if (args.length == 0) {
			return rollback(handler, sender, args);
		}

		switch(args[0].toLowerCase()){
			case "takesnapshot":
			case "ts":
				return takeSnapshot(handler, sender, args);
			case "help":
				return false;
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
