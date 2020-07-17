import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.impls.orient.OrientEdgeType;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateSchemaTest {

    private static OrientGraph graph = null;
    private static String database = "remote:localhost/SWK_Books";
    private static String user = "admin";
    private static String pwd = "admin";


    @BeforeAll
    public static void setup(){

        graph = new OrientGraph( database, user, pwd);


    }

    @AfterAll
    public static void close(){

        graph.shutdown();
    }

    @Test
    public void testCreateVerticesScheme(){

        OrientVertexType bookType;
        try {
            bookType = graph.createVertexType("Book");
            bookType.createProperty("title", OType.STRING);
        } catch (com.orientechnologies.orient.core.exception.OSchemaException e){
            // type already exists
            bookType = graph.getVertexType("Book");
        }

        bookType = graph.getVertexType("Book");
        assertNotNull(bookType, "Type Book does not exist");
        assertNotNull(bookType.getProperty("title"));

        OrientVertexType reader;
        try {
            reader = graph.createVertexType("Reader");
            reader.createProperty("firstname", OType.STRING);
            reader.createProperty("lastname", OType.STRING);
        }catch (com.orientechnologies.orient.core.exception.OSchemaException e){
            // type already exists
            reader = graph.getVertexType("Reader");
        }

        OrientVertexType readerType = graph.getVertexType("Reader");
        assertNotNull(readerType, "Type Reader does not exist");
        assertNotNull(readerType.getProperty("firstname"), "expected a property firstname in type Reader");
        assertNotNull(readerType.getProperty("lastname"), "expected a property lasttname in type Reader");

    }

    @Test
    public void testCreateEdgesScheme(){


        OrientEdgeType boughtType;
        try {
            boughtType = graph.createEdgeType("bought");
        } catch (com.orientechnologies.orient.core.exception.OSchemaException e){
            // type already exists
            boughtType = graph.getEdgeType("bought");
        }

        boughtType = graph.getEdgeType("bought");
        assertNotNull(boughtType, "Edge Type 'bought' does not exist");

    }

}
