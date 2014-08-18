package me.tingri.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.IntObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.Text;

import java.util.List;

public class TransformRowWithHeader extends GenericUDF {
    ListObjectInspector listOI;
    IntObjectInspector elementOI;

    @Override
    public String getDisplayString(String[] arg0) {
        return "transformRowWithHeader(array,indexOfHeader)";
    }

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        if (arguments.length != 2)
            throw new UDFArgumentLengthException("transformRowWithHeader only takes 2 arguments: List<T>, int");

        ObjectInspector a = arguments[0];
        ObjectInspector b = arguments[1];

        if (!(a instanceof ListObjectInspector) || !(b instanceof IntObjectInspector))
            throw new UDFArgumentException("first argument must be a list / array, second argument must be a integer");

        this.listOI = (ListObjectInspector) a;
        this.elementOI = (IntObjectInspector) b;

        return ObjectInspectorFactory.getStandardListObjectInspector(PrimitiveObjectInspectorFactory.writableStringObjectInspector);
    }

    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {

        // get the list and int from the deferred objects using the object inspectors
        List<Text> list = (List<Text>) this.listOI.getList(arguments[0].get());
        Integer index = (Integer) elementOI.getPrimitiveJavaObject(arguments[1].get());

        // check for nulls
        if (list == null || index == null) return null;

        String header = list.get(index).toString();

        for (int i = 0; i < list.size(); i++)
            if (i != index)
                list.set(i, new Text(header + ":" + list.get(i)));

        list.set(index, null);

        return list;
    }

}