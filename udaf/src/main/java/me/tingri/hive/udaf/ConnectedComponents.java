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
        private Components ccs = null;

        public static class Components implements AggregationBuffer {
            List<List> buffer = new ArrayList<List>();
        }

        public CCEvaluator() {
            super();
            init();
        }

        @Override
        public ObjectInspector init(Mode m, ObjectInspector[] parameters) throws HiveException {
            super.init(m, parameters);
            // return type goes here
            return ObjectInspectorFactory.getStandardListObjectInspector(
                    ObjectInspectorFactory.getStandardListObjectInspector(PrimitiveObjectInspectorFactory.javaStringObjectInspector));
        }

        public void init() {
            ccs = new Components();
        }

        @Override
        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            init();
            return ccs;
        }

        @Override
        public void reset(AggregationBuffer aggregationBuffer) throws HiveException {
            ((Components) aggregationBuffer).buffer = new ArrayList<List>();
        }

        @Override
        public void iterate(AggregationBuffer aggregationBuffer, Object[] objects) throws HiveException {
            for (List s : ((Components)aggregationBuffer).buffer) {
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

            ((Components)aggregationBuffer).buffer.add(set);
        }

        @Override
        public Object terminatePartial(AggregationBuffer aggregationBuffer) throws HiveException {
            return ((Components)aggregationBuffer).buffer;
        }

        @Override
        public void merge(AggregationBuffer aggregationBuffer, Object partial) throws HiveException {
            for (Object s: ((LazyBinaryArray) partial).getList()) {
                List s1 = ((LazyBinaryArray) s).getList();
                boolean intersect = false;
                second:
                for (List s2 :((Components) aggregationBuffer).buffer) {
                    for (Object obj : s2) {
                        if (s1.contains(obj)) {
                            addDistinct(s2, s1);
                            intersect = true;
                            break second;
                        }
                    }
                }

                if (!intersect) ((Components) aggregationBuffer).buffer.add(s1);
            }
        }

        private void addDistinct(List s2, List s1) {
            for(Object obj : s1) addDistinct(s2, obj);
        }

        private void addDistinct(List s, Object obj) {
            if (!s.contains(obj)) s.add(obj);
        }

        @Override
        public Object terminate(AggregationBuffer aggregationBuffer) throws HiveException {
            return ((Components) aggregationBuffer).buffer;
        }
    }
}