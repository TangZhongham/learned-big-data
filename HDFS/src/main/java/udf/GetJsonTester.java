package udf;

import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import udf.example.MoveLeftUDTF;

public class GetJsonTester {

    public static void main(String[] args) {
        GetJson getJson = new GetJson();
        /*
         * set two elems in inputOIs to  pass the judge of "args.length <= 1" in MoveLeftUDTF,
         * and first elem must be "int"
         */
        ObjectInspector[] inputOIs = {PrimitiveObjectInspectorFactory.javaIntObjectInspector,
                PrimitiveObjectInspectorFactory.javaStringObjectInspector};
        try{
            getJson.initialize(inputOIs);
//            Object[] row = new Object[] {6, "transwarp", "streamsql", "udtf"};
            Object[] row = new Object[] {"{\"data\":[{\"id\":\"110\",\"name\":\"jacket\",\"description\":\"water resistent white wind breaker\",\"weight\":\"0.2\"}],\"database\":\"inventory\",\"es\":1589373552000,\"id\":6,\"isDdl\":false,\"mysqlType\":{\"id\":\"INTEGER\",\"name\":\"VARCHAR(255)\",\"description\":\"VARCHAR(512)\",\"weight\":\"FLOAT\"},\"old\":null,\"pkNames\":[\"id\"],\"sql\":\"\",\"sqlType\":{\"id\":4,\"name\":12,\"description\":12,\"weight\":7},\"table\":\"products2\",\"ts\":1589373552882,\"type\":\"INSERT\"}\n"};
            ;
            Object[] result = getJson.processPerRow(row);
            for(int i = 0; i < result.length; i++) {
                System.out.print(result[i].toString() + " ");
            }
        } catch(Throwable ex) {
            ex.printStackTrace();
        }
    }
}
