# WPS

It is a library that implements services to process geometries in a way that allows us to recreate obstacle limitation surfaces (OLS) defined by ICAO Annex 14.

These services are exposed by a GeoServer instance as Web Process Services (WPS).
 
 
## How to build

```
mvn clean package

cp ./target/ols-wps-2.11-SNAPSHOT.jar $GEOSERVER_HOME/webapps/geoserver/WEB-INF/lib/.
```

## How to use

http://localhost:8080/geoserver/web/wicket/bookmarkable/org.geoserver.wps.web.WPSRequestBuilder?1

