import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.impls.orient.OrientEdgeType;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class SchemaHelper {

    public static OrientVertexType createVertexTypeIfNotExist(OrientGraph graph, String typeName){

        OrientVertexType newType;
        try {
            newType = graph.createVertexType(typeName);
        } catch (com.orientechnologies.orient.core.exception.OSchemaException e){
            // type already exists
            newType = graph.getVertexType(typeName);
        }
        return newType;
    }

    public static OProperty addPropertyIfNotExists(OrientVertexType vertex, String name, OType type){

        OProperty prop = vertex.getProperty(name);
        if (prop != null && prop.getType() == type){
            return prop;
        } else {
            return vertex.createProperty(name, type);
        }
    }


    public static OrientEdgeType createEdgeTypeIfNotExist(OrientGraph graph, String typeName){

        OrientEdgeType newType;
        try {
            newType = graph.createEdgeType(typeName);
        } catch (com.orientechnologies.orient.core.exception.OSchemaException e){
            // type already exists
            newType = graph.getEdgeType(typeName);
        }
        return newType;
    }

}
