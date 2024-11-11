package MelonUtilities.config.datatypes.data;

import net.minecraft.core.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Kit {

	public long kitCooldown = 0;
	public String kitID;
	public List<ItemStack> kitItemStacks = new ArrayList<>();
	public List<Integer> kitItemSlots = new ArrayList<>();
	public List<String> kitItemNames = new ArrayList<>();
	public List<ItemStack> kitArmorStacks = new ArrayList<>();
	public List<Integer> kitArmorSlots = new ArrayList<>();
	public List<String> kitArmorNames = new ArrayList<>();

	public Kit(String kitID) {
		this.kitID = kitID;
	}

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
