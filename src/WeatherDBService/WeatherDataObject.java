package WeatherDBService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;

/**
 * 
 * @author Johannes Berger
 *
 *         Representation of one dataset as a Java object
 */
public class WeatherDataObject {

	private String weatherIcon;
	private String weatherDesc;
	private String weatherDescDetail;
	private String stationName;
	private double longitude;
	private double latitude;
	private double temperature;
	private int humidity;
	private int pressure;
	private int windDeg;
	private double windSpeed;
	private long timeStamp;

	/**
	 * Will construct a new WeatherDataObject and fill it with the given values
	 * 
	 * @param weatherIcon
	 * @param weatherDesc
	 * @param weatherDescDetail
	 * @param stationName
	 * @param temperature
	 * @param humidity
	 * @param pressure
	 * @param windDeg
	 * @param windSpeed
	 * @param timeStamp
	 */
	public WeatherDataObject(String weatherIcon, String weatherDesc, String weatherDescDetail, String stationName,
			double longitude, double latitude, double temperature, int humidity, int pressure, int windDeg,
			double windSpeed, long timeStamp) {
		super();
		this.weatherIcon = weatherIcon;
		this.weatherDesc = weatherDesc;
		this.weatherDescDetail = weatherDescDetail;
		this.stationName = stationName;
		this.longitude = longitude;
		this.latitude = latitude;
		this.temperature = temperature;
		this.humidity = humidity;
		this.pressure = pressure;
		this.windDeg = windDeg;
		this.windSpeed = windSpeed;
		this.timeStamp = timeStamp;
	}
	
	@Override
	public String toString(){
		String s = "name: " + getStationName() 
				+ " temperature: " + getTemperature() 
				+ " humidity: " + getHumidity() 
				+ " pressure: " + getPressure()
				+ " windDeg: " + getWindDeg()
				+ " windSpeed: " + getWindSpeed()
				+ " descShort: " + getWeatherDesc()  
				+ " descDetail: " + getWeatherDescDetail()  
				+ " descIcon: " + getWeatherIcon()
				+ " timeStamp: " + getTimeStamp();
		return s; 
	}
	
	/**
	 * Converts a JSON String into a Java object
	 * 
	 * @param json
	 * @return WeatherDataObject
	 */
	public static WeatherDataObject jsonToJavaObject(String json) {
		Gson gson = new Gson();
		WeatherDataObject weatherDataObject = gson.fromJson(json, WeatherDataObject.class);
		return weatherDataObject;
	}

	/**
	 * Converts this object into a JSON String
	 * 
	 * @return JSON String
	 */
	public String toJSON() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}

	public String getWeatherIcon() {
		return weatherIcon;
	}

	public String getWeatherDesc() {
		return weatherDesc;
	}

	public String getWeatherDescDetail() {
		return weatherDescDetail;
	}

	public String getStationName() {
		return stationName;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getTemperature() {
		return temperature;
	}

	public int getHumidity() {
		return humidity;
	}

	public int getPressure() {
		return pressure;
	}

	public int getWindDeg() {
		return windDeg;
	}

	public double getWindSpeed() {
		return windSpeed;
	}

	public long getTimeStamp() {
		return timeStamp;
	}
	
	
	
	
	
	private static final Set<String> goodWeatherSet = new HashSet<String>(Arrays.asList(
			"clear sky", 
			"few clouds", 
			"scattered clouds", 
			"calm", 
			"light breeze", 
			"gentle breeze", 
			"moderate breeze", 
			"fresh breeze"
			));
	
	private static final Set<String> okWeatherSet = new HashSet<String>(Arrays.asList(
			"broken clouds", 
			"overcast clouds", 
			"cold", 
			"hot", 
			"windy", 
			"strong breeze" 
			));
	
	private static final Set<String> badWeatherSet = new HashSet<String>(Arrays.asList( 
			"light intensity drizzle", 
			"drizzle", 
			"heavy intensity drizzle", 
			"light intensity drizzle rain", 
			"drizzle rain", 
			"shower rain and drizzle", 
			"shower drizzle", 
			"light rain", 
			"moderate rain", 
			"light intensity shower rain", 
			"shower rain", 
			"light snow", 
			"snow", 
			"light rain and snow", 
			"rain and snow", 
			"light shower snow", 
			"shower snow", 
			"mist", 
			"fog", 
			"dust", 
			"high wind, near gale"
			));
	
	private static final Set<String> reallyBadWeatherSet = new HashSet<String>(Arrays.asList(
			"thunderstorm with light rain", 
			"thunderstorm with rain", 
			"thunderstorm with heavy rain", 
			"light thunderstorm", 
			"thunderstorm", 
			"heavy thunderstorm", 
			"ragged thunderstorm", 
			"thunderstorm with light drizzle", 
			"thunderstorm with drizzle", 
			"thunderstorm with heavy drizzle", 
			"heavy intensity drizzle rain", 
			"heavy shower rain and drizzle", 
			"heavy intensity rain", 
			"very heavy rain", 
			"extreme rain", 
			"freezing rain", 
			"heavy intensity shower rain", 
			"ragged shower rain", 
			"heavy snow", 
			"sleet", 
			"shower sleet", 
			"heavy shower snow", 
			"haze", 
			"sand, dust whirls", 
			"sand", 
			"squalls", 
			"hail", 
			"gale"
			));
	
	private static final Set<String> fuckThisImStayingHomeWeatherSet = new HashSet<String>(Arrays.asList(
			"smoke", 
			"sand, dust whirls", 
			"volcanic ash", 
			"tornado", 
			"tornado", 
			"tropical storm", 
			"hurricane", 
			"severe gale", 
			"storm", 
			"violent storm", 
			"hurricane"));
	
	public double evaluateWeatherCondition(){
		if (goodWeatherSet.contains(weatherDescDetail)) {
			return 0.0;
		}
		else if (okWeatherSet.contains(weatherDescDetail)){
			return 0.25;
		}
		else if (badWeatherSet.contains(weatherDescDetail)){
			return 0.5;
		}
		else if (reallyBadWeatherSet.contains(weatherDescDetail)){
			return 0.75;
		}
		else if (fuckThisImStayingHomeWeatherSet.contains(weatherDescDetail)){
			return 1.0;
		}
		else{
			return 0.0;
		}
	}
}
