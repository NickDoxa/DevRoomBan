package net.oasisgames.ban.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;


public abstract class SQLManager {
	
	/*
	 * MySQL API created by Nick Doxa for free use by developers every where!
	 * Current Spigot/Bukkit version: 1.19.2
	 */
	
	private final JavaPlugin plugin;
	private final String tableName;
	private final Map<Integer, String[]> creationKeyValues;
	private final Map<Integer, String> keyValues;
	private final String primaryKey;
	private final boolean printStatements;
	
    /*Variable Definitions: 
     * main = JavaPlugin class
     * name = table name within database, 
     * primaryKeyValue = the primary key for the table A.K.A. what piece of data you will use to access the rest,
     * showStatements = boolean determining whether to print write/read information to console
     * createValueArray() = A method for defining an array containing a key name followed by a SQL data type,
     * Example:	String[] columns = new String[]{"EXAMPLE1 VARCHAR(100)", "EXAMPLE2 INT(100)", "EXAMPLE3 BOOL"}
     * SQL DATA TYPES: https://www.w3schools.com/sql/sql_datatypes.asp
     * The outcome of this array goes directly into your SQL Table and Database through this class.
     */
	
	protected abstract String[] createKeyValueArray();
	
	public SQLManager(JavaPlugin main, String name, String primaryKeyValue, boolean showStatements) {
		printStatements = showStatements;
		plugin = main;
		tableName = name;
		creationKeyValues = createKeyValueMap();
		primaryKey = primaryKeyValue;
		keyValues = new HashMap<Integer, String>();
		for (int i=0;i<creationKeyValues.size();i++) {
			String key = this.creationKeyValues.get(i)[0];
			keyValues.put(i, key);
		}
		try {
			connect();
		} catch (SQLException e) {
			printToConsole("The database did not connect!");
		}
		if (isConnected()) {
			this.createTable();
		}
	}

	//Connection protocol
    private String host, port, database, username, password;
    protected static Connection connection;

    public void connect() throws SQLException {
    	printToConsole("Connecting to plugin: " + plugin.getName());
	    host = plugin.getConfig().getString("MySQL.host");
	    port = plugin.getConfig().getString("MySQL.port");
	    database = plugin.getConfig().getString("MySQL.database");
	    username = plugin.getConfig().getString("MySQL.username");
	    password = plugin.getConfig().getString("MySQL.password");
	    connection = DriverManager.getConnection("jdbc:mysql://" +
	    	     host + ":" + port + "/" + database + "?useSSL=false",
	    	     username, password);
	    printToConsole("Connected successfully to MySQL Database!");
    }
    
    //Disconnect method
    public void disconnect() {
    	if (isConnected()) {
    		try {
    			connection.close();
    			printToConsole("Connected successfully to MySQL Database!");
    			printToConsole("Disconnected successfully from MySQL Database!");
    		} catch(SQLException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    public static Connection getConnection() {
    	return connection;
    }
    
    public boolean isConnected() {
    	return (connection == null ? false : true);
    }
    
    /*
     * LOCAL METHODS
     */
    
    protected boolean printToConsole(String msg) {
    	if (printStatements) {
    		System.out.println("[MySQL API - " + plugin.getName() + "] " + msg);
    	}
    	return printStatements;
    }
    
    private Map<Integer, String[]> createKeyValueMap() {
    	Map<Integer, String[]> numericKeys = new HashMap<Integer, String[]>();
    	int count = 0;
    	for (String s : this.createKeyValueArray()) {
    		String[] keyArray = s.split(" ", 2);
    		numericKeys.put((count++), keyArray);
    	}
    	printToConsole("Key Value Map created with a total of: " + count + " keys.");
    	return numericKeys;
    }
    
    /*
     * CREATION METHODS
     */
    
    //Creates a data table with the specified key values if one does not already exist
    private void createTable() {
    	PreparedStatement ps;
    	String statement = "CREATE TABLE IF NOT EXISTS " + tableName + " (";
		printToConsole("Key Values: " + keyValues.size());
    	for (int i=0;i<keyValues.size(); i++) {
			statement = statement + creationKeyValues.get(i)[0] + "\s" + creationKeyValues.get(i)[1] + ",";
    	}
    	statement = statement + "" + primaryKey + " PRIMARY KEY)";
    	printToConsole("Executing statement: " + statement);
    	try {
    		ps = getConnection().prepareStatement(statement);
    		ps.executeUpdate();
    		printToConsole("Table Created and Accessed Successfully!");
    	} catch (SQLException | NullPointerException e) {
    		printToConsole("Table: " + tableName + ". Table either already exists, or an error was thrown!");
    		printToConsole("Error code: " + e);
    	}
    }
    
    //Create value key set without defined arguments
    public void createNewKeyValue(String key) {
		try {
			if (!keyExists(key)) {
				String[] basePrime = primaryKey.split(" ");
				String statement = "INSERT IGNORE INTO " + tableName + " (" + basePrime[0] + ",";
				int q = 1;
				for (int i=1;i<keyValues.size();i++) {
					statement = statement + keyValues.get(i) + ",";
					q++;
				}
				statement = statement.substring(0, statement.length()-1) + ") VALUES (";
				for (int i=0;i<q;i++) {
					statement = statement + "?,";
				}
				statement = statement.substring(0, statement.length()-1) + ")";
				PreparedStatement ps = getConnection().prepareStatement(statement);
				ps.setString(1, key);
				for (int i=2;i<q;i++) {
					ps.setObject(i, null);
				}
				ps.executeUpdate();
				return;
			}
		} catch (SQLException | NullPointerException e) {
			printToConsole("Creation error: " + e);
			return;
		}
	}
    
    //Create value key set with defined arguemnts
    public boolean createNewKeyValue(String key, Object[] args) {
		try {
			if (!keyExists(key)) {
				String[] basePrime = primaryKey.split(" ");
				String statement = "INSERT IGNORE INTO " + tableName + " (" + basePrime[0] + ",";
				int q = 1;
				for (int i=1;i<keyValues.size();i++) {
					statement = statement + keyValues.get(i) + ",";
					q++;
				}
				statement = statement.substring(0, statement.length()-1) + ") VALUES (";
				for (int i=0;i<q;i++) {
					statement = statement + "?,";
				}
				statement = statement.substring(0, statement.length()-1) + ")";
				printToConsole("Executing Statement: " + statement);
				PreparedStatement ps = getConnection().prepareStatement(statement);
				ps.setObject(1, key);
				for (int i=2;i<q;i++) {
					ps.setObject(i, args[i-1]);
				}
				ps.setObject(q, args[q-1]);
				ps.executeUpdate();
				return true;
			}
		} catch (SQLException | NullPointerException e) {
			printToConsole("Creation error: " + e);
			return false;
		}
		return false;
	}
    
    //Update a key values information
    public void setValue(String key, Object value, String primary) {
		try {
			PreparedStatement ps = getConnection().prepareStatement("UPDATE " + tableName + " SET " + key + "=? WHERE " + primaryKey + "=?");
			ps.setObject(1, value);
			ps.setObject(2, primary);
			ps.executeUpdate();
		} catch (SQLException e) {
			printToConsole("Error executing value update method!");
		}
    }
    
	//Checking if a key value is within the primaryKey
	public boolean keyExists(String key) {
		try {
			PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM " + tableName + " WHERE " + primaryKey + "=?");
			ps.setString(1, key);
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				//Key found
				return true;
			}
			//Key not found
			return false;
		} catch (SQLException | NullPointerException e) {
			return false;
		}
	}
	
	//Returns a ResultSet from grabbing whatever value is saved under the specified valueKey based on the primaryKey
	public ResultSet getValueByKey(String key, String valueKey) {
		try {
			PreparedStatement ps = getConnection().prepareStatement("SELECT " + valueKey + " FROM " + tableName + " WHERE " + primaryKey + "=?");
			ps.setString(1, key);
			ResultSet rs = ps.executeQuery();
			return rs;
		} catch (SQLException | NullPointerException e) {
			return null;
		}
	}
	
	/*
	 * DELETION METHODS
	 */
	
	public void emptyTable() {
		try {
			PreparedStatement ps = getConnection().prepareStatement("TRUNCATE " + tableName);
			ps.executeUpdate();
		} catch (SQLException e) {
			printToConsole("Error truncating table!");
		}
	}
	
	public void removeKey(String key) {
		try {
			PreparedStatement ps = getConnection().prepareStatement("DELETE FROM " + tableName + " WHERE " + primaryKey + "=?");
			ps.setString(1, key);
			ps.executeUpdate();
		} catch (SQLException e) {
			printToConsole("Error removing key set from table!");
		}
	}
}

