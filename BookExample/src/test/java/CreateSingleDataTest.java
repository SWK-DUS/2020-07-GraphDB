import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateSingleDataTest {

    private static OrientGraph graph = null;
    private static String database = "remote:localhost/SWK_Books";
    private static String user = "admin";
    private static String pwd = "admin";


    @BeforeAll
    public static void setup(){

        graph = new OrientGraph( database, user, pwd);

        // delete existing vertices and edges
        graph.getVerticesOfClass("V").forEach(v -> graph.removeVertex(v));

        graph.getEdgesOfClass("E").forEach(edge -> graph.removeEdge(edge));

        // prepare schema if not already done...
        OrientVertexType bookType = SchemaHelper.createVertexTypeIfNotExist(graph, "Book");
        SchemaHelper.addPropertyIfNotExists(bookType, "title", OType.STRING);

        OrientVertexType readerType = SchemaHelper.createVertexTypeIfNotExist(graph, "Reader");
        SchemaHelper.addPropertyIfNotExists(readerType, "firstname", OType.STRING);
        SchemaHelper.addPropertyIfNotExists(readerType, "lasstname", OType.STRING);
    }

    @AfterAll
    public static void close(){

        graph.shutdown();
    }

    @Test
    public void testCreateReaderBoughtBook(){

        Vertex reader = graph.addVertex("class:Reader");
        reader.setProperty("firstname", "Name1");
        reader.setProperty("lastname", "Gremlin");

        Vertex book   = graph.addVertex("class:Book");
        book.setProperty("title", "title1");
        assertEquals(2, graph.countVertices(), "expected 2 vertices => One Book, one Reader");

        graph.addEdge(null, reader, book, "bought");
        assertEquals(1, graph.countEdges(), "expected 1 edge => One Reader bought one Book.");
    }
}
