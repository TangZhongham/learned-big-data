package udf;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.hive.ql.ErrorMsg;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;


import io.transwarp.guardian.federation.net.sf.json.JSONObject;
import org.apache.htrace.fasterxml.jackson.databind.ObjectMapper;


public class GetJson extends GenericUDTF{
    @Override
    public void close() throws HiveException {
        // TODO Auto-generated method stub
    }
    @Override
    public StructObjectInspector initialize(ObjectInspector[] arg0) throws UDFArgumentException {
        // TODO Auto-generated method stub
        if(arg0.length != 1){
            throw new UDFArgumentLengthException(ErrorMsg.valueOf("SplitString only takes one argument"));
        }

        if(arg0[0].getCategory() != ObjectInspector.Category.PRIMITIVE){
            throw new UDFArgumentException(ErrorMsg.valueOf("SplitString only takes string as a parameter"));
        }

        ArrayList<String> fieldNames = new ArrayList<>();
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<>();

        fieldNames.add("col1");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        fieldNames.add("col2");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);

        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
    }

    public Object[] processPerRow(Object[] args) {
        JSONObject object = JSONObject.fromObject(CanalData.class);

        ObjectMapper objectMapper = new ObjectMapper();

        Object[] result = new Object[args.length-1];

        // TODO 生成多行在这里处理

        for (int i=0;i<args.length-1;i++){
            result[i] = args[i+1];
            try {
                CanalData canalData = objectMapper.readValue((byte[]) result[i], CanalData.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    @Override
    public void process(Object[] arg0) throws HiveException {

        String testJsonInsert = "{\"data\":[{\"id\":\"110\",\"name\":\"jacket\",\"description\":\"water resistent white wind breaker\",\"weight\":\"0.2\"}],\"database\":\"inventory\",\"es\":1589373552000,\"id\":6,\"isDdl\":false,\"mysqlType\":{\"id\":\"INTEGER\",\"name\":\"VARCHAR(255)\",\"description\":\"VARCHAR(512)\",\"weight\":\"FLOAT\"},\"old\":null,\"pkNames\":[\"id\"],\"sql\":\"\",\"sqlType\":{\"id\":4,\"name\":12,\"description\":12,\"weight\":7},\"table\":\"products2\",\"ts\":1589373552882,\"type\":\"INSERT\"}\n";

        JSONObject object = JSONObject.fromObject(CanalData.class);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            CanalData canalData = objectMapper.readValue(testJsonInsert, CanalData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO Auto-generated method stub
        String input = arg0[0].toString();
        String[] inputSplits = input.split("#");
        for (int i = 0; i < inputSplits.length; i++) {
            try {
                String[] result = inputSplits[i].split(":");
                forward(result);
            } catch (Exception e) {
                continue;
            }
        }
    }
}