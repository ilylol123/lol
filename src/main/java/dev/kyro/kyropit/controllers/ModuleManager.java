package dev.kyro.kyropit.controllers;

import dev.kyro.kyropit.Misc;
import dev.kyro.kyropit.controllers.objects.Module;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

	public static List<Module> modules = new ArrayList<>();

	@SubscribeEvent
	public void onKeyInputEvent(InputEvent.KeyInputEvent event) {

		for(Module module : modules) {
			if(module.keyBind == null || !module.keyBind.isPressed()) continue;
			module.toggle();
			if(module.isEnabled()) {
				Misc.sendPlayerMessage(module.getDisplayName() + "&f has been enabled");
			} else {
				Misc.sendPlayerMessage(module.getDisplayName() + "&f has been disabled");
			}
		}
	}

	public static void registerModule(Module module) {

		modules.add(module);
		MinecraftForge.EVENT_BUS.register(module);

		int key = ConfigManager.config.get("keybinds", module.getRefName(), -1).getInt();
		if(key != -1) module.keyBind = new KeyBinding("", key, "");
	}

	public static Module getModule(String name) {

		for(Module module : modules) {

			if(module.getRefName().equalsIgnoreCase(name)) return module;
		}

		return null;
	}
}
