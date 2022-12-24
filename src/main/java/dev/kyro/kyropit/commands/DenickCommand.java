package dev.kyro.kyropit.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.kyro.kyropit.Misc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagEnd;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DenickCommand implements ICommand {

    @Override
    public String getCommandName() {
        return "denick";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "denick <ign>";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if(args.length < 1) {

            Misc.sendPlayerMessage("Usage: /denick <player> (tells you what pants and held item they have--pitpanda it)");
            return;
        }

        EntityPlayer thePlayer = Minecraft.getMinecraft().thePlayer;
        EntityPlayer targetPlayer = null;

        for(Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {

            if(!(entity instanceof EntityPlayer)) continue;
            EntityPlayer player = (EntityPlayer) entity;

            if(!player.getName().equalsIgnoreCase(args[0])) continue;

            targetPlayer = player;
        }
        if(targetPlayer == null) {

            Misc.sendPlayerMessage("That player does not exist");
            return;
        }

        boolean attemptingDenick = false;

        ItemStack held = targetPlayer.getHeldItem();
        if(held != null) {
            NBTTagCompound attributes = held.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes");
            NBTTagList enchants = attributes.getTagList("CustomEnchants", 10);
            if(!enchants.hasNoTags()) {
                int maxLives = attributes.getInteger("MaxLives");
                int tier = attributes.getInteger("UpgradeTier");
                boolean gemmed = attributes.getInteger("UpgradeGemsUses") == 1;

                HashMap<String, Integer> enchantMap = new HashMap<>();
                for(int i = 0; i < 3; i++) {
                    NBTBase testEnchant = enchants.get(i);
                    if(testEnchant instanceof NBTTagEnd) break;
                    NBTTagCompound enchant = (NBTTagCompound) testEnchant;
                    String key = enchant.getString("Key");
                    int level = enchant.getInteger("Level");
                    enchantMap.put(key, level);
                }
                denick(enchantMap, maxLives, tier, gemmed, true);
                attemptingDenick = true;

                Misc.sendPlayerMessage("Held: " + held.getDisplayName());
                List<String> lore = Misc.getItemLore(held);
                for(String line : lore) {
                    Misc.sendPlayerMessage(line);
                }
            }
        }

        ItemStack pants = targetPlayer.inventory.armorItemInSlot(1);
        if(pants != null) {
            NBTTagCompound attributes = pants.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes");
            NBTTagList enchants = attributes.getTagList("CustomEnchants", 10);
            if(!enchants.hasNoTags()) {
                int maxLives = attributes.getInteger("MaxLives");
                int tier = attributes.getInteger("UpgradeTier");
                boolean gemmed = attributes.getInteger("UpgradeGemsUses") == 1;

                HashMap<String, Integer> enchantMap = new HashMap<>();
                for(int i = 0; i < 3; i++) {
                    NBTBase testEnchant = enchants.get(i);
                    if(testEnchant instanceof NBTTagEnd) break;
                    NBTTagCompound enchant = (NBTTagCompound) testEnchant;
                    String key = enchant.getString("Key");
                    int level = enchant.getInteger("Level");
                    enchantMap.put(key, level);
                }
                denick(enchantMap, maxLives, tier, gemmed, false);
                attemptingDenick = true;

                Misc.sendPlayerMessage("Pants: " + pants.getDisplayName());
                List<String> lore = Misc.getItemLore(pants);
                for(String line : lore) {
                    Misc.sendPlayerMessage(line);
                }
            }
        }

        if(attemptingDenick) {
            Misc.sendPlayerMessage("Attempting to denick with pitpanda");
        } else {
            Misc.sendPlayerMessage("Could not denick; no mysticals");
        }
    }

    public static void denick(HashMap<String, Integer> enchantMap, int maxLives, int tier, boolean gemmed, boolean held) {
        String prefix = held ? "&cHeld: &f" : "&9Pants: &f";

        String itemRequest = "";
        for(Map.Entry<String, Integer> entry : enchantMap.entrySet()) {
            itemRequest += entry.getKey() + entry.getValue() + ",";
        }
        itemRequest += "maxlives" + maxLives;
        itemRequest += ",tier" + tier;
        if(gemmed) itemRequest += ",gemmed";

        String finalItemRequest = itemRequest;
        new Thread(() -> {
            try {
                URL url = new URL("https://pitpanda.rocks/api/itemsearch/" + finalItemRequest);
                HttpURLConnection request = (HttpURLConnection) url.openConnection();
                request.setRequestMethod("GET");
                request.connect();

                JsonParser jp = new JsonParser();
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
                JsonObject object = root.getAsJsonObject();

                boolean success = object.get("success").getAsBoolean();
                if(!success) {
                    Misc.sendPlayerMessage(prefix + "Request failed");
                    return;
                }

                JsonArray itemArray = object.getAsJsonArray("items");
                if(itemArray.size() == 0) {
                    Misc.sendPlayerMessage(prefix + "Item not found");
                    return;
                } else if(itemArray.size() > 1) {
                    Misc.sendPlayerMessage(prefix + "More than one player found. Reporting most recent find");
                }

                String ownerUUID = itemArray.get(0).getAsJsonObject().get("owner").getAsString();

                URL nameURL = new URL("https://api.mojang.com/user/profiles/" + ownerUUID + "/names");
                HttpURLConnection nameRequest = (HttpURLConnection) nameURL.openConnection();
                nameRequest.setRequestMethod("GET");
                nameRequest.connect();
                JsonParser nameJP = new JsonParser();
                JsonElement nameRoot = nameJP.parse(new InputStreamReader((InputStream) nameRequest.getContent()));
                String name = nameRoot.getAsJsonArray().get(nameRoot.getAsJsonArray().size() - 1).getAsJsonObject().get("name").getAsString();

                Misc.sendPlayerMessage(prefix + "Found item on &d" + name);

            } catch(IOException exception) {
                throw new RuntimeException(exception);
            }
        }).start();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        String name = args.length > 0 ? args[0].toLowerCase() : "";
        List<String> list = new ArrayList<>();
        for(final NetworkPlayerInfo info : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            String testName = info.getGameProfile().getName();
            if(!testName.toLowerCase().startsWith(name)) continue;
            list.add(testName);
        }
        return list;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return true;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
