package net.oasisgames.ban;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import net.oasisgames.ban.commands.BanCommand;
import net.oasisgames.ban.events.JoinWhileBanned;

public class Main extends JavaPlugin {

	public static Main getInstance;
	public static String prefix;
	
	private BanCommand banCmd;
	private JoinWhileBanned joinEvent;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.prefix")) + "\s";
		getInstance = this;
		
		banCmd = new BanCommand(getConfig());
		this.getCommand("ban").setExecutor(banCmd);
		this.getCommand("tempban").setExecutor(banCmd);
		
		joinEvent = new JoinWhileBanned(BanCommand.getSQL());
		this.getServer().getPluginManager().registerEvents(joinEvent, this);
	}
	
}
