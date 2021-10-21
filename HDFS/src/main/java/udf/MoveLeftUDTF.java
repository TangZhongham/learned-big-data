package udf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;

import java.util.ArrayList;

public class MoveLeftUDTF extends GenericUDTF {
    ObjectInspector[] inputOIs;

    @Override
    public StructObjectInspector initialize(ObjectInspector[] args)
            throws UDFArgumentException {

        /*
         * Write some checking rules of input columns(args) such as its amount and type
         */
        if (args.length <= 1) {
            System.out.println("Input columns amount must be greater than 1");
        }
        if (!args[0].getTypeName().equals("int")) {
            System.out.println("MoveLeftUDTF must take an integer as the first parameter");
        }

        /*
         * Define the output columns names and type, the same type(ObjectInspector) in this example.
         * If inserting output to table, field names will be replaced by column aliases of table
         */
        ArrayList<String> fieldNames = new ArrayList<String>();
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();
        inputOIs = args;
        for (int i=1; i<inputOIs.length; i++) {
            fieldNames.add("field"+i);
            fieldOIs.add(inputOIs[i]);
        }

        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
    }


    /*
     * Parameter "inputIOs" stands for a row which consists of input columns.
     */
    @Override
    public void process(Object[] inputIOs) throws HiveException {
        try {
            Object[] result = processPerRow(inputIOs);
            forward(result);
        }
        catch (Exception e) {
            System.out.println("MoveLeftUDTF processing error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
     * Write business logic out of method "process", for debugging and testing more easily.
     */
    public Object[] processPerRow(Object[] args) {
        Object[] result = new Object[args.length-1];
        for (int i=0;i<args.length-1;i++){
            result[i] = args[i+1];
        }
        return result;
    }


    @Override
    public void close() throws HiveException {
    }
}