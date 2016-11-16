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

/*
 * Author: Jan-Peter Petersen
 * E-Mail: jan-peter.petersen@haw-hamburg.de 
 */

// Alles als Prototyp umgesetzt. dient als Grundlage der naechsten IMplementation
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
	public void insertWeatherData(WeatherDataObject weatherDataObject) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		this.connection = (Connection) DriverManager.getConnection("jdbc:mysql://"+ this.ip + ":" + this.port + "/" + this.database, this.user, this.passwort);
		
		String query = " insert into crawledWeatherData (weatherIcon, weatherDesc, weatherDescDetail, stationName, temperature, humidity, pressure, windDeg, windSpeed, dateTime)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
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
	public String selectTemperaturAtSpecificTime() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
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
