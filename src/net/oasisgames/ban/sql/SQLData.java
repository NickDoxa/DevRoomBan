package net.oasisgames.ban.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import net.oasisgames.ban.Main;

public class SQLData extends SQLManager {

	public SQLData() {
		super(Main.getInstance, "banTable", "UUID VARCHAR(100)", true);
	}

	@Override
	protected String[] createKeyValueArray() {
		String[] columns = new String[] {"USERNAME VARCHAR(100)", "BANS INT(100)", "DATE VARCHAR(100)", "DURATION VARCHAR(100)", "ISSUER VARCHAR(100)", "CONTEXT VARCHAR(100)", "ACTIVE BOOL"};
		return columns;
	}
	
	//Retrieve ban status
	public boolean isPlayerBannedCurrently(Player player) {
		ResultSet answer = this.getValueByKey("" + player.getUniqueId(), "ACTIVE");
		try {
			return answer.first();
		} catch (SQLException e) {
			return false;
		}
	}
	
	//Send new ban information to table
	public void sendBanData(Player player, Player issuer, String context) {
		
	}
	
}
