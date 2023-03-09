package net.oasisgames.ban.commands;

import java.time.Duration;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.oasisgames.ban.Main;
import net.oasisgames.ban.sql.SQLData;
import net.oasisgames.ban.time.Conversion;

public class BanCommand implements CommandExecutor {
	
	private static final SQLData data = new SQLData();
	public static SQLData getSQL() { return data; }
	
	private final String failedCommandMsg;
	private final String offlinePlayerMsg;
	
	public BanCommand(FileConfiguration config) {
		failedCommandMsg = Main.prefix + ChatColor.translateAlternateColorCodes('&', config.getString("messages.failedCommand"));
		offlinePlayerMsg = Main.prefix + ChatColor.translateAlternateColorCodes('&', config.getString("messages.offlinePlayer"));
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//IF SENDER IS PLAYER
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!label.equalsIgnoreCase("ban") && !label.equalsIgnoreCase("tempban")) return true;
			if (args.length < 3) {
				player.sendMessage(failedCommandMsg);
				return true;
			}
			//Attempt to find online player
			try {
				Player target = Bukkit.getPlayer(args[0]);
				Duration banLength = Conversion.convertStringToTime(args[1]);
				String reason = "";
				for (int i = 2; i < args.length; i++) {
					reason += args[i] + "\s";
				}
				player.kickPlayer(target.getName());
				boolean success = data.sendBanData(target, player.getName(), banLength, reason);
				player.sendMessage(success ? Main.prefix + ChatColor.GREEN + "Successfully banned: " + target.getName() + ", for duration of: " + 
					banLength.toString() + ", and the reason of: " + reason :
					Main.prefix + ChatColor.RED + "Ban failed!");
			} catch (NullPointerException e) {
				player.sendMessage(offlinePlayerMsg);
				return true;
			}
		//IF SENDER NOT PLAYER
		} else {
			if (!label.equalsIgnoreCase("ban") && !label.equalsIgnoreCase("tempban")) return true;
			if (args.length < 3) {
				Bukkit.getLogger().info(failedCommandMsg);
				return true;
			}
			//Attempt to find online player
			try {
				Player target = Bukkit.getPlayer(args[0]);
				Duration banLength = Conversion.convertStringToTime(args[1]);
				String reason = "";
				for (int i = 2; i < args.length; i++) {
					reason += args[i] + "\s";
				}
				target.kickPlayer(reason);
				boolean success = data.sendBanData(target, "Console", banLength, reason);
				Bukkit.getLogger().info(success ? Main.prefix + ChatColor.GREEN + "Successfully banned: " + target.getName() + ", for duration of: " + 
					banLength.toString() + ", and the reason of: " + reason :
					Main.prefix + ChatColor.RED + "Ban failed!");
			} catch (NullPointerException e) {
				Bukkit.getLogger().info(offlinePlayerMsg);
				return true;
			}
		}
		
		return false;
	}
	
}
