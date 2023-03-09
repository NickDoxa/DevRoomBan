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
		String[] columns = new String[] {"USERNAME VARCHAR(100)", "BANS INT(100)", "DATE VARCHAR(100)", "DURATION VARCHAR(100)", "ISSUER VARCHAR(100)", "CONTEXT VARCHAR(10000)"};
		return columns;
	}
	
	//Retrieve ban status
	public boolean isPlayerBannedCurrently(Player player) {
		if (!this.keyExists("" + player.getUniqueId())) return false;
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
	
	public String getLastBanMessage(Player player) {
		String output = "";
		ResultSet fullcontext = this.getValueByKey("" + player.getUniqueId(), "CONTEXT");
		try {
			String context = fullcontext.getString(0);
			String[] seperated = context.split(",");
			output = "Banned for: " + seperated[seperated.length-1];
		} catch (SQLException | NullPointerException e) {
			output = "Null context, seek admin help for ban appeal!";
		}
		return output;
	}
	
	public void unbanPlayer() {
		
	}
	
	//Send new ban information to table
	public boolean sendBanData(Player player, String issuer, Duration length, String... context) {
		if (this.keyExists("" + player.getUniqueId())) {
			//Player is not in the records must create new set of keys
			try {
				//CHANGE VALUES
				this.setValue("BANS", ((int) this.getValueByKey("" + player.getUniqueId(), "BANS").getInt(0)) + 1, "" + player.getUniqueId());
				this.setValue("DATE", Conversion.getCurrentDateTimeAsString(), "" + player.getUniqueId());
				this.setValue("DURATION", Conversion.durationToString(length), "" + player.getUniqueId());
				this.setValue("ISSUER", ((String) this.getValueByKey("" + player.getUniqueId(), "ISSUER").getString(0)) + "," + issuer, "" + player.getUniqueId());
				this.setValue("CONTEXT", ((String) this.getValueByKey("" + player.getUniqueId(), "CONTEXT").getString(0)) + "," + issuer, "" + player.getUniqueId());
				return true;
			} catch (SQLException | NullPointerException e) {
				printToConsole("send Data Error: " + e.getMessage());
				return false;
			}
		} else {
			//CREATE NEW VALUES
			Object[] args = new Object[]{
				player.getName(),//USERNAME
				1,//BANS
				Conversion.getCurrentDateTimeAsString(),//CURRENT DATE
				Conversion.durationToString(length),//CURRENT DURATION
				issuer,//ISSUERS
				context //CONTEXTS
			};
			return this.createNewKeyValue("" + player.getUniqueId(), args);
		}
	}
	
}
