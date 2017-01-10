package WeatherDBService;

/**
 * @author Lotaire
 */
import static org.junit.Assert.*;

import static spark.Spark.*;
import org.junit.Before;
import org.junit.Test;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class WeatherDBServiceTest {

	// public DBConnector dbcon;
	// public String message="";
	// public WeatherDataObject weatherDataObject;
	// @Before
	// public void setUp() throws Exception {
	//
	// try {
	//
	// dbcon = new DBConnector("172.17.0.1", "3307", "mi", "mi", "miws16");
	// // REST interfaces, specification @ WeatherDBService.json
	// get("/temperatureAtTime", (req, res) ->
	// dbcon.getTemperaturAtSpecificTime(req.queryParams("time"),
	// req.queryParams("lon"), req.queryParams("lat"))); //Temperature as Double
	// get("/weatherConditionAtTime", (req, res) ->
	// dbcon.getWeatherConditionAtSpecificTime(req.queryParams("time"),
	// req.queryParams("lon"), req.queryParams("lat"))); //Weather Condition as
	// Double: 0 = perfect weather, 1.0 = really bad weather
	// get("/weatherAtTime", (req, res) ->
	// dbcon.getWeatherAtSpecificTime(req.queryParams("time"),
	// req.queryParams("lon"), req.queryParams("lat"))); //returns the complete
	// weather object
	// post("/newWeatherData", (req, res) -> {
	//
	// // get JSON from request body
	// String string = req.body();
	//
	// // create WeatherDataObject from JSON
	// WeatherDataObject weatherDataObject =
	// WeatherDataObject.jsonToJavaObject(string);
	//
	// // insert WeatherDataObject to Database
	// dbcon.insertWeatherData(weatherDataObject);
	// message = "done";
	// return message;
	// });
	//
	// } catch (Exception ex) {
	// throw ex;
	// }
	// }
	//
	// @Test
	// public void test() {
	// // überprüfen Instanzierung geklappt hat
	// assertNotNull(weatherDataObject);
	// // überprüfen ob die Werte besetzt sind
	// assertNotEquals(weatherDataObject.getHumidity(), null);
	// assertNotEquals(weatherDataObject.getLatitude(), null);
	// assertNotEquals(weatherDataObject.getLongitude(), null);
	// assertNotEquals(weatherDataObject.getPressure(),null);
	// assertNotEquals(weatherDataObject.getStationName(),null);
	// assertNotEquals(weatherDataObject.getTemperature(), null);
	// assertNotEquals(weatherDataObject.getTimeStamp(), null);
	// assertNotEquals(weatherDataObject.getWeatherDesc(),null);
	// assertNotEquals(weatherDataObject.getWeatherDescDetail(), null);
	// assertNotEquals(weatherDataObject.getWeatherIcon(), null);
	// assertNotEquals(weatherDataObject.getWindDeg(), null);
	// assertNotEquals(weatherDataObject.getWindSpeed(), null);
	//
	// // Wenn alle Werte besetzt sind, ist der Test bestanden
	//
	//
	// }

	String WeatherDBServerAdress = "http://localhost:4567";

	@Before
	public void setUp() throws Exception {
//		String jsonData1 = "{\"weatherIcon\":\"04n\",\"weatherDesc\":\"Clouds\",\"weatherDescDetail\":\"overcast clouds\",\"stationName\":\"Station-1\",\"longitude\":10.02,\"latitude\":53.55,\"temperature\":4.0,\"humidity\":86,\"pressure\":1030,\"windDeg\":120.0,\"windSpeed\":3.6,\"timeStamp\":1482192460}";
//		String jsonData2 = "{\"weatherIcon\":\"04n\",\"weatherDesc\":\"Clouds\",\"weatherDescDetail\":\"overcast clouds\",\"stationName\":\"Station-2\",\"longitude\":11.05,\"latitude\":54.55,\"temperature\":42.0,\"humidity\":86,\"pressure\":1030,\"windDeg\":120.0,\"windSpeed\":3.6,\"timeStamp\":1482192463}";
//
//		Unirest.post("http://172.17.0.1:4568/newWeatherData").body(jsonData1).asString();
//		Thread.sleep(500);
//		Unirest.post("http://172.17.0.1:4568/newWeatherData").body(jsonData2).asString();
	}

	@Test
	public void getTempWithParams1Test() {
		double result = 0;
		try {
			HttpResponse<String> stringResponse = Unirest.get(WeatherDBServerAdress + "/temperatureAtTime")
					.queryString("time", 5).queryString("lon", 10.0).queryString("lat", 53.0).asString();

			if (stringResponse != null && !stringResponse.getBody().isEmpty()) {
				result = Double.valueOf(stringResponse.getBody().toString());
			} else {
				result = -1;
			}
		} catch (UnirestException e) {
			System.out.println("tough shit");
			System.out.println(e);
		}
		assertTrue(result == 4.0);
	}
	
	@Test
	public void getTempWithParams2Test() {
		double result = 0;
		try {
			HttpResponse<String> stringResponse = Unirest.get(WeatherDBServerAdress + "/temperatureAtTime")
					.queryString("time", 5).queryString("lon", 11).queryString("lat", 54).asString();

			if (stringResponse != null && !stringResponse.getBody().isEmpty()) {
				result = Double.valueOf(stringResponse.getBody().toString());
			} else {
				result = -1;
			}
		} catch (UnirestException e) {
			System.out.println("tough shit");
			System.out.println(e);
		}
		assertTrue(result == 42.0);
	}
	
	@Test
	public void getTempWithParams3Test() {
		double result = 0;
		try {
			HttpResponse<String> stringResponse = Unirest.get(WeatherDBServerAdress + "/temperatureAtTime")
					.queryString("time", 5).asString();

			if (stringResponse != null && !stringResponse.getBody().isEmpty()) {
				result = Double.valueOf(stringResponse.getBody().toString());
			} else {
				result = -1;
			}
		} catch (UnirestException e) {
			System.out.println("tough shit");
			System.out.println(e);
		}
		assertTrue(result == 4.0);
	}
	
	@Test
	public void getTempTest() {
		double result = 0;
		try {
			HttpResponse<String> stringResponse = Unirest.get(WeatherDBServerAdress + "/temperatureAtTime").asString();

			if (stringResponse != null && !stringResponse.getBody().isEmpty()) {
				result = Double.valueOf(stringResponse.getBody().toString());
			} else {
				result = -1;
			}
		} catch (UnirestException e) {
			System.out.println("tough shit");
			System.out.println(e);
		}
		assertTrue(result == 4.0);
	}
	
	@Test
	public void getTempParamFail1Test() {
		String result = "";
		try {
			HttpResponse<String> stringResponse = Unirest.get(WeatherDBServerAdress + "/temperatureAtTime").queryString("time", "4o4").asString();

			if (stringResponse != null && !stringResponse.getBody().isEmpty()) {
				result = stringResponse.getBody().toString();
			} else {
				result = "-1";
			}
		} catch (UnirestException e) {
			System.out.println("tough shit");
			System.out.println(e);
		}
		System.out.println("getTempParamFail1Test: " + result);
		assertTrue(result.equals("Bullshit"));
	}
	
	@Test
	public void getTempParamFail2Test() {
		String result = "";
		try {
			HttpResponse<String> stringResponse = Unirest.get(WeatherDBServerAdress + "/temperatureAtTime").queryString("time", "4").queryString("lon", 11).asString();

			if (stringResponse != null && !stringResponse.getBody().isEmpty()) {
				result = stringResponse.getBody().toString();
			} else {
				result = "-1";
			}
		} catch (UnirestException e) {
			System.out.println("tough shit");
			System.out.println(e);
		}
		assertTrue(result.equals("Bullshit"));
	}

}
