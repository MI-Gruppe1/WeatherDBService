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

public class DBConnector {
	
	private String ip;
	private String port;
	private String database;
	private String user;
	private String passwort;
	private Connection connection;
	private ResultSet resultSet;
	
	public DBConnector(String ip, String port, String database, String user, String passwort) {
		this.ip = ip;
		this.port = port;
		this.database = database;
		this.user = user;
		this.passwort = passwort;
	}

	// Insert WeatherData into MySQL-Database
	public void insertWeatherData(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		this.connection = (Connection) DriverManager.getConnection("jdbc:mysql://"+ this.ip + ":" + this.port + "/" + this.database, this.user, this.passwort);
		
		String query = " insert into crawledWeatherData (weatherIcon, weatherDesc, weatherDescDetail, stationName, temperature, humidity, pressure, windDeg, windSpeed, dateTime)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		// Schnitstelle:
		// 
		// GET TemperatureAtSpecificTime
		// GET PrecipitationAtSpecificTime
		// GET CurrentWeather
		// POST CurrentWeather
		PreparedStatement preparedStmt = connection.prepareStatement(query);
		JSONObject desc = json.getJSONArray("weather").getJSONObject(0);
		preparedStmt.setString(1, desc.get("icon").toString());
		preparedStmt.setString(2, desc.get("main").toString());
		preparedStmt.setString(3, desc.get("description").toString());
		preparedStmt.setString(4, json.getString("name"));
		JSONObject main = json.getJSONObject("main");
		preparedStmt.setString(5, main.get("temp").toString());
		preparedStmt.setString(6, main.get("humidity").toString());
		preparedStmt.setString(7, main.get("pressure").toString());
		JSONObject wind = json.getJSONObject("wind");
		preparedStmt.setString(8, wind.get("deg").toString());
		preparedStmt.setString(9, wind.get("speed").toString());
		preparedStmt.setString(10, String.valueOf(new Date().getTime()));
		
		preparedStmt.execute();

		connection.close();
		System.out.println("Success");
	}
	public String selectTemperaturAtSpecificTime() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		System.out.println("1");
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		this.connection = (Connection) DriverManager.getConnection("jdbc:mysql://"+ this.ip + ":" + this.port + "/" + this.database, this.user, this.passwort);
		String query = "SELECT temperature FROM crawledWeatherData LIMIT 1;";
		Statement statement = (Statement) this.connection.createStatement();
		resultSet = statement.executeQuery(query);
		resultSet.first();
		return resultSet.getString("temperature");
	}
	
	public double selectPrecipitationAtSpecificTime() {
		return 0.0;
	}
	
	public JSONObject selectCurrentWeather() {
		JSONObject k = new JSONObject();
		return k;
	}
}
