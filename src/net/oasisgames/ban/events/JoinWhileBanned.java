package net.oasisgames.ban.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.oasisgames.ban.sql.SQLData;

public class JoinWhileBanned implements Listener {
	
	private final SQLData data;
	
	public JoinWhileBanned(SQLData dt) {
		data = dt;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!data.isPlayerBannedCurrently(player)) return;
		player.kickPlayer(data.getLastBanMessage(player));
	}
	
}
