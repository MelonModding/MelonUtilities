package MelonUtilities.config.datatypes.jsonadapters;

import MelonUtilities.config.datatypes.data.Kit;
import com.google.gson.*;
import net.minecraft.core.item.ItemStack;

import java.lang.reflect.Type;

public class KitJsonAdapter implements JsonDeserializer<Kit>, JsonSerializer<Kit> {

	@Override
	public Kit deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		JsonObject kitItems = obj.getAsJsonObject("Kit Items");
		JsonObject kitArmors = obj.getAsJsonObject("Kit Armors");
		JsonObject generalValues = obj.getAsJsonObject("General Values");

		Kit kit = new Kit(generalValues.get("kitID").getAsString());

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

	@Override
	public JsonElement serialize(Kit src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();


		JsonObject kitItems = new JsonObject();
		JsonArray kitItemStacks = new JsonArray();
		for(ItemStack itemStack : src.kitItemStacks){
			kitItemStacks.add(context.serialize(itemStack));
		}
		kitItems.add("kitItemStacks", kitItemStacks);

		JsonArray kitItemSlots = new JsonArray();
		for(Integer itemSlot : src.kitItemSlots){
			kitItemSlots.add(itemSlot);
		}
		kitItems.add("kitItemSlots", kitItemSlots);

		JsonArray kitItemNames = new JsonArray();
		for(String itemName : src.kitItemNames){
			kitItemNames.add(itemName);
		}
		kitItems.add("kitItemNames", kitItemNames);
		obj.add("Kit Items", kitItems);


		JsonObject kitArmors = new JsonObject();
		JsonArray kitArmorStacks = new JsonArray();
		for(ItemStack armorItemStack : src.kitArmorStacks){
			kitArmorStacks.add(context.serialize(armorItemStack));
		}
		kitArmors.add("kitArmorStacks", kitArmorStacks);

		JsonArray kitArmorSlots = new JsonArray();
		for(Integer armorItemSlot : src.kitArmorSlots){
			kitArmorSlots.add(armorItemSlot);
		}
		kitArmors.add("kitArmorSlots", kitArmorSlots);

		JsonArray kitArmorNames = new JsonArray();
		for(String armorItemName : src.kitArmorNames){
			kitArmorNames.add(armorItemName);
		}
		kitArmors.add("kitArmorNames", kitArmorNames);
		obj.add("Kit Armors", kitArmors);


		JsonObject generalValues = new JsonObject();
		generalValues.addProperty("kitID", src.kitID);
		generalValues.addProperty("kitCooldown", src.kitCooldown);
		obj.add("General Values", generalValues);

		return obj;
	}
}
