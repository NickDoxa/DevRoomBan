package net.oasisgames.ban;

import org.bukkit.plugin.java.JavaPlugin;

import net.oasisgames.ban.commands.BanCommand;

public class Main extends JavaPlugin {

	public static Main getInstance;
	
	private BanCommand banCmd;
	
	@Override
	public void onEnable() {
		getInstance = this;
		banCmd = new BanCommand(getConfig());
		this.getCommand("ban").setExecutor(banCmd);
		this.getCommand("tempban").setExecutor(banCmd);
	}
	
}
