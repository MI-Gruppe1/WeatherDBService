-- SQL Testscript Schema Dummy
-- author: Jan-Peter Petersen
-- Feedback immer her damit


-- select database
use testing;

-- drop existing tables
DROP TABLE IF EXISTS WeatherData;
DROP TABLE IF EXISTS WeatherStation;
DROP TABLE IF EXISTS WeatherDescription;
DROP TABLE IF EXISTS WeatherDescriptionDetail;
DROP TABLE IF EXISTS WeatherDescriptionShort;
DROP TABLE IF EXISTS WeatherIcon;


CREATE TABLE WeatherDescriptionDetail(
	id INTEGER(10) AUTO_INCREMENT PRIMARY KEY,
	description CHAR(100)
) ENGINE = InnoDB;

CREATE TABLE WeatherDescriptionShort(
	id INTEGER(10) AUTO_INCREMENT PRIMARY KEY,
	description CHAR(100)
) ENGINE = InnoDB;

CREATE TABLE WeatherIcon(
	id INTEGER(10) AUTO_INCREMENT PRIMARY KEY,
	icon_code CHAR(5),
	icon_url CHAR(150)
) ENGINE = InnoDB;

CREATE TABLE WeatherDescription(
	id INTEGER(10) AUTO_INCREMENT PRIMARY KEY,
	short_id INTEGER(10),
	detail_id INTEGER(10),
	icon_id INTEGER(10),
	FOREIGN KEY (short_id) REFERENCES WeatherDescriptionShort(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (detail_id) REFERENCES WeatherDescriptionDetail(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (icon_id) REFERENCES WeatherIcon(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE WeatherStation(
	id INTEGER(10) AUTO_INCREMENT PRIMARY KEY,
	name CHAR(100) UNIQUE,
	longitude double(20,3),
	langitude double(20,3)
) ENGINE = InnoDB;

CREATE TABLE WeatherData( 
	id INTEGER(10) AUTO_INCREMENT PRIMARY KEY,
	station_id INTEGER(10),
	icon_id INTEGER(10),
	desc_id INTEGER(10),
	temperature DOUBLE(3,2),
	humidity INTEGER(4),
	pressure INTEGER(5),
	winddeg INTEGER(4),
	windSpeed DOUBLE(3,2),
	DateTime INTEGER(15),
	FOREIGN KEY (icon_id) REFERENCES WeatherIcon(id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (desc_id) REFERENCES WeatherDescription(id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (station_id) REFERENCES WeatherStation(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

show tables;
