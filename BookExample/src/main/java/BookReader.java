import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

/**
 * DEPRECATED not finished, yet!!!!
 * ToDo: Implement bought method
 */
public class BookReader {

    private Vertex myVertex;

    public BookReader getByFirstName(OrientGraph graph, String name1) {

        Iterable<Vertex> vertices = graph.getVertices("firstname", name1);
        myVertex = vertices.iterator().next();
        return this;
    }

    public void bought(String[] listOfBooks){

        for (int i=0; i < listOfBooks.length; i++){
            // get book
            //myVertex.
        }
    }
}
