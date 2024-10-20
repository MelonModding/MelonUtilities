package MelonUtilities.commands.kit;

import MelonUtilities.config.Data;
import MelonUtilities.utility.FeedbackHandler;
import MelonUtilities.utility.SyntaxBuilder;
import MelonUtilities.config.datatypes.KitData;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.InventoryPlayer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

//TODO Needs a complete rewrite: Switch Statements need put into separate classes similar to Roles, as well as Integration with CommandSyntaxBuilder

public class KitCommand extends Command {
	private final static String COMMAND = "kit";
	private final static String NAME = "Kit";
	public static HashMap<String, HashMap<String, Long>> cooldowns = new HashMap<>();

	private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

		public boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		return pattern.matcher(strNum).matches();
	}

	public static String hmsConversion(long millis) {

		Duration duration = Duration.ofMillis(millis);

		long h = duration.toHours();
		long m = duration.toMinutes() % 60;
		long s = duration.getSeconds() % 60;

	    return String.format("%02d:%02d:%02d [h:m:s]", h, m, s);
	}

	public KitCommand() {
		super(COMMAND);
	}

	public static int[] evalInventory(InventoryPlayer inventory, ItemStack comparisonStack){

		int[] slotEval = new int[36];

		/* slotEval Key
		* 0 = null
		* 1 = can merge into slot
		* 2 = merge and split
		* 3 = cannot go into slot
		*/

		for(int i=0; i < 36; i++){
			ItemStack stackInSlot = inventory.getStackInSlot(i);

			if(stackInSlot == null){
				slotEval[i] = 0;
				continue;
			}

			if(stackInSlot.stackSize >= stackInSlot.getMaxStackSize()){
				slotEval[i] = 3;
				continue;
			}

			if(stackInSlot.isItemEqual(comparisonStack)){

				if(stackInSlot.stackSize + comparisonStack.stackSize > stackInSlot.getMaxStackSize()){
					slotEval[i] = 2;
					continue;
				}

				slotEval[i] = 1;
				continue;
			}

			slotEval[i] = 3;
			continue;
		}
		return slotEval;
	}

	public static void insertItemAtSlot(int idealSlot, ItemStack item, CommandSender sender){

		int[] slotEval = evalInventory(sender.getPlayer().inventory, item);


		for (int i = 0; i < 36; i++) {

			int currentID = (idealSlot + i) % 36;
			int slot = slotEval[currentID];

			if(slot == 3){
				continue;
			}

			if(slot == 0){
				sender.getPlayer().inventory.setInventorySlotContents(currentID, new ItemStack(item));
				return;
			}

			ItemStack inventoryStack = sender.getPlayer().inventory.getStackInSlot(currentID);

			if(slot == 1){
				inventoryStack.stackSize += item.stackSize;
				return;
			}

			if(slot == 2){

				int stackSum = inventoryStack.stackSize + item.stackSize;

				inventoryStack.stackSize = item.getMaxStackSize();
				ItemStack newStack = new ItemStack(item);
				newStack.stackSize = stackSum - item.getMaxStackSize();
				insertItemAtSlot((currentID + 1) % 36, newStack, sender);
				return;
			}
		}
		EntityItem itemToDrop = new EntityItem(sender.getPlayer().world, sender.getPlayer().x, sender.getPlayer().y + 1, sender.getPlayer().z, new ItemStack(item));
		itemToDrop.delayBeforeCanPickup = 10;
		sender.getPlayer().world.entityJoinedWorld(itemToDrop);
	}

	static int listIndexOf(ItemStack[] items, ItemStack target) {
		List<ItemStack> list = Arrays.asList(items);
		return list.indexOf(target);
	}

	static SyntaxBuilder syntax = new SyntaxBuilder();

	public static void buildKitSyntax(){
		syntax.clear();
		syntax.append("title",                                TextFormatting.LIGHT_GRAY + "< Command Syntax >");
		syntax.append("give",                                 TextFormatting.LIGHT_GRAY + "  > /kit give <kit> [<overwrite?>]");
		syntax.append("create",                               TextFormatting.LIGHT_GRAY + "  > /kit create <kit> [<cooldown>]");
		syntax.append("delete",                               TextFormatting.LIGHT_GRAY + "  > /kit delete <kit>");
		syntax.append("setcooldown",                          TextFormatting.LIGHT_GRAY + "  > /kit setcooldown <kit> <cooldown>");
		syntax.append("addto",                                TextFormatting.LIGHT_GRAY + "  > /kit addto <kit> <mode>");
		syntax.append("addtoMode", "addto",             TextFormatting.LIGHT_GRAY + "    > item/row/armor/all");
		syntax.append("addtoArmorMode", "addtoMode",    TextFormatting.LIGHT_GRAY + "      > [head/chest/legs/boots/all] (if armor)");
		syntax.append("reset",                                TextFormatting.LIGHT_GRAY + "  > /kit reset <kit> [<username>]");
		syntax.append("list",                                 TextFormatting.LIGHT_GRAY + "  > /kit list [<kit>]");
		syntax.append("reload",                               TextFormatting.LIGHT_GRAY + "  > /kit reload");
	}

	@Override
	public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
        {
            if (args.length == 0) {
                return false;
            }

            if (args[0].equals("give")) {
                if (args.length == 1) {
					FeedbackHandler.success(sender, "Failed to Give Kit (Invalid Syntax)");
                    FeedbackHandler.syntax(sender, "/kit give <kit> [<overwrite?>]");
                    return true;
                }

                if (Data.kits.dataHashMap.containsKey(args[1])) {

                    String kit = args[1];
                    KitData kitdata = Data.kits.getOrCreate(kit, KitData.class);
                    long cooldown = kitdata.kitCooldown * 1000L;

                    if (args.length > 2 && args[2].equals("true")) {

                        if (System.currentTimeMillis() - cooldowns.getOrDefault(kit, new HashMap<>()).getOrDefault(sender.getPlayer().username, 0L) > cooldown) {
                            cooldowns.get(kit).put(sender.getPlayer().username, System.currentTimeMillis());

                            int counter = 0;

                            for (ItemStack item : kitdata.kitItemStacks) {
                                if (kitdata.kitItemNames.get(counter) != null) {
                                    item.setCustomName(kitdata.kitItemNames.get(counter));
                                }
                                sender.getPlayer().inventory.setInventorySlotContents(kitdata.kitItemSlots.get(counter++), new ItemStack(item));
                            }
                            //give items ^

                            counter = 0;

                            for (ItemStack armor : kitdata.kitArmorStacks) {
                                if (kitdata.kitArmorNames.get(counter) != null) {
                                    armor.setCustomName(kitdata.kitArmorNames.get(counter));
                                }
                                sender.getPlayer().inventory.setInventorySlotContents(kitdata.kitArmorSlots.get(counter++), new ItemStack(armor));
                            }
                            //give armor ^

                            Data.kits.saveAll();
							FeedbackHandler.success(sender, "Given Kit: '" + kit + "' to " + sender.getPlayer().username);
                            return true;
                        }
                    }

                    if (System.currentTimeMillis() - cooldowns.getOrDefault(kit, new HashMap<>()).getOrDefault(sender.getPlayer().username, 0L) > cooldown) {
                        cooldowns.putIfAbsent(kit, new HashMap<>());
                        cooldowns.getOrDefault(kit, new HashMap<>()).put(sender.getPlayer().username, System.currentTimeMillis());

                        int counter = 0;


                        for (ItemStack item : kitdata.kitItemStacks) {
                            if (kitdata.kitItemNames.get(counter) != null) {
                                item.setCustomName(kitdata.kitItemNames.get(counter));
                            }
                            insertItemAtSlot(kitdata.kitItemSlots.get(counter++), item, sender);
                        }
                        //give items ^

                        counter = 0;

                        for (ItemStack armor : kitdata.kitArmorStacks) {
                            if (kitdata.kitArmorNames.get(counter) != null) {
                                armor.setCustomName(kitdata.kitArmorNames.get(counter));
                            }
                            if (sender.getPlayer().inventory.getStackInSlot(39 - counter) != null) {
                                insertItemAtSlot(0, armor, sender);
                                counter++;
                                continue;
                            }
                            sender.getPlayer().inventory.setInventorySlotContents(kitdata.kitArmorSlots.get(counter++), new ItemStack(armor));
                        }
                        //give armor ^

                        Data.kits.saveAll();
						FeedbackHandler.success(sender, "Given Kit: '" + kit + "' to " + sender.getPlayer().username);
                        return true;
                    }
                    if (!Data.kits.dataHashMap.containsKey(kit)) {
						FeedbackHandler.error(sender, "Failed to Give Kit: '" + kit + "' to " + sender.getPlayer().username + " (Kit Doesn't Exist)");
                        sender.sendMessage("");
                    } else {
						FeedbackHandler.destructive(sender, "You've already used this kit... time left until next kit: ");
						FeedbackHandler.destructive(sender, hmsConversion(cooldown - (System.currentTimeMillis() - cooldowns.getOrDefault(kit, new HashMap<>()).getOrDefault(sender.getPlayer().username, 0L))));
                        return true;
                    }
                }
            }
            if (args[0].equals("reset")) {
                if (args.length == 1) {
					FeedbackHandler.error(sender, "Failed to Reset Kit Cooldown (Invalid Syntax)");
					FeedbackHandler.syntax(sender, "/kit reset <kit> [<player>]");
                    return true;
                }
                if (args.length > 2) {
                    String kit = args[1];
                    String player = args[2];
                    if (handler.playerExists(player)) {
                        cooldowns.getOrDefault(kit, new HashMap<>()).put(handler.getPlayer(player).username, 0L);
						FeedbackHandler.success(sender, handler.getPlayer(player).username + "'s Kit: '" + kit + "' Cooldown Reset");
                        return true;
                    } else {
                        FeedbackHandler.error(sender, "Failed to Reset " + player + "'s Cooldown for Kit: " + kit);
                        FeedbackHandler.error(sender, "(Player Doesn't Exist)");
                        return true;
                    }
                }
                String kit = args[1];
                if (Data.kits.dataHashMap.containsKey(kit)) {
                    cooldowns.getOrDefault(kit, new HashMap<>()).put(sender.getPlayer().username, 0L);
					FeedbackHandler.success(sender, "Kit: '" + kit + "' Cooldown Reset!");
                    return true;
                }

                return true;
            }


            if (args[0].equals("reload")) {
                Data.kits.loadAll(KitData.class);
				FeedbackHandler.success(sender, "Reloaded " + Data.kits.dataHashMap.size() + " Kit(s)!");
				buildKitSyntax();
				FeedbackHandler.success(sender, "Built Kit Syntax!");
                return true;
            }


            if (args[0].equals("setcooldown")) {

                if (args.length == 1) {
					FeedbackHandler.error(sender, "Failed to Set Kit Cooldown (Invalid Syntax)");
					FeedbackHandler.syntax(sender, "/kit setcooldown <kit> <cooldown>");
                    return true;
                }

                String kit = args[1];

                if (args.length > 2 && Data.kits.dataHashMap.containsKey(kit) && isNumeric(args[2])) {
                    KitData kitdata = Data.kits.getOrCreate(kit, KitData.class);
                    kitdata.kitCooldown = Long.parseLong(args[2]);
                    Data.kits.saveAll();
					FeedbackHandler.success(sender, "Set Cooldown for Kit: '" + kit + "' to: " + args[2]);
                    return true;
                }

                return true;
            }


            if (args[0].equals("list")) {

                if (args.length > 1 && Data.kits.dataHashMap.containsKey(args[1])) {

                    KitData kitdata = Data.kits.getOrCreate(args[1], KitData.class);

                    FeedbackHandler.syntax(sender, "< Kit: '" + args[1] + "' List >");
                    FeedbackHandler.syntax(sender, "  < Cooldown: " + hmsConversion(kitdata.kitCooldown * 1000) + " >");
                    FeedbackHandler.syntax(sender, "  < Armor: >");
                    for (ItemStack armor : kitdata.kitArmorStacks) {
                        FeedbackHandler.syntax(sender, "    > " + armor.getDisplayName());
                    }
                    FeedbackHandler.syntax(sender, "  < Items: >");
                    for (ItemStack item : kitdata.kitItemStacks) {
                        FeedbackHandler.syntax(sender, "    > " + item.getDisplayName() + " * " + item.stackSize);
                    }


                    return true;

                }

                if (Data.kits.dataHashMap.isEmpty()) {
                    FeedbackHandler.syntax(sender, "< Kits: >");
                    FeedbackHandler.syntax(sender, "  -No Kits Created-");
                    return true;
                }

                FeedbackHandler.syntax(sender, "< Kits: >");

                for (String kit : Data.kits.dataHashMap.keySet()) {
                    FeedbackHandler.syntax(sender, "  > " + kit);
                }

                return true;
            }

            if (args[0].equals("create")) {

                if (args.length == 1) {
					FeedbackHandler.error(sender, "Failed to Create Kit (Invalid Syntax)");
					FeedbackHandler.syntax(sender, "/kit create <kit> [<cooldown>]");
                    return true;
                }

                String kit = args[1];

                if (Data.kits.dataHashMap.containsKey(kit)) {
                    FeedbackHandler.error(sender, "Failed to Create Kit: '" + kit + "' (Kit Already Exists)");
                    return true;
                }


                if (args.length > 2 && isNumeric(args[2])) {

                    if (args.length > 3 && args[3].equals("inv")) {
                        return true;
                    }

                    KitData kitdata = Data.kits.getOrCreate(kit, KitData.class);
                    kitdata.kitCooldown = Long.parseLong(args[2]);
                    Data.kits.saveAll();
                    FeedbackHandler.success(sender, "Created Kit: '" + kit + "' with Cooldown: " + args[2]);
                    return true;
                }

                Data.kits.getOrCreate(kit, KitData.class);
                Data.kits.saveAll();
                FeedbackHandler.success(sender, "Created Kit: '" + kit + "' with Cooldown: 0");
                return true;
            }

            if (args[0].equals("addto")) {

                if (args.length == 1) {
                    FeedbackHandler.error(sender, "Failed to Add To Kit (Invalid Syntax)");
					syntax.printLayerAndSubLayers("addto", sender);
                    return true;
                }

                String kit = args[1];

                if (!Data.kits.dataHashMap.containsKey(kit)) {
                    FeedbackHandler.error(sender, "Failed to Add To Kit: '" + kit + "' (Kit Doesn't Exist)");
                    FeedbackHandler.syntax(sender, "*Tip: Double Check your Spelling*");
                    return true;
                }

                KitData kitdata = Data.kits.getOrCreate(kit, KitData.class);

				switch (args[2]) {
					case "item":

						if (sender.getPlayer().getHeldItem() == null) {
							FeedbackHandler.error(sender, "Failed to Add To Kit: '" + kit + "' (Held Item is Null)");
							FeedbackHandler.syntax(sender, "*Tip: Hold an item in your hand*");
							return true;
						}

						kitdata.additem(new ItemStack(sender.getPlayer().getHeldItem()), listIndexOf(sender.getPlayer().inventory.mainInventory, sender.getPlayer().getHeldItem()));
						FeedbackHandler.success(sender, "Added [" + sender.getPlayer().getHeldItem() + "] to Kit: '" + kit + "'");
						Data.kits.saveAll();
						return true;
					case "row":
						int row = sender.getPlayer().inventory.hotbarOffset;
						for (int i = 0; i < 9; i++) {

							if (sender.getPlayer().inventory.getStackInSlot(i + row) == null) {
								continue;
							}

							kitdata.additem(new ItemStack(sender.getPlayer().inventory.getStackInSlot(i + row)), listIndexOf(sender.getPlayer().inventory.mainInventory, sender.getPlayer().inventory.getStackInSlot(i + row)));

						}

						Data.kits.saveAll();
						FeedbackHandler.success(sender, "Added Row to Kit: '" + kit + "'");

						return true;
					case "armor":
						switch (args[3]) {
							case "head":
								if (sender.getPlayer().inventory.getStackInSlot(39) == null) {
									FeedbackHandler.error(sender, "Failed to Add To Kit: '" + kit + "' (Equipped Armor is Null)");
									FeedbackHandler.syntax(sender, "*Tip: Equip armor in your " + args[3] + " slot*");
									return true;
								}
								kitdata.addarmor(sender.getPlayer().inventory.getStackInSlot(39), 39);
								FeedbackHandler.success(sender, "Added " + sender.getPlayer().inventory.getStackInSlot(39) + " to Kit: '" + kit + "'");
								return true;
							case "chest":
								if (sender.getPlayer().inventory.getStackInSlot(38) == null) {
									FeedbackHandler.error(sender, "Failed to Add To Kit: '" + kit + "' (Equipped Armor is Null)");
									FeedbackHandler.syntax(sender, "*Tip: Equip armor in your " + args[3] + " slot*");
									return true;
								}
								kitdata.addarmor(sender.getPlayer().inventory.getStackInSlot(38), 38);
								FeedbackHandler.success(sender, "Added " + sender.getPlayer().inventory.getStackInSlot(38) + " to Kit: '" + kit + "'");
								return true;
							case "legs":
								if (sender.getPlayer().inventory.getStackInSlot(37) == null) {
									FeedbackHandler.error(sender, "Failed to Add To Kit: '" + kit + "' (Equipped Armor is Null)");
									FeedbackHandler.syntax(sender, "*Tip: Equip armor in your " + args[3] + " slot*");
									return true;
								}
								kitdata.addarmor(sender.getPlayer().inventory.getStackInSlot(37), 37);
								FeedbackHandler.success(sender, "Added " + sender.getPlayer().inventory.getStackInSlot(37) + " to Kit: '" + kit + "'");
								return true;
							case "boots":
								if (sender.getPlayer().inventory.getStackInSlot(36) == null) {
									FeedbackHandler.error(sender, "Failed to Add To Kit: '" + kit + "' (Equipped Armor is Null)");
									FeedbackHandler.syntax(sender, "*Tip: Equip armor in your " + args[3] + " slot*");
									return true;
								}
								kitdata.addarmor(sender.getPlayer().inventory.getStackInSlot(36), 36);
								FeedbackHandler.success(sender, "Added " + sender.getPlayer().inventory.getStackInSlot(36) + " to Kit: '" + kit + "'");
								return true;
							case "all":
								if (sender.getPlayer().inventory.getStackInSlot(39) != null) {
									kitdata.addarmor(sender.getPlayer().inventory.getStackInSlot(39), 39);
								}
								if (sender.getPlayer().inventory.getStackInSlot(38) != null) {
									kitdata.addarmor(sender.getPlayer().inventory.getStackInSlot(38), 38);
								}
								if (sender.getPlayer().inventory.getStackInSlot(37) != null) {
									kitdata.addarmor(sender.getPlayer().inventory.getStackInSlot(37), 37);
								}
								if (sender.getPlayer().inventory.getStackInSlot(36) != null) {
									kitdata.addarmor(sender.getPlayer().inventory.getStackInSlot(36), 36);
								}
								FeedbackHandler.success(sender, "Added All Armor to Kit: '" + kit + "'");
								return true;
						}
						return true;
					case "all":
						for (int i = 0; i < 4; i++) {
							int row = i * 9;
							for (int j = 0; j < 9; j++) {

								if (sender.getPlayer().inventory.getStackInSlot(j + row) == null) {
									continue;
								}

								kitdata.additem(new ItemStack(sender.getPlayer().inventory.getStackInSlot(j + row)), listIndexOf(sender.getPlayer().inventory.mainInventory, sender.getPlayer().inventory.getStackInSlot(j + row)));

							}
						}
						if (sender.getPlayer().inventory.getStackInSlot(39) != null) {
							kitdata.addarmor(sender.getPlayer().inventory.getStackInSlot(39), 39);
						}
						if (sender.getPlayer().inventory.getStackInSlot(38) != null) {
							kitdata.addarmor(sender.getPlayer().inventory.getStackInSlot(38), 38);
						}
						if (sender.getPlayer().inventory.getStackInSlot(37) != null) {
							kitdata.addarmor(sender.getPlayer().inventory.getStackInSlot(37), 37);
						}
						if (sender.getPlayer().inventory.getStackInSlot(36) != null) {
							kitdata.addarmor(sender.getPlayer().inventory.getStackInSlot(36), 36);
						}

						FeedbackHandler.success(sender, "Added All Items and Armor to Kit: " + kit);
						Data.kits.saveAll();
						return true;
				}
				return true;
            }

            if (args[0].equals("delete")) {

                if (args.length == 1) {
                    FeedbackHandler.error(sender, "Failed to Delete Kit (Invalid Syntax)");
                    FeedbackHandler.syntax(sender, "/kit delete <kit>");
                    return true;
                }

                String kit = args[1];

                switch (Data.kits.remove(kit)) {
                    case 0:
                        sender.sendMessage(TextFormatting.ORANGE + "Deleted Kit: '" + kit + "'");
                        return true;
                    case 1:
                        FeedbackHandler.error(sender, "Failed to Delete Kit: '" + kit + "' (Kit Doesn't Exist)");
                        return true;
                    case 2:
                        FeedbackHandler.error(sender, "Failed to Delete Kit: '" + kit + "' (IO Error)");
                        return true;
                }
            }
        }
		FeedbackHandler.error(sender, " Kit Error: (Invalid Syntax)");
		return false;
	}

	private static final List<String> opList = new ArrayList<>();
	static{
		opList.add("create");
		opList.add("reset");
		opList.add("reload");
		opList.add("setcooldown");
		opList.add("delete");
		opList.add("addto");
	}
	@Override
	public boolean opRequired(String[] args) {

		if (args == null) return false;
		if (args.length == 0) return false;
		if (opList.contains(args[0])) return true;
        return args[0].equals("give") && args.length > 2;
    }

	@Override
	public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
		if (sender.isAdmin()) {
			syntax.printAllLines(sender);
		} else {
			FeedbackHandler.syntax(sender, "< Command Syntax >");
			FeedbackHandler.syntax(sender, "  > /kit give <kit> [<overwrite?>]");
			FeedbackHandler.syntax(sender, "  > /kit list [<kit>]");
		}
	}
}
