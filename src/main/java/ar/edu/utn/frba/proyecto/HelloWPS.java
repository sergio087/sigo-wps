package ar.edu.utn.frba.proyecto;

import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geoserver.wps.gs.GeoServerProcess;

@DescribeProcess(title="helloWPS", description="Hello WPS Sample")
public class HelloWPS implements GeoServerProcess {

    @DescribeResult(name="result", description="output result")
    public String execute(@DescribeParameter(name="name", description="name to return") String name) {
        return "Hello, " + name;
    }
}
