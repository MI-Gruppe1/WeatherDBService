package de.haw.web.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.haw.ejb.interfaces.WeatherClassEntityDAO;
import de.haw.persistence.entities.WeatherClassEntity;


@Path("/WeatherClassEntitys")
@Stateless
public class WeatherEndpoint {
	
	@EJB
	private WeatherClassEntityDAO weatherClassEntityDAO;
	
	//http://localhost:8080/WeatherCrawlerserviceWeb/api/v1/WeatherClassEntitys/list
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<WeatherClassEntity> getAll(){
		
		return weatherClassEntityDAO.getWeatherobject();
		
	}
	/*test
	 * 
	 */
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public void getAllMY(){
		
		System.out.println("ich bin"+ " "+getAll());
		
	}
	/*
	 * ende
	 */
	
	//http://localhost:8080/WeatherCrawlerserviceWeb/api/v1/WeatherClassEntitys/listCurrentWeather
	@GET
	@Path("listCurrentWeather")
	@Produces(MediaType.APPLICATION_JSON)
	public WeatherClassEntity getCurrentWeather(int id)
	{
		return weatherClassEntityDAO.getCurrentWeather(id);
	}
	
	//http://localhost:8080/WeatherCrawlerserviceWeb/api/v1/WeatherClassEntitys/listTemperature
	@GET
	@Path("listTemperature")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTemperatureAtSpecificTime(int id)
	{
		return weatherClassEntityDAO.getTemperatureAtSpecificTime(id);
	}
	
	//http://localhost:8080/WeatherCrawlerserviceWeb/api/v1/WeatherClassEntitys/listPersipitation
	@GET
	@Path("listPersipitation")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPersipitationAtSpecificTime(int id)	
	{
		return weatherClassEntityDAO.getPersipitationAtSpecificTime(id);
	}
	
	

}
