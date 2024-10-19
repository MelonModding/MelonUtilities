package MelonUtilities.config.datatypes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.core.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitData {

	@SerializedName(value = "Kit Cooldown")
	@Expose public long kitCooldown = 0;

	@SerializedName(value = "Kit Items (Item Stack List)")
	@Expose public List<ItemStack> kitItemStacks = new ArrayList<>();

	@SerializedName(value = "Kit Items Slots (Integer List)")
	@Expose public List<Integer> kitItemSlots = new ArrayList<>();

	@SerializedName(value = "Kit Items Names (String List)")
	@Expose public List<String> kitItemNames = new ArrayList<>();

	@SerializedName(value = "Kit Armor (Item Stack List)")
	@Expose public List<ItemStack> kitArmorStacks = new ArrayList<>();

	@SerializedName(value = "Kit Armor Slots (Integer List)")
	@Expose public List<Integer> kitArmorSlots = new ArrayList<>();

	@SerializedName(value = "Kit Armor Names (Integer List)")
	@Expose public List<String> kitArmorNames = new ArrayList<>();

	public void additem(ItemStack stack, int position){
		stack = new ItemStack(stack);
		if(kitItemSlots.contains(position)){
			int i = kitItemSlots.indexOf(position);
			removeitem(i);
		}

		kitItemStacks.add(stack);
		kitItemSlots.add(position);

		if(stack.hasCustomName()){
			kitItemNames.add(stack.getCustomName());
		}
		else{
			kitItemNames.add(null);
		}
	}

	public void removeitem(int index){

		kitItemStacks.remove(index);
		kitItemSlots.remove(index);
		kitItemNames.remove(index);

	}

	public void addarmor(ItemStack stack, int position){
		stack = new ItemStack(stack);
		if(kitArmorSlots.contains(position)){
			int i = kitArmorSlots.indexOf(position);
			removearmor(i);
		}

		kitArmorStacks.add(stack);
		kitArmorSlots.add(position);

		if(stack.hasCustomName()){
			kitArmorNames.add(stack.getCustomName());
		}
		else{
			kitArmorNames.add(null);
		}
	}

	public void removearmor(int index){

		kitArmorStacks.remove(index);
		kitArmorSlots.remove(index);
		kitArmorNames.remove(index);

	}

}
