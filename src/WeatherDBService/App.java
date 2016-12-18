package WeatherDBService;

import static spark.Spark.*;


/**
 * @author: Jan-Peter Petersen & Johannes Berger
 * 
 *          Starts the Weather Data Base Service and connects to the mysql DB
 * 
 */

public class App {
	
	public static void main(String[] args) {
		// info for LOGGING!
		// http://stackoverflow.com/questions/7421612/slf4j-failed-to-load-class-org-slf4j-impl-staticloggerbinder
		// http://stackoverflow.com/questions/12532339/no-appenders-could-be-found-for-loggerlog4j
		// BasicConfigurator.configure();

		// Create DBConnector Object for database interaction
		DBConnector dbcon = new DBConnector("172.17.0.1", "3307", "mi", "mi", "miws16");

		// REST interfaces, specification @ WeatherDBService.json
		
		// Temperature as Double
		get("/temperatureAtTime", (req, res) -> {
			String time = req.queryParams("time");
			String lon = req.queryParams("lon");
			String lat = req.queryParams("lat");
			if (parametersAreOkay(time, lon, lat)) {
				return dbcon.getTemperaturAtSpecificTime(time, lon, lat);
			} else {
				res.status(400);
				return "Bullshit";
			}

		}); 

		// Weather Condition as Double: 0 = perfect weather, 1.0 = really bad weather
		get("/weatherConditionAtTime", (req, res) -> {
			String time = req.queryParams("time");
			String lon = req.queryParams("lon");
			String lat = req.queryParams("lat");
			if (parametersAreOkay(time, lon, lat)) {
				return dbcon.getWeatherConditionAtSpecificTime(time, lon, lat);
			} else {
				res.status(400);
				return "Bullshit";
			}

		});
		
		// returns the complete weather object
		get("/weatherAtTime", (req, res) -> {
			String time = req.queryParams("time");
			String lon = req.queryParams("lon");
			String lat = req.queryParams("lat");
			if (parametersAreOkay(time, lon, lat)) {
				return dbcon.getWeatherAtSpecificTime(time, lon, lat);
			} else {
				res.status(400);
				return "Bullshit";
			}

		});
		
		// Receive weather Data from the crawler
		post("/newWeatherData", (req, res) -> {

			// get JSON from request body
			String string = req.body();

			// create WeatherDataObject from JSON
			WeatherDataObject weatherDataObject = WeatherDataObject.jsonToJavaObject(string);

			// insert WeatherDataObject to Database
			dbcon.insertWeatherData(weatherDataObject);
			return "done";
		});
	}

	private static boolean parametersAreOkay(String time, String lon, String lat) {
		if (time != null) {
			try {
				long tmp = Long.parseLong(time);
				if (tmp < 0) {
					return false; // no negative time
				}
			} catch (NumberFormatException e) {
				return false; // String not parseable
			}
		}
		if ((lon == null) ^ (lat == null)) {
			return false; // either both or neither should be null
		}
		if (lon != null && lat != null) {
			try {
				double lonTmp = Double.parseDouble(lon);
				if (lonTmp < -180 || lonTmp > 180) {
					return false; // outside of possible range
				}
				double latTmp = Double.parseDouble(lat);
				if (latTmp < -90 || latTmp > 90) {
					return false; // outside of possible range
				}
			} catch (NumberFormatException e) {
				return false; // String not parseable
			}
		}
		return true;
	}
}
