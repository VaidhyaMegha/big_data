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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
        private SetOfSets ccs = null;

        public static class SetOfSets implements AggregationBuffer {
            Set<HashSet> buffer = new HashSet<HashSet>();
        }

        public CCEvaluator() {
            super();
            init();
        }

        public void init() {
            ccs = new SetOfSets();
        }

        @Override
        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            init();
            return ccs;
        }

        @Override
        public void reset(AggregationBuffer aggregationBuffer) throws HiveException {
            ((SetOfSets)aggregationBuffer).buffer = new HashSet<HashSet>();
        }

        @Override
        public void iterate(AggregationBuffer aggregationBuffer, Object[] objects) throws HiveException {
            System.out.println(Arrays.toString(objects));

            Iterator<HashSet> iter = ccs.buffer.iterator();

            for(Set s = iter.next();iter.hasNext(); s = iter.next()){
                if(s.contains(objects[0])){
                    s.add(objects[1]);
                    return;
                } else if(s.contains(objects[1])){
                    s.add(objects[0]);
                    return;
                }
            }

            HashSet set = new HashSet();
            set.add(objects[0]);
            set.add(objects[1]);

            ccs.buffer.add(set);
        }

        @Override
        public Object terminatePartial(AggregationBuffer aggregationBuffer) throws HiveException {
            return ccs;
        }

        @Override
        public void merge(AggregationBuffer aggregationBuffer, Object partial) throws HiveException {
            Iterator<HashSet> iter1 = ((SetOfSets)partial).buffer.iterator();

            for(Set s1 = iter1.next();iter1.hasNext(); s1 = iter1.next()){
                Iterator<HashSet> iter2 = ((SetOfSets)aggregationBuffer).buffer.iterator();
                boolean intersect = false;

                second:
                for(Set s2 = iter2.next();iter2.hasNext(); s2 = iter2.next()){
                    Iterator objIter = s2.iterator();

                    for(Object obj = objIter.next();objIter.hasNext(); obj = objIter.next()) {
                        if (s1.contains(obj)) {
                            s2.addAll(s1);
                            intersect = true;
                            break second;
                        }
                    }
                }

                if(!intersect) ((SetOfSets) aggregationBuffer).buffer.add((HashSet)s1);
            }
        }

        @Override
        public Object terminate(AggregationBuffer aggregationBuffer) throws HiveException {
            return ccs;
        }
    }
}