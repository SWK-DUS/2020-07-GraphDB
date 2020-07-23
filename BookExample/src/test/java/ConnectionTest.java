import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ConnectionTest {


    @Test
    public void testConnectionViaGraphClass(){

        assertEquals(3,3);
        // Database has to exist!!!
        OrientGraph graph = new OrientGraph("remote:localhost/SWK_Books", "admin", "admin");
        assertNotNull(graph);

        graph.shutdown();

        //graph = new OrientGraph("remote:localhost/swk_books", "admin", "admin");
        //assertNotNull(graph);

        graph.shutdown();
    }

    @Test
    public void testConnectionViaDBClassCheck(){

        OrientDB orientDB = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
        assertNotNull(orientDB);

        ODatabaseSession dbSession = orientDB.open("test", "admin", "admin");
        assertNotNull(dbSession);

        dbSession.close();
        orientDB.close();
    }

}
