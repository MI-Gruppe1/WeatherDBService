package de.haw.ejb.interfaces;

import java.util.List;

import de.haw.persistence.entities.WeatherClassEntity;

public interface WeatherClassEntityDAO {
			
public WeatherClassEntity getCurrentWeather(int id);
public String getTemperatureAtSpecificTime(int id);
public String getPersipitationAtSpecificTime(int id);	
public List<WeatherClassEntity>  getWeatherobject();

}
