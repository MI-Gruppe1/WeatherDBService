package WeatherDBService.WeatherDBService;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

/**
 * MySQL DBConnector 
 * @author Jan-Peter Petersen 
 */

// Alles als Prototyp umgesetzt. dient als Grundlage der naechsten IMplementation
public class DBConnector {
	
	private String ip;
	private String port;
	private String database;
	private String user;
	private String passwort;
	private Connection connection;
	
	/**
	 * @param ip
	 * @param port
	 * @param database
	 * @param user  
	 * @param pass 
	 */
	
	public DBConnector(String ip, String port, String database, String user, String passwort) {
		this.ip = ip;
		this.port = port;
		this.database = database;
		this.user = user;
		this.passwort = passwort;
	}

	// Insert WeatherData into MySQL-Database
	public void insertWeatherData(WeatherDataObject weatherDataObject) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		this.connection = (Connection) DriverManager.getConnection("jdbc:mysql://"+ this.ip + ":" + this.port + "/" + this.database, this.user, this.passwort);
		
		String query = "INSERT INTO crawledWeatherData (weatherIcon, weatherDesc, weatherDescDetail, stationName, temperature, humidity, pressure, windDeg, windSpeed, dateTime)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		// Prepare SQL Statement
		PreparedStatement preparedStmt = connection.prepareStatement(query);
		preparedStmt.setString(1, weatherDataObject.getWeatherIcon());
		preparedStmt.setString(2, weatherDataObject.getWeatherDesc());
		preparedStmt.setString(3, weatherDataObject.getWeatherDescDetail());
		preparedStmt.setString(4, weatherDataObject.getStationName());
		preparedStmt.setString(5, String.valueOf(weatherDataObject.getTemperature()));
		preparedStmt.setString(6, String.valueOf(weatherDataObject.getHumidity()));
		preparedStmt.setString(7, String.valueOf(weatherDataObject.getPressure()));
		preparedStmt.setString(8, String.valueOf(weatherDataObject.getWindDeg()));
		preparedStmt.setString(9, String.valueOf(weatherDataObject.getWindSpeed()));
		preparedStmt.setString(10, String.valueOf(weatherDataObject.getDateTime()));
		preparedStmt.execute();
		connection.close();
		System.out.println("Success");
	}
	
	private int insertWeatherStation(String name, double longitude, double latitude) {
		
		// check for existing WeatherStation
		// id == 0 WeatherStation unknown or error
		// id != 0 WeatherStation exists
		int id = checkWeatherStationId(name);
		
		// if id == 0, then insert new WeatherStation and return id.WeatherStation
		if (id == 0) {
			String query = "INSERT INTO WeatherStation (name, longitude, latitude) VALUES (?, ?, ?)";
			try {
				PreparedStatement stmt = connection.prepareStatement(query);
				stmt.setString(1, name);
				stmt.setDouble(2, longitude);
				stmt.setDouble(3, latitude);			
				stmt.execute();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return checkWeatherStationId(name);
	}
	
	private int checkWeatherStationId(String name) {
		// check for existing WeatherStation
		String query = "SELECT id FROM WeatherStation WHERE name = '?'";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, name);
			ResultSet result = stmt.executeQuery(query);
			if (result.first() == true) {
				// return id.WeatherStation
				return result.getInt("id"); 
			} else {
				return 0;
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	
	
	public String selectTemperaturAtSpecificTime() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		this.connection = (Connection) DriverManager.getConnection("jdbc:mysql://"+ this.ip + ":" + this.port + "/" + this.database, this.user, this.passwort);
		
		String query = "SELECT temperature FROM crawledWeatherData LIMIT 1;";
		Statement statement = (Statement) this.connection.createStatement();
		ResultSet result = statement.executeQuery(query);
		result.first();
		return result.getString("temperature");
	}

	public double selectPrecipitationAtSpecificTime() {
		return 0.0;
	}
	
	public JSONObject selectCurrentWeather() {
		JSONObject k = new JSONObject();
		return k;
	}
}
