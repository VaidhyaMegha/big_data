/***********************************************************************
 * PEGASUS: Peta-Scale Graph Mining System
 * Authors: U Kang, Duen Horng Chau, and Christos Faloutsos
 * <p/>
 * This software is licensed under Apache License, Version 2.0 (the  "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -------------------------------------------------------------------------
 * File: GIMV.java
 * - A main class for Generalized Iterative Matrix-Vector multiplication.
 * Version: 2.0
 ***********************************************************************/

package me.tingri.graphs.gimv.block;

import org.apache.hadoop.io.Text;

import java.util.*;

import static me.tingri.util.CONSTANTS.SPACE;

public class GIMVUtility {

    /**
     * convert strVal to array of VectorElem<Long>.
     * strVal is msu(ROW-ID VALUE)s. ex) 0 5 1 3
     */
    public static ArrayList<VectorElem<Long>> parseVectorVal(String strVal) {
        ArrayList<VectorElem<Long>> arr = new ArrayList<VectorElem<Long>>();
        final String[] tokens = strVal.split(SPACE);

        for (int i = 0; i < tokens.length; i += 2) {
            short row = Short.parseShort(tokens[i]);
            long val = Long.parseLong(tokens[i + 1]);

            arr.add(new VectorElem<Long>(row, val));
        }

        return arr;
    }

    /**
     * convert strVal to array of BlockElem<Integer>.
     * strVal is (COL-ID ROW-ID VALUE)s. ex) 0 0 1 1 0 1 1 1 1
     * note the strVal is transposed. So we should transpose it to (ROW-ID   COL-ID ...) format.
     */
    public static ArrayList<BlockElem<Integer>> parseBlockVal(String strVal) {
        ArrayList<BlockElem<Integer>> arr = new ArrayList<BlockElem<Integer>>();
        final String[] tokens = strVal.split(SPACE);

        for (int i = 0; i < tokens.length; i += 2) {
            short row = Short.parseShort(tokens[i + 1]);
            short col = Short.parseShort(tokens[i]);

            arr.add(new BlockElem<Integer>(row, col, 1));
        }

        return arr;
    }

    public static ArrayList<VectorElem<Long>> minBlockVector(ArrayList<BlockElem<Integer>> block,
                                                                ArrayList<VectorElem<Long>> vector, int block_width, boolean isFastMethod) {
        long[] out_vals = new long[block_width];

        for (short i = 0; i < block_width; i++)
            out_vals[i] = -1;

        Map<Short, Long> vector_map = new HashMap<Short, Long>();

        // initialize out_vals
        if (isFastMethod)
            for (VectorElem<Long> v_elem : vector)
                out_vals[v_elem.row] = v_elem.val;

        for (VectorElem<Long> v_elem : vector)
            vector_map.put(v_elem.row, v_elem.val);


        for (BlockElem<Integer> b_elem : block) {
            Long vector_val = vector_map.get(b_elem.col);
            if (vector_val != null && (out_vals[b_elem.row] == -1 || out_vals[b_elem.row] > vector_val))
                out_vals[b_elem.row] = vector_val;
        }

        ArrayList<VectorElem<Long>> result_vector = new ArrayList<VectorElem<Long>>();

        for (short i = 0; i < block_width; i++)
            if (out_vals[i] != -1)
                result_vector.add(new VectorElem<Long>(i, out_vals[i]));

        return result_vector.size() == 0 ? null : result_vector;
    }

    /**
     * make Text format output by combining vector elements.
     */
    public static Text formatVectorElemOutput(ArrayList<VectorElem<Long>> vector) {
        StringBuilder output = new StringBuilder();

        if (vector != null && vector.size() > 0) {
            for (VectorElem<Long> elem : vector)
                output.append(elem.row).append(SPACE).append(elem.val).append(SPACE);

            return new Text(output.toString().trim());
        } else {
            return new Text("");
        }
    }


    /**
     * compare two vectors.
     *
     * @param v1 vector 1
     * @param v2 vector 2
     * @return value : 0 (same) 1 (different)
     */
    public static int compareVectors(ArrayList<VectorElem<Long>> v1, ArrayList<VectorElem<Long>> v2) {
        if (v1.size() != v2.size())
            return 1;

        Iterator<VectorElem<Long>> v2_iter = v2.iterator();

        for (VectorElem<Long> elem1 : v1) {
            VectorElem<Long> elem2 = v2_iter.next();

            if (elem1.row != elem2.row || elem1.val.compareTo(elem2.val) != 0)
                return 1;
        }

        return 0;
    }

}

