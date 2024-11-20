package MelonUtilities.utility.classes;

import net.minecraft.core.item.ItemStack;

public class Icon {
	public String name;
	public byte color;
	public ItemStack itemDisplayed;
	public ItemStack icon;

	public Icon(String name, byte color, ItemStack itemDisplayed){
		this.name = name;
		this.color = color;
		this.itemDisplayed = itemDisplayed;

		icon = this.itemDisplayed;
		icon.setCustomName(name);
		icon.setCustomColor(color);
	}
}
