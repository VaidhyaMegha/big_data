package me.tingri.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.List;

public class MinArrayofInts extends GenericUDF {
    ListObjectInspector listOI;

    @Override
    public String getDisplayString(String[] arg0) {
        return "MinArrayofInts(array)";
    }

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        if (arguments.length != 1)
            throw new UDFArgumentLengthException("MinArrayofInts only takes 1 arguments: List<T>");

        ObjectInspector a = arguments[0];

        if (!(a instanceof ListObjectInspector) )
            throw new UDFArgumentException("first argument must be a list / array");

        this.listOI = (ListObjectInspector) a;

        return PrimitiveObjectInspectorFactory.javaLongObjectInspector;
    }

    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {

        // get the list from the deferred objects using the object inspectors
        List list = this.listOI.getList(arguments[0].get());

        // check for nulls
        if (list == null) return null;

        long min = Long.parseLong(list.get(0).toString());

        for(Object s: list) if ( min > Long.parseLong(s.toString())) min = Long.parseLong(s.toString());

        return min;
    }
}