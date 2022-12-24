package dev.kyro.kyropit.modules;

import dev.kyro.kyropit.Misc;
import dev.kyro.kyropit.controllers.objects.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class SwordSwap extends Module {
//	TODO: Make the slots configurable

	private int currentItem = 0;

	private int firstSlot = 0;
	private int secondSlot = 1;

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if(!isEnabled() || event.phase != TickEvent.Phase.START) return;
		EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
		if(Minecraft.getMinecraft().currentScreen != null) return;
		int previousItem = thePlayer.inventory.currentItem;
		if(previousItem != firstSlot && previousItem != secondSlot) return;
		thePlayer.inventory.currentItem = currentItem;
		if(currentItem == firstSlot) currentItem = secondSlot; else currentItem = firstSlot;
	}

	@Override
	public String getDisplayName() {
		return "&dSword Swap";
	}

	@Override
	public String getRefName() {
		return "swap";
	}

	@Override
	public void onEnable() {
		EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
		int previousItem = thePlayer.inventory.currentItem;
		if(previousItem != firstSlot && previousItem != secondSlot) {
			Misc.sendPlayerMessage("Select your first or second item slot to begin swapping");
		}
	}

	@Override
	public void onDisable() {

	}
}
