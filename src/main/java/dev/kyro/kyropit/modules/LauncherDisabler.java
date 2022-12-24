package dev.kyro.kyropit.modules;

import dev.kyro.kyropit.Misc;
import dev.kyro.kyropit.controllers.objects.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class LauncherDisabler extends Module {
	private boolean foundSlime = false;

	@SubscribeEvent
	public void tickEvent(TickEvent.ClientTickEvent event) {
		if(!isEnabled() || Minecraft.getMinecraft().theWorld == null) return;

		EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
		World theWorld = Minecraft.getMinecraft().theWorld;

		boolean slime = false;
		main:
		for(int x = thePlayer.getPosition().getX() - 4, x2 = thePlayer.getPosition().getX() + 5; x < x2; x++) {
			for(int z = thePlayer.getPosition().getZ() - 4, z2 = thePlayer.getPosition().getZ() + 5; z < z2; z++) {
				for(int y = thePlayer.getPosition().getY() - 2, y2 = thePlayer.getPosition().getY(); y < y2; y++) {
					if(theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.slime_block) continue;
					slime = true; foundSlime = true;
					break main;
				}
			}
		}
		int sneakKey = Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode();
		if(!slime) {
			if(foundSlime) {
//				Lower pass
				main:
				for(int x = thePlayer.getPosition().getX() - 4, x2 = thePlayer.getPosition().getX() + 5; x < x2; x++) {
					for(int z = thePlayer.getPosition().getZ() - 4, z2 = thePlayer.getPosition().getZ() + 5; z < z2; z++) {
						for(int y = thePlayer.getPosition().getY() - 4, y2 = thePlayer.getPosition().getY() - 2; y < y2; y++) {
							if(theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.slime_block) continue;
							slime = true;
							break main;
						}
					}
				}
				if(!slime) {
					toggle();
					Misc.sendPlayerMessage(getDisplayName() + "&f has been auto-disabled");
					KeyBinding.setKeyBindState(sneakKey, false);
				}
			}
			return;
		}

		KeyBinding.setKeyBindState(sneakKey, !Minecraft.getMinecraft().thePlayer.isSneaking());
	}

	@Override
	public String getDisplayName() {
		return "&bLauncher Disabler";
	}

	@Override
	public String getRefName() {
		return "launcher";
	}

	@Override
	public void onEnable() {
		foundSlime = false;
	}

	@Override
	public void onDisable() {
		int sneakKey = Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode();
		KeyBinding.setKeyBindState(sneakKey, false);
	}
}
