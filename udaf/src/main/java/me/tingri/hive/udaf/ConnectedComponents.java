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
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.ArrayList;
import java.util.List;

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
            List<List> buffer = new ArrayList<List>();
        }

        @Override
        public ObjectInspector init(Mode m, ObjectInspector[] parameters) throws HiveException {
            super.init(m, parameters);
            // return type goes here
            return ObjectInspectorFactory.getStandardListObjectInspector(
                    ObjectInspectorFactory.getStandardListObjectInspector(PrimitiveObjectInspectorFactory.javaStringObjectInspector));
        }

        @Override
        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            return new Components();
        }

        @Override
        public void reset(AggregationBuffer agg) throws HiveException {
            ((Components) agg).buffer = new ArrayList<List>();
        }

        @Override
        public void iterate(AggregationBuffer agg, Object[] objects) throws HiveException {
            for (List s : ((Components) agg).buffer) {
                if (s.contains(objects[0])) {
                    addDistinct(s, objects[1]);
                    return;
                } else if (s.contains(objects[1])) {
                    addDistinct(s, objects[0]);
                    return;
                }
            }

            List set = new ArrayList();
            set.add(objects[0]);
            set.add(objects[1]);

            ((Components) agg).buffer.add(set);
        }

        @Override
        public Object terminatePartial(AggregationBuffer agg) throws HiveException {
            return ((Components) agg).buffer;
        }

        @Override
        public void merge(AggregationBuffer agg, Object partial) throws HiveException {
            List list = getList(partial);

            for (Object s : list) {
                List s1 = getList(s);

                boolean intersect = false;
                second:
                for (List s2 : ((Components) agg).buffer) {
                    for (Object obj : s2) {
                        if (s1.contains(obj)) {
                            addDistinct(s2, s1);
                            intersect = true;
                            break second;
                        }
                    }
                }

                if (!intersect) ((Components) agg).buffer.add(s1);
            }
        }

        private List getList(Object partial) {
            List list;
            if (!(partial instanceof List)) {
                list = ((LazyBinaryArray) partial).getList();
            } else {
                list = (List) partial;
            }
            return list;
        }

        private void addDistinct(List s2, List s1) {
            for (Object obj : s1) addDistinct(s2, obj);
        }

        private void addDistinct(List s, Object obj) {
            if (!s.contains(obj)) s.add(obj);
        }

        @Override
        public Object terminate(AggregationBuffer agg) throws HiveException {
            return ((Components) agg).buffer;
        }
    }
}