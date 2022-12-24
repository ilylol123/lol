package dev.kyro.kyropit.events;

import dev.kyro.kyropit.controllers.WorldManager;
import net.minecraftforge.fml.common.eventhandler.Event;

public class WorldChangeEvent extends Event {
	public State state;
	public WorldManager.WorldType worldType;

	public WorldChangeEvent(State state, WorldManager.WorldType worldType) {
		this.state = state;
		this.worldType = worldType;
	}

	public enum State {
		INITIALIZED,
		COMPLETED
	}
}
