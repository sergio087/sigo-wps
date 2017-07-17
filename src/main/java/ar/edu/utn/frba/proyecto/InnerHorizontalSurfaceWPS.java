package ar.edu.utn.frba.proyecto;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.vector.VectorProcess;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


@DescribeProcess(title="InnerHorizontalSurfaceWPS", description="Inner horizontal surface")
/**
 *
 *
 * @source $URL$
 */
public class InnerHorizontalSurfaceWPS implements VectorProcess  {


    @DescribeResult(name = "result", description = "Surface feature")
    public static SimpleFeatureCollection execute(
            @DescribeParameter(name = "point1", description = "Polygon to be split") Point point1,
            @DescribeParameter(name = "point2", description = "Polygon to be split") Point point2
    ){

        // calculate the geometry
        Geometry buffer1 = point1.buffer(0.02);
        Geometry buffer2 = point2.buffer(0.02);
        Geometry geometry = buffer1.union(buffer2);


        // build the feature type
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("Polygon");
        tb.add("geom", geometry.getClass(), DefaultGeographicCRS.WGS84);
        tb.add("name", String.class);
        tb.add( "height", Double.class );
        tb.add( "radius", Double.class );
        SimpleFeatureType schema = tb.buildFeatureType();


        // build the feature
        SimpleFeature sf = SimpleFeatureBuilder.build(schema, new Object[] { geometry, "Inner Horizontal", 45, 2000 }, null);
        ListFeatureCollection result = new ListFeatureCollection(schema);
        result.add(sf);

        return result;
    }
}
