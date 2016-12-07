package WeatherDBService;

/**
 * @author Jan-Peter Petersen & Johannes Berger
 * 
 * Utility functions that connect to the DB
 */

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

import java.util.ArrayList;



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
		PreparedStatement stmt = null;
		System.out.println("\n" + weatherDataObject.toString());
		try {
			int stationId = insertWeatherStation(weatherDataObject.getStationName(), weatherDataObject.getLongitude(), weatherDataObject.getLatitude());
			int descriptionId = insertWeatherDescription(weatherDataObject.getWeatherIcon(), weatherDataObject.getWeatherDesc(), weatherDataObject.getWeatherDescDetail());
			System.out.println("StationId:" + stationId);
			System.out.println("DescciptionId: " + descriptionId);
			connection = getMySQLConnection();
			String query = "INSERT INTO WeatherData (station_id, desc_id, temperature, humidity, pressure, windDeg, windSpeed, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			stmt = (PreparedStatement) connection.prepareStatement(query);
			stmt.setInt(1, stationId);
			stmt.setInt(2, descriptionId);
			stmt.setDouble(3, weatherDataObject.getTemperature());
			stmt.setInt(4, weatherDataObject.getHumidity());
			stmt.setInt(5, weatherDataObject.getPressure());
			stmt.setDouble(6, weatherDataObject.getWindDeg());
			stmt.setDouble(7, weatherDataObject.getWindSpeed());
			stmt.setLong(8, weatherDataObject.getTimeStamp());
			stmt.execute();
			connection.close();
			System.out.println("Success");
		}  catch (Exception e) {
			MailNotification.sendMail(e);
		}finally {
			try { connection.close(); } catch (Exception e) { /* ignored */ }
		    try { stmt.close(); } catch (Exception e) { /* ignored */ }
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
		PreparedStatement stmt = null;
		int weatherDescriptionId = checkWeatherDescription(weatherDescriptionIconId, weatherDescriptionShortId, weatherDescriptionDetailId);
		try {
			if (0 == weatherDescriptionId) {
				connection = getMySQLConnection();		
				
				String query = "INSERT INTO WeatherDescription (icon_id, short_id, detail_id) VALUES (?, ?, ?)";
				stmt = (PreparedStatement) connection.prepareStatement(query);
				stmt.setInt(1, weatherDescriptionIconId);
				stmt.setInt(2, weatherDescriptionShortId);
				stmt.setInt(3, weatherDescriptionDetailId);
				stmt.execute();
				weatherDescriptionId = checkWeatherDescription(weatherDescriptionIconId, weatherDescriptionShortId, weatherDescriptionDetailId);
			} else {			}
			return weatherDescriptionId;
		} catch (SQLException e) {
			MailNotification.sendMail(e);
			return -1;
		}finally {
			try { connection.close(); } catch (Exception e) { /* ignored */ }
		    try { stmt.close(); } catch (Exception e) { /* ignored */ }
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
		PreparedStatement stmt = null;
		ResultSet result = null;
		int descriptionId = 0;
		try {
			connection = getMySQLConnection();		
			String query = "SELECT id FROM WeatherDescription WHERE icon_id = ? AND short_id = ? AND detail_id = ?";
			stmt = connection.prepareStatement(query);
			stmt.setInt(1, WeatherDescriptionIconId);
			stmt.setInt(2, WeatherDescriptionShortId);
			stmt.setInt(3, WeatherDescriptionDetailId);
			stmt.executeQuery();
			result = stmt.getResultSet();
			if (result.first() == true) {
				descriptionId = result.getInt("id");
			}
			connection.close();
			return descriptionId;
		} catch (SQLException e) {
			MailNotification.sendMail(e);
			return -1;
		}finally {
			try { connection.close(); } catch (Exception e) { /* ignored */ }
			try { stmt.close(); } catch (Exception e) { /* ignored */ }
			try { result.close(); } catch (Exception e) { /* ignored */ }
		}
	}
	
	/**
	 * insert new WeatherDescriptionShort
	 * @param description
	 * @return WeatherDescriptionShortId
	 */
	private int insertWeatherDescriptionShort(String description) {
		Connection connection = null;
		PreparedStatement stmt = null;
		if (checkWeatherDescriptionShort(description) == 0) {
			try {
				connection = getMySQLConnection();
				String query = "INSERT INTO WeatherDescriptionShort (description) VALUES (?)";
				stmt = connection.prepareStatement(query);
				stmt.setString(1, description);		
				stmt.execute();
				connection.close();
			} catch (SQLException e) {
				MailNotification.sendMail(e);
			}finally {
				try { connection.close(); } catch (Exception e) { /* ignored */ }
			    try { stmt.close(); } catch (Exception e) { /* ignored */ }
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
		PreparedStatement stmt = null;
		ResultSet result = null;
		int descShortId = 0;
		try {
			connection = getMySQLConnection();		
			String query = "SELECT id FROM WeatherDescriptionShort WHERE description = ?";
			stmt = (PreparedStatement) connection.prepareStatement(query);
			stmt.setString(1, description);
			stmt.execute();
			result = stmt.getResultSet();
			if (result.first() == true) {
				descShortId = result.getInt("id"); 
			}
			connection.close();
			return descShortId;
		} catch (SQLException e) {
			MailNotification.sendMail(e);
			return -1;
		}finally {
			try { connection.close(); } catch (Exception e) { /* ignored */ }
		    try { stmt.close(); } catch (Exception e) { /* ignored */ }
		    try { result.close(); } catch (Exception e) { /* ignored */ }
		}
	}

	/**
	 * insert new WeatherDescriptionDetail
	 * @param description
	 * @return WeatherDescriptionDetailId
	 */
	private int insertWeatherDescriptionDetail(String description) {
		Connection connection = null;
		PreparedStatement stmt = null;
		if (checkWeatherDescriptionDetail(description) == 0) {
			try {
				connection = getMySQLConnection();
				String query = "INSERT INTO WeatherDescriptionDetail (description) VALUES (?)";
				stmt = (PreparedStatement) connection.prepareStatement(query);
				stmt.setString(1, description);		
				stmt.execute();
				connection.close();
			} catch (SQLException e) {
				MailNotification.sendMail(e);
			}finally {
				try { connection.close(); } catch (Exception e) { /* ignored */ }
			    try { stmt.close(); } catch (Exception e) { /* ignored */ }
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
		PreparedStatement stmt = null;
		ResultSet result = null;
		int descriptionDetailId = 0;
		try {
			connection = getMySQLConnection();		
			String query = "SELECT id FROM WeatherDescriptionDetail WHERE description = ?";
			stmt = (PreparedStatement) connection.prepareStatement(query);
			stmt.setString(1, description);
			stmt.execute();
			result = stmt.getResultSet();
			
			if (result.first() == true) {
				descriptionDetailId = result.getInt("id");
			}
			connection.close();
			return descriptionDetailId;
		} catch (SQLException e) {
			MailNotification.sendMail(e);
			return -1;
		}finally {
			try { connection.close(); } catch (Exception e) { /* ignored */ }
		    try { stmt.close(); } catch (Exception e) { /* ignored */ }
		    try { result.close(); } catch (Exception e) { /* ignored */ }
		}
	}
	
	/**
	 * insert new WeatherIcon
	 * @param icon_code
	 * @return WeatherDescriptionIconId
	 */
	private int insertWeatherIcon(String icon_code) {
		Connection connection = null;
		PreparedStatement stmt = null;
		int iconId = checkWeatherDescriptionIcon(icon_code);
		try {
			if (iconId == 0) {
				connection = getMySQLConnection();
				String query = "INSERT INTO WeatherDescriptionIcon (icon_code) VALUES (?)";
				stmt = (PreparedStatement) connection.prepareStatement(query);
				stmt.setString(1, icon_code);		
				stmt.execute();
				connection.close();
			}
		} catch (SQLException e) {
			MailNotification.sendMail(e);
		}finally {
			try { connection.close(); } catch (Exception e) { /* ignored */ }
		    try { stmt.close(); } catch (Exception e) { /* ignored */ }
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
		PreparedStatement stmt = null;
		ResultSet result = null;
		try {
			connection = getMySQLConnection();		
			String query = "SELECT id FROM WeatherDescriptionIcon WHERE icon_code = ?";
			stmt = (PreparedStatement) connection.prepareStatement(query);
			stmt.setString(1, icon_code);
			stmt.execute();
			result = stmt.getResultSet();
			if (result.first() == true) {
				iconId = result.getInt("id");
			}
			connection.close();
			return iconId;
		} catch (SQLException e) {
			MailNotification.sendMail(e);
			return -1;
		}finally {
			try { connection.close(); } catch (Exception e) { /* ignored */ }
		    try { stmt.close(); } catch (Exception e) { /* ignored */ }
		    try { result.close(); } catch (Exception e) { /* ignored */ }
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
		PreparedStatement stmt = null;
		try {
			if (checkWeatherStationId(name) == 0) {
				// insert new WeatherStation
				connection = getMySQLConnection();
				String query = "INSERT INTO WeatherStation (name, longitude, latitude) VALUES (?, ?, ?)";
				stmt = (PreparedStatement) connection.prepareStatement(query);
				stmt.setString(1, name);
				stmt.setDouble(2, longitude);
				stmt.setDouble(3, latitude);			
				stmt.execute();
				connection.close();
			}
		} catch (SQLException e) {
			MailNotification.sendMail(e);
		}finally {
			try { connection.close(); } catch (Exception e) { /* ignored */ }
		    try { stmt.close(); } catch (Exception e) { /* ignored */ }
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
		PreparedStatement stmt = null;
		ResultSet result = null;
		try {
			String query = "SELECT id FROM WeatherStation WHERE name = ?";
			connection = getMySQLConnection();
			stmt = (PreparedStatement) connection.prepareStatement(query);
			stmt.setString(1, name);
			stmt.execute();
			result = stmt.getResultSet();
			if (result.first() == true) {
				stationId = result.getInt("id");
			}
			connection.close();
			return stationId;
		} catch (SQLException e) {
			MailNotification.sendMail(e);
			return -1;
		}finally {
			try { connection.close(); } catch (Exception e) { /* ignored */ }
		    try { stmt.close(); } catch (Exception e) { /* ignored */ }
		    try { result.close(); } catch (Exception e) { /* ignored */ }
		}
	}

	/**
	 * get specific WeatherDataObject from Database
	 * @param long
	 * @param lat
	 * @param time
	 */
	public WeatherDataObject getWeatherDataObject(double longi, double lati, long time) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet result = null;
		
		String weatherIcon = null;
		String weatherDesc = null;
		String weatherDescDetail = null;
		String stationName = null;
		double longitude = 0.0;
		double latitude = 0.0;
		double temperature = 0.0;
		int humidity = 0;
		int pressure = 0;
		double windDeg = 0;
		double windSpeed = 0; 
		long timeStamp = 0;
		WeatherStationObject station = getClosestWeatherStation(longi, lati);
		
		String query = "SELECT WeatherData.temperature, WeatherData.humidity, WeatherData.pressure, WeatherData.windDeg, WeatherData.windSpeed, WeatherData.timestamp, "
				+ "WeatherDescriptionDetail.description, WeatherDescriptionShort.description, WeatherDescriptionIcon.icon_code "
				+ "FROM WeatherStation, WeatherData, WeatherDescription, WeatherDescriptionDetail, WeatherDescriptionShort, WeatherDescriptionIcon "
				+ "WHERE WeatherData.timestamp = (SELECT timestamp FROM WeatherData ORDER BY abs(timeStamp - ?) LIMIT 1) " 
				+ "AND WeatherStation.id = ? AND WeatherData.desc_id = WeatherDescription.id AND WeatherDescription.detail_id = WeatherDescriptionDetail.id AND WeatherDescription.short_id = WeatherDescriptionShort.id AND WeatherDescription.icon_id = WeatherDescriptionIcon.id;";
		
		try {
			connection = getMySQLConnection();
			stmt = (PreparedStatement) connection.prepareStatement(query);
			stmt.setLong(1, time);
			stmt.setDouble(2, station.getId());
			stmt.execute();
			result = stmt.getResultSet();
			if (result.first() == true){
				temperature = result.getDouble("WeatherData.temperature");
				humidity = result.getInt("WeatherData.humidity");
				pressure = result.getInt("WeatherData.pressure");
				windDeg = result.getDouble("WeatherData.windDeg");
				windSpeed = result.getDouble("WeatherData.windSpeed");
				timeStamp = result.getLong("WeatherData.timestamp");	
				weatherIcon = result.getString("WeatherDescriptionIcon.icon_code");
				weatherDesc = result.getString("WeatherDescriptionShort.description");
				weatherDescDetail = result.getString("WeatherDescriptionDetail.description");
				stationName = station.getStationName();
				longitude = station.getLongitude();
				latitude = station.getLatitude();
			}			
		} catch (SQLException e) {
			MailNotification.sendMail(e);	
		}finally {
			try { connection.close(); } catch (Exception e) { /* ignored */ }
		    try { stmt.close(); } catch (Exception e) { /* ignored */ }
		    try { result.close(); } catch (Exception e) { /* ignored */ }
		}
		WeatherDataObject weatherDataObject = new WeatherDataObject(weatherIcon, weatherDesc, weatherDescDetail, stationName, longitude, latitude, temperature, humidity, pressure, windDeg, windSpeed, timeStamp);
		return weatherDataObject;
	}
	
	/**
	 * gets all Weatherstations from Database
	 */
	private ArrayList <WeatherStationObject> getAllWeatherStations() {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet result = null;
		
		int id = 0;
		String stationName = "";
		double longitude = 0.0;
		double latitude = 0.0;
		ArrayList <WeatherStationObject> stations = new ArrayList<>();
		
		String query = "SELECT WeatherStation.id, WeatherStation.name, WeatherStation.longitude, WeatherStation.latitude FROM WeatherStation";
		
		try {
			connection = getMySQLConnection();
			stmt = (PreparedStatement) connection.prepareStatement(query);
			stmt.execute();
			result = stmt.getResultSet();
			while (result.next()) {
				id = result.getInt("WeatherStation.id");
				stationName = result.getString("WeatherStation.name");
				longitude = result.getDouble("WeatherStation.longitude");
				latitude = result.getDouble("WeatherStation.latitude");
				WeatherStationObject station = new WeatherStationObject(id, stationName, longitude, latitude);
				stations.add(station);
			}
		} catch (SQLException e) {
			MailNotification.sendMail(e);	
		}finally {
			try { connection.close(); } catch (Exception e) { /* ignored */ }
		    try { stmt.close(); } catch (Exception e) { /* ignored */ }
		    try { result.close(); } catch (Exception e) { /* ignored */ }
		}
		
		return stations;
	}
	
	/**
	 * gets the closest Weatherstation to lon, lat from Database
	 */
	private WeatherStationObject getClosestWeatherStation(double lon, double lat) {
		ArrayList <WeatherStationObject> stations = getAllWeatherStations();
		double closestDistance = Double.MAX_VALUE;
		WeatherStationObject closestStation = stations.get(0);
		for (WeatherStationObject station : stations) {
			double distance = station.getDistanceTo(lon, lat); 
			if (distance <= closestDistance) {
				closestDistance = distance;
				closestStation = station;
			}
		}
		return closestStation;
	}
	
	/**
	 * returns the temperature for a specified time and place. If no time is specified it defaults to the current time
	 *
	 * @param timeString Time stamp as String
	 * @param lonString Longitude as String
	 * @param latString Latitude as String
	 * @return temperature
	 */
	public double getTemperaturAtSpecificTime(String timeString, String lonString, String latString) {
		
		long time;
		double lon;
		double lat;
		if (timeString.equals("null")) {
			time = System.currentTimeMillis() / 1000L; //get current unix time
		}else{
			time = Long.parseLong(timeString);
		}
		if (lonString.equals("null") || latString.equals("null")) { //Default is Hamburg-Mitte
			lon = 10.02;
			lat = 53.55;
		}else{
			lon = Double.parseDouble(lonString);
			lat = Double.parseDouble(latString);
		}
		
		WeatherDataObject wd = getWeatherDataObject(lon, lat, time);
		return wd.getTemperature();
	}

	/**
	 * returns the weather condition for a specified time and place. If no time is specified it defaults to the current time. if no place is specified it defaults to hamburg-mitte.
	 *
	 * @param timeString Time stamp as String
	 * @param lonString Longitude as String
	 * @param latString Latitude as String
	 * @return weather Condition
	 */
	public double getWeatherConditionAtSpecificTime(String timeString, String lonString, String latString) {
		
		long time;
		double lon;
		double lat;
		if (timeString.equals("null")) {
			time = System.currentTimeMillis() / 1000L; //get current unix time
		}else{
			time = Long.parseLong(timeString);
		}
		if (lonString.equals("null") || latString.equals("null")) { //Default is Hamburg-Mitte
			lon = 10.02;
			lat = 53.55;
		}else{
			lon = Double.parseDouble(lonString);
			lat = Double.parseDouble(latString);
		}
		
		WeatherDataObject wd = getWeatherDataObject(lon, lat, time);
		
		return wd.evaluateWeatherCondition();
	}
	
	/**
	 * * returns the complete weather data for a specified time and place. If no time is specified it defaults to the current time. if no place is specified it defaults to hamburg-mitte.
	 *
	 * @param timeString Time stamp as String
	 * @param lonString Longitude as String
	 * @param latString Latitude as String
	 * @return weather Data
	 */
	public String getWeatherAtSpecificTime(String timeString, String lonString, String latString) {
		
		long time;
		double lon;
		double lat;
		if (timeString.equals("null")) {
			time = System.currentTimeMillis() / 1000L; //get current unix time
		}else{
			time = Long.parseLong(timeString);
		}
		if (lonString.equals("null") || latString.equals("null")) { //Default is Hamburg-Mitte
			lon = 10.02;
			lat = 53.55;
		}else{
			lon = Double.parseDouble(lonString);
			lat = Double.parseDouble(latString);
		}
		WeatherDataObject wd = getWeatherDataObject(lon, lat, time);
		
		return wd.toJSON();
	}
	
}
