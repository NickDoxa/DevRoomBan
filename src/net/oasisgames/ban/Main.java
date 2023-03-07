package net.oasisgames.ban;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.oasisgames.ban.commands.BanCommand;
import net.oasisgames.ban.sql.SQLData;

public class Main extends JavaPlugin {

	public static Main getInstance;
	
	private BanCommand banCmd;
	private SQLData data;
	
	@Override
	public void onEnable() {
		getInstance = this;
		
		banCmd = new BanCommand(getConfig());
		this.getCommand("ban").setExecutor(banCmd);
		this.getCommand("tempban").setExecutor(banCmd);
		
		data = new SQLData();
		Bukkit.getLogger().info(data.isConnected() ? "Connected successfully to the SQL Database, ready to send and receive data!" : "\nDatabase connection failed!\n");
	}
	
}
