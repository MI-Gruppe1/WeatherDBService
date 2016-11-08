package de.haw.persistence.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class WeatherClassEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Weatherid;
	public int getWeatherid() {
		return Weatherid;
	}
	public void setWeatherid(int weatherid) {
		Weatherid = weatherid;
	}
	@NotNull
	 private String stationName;
	@NotNull
	 private String weatherIcon;
	@NotNull
	 private String weatherDesc;
	@NotNull
	 private String weatherDescDetail;
	@NotNull
	 private String temperature;
	@NotNull
	 private String humidity;
	@NotNull
	 private String pressure;
	@NotNull
	 private String windDeg;
	@NotNull
	 private String windSpeed;
	private List<WeatherClassEntity> WeatherClassEntitys = new ArrayList<WeatherClassEntity>();
	public List<WeatherClassEntity> getWeatherClassEntitys() {
		return WeatherClassEntitys;
	}
	public void setWeatherClassEntitys(List<WeatherClassEntity> weatherClassEntitys) {
		WeatherClassEntitys = weatherClassEntitys;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getWeatherIcon() {
		return weatherIcon;
	}
	public void setWeatherIcon(String weatherIcon) {
		this.weatherIcon = weatherIcon;
	}
	public String getWeatherDesc() {
		return weatherDesc;
	}
	public void setWeatherDesc(String weatherDesc) {
		this.weatherDesc = weatherDesc;
	}
	public String getWeatherDescDetail() {
		return weatherDescDetail;
	}
	public void setWeatherDescDetail(String weatherDescDetail) {
		this.weatherDescDetail = weatherDescDetail;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getPressure() {
		return pressure;
	}
	public void setPressure(String pressure) {
		this.pressure = pressure;
	}
	public String getWindDeg() {
		return windDeg;
	}
	public void setWindDeg(String windDeg) {
		this.windDeg = windDeg;
	}
	public String getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
	}
	 

}
