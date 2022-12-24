package dev.kyro.kyropit.controllers.objects;

import net.minecraft.client.settings.KeyBinding;

public abstract class Module {
	private boolean isEnabled = false;
	public KeyBinding keyBind;

	public abstract String getDisplayName();
	public abstract String getRefName();
	public abstract void onEnable();
	public abstract void onDisable();

	public void toggle() {
		isEnabled = !isEnabled;
		if(!isEnabled) onDisable(); else onEnable();
	}

	public boolean isEnabled() {
		return isEnabled;
	}
}
