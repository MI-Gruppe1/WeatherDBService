package WeatherDBService;

/**
 * @author Jan-Peter Petersen 
 */

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DBConnector {
	
	private String ip;
	private String port;
	private String database;
	private String user;
	private String passwort;
	
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
	
	/**
	 * create new MySQL Connection
	 * @return Connection
	 */
	private Connection getMySQLConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = (Connection) DriverManager.getConnection("jdbc:mysql://"+ this.ip + ":" + this.port + "/" + this.database, this.user, this.passwort);
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			System.out.println(e);
		}
		return connection;
	}

	/**
	 * insert new WeatherData
	 * @param WeatherDataObject
	 */
	public void insertWeatherData(WeatherDataObject weatherDataObject) {
		Connection connection = null;
		System.out.println("\n" + weatherDataObject.toString());
		try {
			int stationId = insertWeatherStation(weatherDataObject.getStationName(), weatherDataObject.getLongitude(), weatherDataObject.getLatitude());
			int descriptionId = insertWeatherDescription(weatherDataObject.getWeatherIcon(), weatherDataObject.getWeatherDesc(), weatherDataObject.getWeatherDescDetail());
			System.out.println("StationId:" + stationId);
			System.out.println("DescciptionId: " + descriptionId);
			connection = getMySQLConnection();
			String query = "INSERT INTO WeatherData (station_id, desc_id, temperature, humidity, pressure, windDeg, windSpeed, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(query);
			stmt.setInt(1, stationId);
			stmt.setInt(2, descriptionId);
			stmt.setDouble(3, weatherDataObject.getTemperature());
			stmt.setInt(4, weatherDataObject.getHumidity());
			stmt.setInt(5, weatherDataObject.getPressure());
			stmt.setInt(6, weatherDataObject.getWindDeg());
			stmt.setDouble(7, weatherDataObject.getWindSpeed());
			stmt.setLong(8, weatherDataObject.getTimeStamp());
			stmt.execute();
			connection.close();
			System.out.println("Success");
		}  catch (Exception e) {
			MailNotification.sendMail(e);
		}
	}
	
	/**
	 * insert new WeatherDescription
	 * @param descIcon
	 * @param descShort
	 * @param descDetail
	 * @return WeatherDescriptionId
	 */
	private int insertWeatherDescription(String descIcon, String descShort, String descDetail) {	
		int weatherDescriptionIconId = insertWeatherIcon(descIcon);
		int weatherDescriptionShortId = insertWeatherDescriptionShort(descShort);
		int weatherDescriptionDetailId = insertWeatherDescriptionDetail(descDetail);
		
		Connection connection = null;
		int weatherDescriptionId = checkWeatherDescription(weatherDescriptionIconId, weatherDescriptionShortId, weatherDescriptionDetailId);
		try {
			if (0 == weatherDescriptionId) {
				connection = getMySQLConnection();		
				
				String query = "INSERT INTO WeatherDescription (icon_id, short_id, detail_id) VALUES (?, ?, ?)";
				PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(query);
				stmt.setInt(1, weatherDescriptionIconId);
				stmt.setInt(2, weatherDescriptionShortId);
				stmt.setInt(3, weatherDescriptionDetailId);
				stmt.execute();
				connection.close();
				weatherDescriptionId = checkWeatherDescription(weatherDescriptionIconId, weatherDescriptionShortId, weatherDescriptionDetailId);
			} else {			}
			return weatherDescriptionId;
		} catch (SQLException e) {
			MailNotification.sendMail(e);
			return -1;
		}
	}

	/**
	 * check existing WeatherDescription
	 * @param WeatherDescriptionIconId
	 * @param WeatherDescriptionShortId
	 * @param weatherDescriptionDetailId
	 * @return WeatherDescriptionId
	 */
	private int checkWeatherDescription(int WeatherDescriptionIconId, int WeatherDescriptionShortId, int WeatherDescriptionDetailId) {
		Connection connection = null;
		int descriptionId = 0;
		try {
			connection = getMySQLConnection();		
			String query = "SELECT id FROM WeatherDescription WHERE icon_id = ? AND short_id = ? AND detail_id = ?";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, WeatherDescriptionIconId);
			stmt.setInt(2, WeatherDescriptionShortId);
			stmt.setInt(3, WeatherDescriptionDetailId);
			stmt.executeQuery();
			ResultSet result = stmt.getResultSet();
			if (result.first() == true) {
				descriptionId = result.getInt("id");
			}
			connection.close();
			return descriptionId;
		} catch (SQLException e) {
			MailNotification.sendMail(e);
			return -1;
		}
	}
	
	/**
	 * insert new WeatherDescriptionShort
	 * @param description
	 * @return WeatherDescriptionShortId
	 */
	private int insertWeatherDescriptionShort(String description) {
		Connection connection = null;
		if (checkWeatherDescriptionShort(description) == 0) {
			try {
				connection = getMySQLConnection();
				String query = "INSERT INTO WeatherDescriptionShort (description) VALUES (?)";
				PreparedStatement stmt = connection.prepareStatement(query);
				stmt.setString(1, description);		
				stmt.execute();
				connection.close();
			} catch (SQLException e) {
				MailNotification.sendMail(e);
			} 
		}
		return checkWeatherDescriptionShort(description);
	}

	/**
	 * check existing WeatherDescriptionShort
	 * @param description
	 * @return WeatherDescriptionShortId
	 */
	private int checkWeatherDescriptionShort(String description) {
		Connection connection = null;
		int descShortId = 0;
		try {
			connection = getMySQLConnection();		
			String query = "SELECT id FROM WeatherDescriptionShort WHERE description = ?";
			PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(query);
			stmt.setString(1, description);
			stmt.execute();
			ResultSet result = stmt.getResultSet();
			if (result.first() == true) {
				descShortId = result.getInt("id"); 
			}
			connection.close();
			return descShortId;
		} catch (SQLException e) {
			MailNotification.sendMail(e);
			return -1;
		}
	}

	/**
	 * insert new WeatherDescriptionDetail
	 * @param description
	 * @return WeatherDescriptionDetailId
	 */
	private int insertWeatherDescriptionDetail(String description) {
		Connection connection = null;
		if (checkWeatherDescriptionDetail(description) == 0) {
			try {
				connection = getMySQLConnection();
				String query = "INSERT INTO WeatherDescriptionDetail (description) VALUES (?)";
				PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(query);
				stmt.setString(1, description);		
				stmt.execute();
				connection.close();
			} catch (SQLException e) {
				MailNotification.sendMail(e);
			} 
		}
		return checkWeatherDescriptionDetail(description);
	}
	
	/**
	 * check existing WeatherDescriptionDetail
	 * @param description
	 * @return WeatherDescriptionDetailId
	 */
	private int checkWeatherDescriptionDetail(String description) {
		Connection connection = null;
		int descriptionDetailId = 0;
		try {
			connection = getMySQLConnection();		
			String query = "SELECT id FROM WeatherDescriptionDetail WHERE description = ?";
			PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(query);
			stmt.setString(1, description);
			stmt.execute();
			ResultSet result = stmt.getResultSet();
			
			if (result.first() == true) {
				descriptionDetailId = result.getInt("id");
			}
			connection.close();
			return descriptionDetailId;
		} catch (SQLException e) {
			MailNotification.sendMail(e);
			return -1;
		}
	}
	
	/**
	 * insert new WeatherIcon
	 * @param icon_code
	 * @return WeatherDescriptionIconId
	 */
	private int insertWeatherIcon(String icon_code) {
		Connection connection = null;
		int iconId = checkWeatherDescriptionIcon(icon_code);
		try {
			if (iconId == 0) {
				connection = getMySQLConnection();
				String query = "INSERT INTO WeatherDescriptionIcon (icon_code) VALUES (?)";
				PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(query);
				stmt.setString(1, icon_code);		
				stmt.execute();
				connection.close();
			}
		} catch (SQLException e) {
			MailNotification.sendMail(e);
		}
		return checkWeatherDescriptionIcon(icon_code);
	}

	/**
	 * check existing WeatherDescriptionIcon
	 * @param icon_code
	 * @return WeatherDescriptionIconId
	 */
	private int checkWeatherDescriptionIcon(String icon_code) {
		int iconId = 0;
		Connection connection = null;
		try {
			connection = getMySQLConnection();		
			String query = "SELECT id FROM WeatherDescriptionIcon WHERE icon_code = ?";
			PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(query);
			stmt.setString(1, icon_code);
			stmt.execute();
			ResultSet result = stmt.getResultSet();
			if (result.first() == true) {
				iconId = result.getInt("id");
			}
			connection.close();
			return iconId;
		} catch (SQLException e) {
			MailNotification.sendMail(e);
			return -1;
		} 
	}

	/**
	 * @param name 
	 * @param longitude
	 * @param latitude
	 * @return WeatherStationId
	 */
	private int insertWeatherStation(String name, double longitude, double latitude) {
		Connection connection = null;
		try {
			if (checkWeatherStationId(name) == 0) {
				// insert new WeatherStation
				connection = getMySQLConnection();
				String query = "INSERT INTO WeatherStation (name, longitude, latitude) VALUES (?, ?, ?)";
				PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(query);
				stmt.setString(1, name);
				stmt.setDouble(2, longitude);
				stmt.setDouble(3, latitude);			
				stmt.execute();
				connection.close();
			}
		} catch (SQLException e) {
			MailNotification.sendMail(e);
		}
		return checkWeatherStationId(name);
	}
		
	/**
	 * check existing WeatherStationId
	 * @param name
	 * @return WeatherStationId
	 */
	private int checkWeatherStationId(String name) {
		int stationId = 0;
		Connection connection = null;
		try {
			String query = "SELECT id FROM WeatherStation WHERE name = ?";
			connection = getMySQLConnection();
			PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(query);
			stmt.setString(1, name);
			stmt.execute();
			ResultSet result = stmt.getResultSet();
			if (result.first() == true) {
				stationId = result.getInt("id");
			}
			connection.close();
			return stationId;
		} catch (SQLException e) {
			MailNotification.sendMail(e);
			return -1;
		}
	}

	/**
	 * get specific WeatherDataObject from Database
	 * @param long
	 * @param lat
	 * @param time
	 */
	public WeatherDataObject getWeatherDataObject(double longi, double lati, long time) {
		String weatherIcon = null;
		String weatherDesc = null;
		String weatherDescDetail = null;
		String stationName = null;
		double longitude = 0.0;
		double latitude = 0.0;
		double temperature = 0.0;
		int humidity = 0;
		int pressure = 0;
		int windDeg = 0;
		int windSpeed = 0; 
		long timeStamp = 0;
		
		String query = "SELECT WeatherData.temperature, WeatherData.humidity, WeatherData.pressure, WeatherData.windDeg, WeatherData.windSpeed, WeatherData.timestamp, "
				+ "WeatherStation.name, WeatherStation.longitude, WeatherStation.latitude, "
				+ "WeatherDescriptionDetail.description, WeatherDescriptionShort.description, WeatherDescriptionIcon.icon_code "
				+ "FROM WeatherStation, WeatherData, WeatherDescription, WeatherDescriptionDetail, WeatherDescriptionShort, WeatherDescriptionIcon "
				+ "WHERE WeatherData.timestamp = ? AND WeatherStation.longitude = ? AND WeatherStation.latitude = ? AND WeatherData.desc_id = WeatherDescription.id AND WeatherDescription.detail_id = WeatherDescriptionDetail.id AND WeatherDescription.short_id = WeatherDescriptionShort.id AND WeatherDescription.icon_id = WeatherDescriptionIcon.id;";
		
		try {
			Connection connection = getMySQLConnection();
			PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(query);
			stmt.setLong(1, time);
			stmt.setDouble(2, longi);
			stmt.setDouble(3, lati);
			stmt.execute();
			ResultSet result = stmt.getResultSet();
			if (result.first() == true){
				temperature = result.getDouble("WeatherData.temperature");
				humidity = result.getInt("WeatherData.humidity");
				pressure = result.getInt("WeatherData.pressure");
				windDeg = result.getInt("WeatherData.windDeg");
				windSpeed = result.getInt("WeatherData.windSpeed");
				timeStamp = result.getLong("WeatherData.timestamp");	
				weatherIcon = result.getString("WeatherDescriptionIcon.icon_code");
				weatherDesc = result.getString("WeatherDescriptionShort.description");
				weatherDescDetail = result.getString("WeatherDescriptionDetail.description");
				stationName = result.getString("WeatherStation.name");
				longitude = result.getDouble("WeatherStation.longitude");
				latitude = result.getDouble("WeatherStation.latitude");
			}			
		} catch (SQLException e) {
			MailNotification.sendMail(e);	
		}
		WeatherDataObject weatherDataObject = new WeatherDataObject(weatherIcon, weatherDesc, weatherDescDetail, stationName, longitude, latitude, temperature, humidity, pressure, windDeg, windSpeed, timeStamp);
		return weatherDataObject;
	}
	
	// TODO
	public double selectTemperaturAtSpecificTime() {
		return 0.0;
	}

	// TODO
	public double selectPrecipitationAtSpecificTime() {
		return 0.0;
	}
	
	// TODO
	public JSONObject selectCurrentWeather() {
		JSONObject k = new JSONObject();
		return k;
	}
}
