package WeatherDBService.WeatherDBService;

import com.google.gson.Gson;

public class WeatherDataObject {

	private String weatherIcon;
	private String weatherDesc;
	private String weatherDescDetail;
	private String stationName;
	private double temperature;
	private int humidity;
	private int pressure;
	private int windDeg;
	private double windSpeed;
	private long dateTime;

	/**
	 * Will construct a new WeatherDataObject and fill it with the given values
	 * @param weatherIcon
	 * @param weatherDesc
	 * @param weatherDescDetail
	 * @param stationName
	 * @param temperature
	 * @param humidity
	 * @param pressure
	 * @param windDeg
	 * @param windSpeed
	 * @param dateTime
	 */
	public WeatherDataObject(String weatherIcon, String weatherDesc, String weatherDescDetail, String stationName,
			double temperature, int humidity, int pressure, int windDeg, double windSpeed, long dateTime) {
		super();
		this.weatherIcon = weatherIcon;
		this.weatherDesc = weatherDesc;
		this.weatherDescDetail = weatherDescDetail;
		this.stationName = stationName;
		this.temperature = temperature;
		this.humidity = humidity;
		this.pressure = pressure;
		this.windDeg = windDeg;
		this.windSpeed = windSpeed;
		this.dateTime = dateTime;
	}
	
	public static WeatherDataObject jsonToJavaObeject(String json) {
		Gson gson = new Gson();
		WeatherDataObject weatherDataObject = gson.fromJson(json, WeatherDataObject.class);
		return weatherDataObject;
	}
	
	public String toJSON(){
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}
}
