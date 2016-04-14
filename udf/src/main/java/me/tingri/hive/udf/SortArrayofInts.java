package me.tingri.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortArrayofInts extends GenericUDF {
    ListObjectInspector listOI;

    @Override
    public String getDisplayString(String[] arg0) {
        return "SortArrayofInts(array,indexOfHeader)";
    }

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        if (arguments.length != 1)
            throw new UDFArgumentLengthException("SortArrayofInts only takes 1 arguments: List<T>");

        ObjectInspector a = arguments[0];

        if (!(a instanceof ListObjectInspector) )
            throw new UDFArgumentException("first argument must be a list / array");

        this.listOI = (ListObjectInspector) a;

        return ObjectInspectorFactory.getStandardListObjectInspector(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
    }

    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {

        // get the list from the deferred objects using the object inspectors
        List<String> list = (List<String>) this.listOI.getList(arguments[0].get());

        // check for nulls
        if (list == null) return null;

        Collections.sort(list, new CustomComparator<String>());

        return list;
    }

    class CustomComparator<T> implements Comparator<T> {

        @Override
        public int compare(T str1, T str2) {
            long value = Long.parseLong(str1.toString())- Long.parseLong(str2.toString());

            return value > 0 ? 1 : value == 0 ? 0 : -1;
        }
    }
}