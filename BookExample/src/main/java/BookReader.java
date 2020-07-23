import com.tinkerpop.blueprints.Vertex;

public class BookReader {

    public BookReader(Vertex vertex){
       this.vertex = vertex;
    }

    public Vertex vertex = null;

    public String getFirstName(){
        return vertex.getProperty("firstname");
    }

    public String getLastName(){
        return vertex.getProperty("lastname");
    }

    public boolean equals(Object obj) {
        if (obj instanceof BookReader) {
            BookReader another = (BookReader) obj;

            if (this.vertex.getId().equals(another.vertex.getId() )) {
                return true;
            }
        }

        return false;
    }

    public int hashCode(){
        return vertex.getId().hashCode();
    }

}
