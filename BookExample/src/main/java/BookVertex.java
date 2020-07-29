import com.tinkerpop.blueprints.Vertex;

import java.util.Comparator;

public class BookVertex {

    private final Vertex vertex;
    public Vertex getVertex(){ return vertex;}

    private int counter = 1;
    public int getCount(){ return counter; }

    public BookVertex(Vertex book) {
        this.vertex = book;
    }

    public String getTitle(){
        return vertex.getProperty("title");
    }

    public boolean equals(Object obj) {
        if (obj instanceof BookVertex) {
            BookVertex another = (BookVertex) obj;
            return this.vertex.getId().equals(another.vertex.getId());
        }

        return false;
    }

    public int hashCode(){
        Object id = vertex.getId();
        return id.hashCode();
    }

    public void incCounter() {
        counter++;
    }
}

/**
 * Used for sorting in descending order of count
 */
class SortBookVertexByCount implements Comparator<BookVertex>
{

    public int compare(BookVertex a, BookVertex b)
    {
        return b.getCount() - a.getCount();
    }
}
