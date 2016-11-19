-- SQL Testscript Schema Dummy
-- author: Jan-Peter Petersen
-- Feedback immer her damit


-- select database
use mi;

-- drop existing tables
DROP TABLE IF EXISTS WeatherData;
DROP TABLE IF EXISTS WeatherStation;
DROP TABLE IF EXISTS WeatherDescription;
DROP TABLE IF EXISTS WeatherDescriptionDetail;
DROP TABLE IF EXISTS WeatherDescriptionShort;
DROP TABLE IF EXISTS WeatherDescriptionIcon;


CREATE TABLE WeatherDescriptionDetail(
	id INTEGER(10) AUTO_INCREMENT PRIMARY KEY,
	description CHAR(100) UNIQUE
) ENGINE = InnoDB;

CREATE TABLE WeatherDescriptionShort(
	id INTEGER(10) AUTO_INCREMENT PRIMARY KEY,
	description CHAR(100) UNIQUE
) ENGINE = InnoDB;

CREATE TABLE WeatherDescriptionIcon(
	id INTEGER(10) AUTO_INCREMENT PRIMARY KEY,
	icon_code CHAR(5) UNIQUE,
	icon_url CHAR(150) UNIQUE
) ENGINE = InnoDB;

CREATE TABLE WeatherDescription(
	id INTEGER(10) AUTO_INCREMENT PRIMARY KEY,
	short_id INTEGER(10),
	detail_id INTEGER(10),
	icon_id INTEGER(10),
	UNIQUE(short_id, detail_id, icon_id),
	FOREIGN KEY (short_id) REFERENCES WeatherDescriptionShort(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (detail_id) REFERENCES WeatherDescriptionDetail(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (icon_id) REFERENCES WeatherDescriptionIcon(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE WeatherStation(
	id INTEGER(10) AUTO_INCREMENT PRIMARY KEY,
	name CHAR(100) UNIQUE,
	longitude double(20,3),
	latitude double(20,3),
	UNIQUE(longitude, latitude)
) ENGINE = InnoDB;

CREATE TABLE WeatherData( 
	id INTEGER(10) AUTO_INCREMENT PRIMARY KEY,
	station_id INTEGER(10),
	desc_id INTEGER(10),
	temperature DOUBLE(3,2),
	humidity INTEGER(4),
	pressure INTEGER(5),
	windDeg INTEGER(4),
	windSpeed DOUBLE(3,2),
	timestamp BIGINT(15),
	insertTime DATETIME NOT NULL DEFAULT NOW(),
	FOREIGN KEY (desc_id) REFERENCES WeatherDescription(id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (station_id) REFERENCES WeatherStation(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

show tables;
