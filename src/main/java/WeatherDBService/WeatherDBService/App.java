package WeatherDBService.WeatherDBService;
import static spark.Spark.*;

import org.apache.log4j.BasicConfigurator;

/**
 * WeatherDBService!
 */
public class App 
{
    public static void main( String[] args )
    {
    	
    	// http://stackoverflow.com/questions/7421612/slf4j-failed-to-load-class-org-slf4j-impl-staticloggerbinder
    	// http://stackoverflow.com/questions/12532339/no-appenders-could-be-found-for-loggerlog4j
    	// BasicConfigurator.configure();
    	// LOGGING!
    	DBConnector dbcon = new DBConnector("172.17.0.1", "3307", "mi", "mi", "miws16");
    	get("/temperatureAtTime", (req, res) -> dbcon.selectTemperaturAtSpecificTime());
    	get("/precipitationAtTime ", (req, res) -> dbcon.selectPrecipitationAtSpecificTime());
    	get("/currentWeather", (req, res) -> dbcon.selectPrecipitationAtSpecificTime());
    }
}
