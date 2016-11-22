package WeatherDBService;
import static spark.Spark.*;

// import org.apache.log4j.BasicConfigurator;
import org.json.JSONObject;
import org.omg.Messaging.SyncScopeHelper;

/**
 * @author: Jan-Peter Petersen
 */

public class App 
{
    public static void main( String[] args ) {
    	// info for LOGGING!
    	// http://stackoverflow.com/questions/7421612/slf4j-failed-to-load-class-org-slf4j-impl-staticloggerbinder
    	// http://stackoverflow.com/questions/12532339/no-appenders-could-be-found-for-loggerlog4j
    	// BasicConfigurator.configure();
    	
    	// Create DBConnector Object for database interaction
    	DBConnector dbcon = new DBConnector("172.17.0.1", "3307", "mi", "mi", "miws16");
    	
    	// REST interfaces, specification @ WeatherDBService.json
    	get("/temperatureAtTime", (req, res) -> dbcon.getTemperaturAtSpecificTime(req.queryParams("time"), req.queryParams("lon"), req.queryParams("lat")));
    	get("/weatherConditionAtTime", (req, res) -> dbcon.getWeatherConditionAtSpecificTime(req.queryParams("time"), req.queryParams("lon"), req.queryParams("lat")));
    	get("/currentWeather", (req, res) -> dbcon.getCurrentWeather(req.queryParams("lon"), req.queryParams("lat")));
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
}
