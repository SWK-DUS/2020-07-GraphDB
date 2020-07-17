import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import java.util.ArrayList;
import java.util.List;

public class DataHelper {

    public static void cleanupVerticesAndEdges(OrientGraph graph){

        graph.getVerticesOfClass("V").forEach(graph::removeVertex);
        graph.getEdgesOfClass("E").forEach(graph::removeEdge);

    }

    public static void createBooksWithRandomTitle(OrientGraph graph, int numOfBooks) {

        try {
            for (int i = 0; i < numOfBooks; i++) {
                Vertex book = graph.addVertex("class:Book");
                book.setProperty("title", "title_" + i);
                System.out.println("Created book: " + "title_" + i);
            }
            graph.commit();
        } catch( Exception e ) {
            graph.rollback();
        }
    }

    public static void createReadersWithRandomNames(OrientGraph graph, int numOfReaders) {

        try{
            for (int i=0; i < numOfReaders; i++) {
                Vertex reader = graph.addVertex("class:Reader");
                reader.setProperty("firstname", "Name_" + i);
                reader.setProperty("lastname", "Gremlin_" + i);
                System.out.println("Created user: " + "Name_" + i + " " + "Gremlin_" + i);
            }
            graph.commit();
        } catch( Exception e ) {
            graph.rollback();
        }
    }

    public static void createEdgeReaderBoughtBook(OrientGraph graph, String readerLastname, String[] bookTitles){

        System.out.println("Reader: " + readerLastname);
        Vertex reader = graph.getVertices("lastname", readerLastname).iterator().next();
        for (String bookTitle : bookTitles) {
            System.out.println("   Booktitle: " + bookTitle);
            Iterable<Vertex> books = graph.getVertices("title", bookTitle);
            Vertex book = books.iterator().next();
            reader.addEdge("bought", book);
        }

    }
}
