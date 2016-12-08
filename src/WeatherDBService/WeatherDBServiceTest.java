package WeatherDBService;

/**
 * @author Lotaire
 */
import static org.junit.Assert.*;

import static spark.Spark.*;
import org.junit.Before;
import org.junit.Test;

public class WeatherDBServiceTest {

	public DBConnector dbcon;
	public String message="";
	public WeatherDataObject weatherDataObject;
	@Before
	public void setUp() throws Exception {
		
		try {
	     
			dbcon = new DBConnector("172.17.0.1", "3307", "mi", "mi", "miws16");
		 // REST interfaces, specification @ WeatherDBService.json
	    	get("/temperatureAtTime", (req, res) -> dbcon.getTemperaturAtSpecificTime(req.queryParams("time"), req.queryParams("lon"), req.queryParams("lat"))); //Temperature as Double
	    	get("/weatherConditionAtTime", (req, res) -> dbcon.getWeatherConditionAtSpecificTime(req.queryParams("time"), req.queryParams("lon"), req.queryParams("lat"))); //Weather Condition as Double: 0 = perfect weather, 1.0 = really bad weather
	    	get("/weatherAtTime", (req, res) -> dbcon.getWeatherAtSpecificTime(req.queryParams("time"), req.queryParams("lon"), req.queryParams("lat"))); //returns the complete weather object
	    	post("/newWeatherData", (req, res) -> {
	    		
	    		// get JSON from request body
	    		String string = req.body();
	    		
	    		// create WeatherDataObject from JSON
	    		WeatherDataObject weatherDataObject = WeatherDataObject.jsonToJavaObject(string);
	    		
	    		// insert WeatherDataObject to Database
	    		dbcon.insertWeatherData(weatherDataObject);
	    		message = "done";
	    		return message;
	    	});
	          
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Test
	public void test() {
	// überprüfen Instanzierung geklappt hat
		assertNotNull(weatherDataObject);
		// überprüfen ob die Werte besetzt sind
		assertNotEquals(weatherDataObject.getHumidity(), null);
		assertNotEquals(weatherDataObject.getLatitude(), null);
		assertNotEquals(weatherDataObject.getLongitude(), null);
		assertNotEquals(weatherDataObject.getPressure(),null);
		assertNotEquals(weatherDataObject.getStationName(),null);
		assertNotEquals(weatherDataObject.getTemperature(), null);
		assertNotEquals(weatherDataObject.getTimeStamp(), null);
		assertNotEquals(weatherDataObject.getWeatherDesc(),null);
		assertNotEquals(weatherDataObject.getWeatherDescDetail(), null);
		assertNotEquals(weatherDataObject.getWeatherIcon(), null);
		assertNotEquals(weatherDataObject.getWindDeg(), null);
		assertNotEquals(weatherDataObject.getWindSpeed(), null);
		
		// Wenn alle Werte besetzt sind, ist der Test bestanden
		
		
	}

}
