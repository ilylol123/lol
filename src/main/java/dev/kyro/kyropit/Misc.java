package dev.kyro.kyropit;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Misc {

	public static boolean isUUID(String uuid) {
		try {
			UUID.fromString(uuid);
			return true;
		} catch(Exception ignored) {
			return false;
		}
	}

	public static String getUnicodeNumber(int number) {

		String unicode = "";
		number = Math.max(Math.min(number, 10), 0);
		switch(number) {
			case 0:
				unicode = "\u0030";
				break;
			case 1:
				unicode = "\u0031";
				break;
			case 2:
				unicode = "\u0032";
				break;
			case 3:
				unicode = "\u0033";
				break;
			case 4:
				unicode = "\u0034";
				break;
			case 5:
				unicode = "\u0035";
				break;
			case 6:
				unicode = "\u0036";
				break;
			case 7:
				unicode = "\u0037";
				break;
			case 8:
				unicode = "\u0038";
				break;
			case 9:
				unicode = "\u0039";
				break;
			case 10:
				return "\uD83D\uDD1F";
		}
		return unicode + "\u20E3";
	}

	public static boolean playerHasEnchant(EntityPlayer player, String enchantName) {

		ItemStack pants = player.inventory.armorItemInSlot(1);
		if(pants != null) {
			List<String> lore = getItemLore(pants);
			for(String line : lore) {

				if(line.contains(color(enchantName))) return true;
			}
		}

		ItemStack sword = player.getHeldItem();
		if(sword != null) {
			List<String> lore = getItemLore(sword);
			for(String line : lore) {

				if(line.contains(color(enchantName))) return true;
			}
		}

		return false;
	}

	public static boolean playerHasArmas(EntityPlayer player) {

		ItemStack boots = player.inventory.armorItemInSlot(0);

		return boots != null && boots.getItem() == Items.leather_boots;
	}

	public static double distanceBetween(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2));
	}

	public static void sendPlayerMessage(String message) {
		if(Minecraft.getMinecraft() == null || Minecraft.getMinecraft().thePlayer == null) return;
		IChatComponent chatComponent = new ChatComponentText(color(KyroPit.prefix + message));
		Minecraft.getMinecraft().thePlayer.addChatMessage(chatComponent);
	}

	public static String color(String message) {

		return message.replaceAll("&", "\u00A7");
	}

	public static List<String> getItemLore(ItemStack itemStack) {

		List<String> lore = new ArrayList<>();

		NBTTagList loreNBT;
		try {
			loreNBT = itemStack.serializeNBT().getCompoundTag("tag").getCompoundTag("display").getTagList("Lore", 8);
		} catch(Exception ignored) {
			return lore;
		}

		for(int i = 0; i < loreNBT.tagCount(); i++) {

			lore.add(loreNBT.getStringTagAt(i));
		}

		return lore;
	}

	public static boolean isStackEmpty(ItemStack stack) {
		return stack == null;
	}

	/**
	 * Shift+Left clicks the specified item
	 *
	 * @param item        Slot of the item to click
	 * @param isArmorSlot
	 * @return True if it is unable to move the item
	 */
	public static boolean moveArmor(int item, boolean isArmorSlot) {
		Minecraft mc = Minecraft.getMinecraft();
		boolean full = isArmorSlot;

		if (full) {
			for (ItemStack iItemStack : mc.thePlayer.inventory.mainInventory) {
				if (isStackEmpty(iItemStack)) {
					full = false;
					break;
				}
			}
		}

		if (full) {
//			System.out.println("full");
			mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, item, 1, 4, mc.thePlayer);
		} else {
//			System.out.println("not full");
			mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId,
					(isArmorSlot ? item : (item < 9 ? item + 36 : item)), 0, 1, mc.thePlayer);
		}

		return false;
	}
}
