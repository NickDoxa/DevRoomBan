package net.oasisgames.ban;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static Main getInstance;
	
	@Override
	public void onEnable() {
		getInstance = this;
	}
	
}
