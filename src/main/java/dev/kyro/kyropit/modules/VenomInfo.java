package dev.kyro.kyropit.modules;

import dev.kyro.kyropit.Misc;
import dev.kyro.kyropit.controllers.objects.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VenomInfo extends Module {
	public static VenomInfo INSTANCE;
	public static Map<EntityPlayer, Integer> venomedPlayers = new LinkedHashMap<>();
	public int tick = 0;

	public VenomInfo() {
		INSTANCE = this;
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if(event.phase != TickEvent.Phase.START) return;
		tick++;
		List<EntityPlayer> toRemove = new ArrayList<>();
		for(Map.Entry<EntityPlayer, Integer> entry : VenomInfo.venomedPlayers.entrySet()) {
			int time = entry.getValue();
			if(time <= 1) {
				toRemove.add(entry.getKey());
				continue;
			}
			venomedPlayers.put(entry.getKey(), time - 1);
		}
		for(EntityPlayer player : toRemove) venomedPlayers.remove(player);
	}

	@SubscribeEvent
	public void onAttack(AttackEntityEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if(!isEnabled()) return;
		if(!(event.target instanceof EntityPlayer) || event.entityPlayer == event.target) return;
		EntityPlayer targetPlayer = (EntityPlayer) event.target;
		new Thread(() -> {
			try {
				Thread.sleep(250);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			if(!player.isPotionActive(Potion.poison)) return;
			if(player.getActivePotionEffect(Potion.poison).getDuration() < 470) return;
			venomedPlayers.put(targetPlayer, 240);
			Misc.sendPlayerMessage(event.target.getName() + " is venomed");

			new Thread(() -> {
				try {
					Thread.sleep(1000 * 12);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
				venomedPlayers.remove(targetPlayer);
			}).start();
		}).start();
	}

	@SubscribeEvent
	public void modifyName(PlayerEvent.NameFormat event) {
		if(!isEnabled()) return;
		EntityPlayer entityPlayer = event.entityPlayer;
		if(entityPlayer == Minecraft.getMinecraft().thePlayer || !(event.entity instanceof EntityPlayer)) return;
		if(!venomedPlayers.containsKey(entityPlayer)) return;
		event.displayname = Misc.color("&2&lVENOM " + Math.ceil(venomedPlayers.get(entityPlayer) / 2.0) / 10.0 + "&r " + event.username);
	}

	@SubscribeEvent
	public void updateName(TickEvent.PlayerTickEvent event) {
		if(!isEnabled()) return;
		EntityPlayer entityPlayer = event.player;
		if(entityPlayer == Minecraft.getMinecraft().thePlayer) return;
		event.player.refreshDisplayName();
	}

	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Pre event) {
		if(!isEnabled() || event.entityPlayer == null || event.entityPlayer.getDisplayName() == null) return;
		EntityPlayer entityPlayer = event.entityPlayer;
		if(entityPlayer == Minecraft.getMinecraft().thePlayer) return;
		if(!venomedPlayers.containsKey(event.entityPlayer)) return;
		GlStateManager.color(255.0F, 255.0F, 255.0F);
	}

	@Override
	public String getDisplayName() {
		return "&2Venom Info";
	}

	@Override
	public String getRefName() {
		return "venom";
	}

	@Override
	public void onEnable() { }

	@Override
	public void onDisable() { }
}
