package dev.kyro.kyropit;

import dev.kyro.kyropit.commands.BindCommand;
import dev.kyro.kyropit.commands.ToggleCommand;
import dev.kyro.kyropit.commands.DenickCommand;
import dev.kyro.kyropit.controllers.ConfigManager;
import dev.kyro.kyropit.controllers.ModuleManager;
import dev.kyro.kyropit.controllers.WorldManager;
import dev.kyro.kyropit.modules.*;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "KyroPit", version = KyroPit.VERSION)
public class KyroPit {
	public static final String VERSION = "1.1.6";

	public static KyroPit INSTANCE;

	public static String prefix = "&8[&bK&8]&f ";

	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		ConfigManager.init();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {

		INSTANCE = this;

		for(int i = 0; i < 25; i++) {
			System.out.println("kyro mod loading");
		}

		ClientCommandHandler.instance.registerCommand(new ToggleCommand());
		ClientCommandHandler.instance.registerCommand(new BindCommand());
		ClientCommandHandler.instance.registerCommand(new DenickCommand());

		MinecraftForge.EVENT_BUS.register(new ModuleManager());
		MinecraftForge.EVENT_BUS.register(new WorldManager());

		registerModules();
	}

	public void registerModules() {
		ModuleManager.registerModule(new VenomInfo());
		ModuleManager.registerModule(new SwordSwap());
		ModuleManager.registerModule(new HideBounties());
		ModuleManager.registerModule(new LauncherDisabler());
		ModuleManager.registerModule(new AutoAFK());
	}
}
