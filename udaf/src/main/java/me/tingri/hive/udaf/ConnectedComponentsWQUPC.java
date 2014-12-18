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
import org.apache.hadoop.io.IntWritable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Description(
        name = "ConnectedComponentsList",
        value = "_FUNC_(String, String) - Computes connected components",
        extended = "select components_list(node1, node2) from edges group by attr1;"
)
public class ConnectedComponentsWQUPC extends AbstractGenericUDAFResolver {
    public static final Log LOG = LogFactory.getLog(ConnectedComponentsWQUPC.class.getName());
    private static final int ROOT = 0;
    private static final int SIZE = 1;
    private static final int MAX_FILE_SIZE=22;
    private static final int NULL_INDICATOR =0;

    public GenericUDAFEvaluator getEvaluator(GenericUDAFParameterInfo info) throws SemanticException {
        return new CCEvaluator();
    }

    public static class CCEvaluator extends GenericUDAFEvaluator {
        public static class Components implements AggregationBuffer {
            int[][] buffer = new int[MAX_FILE_SIZE][];
        }

        @Override
        public ObjectInspector init(Mode m, ObjectInspector[] parameters) throws HiveException {
            super.init(m, parameters);
            // return type goes here
            return ObjectInspectorFactory.getStandardListObjectInspector(
                    PrimitiveObjectInspectorFactory.javaIntObjectInspector);
        }

        @Override
        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            return new Components();
        }

        @Override
        public void reset(AggregationBuffer agg) throws HiveException {
            ((Components) agg).buffer = new int[MAX_FILE_SIZE][];
        }

        @Override
        public void iterate(AggregationBuffer agg, Object[] objects) throws HiveException {
            LOG.info("*********************************");
            LOG.info("Entering iterate Method");
            LOG.info("Iterate Values"+ Arrays.toString(objects));

            int node0 = Integer.valueOf(String.valueOf(objects[0]));
            int node1 = Integer.valueOf(String.valueOf(objects[1]));

            LOG.info("Node0 "+ node0 + " Node1 " + node1);

            int[][] list = ((Components) agg).buffer;

            addNodeIfNotExists(node0, list);
            addNodeIfNotExists(node1, list);

            union(node0, node1, list);

            LOG.info("Exiting iterate Method");
        }

        @Override
        public void merge(AggregationBuffer agg, Object partial) throws HiveException {
            LOG.info("*********************************");
            LOG.info("Entering merge Method");
            List partial_list = getList(partial);
            int size = partial_list.size();

            int[][] cur_list = ((Components) agg).buffer;

            LOG.info("Lists Before a Merge ************");
            LOG.info("Partial List" + partial_list.toString());
            for(int[] element: cur_list)
                LOG.info(Arrays.toString(element));


            for (int i=0; i < size; i++) {
                int par_root = ((IntWritable) partial_list.get(i)).get();

                if (par_root != NULL_INDICATOR) {
                    addNodeIfNotExists(i, cur_list);
                    addNodeIfNotExists(par_root, cur_list);

                    union(i, par_root, cur_list);
                }
            }

            LOG.info("Final List After a Merge ************");
            for(int[] element: ((Components) agg).buffer)
                LOG.info(Arrays.toString(element));

            LOG.info("Exiting merge Method");
        }

        @Override
        public Object terminatePartial(AggregationBuffer agg) throws HiveException {
            LOG.info("*********************************");
            LOG.info("Entering Terminate Partial Method");
            Object results = getResult((Components) agg);
            LOG.info("Exiting Terminate Partial Method");
            return results;
        }


        @Override
        public Object terminate(AggregationBuffer agg) throws HiveException {
            LOG.info("*********************************");
            LOG.info("Entering Terminate Method");
            Object results = getResult((Components) agg);
            LOG.info("Exiting Terminate Method");
            return results;
        }

        private void union(int node0, int node1, int[][] list) {
            int[] tree0 = findRoot(list, node0);
            int[] tree1 = findRoot(list, node1);

            // Weight the trees by their size
            // The larger tree's root becomes the root of the smaller tree
            // The larger tree's size is increased by the size of the smaller tree
            if(tree1 == tree0) return;
            else if(tree1[SIZE] >= tree0[SIZE]){
                tree1[SIZE] += tree0[SIZE];
                tree0[ROOT] = tree1[ROOT];
            } else {
                tree0[SIZE] += tree1[SIZE];
                tree1[ROOT] = tree0[ROOT];
            }
        }

        private int[] findRoot(int[][] list, int node){
            int cur_root = list[node][ROOT];

            if (cur_root == node) return list[node];
            else {
                int[] tree = findRoot(list, cur_root);

                //Compress the path by making all descendants of the root in this path
                //point to root directly.
                //At the end of this recursion, this path will be fully compressed.
                list[node] = tree;

                return tree;
            }
        }

        private Object getResult(Components agg) {
            int[][] cur_list = agg.buffer;

            Integer[] list = new Integer[MAX_FILE_SIZE];

            //copy from current list of int[] data structure to list of roots data structure.
            for (int i=0; i < MAX_FILE_SIZE; i++) {
                if(cur_list[i] != null) list[i] = findRoot(cur_list, i)[ROOT];
                else list[i] = NULL_INDICATOR;
            }

            LOG.info("Result" + Arrays.toString(list));

            return list;
        }

        private List getList(Object partial) {
            //rebuild or retrieve the data structure interface from the serialization lazy wrapper.
            if (null == partial) return new ArrayList();
            else if (partial instanceof List) return (List) partial;
            else return ((LazyBinaryArray) partial).getList();
        }

        private void addNodeIfNotExists(int node, int[][] list) {
            if(list[node] == null) {
                LOG.info("Node " + node + " does not exist yet");
                list[node] = new int[]{node, 1};
            }
        }
    }
}