package dev.kyro.kyropit.modules;

import dev.kyro.kyropit.controllers.objects.Module;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HideBounties extends Module {

	private long launchDetected = 0;
	private long magnumOpus = 0;

//	@SubscribeEvent
//	public void onChat(ClientChatReceivedEvent event) {
//		System.out.println(event.message.getFormattedText());
//		System.out.println("contains: " + event.message.getFormattedText().contains("\u00A7r\u00A77activated \u00A7r\u00A7e\u00A7lMAGNUM OPUS"));
//		if(!event.message.getFormattedText().contains("\u00A7r\u00A77activated \u00A7r\u00A7e\u00A7lMAGNUM OPUS")) return;
//		magnumOpus = System.currentTimeMillis();
//	}
//
//	@SubscribeEvent(priority = EventPriority.HIGHEST)
//	public void onSound(SoundEvent.SoundSourceEvent event) {
//		if(!isEnabled()) return;
//		if(magnumOpus + 500 > System.currentTimeMillis()) return;
//		if(!event.name.equals("fireworks.launch") && event.sound.getPitch() != 1.1904762) return;
//
//		if(Minecraft.getMinecraft().thePlayer == null) return;
//		Misc.sendPlayerMessage("Disabling bounty hider for 5s due to launcher use");
//		launchDetected = System.currentTimeMillis();
//	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onRender(EntityJoinWorldEvent event) {
		if(launchDetected + 5000 > System.currentTimeMillis()) return;
		if(!isEnabled()) return;
		if(!(event.entity instanceof EntityArmorStand)) return;
		if(event.entity.getCustomNameTag().isEmpty()) event.setCanceled(true);
		if(!event.entity.getDisplayName().getFormattedText().contains("\u00A76\u00A7l") ||
				!event.entity.getDisplayName().getFormattedText().contains("0g\u00A7r")) return;
		event.setCanceled(true);
	}

	@Override
	public String getDisplayName() {
		return "&6Hide Bounties";
	}

	@Override
	public String getRefName() {
		return "bounty";
	}

	@Override
	public void onEnable() { }

	@Override
	public void onDisable() { }
}
