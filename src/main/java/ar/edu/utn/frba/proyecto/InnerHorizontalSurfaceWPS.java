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

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.stream.events.Characters;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


@DescribeProcess(title="InnerHorizontalSurfaceWPS", description="Inner horizontal surface")
/**
 *
 *
 * @source $URL$
 */
public class InnerHorizontalSurfaceWPS implements VectorProcess  {


    @DescribeResult(name = "result", description = "Surface feature")
    public static SimpleFeatureCollection execute(
            @DescribeParameter(name = "point1", description = "Runway threshold 1") Point point1,
            @DescribeParameter(name = "point2", description = "Runway threshold 2") Point point2
    ){

        DataSource myDS = null;
        Connection connection;

        try {
            InitialContext ic = new InitialContext();
            myDS = (DataSource)ic.lookup("jdbc/sigodb");
            connection = myDS.getConnection();
        } catch (NamingException | SQLException e) {
            throw new Error(e);
        }

        //fetch data from regulation

        String runwayClassification = "aprox_visual";
        String runwayCategory = "null";
        String runwayCodeNumber = "1";

        String query = new StringBuilder()
                .append("SELECT property, value FROM tbl_ols_rules_icaoannex14 ")
                .append("WHERE surface_name = 'horizontal_interna' ")
                .append("AND runway_clasification =")
                .append(" '")
                .append(runwayClassification)
                .append("' ")
                .append("AND runway_category =")
                .append(" '")
                .append(runwayCategory)
                .append("' ")
                .append("AND runway_code_number =")
                .append(" '")
                .append(runwayCodeNumber)
                .append("' ")
                .toString();

        Double height = null, radio = null;

        try {
            ResultSet resultSet = myDS.getConnection().createStatement().executeQuery(query);

            while(resultSet.next()){
                switch (resultSet.getString(1)){
                    case "altura" :
                        height = new Double(resultSet.getString(2));
                        break;
                    case "radio" :
                        radio = new Double(resultSet.getString(2));
                        break;
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Error(e);
        }

        // calculate the geometry
        Geometry buffer1 = point1.buffer(radio); //0.02
        Geometry buffer2 = point2.buffer(radio); //0.02
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
        SimpleFeature sf = SimpleFeatureBuilder.build(schema, new Object[] { geometry, "Inner Horizontal", height, radio }, null);
        ListFeatureCollection result = new ListFeatureCollection(schema);
        result.add(sf);

        return result;
    }
}
