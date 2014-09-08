package me.tingri.hive.udaf;
/**
 * Created by sandeep on 9/5/14.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFParameterInfo;
import org.apache.hadoop.hive.serde2.lazybinary.LazyBinaryArray;
import org.apache.hadoop.hive.serde2.lazybinary.LazyBinaryMap;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.Text;

import java.util.*;

@Description(
        name = "ConnectedComponents",
        value = "_FUNC_(String, String) - Computes connected components",
        extended = "select ConnectedComponents(node1, node2) from edges group by attr1;"
)
public class ConnectedComponents extends AbstractGenericUDAFResolver {
    public static final Log LOG = LogFactory.getLog(ConnectedComponents.class.getName());

    public GenericUDAFEvaluator getEvaluator(GenericUDAFParameterInfo info) throws SemanticException {
        return new CCEvaluator();
    }

    public static class CCEvaluator extends GenericUDAFEvaluator {
        public static class Components implements AggregationBuffer {
            Map<Text, List> buffer = new HashMap<Text, List>();
        }

        @Override
        public ObjectInspector init(Mode m, ObjectInspector[] parameters) throws HiveException {
            super.init(m, parameters);
            // return type goes here
            return ObjectInspectorFactory.getStandardMapObjectInspector(PrimitiveObjectInspectorFactory.writableStringObjectInspector,
                    ObjectInspectorFactory.getStandardListObjectInspector(PrimitiveObjectInspectorFactory.writableStringObjectInspector));
        }

        @Override
        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            return new Components();
        }

        @Override
        public void reset(AggregationBuffer agg) throws HiveException {
            ((Components) agg).buffer = new HashMap<Text, List>();
        }

        @Override
        public void iterate(AggregationBuffer agg, Object[] objects) throws HiveException {
            Text s0 = new Text(String.valueOf(objects[0]));
            Text s1 = new Text(String.valueOf(objects[1]));

            Map<Text, List> map = ((Components) agg).buffer;

            if (map.containsKey(s0) && !map.containsKey(s1)) {
                addDistinct(map.get(s0), s1);
                map.put(s1, map.get(s0));
                return;
            } else if (map.containsKey(s1) && !map.containsKey(s0)) {
                addDistinct(map.get(s1), s0);
                map.put(s0, map.get(s1));
                return;
            } else if (!map.containsKey(s1) && !map.containsKey(s0)){
                List set = new ArrayList();

                set.add(s0);
                set.add(s1);

                map.put(s0, set);
                map.put(s1, set);
            }
        }

        @Override
        public Object terminatePartial(AggregationBuffer agg) throws HiveException {
            return ((Components) agg).buffer;
        }

        @Override
        public void merge(AggregationBuffer agg, Object partial) throws HiveException {
            Map<Text, Object> map1 = getMap(partial);
            Set<Text> set1 = map1.keySet();

            for (Text key1 : set1) {
                List list1 = getList(map1, key1);

                Map<Text, List> map2 = ((Components) agg).buffer;

                if(map2.containsKey(key1)){
                    addDistinct(map2.get(key1), list1);
                } else {
                    map2.put(key1, list1);
                }
            }
        }

        @Override
        public Object terminate(AggregationBuffer agg) throws HiveException {
            return ((Components) agg).buffer;
        }

        private Map getMap(Object partial) {
            return ((LazyBinaryMap) partial).getMap();
        }

        private List getList(Map<Text, Object> map1, Text key1) {
            return ((LazyBinaryArray) map1.get(key1)).getList();
        }

        private void addDistinct(List s2, List s1) {
            for (Object obj : s1) addDistinct(s2, (Text)obj);
        }

        private void addDistinct(List s, Text obj) {
            if (!s.contains(obj)) s.add(obj);
        }
    }
}