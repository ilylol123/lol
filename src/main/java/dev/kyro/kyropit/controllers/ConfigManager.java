package dev.kyro.kyropit.controllers;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

import java.io.File;

public class ConfigManager {
	public static Configuration config;

	public static void init() {
		File configFile = new File(Loader.instance().getConfigDir(), "kyropit.cfg");
		config = new Configuration(configFile);
		config.load();
	}

	public static void saveConfig() {
		if (config.hasChanged()) config.save();
	}
}
