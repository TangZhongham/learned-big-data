package udf.example;

import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;


public class MoveLeftUDTFTester {
    public static void main(String[] args) {
        MoveLeftUDTF moveLeftUDTF = new MoveLeftUDTF();
        /*
         * set two elems in inputOIs to  pass the judge of "args.length <= 1" in MoveLeftUDTF,
         * and first elem must be "int"
         */
        ObjectInspector[] inputOIs = {PrimitiveObjectInspectorFactory.javaIntObjectInspector,
                PrimitiveObjectInspectorFactory.javaStringObjectInspector};
        try{
            moveLeftUDTF.initialize(inputOIs);
            Object[] row = new Object[] {6, "transwarp", "streamsql", "udtf"};
            Object[] result = moveLeftUDTF.processPerRow(row);
            for(int i = 0; i < result.length; i++) {
                System.out.print(result[i].toString() + " ");
            }
        } catch(Throwable ex) {
            ex.printStackTrace();
        }
    }
}

/*
 * result of test:

   transwarp streamsql udtf
   Process finished with exit code 0
 */
