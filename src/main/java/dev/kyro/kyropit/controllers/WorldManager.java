package dev.kyro.kyropit.controllers;

import dev.kyro.kyropit.events.WorldChangeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class WorldManager {
	public static WorldType currentWorld = WorldType.UNKNOWN;

	private static final List<WorldType> queuedWorldChanges = new ArrayList<>();
	private static final List<List<Thread>> queuedWorldChangeCallbacks = new ArrayList<>();

	public static long lastWorldChange = 0;

	static {
		new Thread(() -> {
			while(true) {
				if(queuedWorldChanges.isEmpty() || lastWorldChange + 5250 > System.currentTimeMillis()) {
					sleep(100);
					continue;
				}

				WorldType worldType = queuedWorldChanges.remove(0);
				List<Thread> threads = queuedWorldChangeCallbacks.remove(0);

				if(worldType == WorldType.PIT) {
					Minecraft.getMinecraft().thePlayer.sendChatMessage("/play pit");
				} else if(worldType == WorldType.LOBBY) {
					Minecraft.getMinecraft().thePlayer.sendChatMessage("/l");
				}
				for(Thread thread : threads) {
					if(thread == null) continue;
					thread.start();
				}
				sleep(5500);
			}
		}).start();
	}

	@SubscribeEvent
	public void worldChangeEvent(WorldChangeEvent event) {
		lastWorldChange = System.currentTimeMillis();
	}

	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent event) {
		String message = event.message.getFormattedText();

		if(message.contains("\u00A7a\u00A7lSERVER FOUND!")) {
			MinecraftForge.EVENT_BUS.post(new WorldChangeEvent(WorldChangeEvent.State.INITIALIZED, WorldType.PIT));
			currentWorld = WorldType.PIT;
		} else if(message.contains("\u00A7eClick here to view") ||
				message.contains("\u00A76joined the lobby!")) {
			if(currentWorld != WorldType.LOBBY) MinecraftForge.EVENT_BUS.post(new WorldChangeEvent(WorldChangeEvent.State.COMPLETED, WorldType.LOBBY));
			currentWorld = WorldType.LOBBY;
		} else if(message.contains("\u00A7cYou are AFK. Move around to return from AFK.")
				|| message.contains("\u00A7cA kick occurred in your connection, so you have been routed to limbo!")) {
			MinecraftForge.EVENT_BUS.post(new WorldChangeEvent(WorldChangeEvent.State.INITIALIZED, WorldType.LIMBO));
			currentWorld = WorldType.LIMBO;
		}
	}

	public static boolean isQueuedForChange(WorldType worldType) {
		return queuedWorldChanges.contains(worldType);
	}

	public static void changeWorld(WorldType worldType) {
		changeWorld(worldType, null);
	}

	public static void changeWorld(WorldType worldType, Thread thread) {
		if(isQueuedForChange(worldType)) {
			if(thread == null) return;
			for(int i = 0; i < queuedWorldChanges.size(); i++) {
				WorldType testWolrdType = queuedWorldChanges.get(i);
				if(testWolrdType != worldType) continue;

				List<Thread> threads = queuedWorldChangeCallbacks.get(i);
				threads.add(thread);
			}
			return;
		}

		queuedWorldChanges.add(worldType);
		List<Thread> threads = new ArrayList<>();
		threads.add(thread);
		queuedWorldChangeCallbacks.add(threads);
	}

	public enum WorldType {
		UNKNOWN,
		PIT,
		LOBBY,
		LIMBO
	}

	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch(Exception ignored) { }
	}
}
