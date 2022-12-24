package dev.kyro.kyropit.modules;

import dev.kyro.kyropit.controllers.WorldManager;
import dev.kyro.kyropit.controllers.objects.Module;
import dev.kyro.kyropit.events.WorldChangeEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoAFK extends Module {

	@Override
	public String getDisplayName() {
		return "&cAuto AFK";
	}

	@Override
	public String getRefName() {
		return "afk";
	}

	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent event) {
		if(!isEnabled()) return;
		String message = event.message.getFormattedText();
		System.out.println(message);

		if(message.contains("\u00A7r\u00A7c\u00A7lAFK WARNING!") ||
				message.contains("\u00A7r\u00A7c\u00A7lINSTANCE SHUTDOWN") ||
				message.contains("\u00A7cPlease don't spam") ||
				message.contains("\u00A7cYou were kicked for inactivity!") ||
				message.contains("\u00A7cA kick occurred in your connection, so you were put in the Pit lobby!") ||
				message.contains("\u00A7c\u00A7lCOULDN'T JOIN!") ||
				message.contains("\u00A7c\u00A7lEEK! &r&7Are you trying")) {
			WorldManager.changeWorld(WorldManager.WorldType.PIT);
		}
	}

	@SubscribeEvent
	public void onWorldChange(WorldChangeEvent event) {
		if(!isEnabled()) return;
		if(event.worldType == WorldManager.WorldType.LIMBO) {
			WorldManager.changeWorld(WorldManager.WorldType.LOBBY);
		} else if(event.worldType == WorldManager.WorldType.LOBBY) {
			WorldManager.changeWorld(WorldManager.WorldType.PIT);
		}
	}

	@Override
	public void onEnable() {}

	@Override
	public void onDisable() {}
}
