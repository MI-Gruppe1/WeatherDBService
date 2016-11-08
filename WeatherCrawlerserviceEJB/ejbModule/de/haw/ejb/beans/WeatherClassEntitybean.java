package de.haw.ejb.beans;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.haw.ejb.interfaces.WeatherClassEntityDAO;
import de.haw.persistence.entities.WeatherClassEntity;

@Stateless
@Remote(WeatherClassEntity.class)
public class WeatherClassEntitybean implements WeatherClassEntityDAO{
	
	@PersistenceContext
	private EntityManager em;


	@Override
	public WeatherClassEntity getCurrentWeather(int id) {
		// gib vom aktuellen Wetter den ganzen Datensatz zurückt
		return em.find(WeatherClassEntity.class, id);
	}

	@Override
	public String getTemperatureAtSpecificTime(int id) {
		// gib vom bestimmte vom aktuellen Wetter, di Temperatur zurück
		WeatherClassEntity neuesObject1 = em.find(WeatherClassEntity.class, id);
		return neuesObject1.getTemperature();
	}

	@Override
	public String getPersipitationAtSpecificTime(int id) {
		// TODO noch zu arbeiten
		WeatherClassEntity neuesObject= em.find(WeatherClassEntity.class, id);
		return neuesObject.getHumidity();
	}

	@Override
	public List<WeatherClassEntity> getWeatherobject() {
		// gib alle Datensätzen im aus dem Datenbank zurück
		return 
				em.createQuery("SELECT w FROM WeatherClassEntity w", WeatherClassEntity.class)
				.getResultList();
	}

}
