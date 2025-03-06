package MelonUtilities.config.datatypes.legacydeserializers;

import MelonUtilities.MelonUtilities;
import MelonUtilities.config.datatypes.data.Kit;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.item.ItemStack;

public class KitLDs {
	public static Kit legacyDeserialize0(JsonDeserializationContext context, JsonObject obj){
		JsonObject generalValues = obj.getAsJsonObject("General Values");

		//New Kit Config
		Kit kit = new Kit(generalValues.get("kitID").getAsString());
		kit.kitVersion = MelonUtilities.kitConfigVersion;

		JsonObject kitItems = obj.getAsJsonObject("Kit Items");
		JsonObject kitArmors = obj.getAsJsonObject("Kit Armors");

		JsonArray kitItemStacks = kitItems.getAsJsonArray("kitItemStacks");
		for(JsonElement element : kitItemStacks){
			kit.kitItemStacks.add(context.deserialize(element, ItemStack.class));
		}

		JsonArray kitItemSlots = kitItems.getAsJsonArray("kitItemSlots");
		for(JsonElement element : kitItemSlots){
			kit.kitItemSlots.add(element.getAsInt());
		}

		JsonArray kitItemNames = kitItems.getAsJsonArray("kitItemNames");
		for(JsonElement element : kitItemNames){
			kit.kitItemNames.add(element.getAsString());
		}

		JsonArray kitArmorStacks = kitArmors.getAsJsonArray("kitArmorStacks");
		for(JsonElement element : kitArmorStacks){
			kit.kitArmorStacks.add(context.deserialize(element, ItemStack.class));
		}

		JsonArray kitArmorSlots = kitArmors.getAsJsonArray("kitArmorSlots");
		for(JsonElement element : kitArmorSlots){
			kit.kitArmorSlots.add(element.getAsInt());
		}

		JsonArray kitArmorNames = kitArmors.getAsJsonArray("kitArmorNames");
		for(JsonElement element : kitArmorNames){
			kit.kitArmorNames.add(element.getAsString());
		}

		kit.kitCooldown = generalValues.get("kitCooldown").getAsInt();

		return kit;
	}
}
