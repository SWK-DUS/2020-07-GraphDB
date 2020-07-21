import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//import org.apache.tinkerpop.gremlin.orientdb.OrientGraph;
//import org.apache.tinkerpop.gremlin.orientdb.OrientGraphFactory;
//import org.apache.tinkerpop.gremlin.structure.Vertex;
//import com.orientechnologies.orientdb.gremlin;
//import org.apache.tinkerpop.gremlin.orientdb;
//import org.apache.tinkerpop.gremlin.orientdb.*
//import com.tinkerpop.gremlin.process.traversal.Traversal;


/**
 *     inspired by Gremlin example 5 at https://tinkerpop.apache.org/
 *
 *     // Get a ranking of the most relevant products for Gremlin given his purchase history.
 *     g.V().has("name","gremlin").out("bought").aggregate("stash").
 *       in("bought").out("bought").
 *       where(not(within("stash"))).
 *       groupCount().
 *       order(local).by(values,desc)
 *
 *       Tinker Pop / Gremlin is not supported bys the current OrienDB distribution, but there is
 *       a deveoper versionavailable at http://orientdb.com/download
 *
 */
public class RecommendationTest {

    private static OrientGraph graph = null;
    private static String database = "remote:localhost/SWK_Books";
    private static String user = "admin";
    private static String pwd = "admin";


    /**
     * Recommended in docu:
     *
     * // AT THE BEGINNING
     * OrientGraphFactory factory = new OrientGraphFactory("plocal:/tmp/graph/db").setupPool(1,10);
     *
     * // EVERY TIME YOU NEED A GRAPH INSTANCE
     * OrientGraph graph = factory.getTx();
     */
    @BeforeAll
    public static void setup(){

        graph = new OrientGraph( database, user, pwd);

        // delete existing vertices and edges
        DataHelper.cleanupVerticesAndEdges(graph);


        // prepare schema if not already done...
        OrientVertexType bookType = SchemaHelper.createVertexTypeIfNotExist(graph, "Book");
        SchemaHelper.addPropertyIfNotExists(bookType, "title", OType.STRING);

        OrientVertexType readerType = SchemaHelper.createVertexTypeIfNotExist(graph, "Reader");
        SchemaHelper.addPropertyIfNotExists(readerType, "firstname", OType.STRING);
        SchemaHelper.addPropertyIfNotExists(readerType, "lasstname", OType.STRING);

        SchemaHelper.createEdgeTypeIfNotExist(graph, "bought");

        // add books and readers for test
        DataHelper.createBooksWithRandomTitle(graph,20);
        DataHelper.createReadersWithRandomNames(graph, 5);

        // add relations
        DataHelper.createEdgeReaderBoughtBook(graph, "Gremlin_0", new String[]{"title_1", "title_5", "title_10", "title_17"});
        DataHelper.createEdgeReaderBoughtBook(graph, "Gremlin_1", new String[]{"title_1", "title_4", "title_6", "title_9", "title_13", "title_18", "title_0"});
        DataHelper.createEdgeReaderBoughtBook(graph, "Gremlin_2", new String[]{"title_0", "title_4", "title_11"});
        DataHelper.createEdgeReaderBoughtBook(graph, "Gremlin_3", new String[]{"title_2", "title_3", "title_10", "title_17", "title_18", "title_19"});
        DataHelper.createEdgeReaderBoughtBook(graph, "Gremlin_4", new String[]{"title_4", "title_7", "title_9", "title_17"});

    }

    @AfterAll
    public static void close(){

        graph.shutdown();
    }

    /**
     *       graph.V().has("lastname","gremlin1").out("bought").aggregate("stash").
     *                        in("bought").out("bought").
     *                        where(not(within("stash"))).
     *                        groupCount().
     *                        order(local).by(values,desc);
     *
     *      Reader Gremlin1.out(bought) all books reader bought by Gremlin1 / stash them
     *      in (bought) all other reader bought this book
     *      out(bought) all other books these other readers bought
     *      which were not bought by Gremlin1
     *      count number ofh results....
     */
    @Test
    public void testRecommendation(){

        //graph.getVerticesOfClass()
        // check database
        assertEquals(25, graph.countVertices(), "expected 25 vertices => One Book, one Reader");

        assertEquals(24, graph.countEdges("bought"), "expected 24 edges of type bought");
    }

    @Test
    public void testBoughtBooks(){
        Vertex gremlin0 = graph.getVertices("lastname","Gremlin_0").iterator().next();

        assertEquals("Name_0", gremlin0.getProperty("firstname"));

        Iterable<Vertex> booksBought =   gremlin0.getVertices(Direction.OUT, "bought");
        assertNotNull(booksBought, "bought books should not be null");
        System.out.println("Gremlin0 bought these books");
        int i = 0;
        for (Vertex book  : booksBought) {
            i++;
            System.out.println(book.getProperty("title").toString());
        }
        assertEquals(4,i);

        System.out.println("Other readers");
        List<Vertex> otherReaders = new ArrayList<>();
        for (Vertex book : booksBought){
            Iterable<Vertex> readers = book.getVertices(Direction.IN);
            for (Vertex otherReader : readers){

                String lastname = otherReader.getProperty("lastname").toString();
                if (!lastname.equals("Gremlin_0")) {
                    otherReaders.add(otherReader);
                    System.out.println(lastname);
                }
            }
        }
        assertEquals(4,otherReaders.size());

        List<Vertex> otherBooks = new ArrayList<>();
        System.out.println("... other books bought by these users");
        for (Vertex reader : otherReaders){
              Iterable<Vertex> otherBookList = reader.getVertices(Direction.OUT, "bought");
              for (Vertex book : otherBookList){
                  otherBooks.add(book);
                  System.out.println(book.getProperty("title").toString());
              }
        }
        assertEquals(23, otherBooks.size());

        // Recommondation: Remove duplicates and book user already bought ...
    }

}
