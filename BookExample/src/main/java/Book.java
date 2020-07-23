import com.tinkerpop.blueprints.Vertex;

public class Book {

    public Vertex vertex = null;

    public Book(Vertex book) {
        this.vertex = book;
    }

    public String getTitle(){
        return vertex.getProperty("title");
    }

    public boolean equals(Object obj) {
        if (obj instanceof Book) {
            Book another = (Book) obj;
            if (this.vertex.getId().equals(another.vertex.getId())) {
                return true;
            }
        }

        return false;
    }

    public int hashCode(){
        Object id = vertex.getId();
        return id.hashCode();
    }

}
