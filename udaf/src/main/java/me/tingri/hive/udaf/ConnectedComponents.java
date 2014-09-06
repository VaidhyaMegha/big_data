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

import java.util.HashSet;

@Description(
        name = "ConnectedComponents",
        value = "_FUNC_(String, String) - Computes connected components",
        extended = "select ConnectedComponents(node1, node2) from edges group by attr1;"
)
public class ConnectedComponents extends AbstractGenericUDAFResolver {
    static final Log LOG = LogFactory.getLog(ConnectedComponents.class.getName());

    public GenericUDAFEvaluator getEvaluator(GenericUDAFParameterInfo info) throws SemanticException {
        return new CCEvaluator();
    }

    public static class CCEvaluator extends GenericUDAFEvaluator {
        private SetOfSets ccs = null;

        public static class SetOfSets {
            HashSet<HashSet<Integer>> setOfSets = new HashSet<HashSet<Integer>>();
        }

        /**
         * function: Constructor
         */
        public CCEvaluator() {
            super();
            init();
        }

        @Override
        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            return null;
        }

        @Override
        public void reset(AggregationBuffer aggregationBuffer) throws HiveException {

        }

        @Override
        public void iterate(AggregationBuffer aggregationBuffer, Object[] objects) throws HiveException {

        }

        @Override
        public Object terminatePartial(AggregationBuffer aggregationBuffer) throws HiveException {
            return null;
        }

        @Override
        public void merge(AggregationBuffer aggregationBuffer, Object o) throws HiveException {

        }

        @Override
        public Object terminate(AggregationBuffer aggregationBuffer) throws HiveException {
            return null;
        }

        /**
         * function: init()
         * Its called before records pertaining to a new group are streamed
         */
        public void init() {
        }
    }
}