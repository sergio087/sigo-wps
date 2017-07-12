package ar.edu.utn.frba.proyecto;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.geoserver.wps.gs.GeoServerProcess;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;

@DescribeProcess(title="InnerHorizontalSurfaceWPS", description="Inner horizontal surface")
public class InnerHorizontalSurfaceWPS implements GeoServerProcess {


    @DescribeResult(name="surface")
    public static Geometry execute(
            @DescribeParameter(name = "point1", description = "Polygon to be split") Point point1,
            @DescribeParameter(name = "point2", description = "Polygon to be split") Point point2
    ){

        Geometry buffer1 = point1.buffer(0.02);
        Geometry buffer2 = point2.buffer(0.02);

        return buffer1.union(buffer2);
    }
}
