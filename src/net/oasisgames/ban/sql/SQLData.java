package net.oasisgames.ban.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Duration;
import java.util.Date;

import org.bukkit.entity.Player;

import net.oasisgames.ban.Main;
import net.oasisgames.ban.time.Conversion;

public class SQLData extends SQLManager {

	public SQLData() {
		super(Main.getInstance, "banTable", "UUID VARCHAR(100)", true);
	}

	@Override
	protected String[] createKeyValueArray() {
		String[] columns = new String[] {"USERNAME VARCHAR(100)", "BANS INT(100)", "DATE VARCHAR(100)", "DURATION VARCHAR(100)", "ISSUER VARCHAR(100)", "CONTEXT VARCHAR(1000)"};
		return columns;
	}
	
	//Retrieve ban status
	public boolean isPlayerBannedCurrently(Player player) {
		ResultSet answer = this.getValueByKey("" + player.getUniqueId(), "DATE");
		ResultSet answer2 = this.getValueByKey("" + player.getUniqueId(), "DURATION");
		try {
			Date checkDate = Conversion.parseDateTimeString(answer.getString(0));
			Duration checkDuration = Conversion.convertStringToTime(answer2.getString(0));
			return Conversion.isBeforeDuration(checkDate, checkDuration);
		} catch (SQLException | ParseException e) {
			return false;
		}
	}
	
	//Send new ban information to table
	public void sendBanData(Player player, Player issuer, Duration length, Date date, String... context) {
		
	}
	
}
