package WeatherDBService;

public class WeatherStationObject {

	private int id;
	private String stationName;
	private double longitude;
	private double latitude;
	
	public static final double R = 6372.8;//Erdradius in km

	public WeatherStationObject(int id, String stationName, double longitude, double latitude) {
		super();
		this.id = id;
		this.stationName = stationName;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public double getDistanceTo(double longitude, double latitude) {
		return haversine(this.latitude, this.longitude, latitude, longitude);
	}

	private double haversine(double latStation, double lngStation, double latWaypoint, double lngWaypoint){
        double dLat = Math.toRadians(latWaypoint - latStation);
        double dLon = Math.toRadians(lngWaypoint - lngStation);
        latStation = Math.toRadians(latStation);
        latWaypoint = Math.toRadians(latWaypoint);
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(latStation) * Math.cos(latWaypoint);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
	
	public int getId() {
		return id;
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

}
