package test.me.tingri.graphs.gimv;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.types.Pair;

import java.util.ArrayList;
import java.util.List;

import static me.tingri.util.CONSTANTS.*;

/**
 * Created by sandeep on 12/27/15.
 */
public class TESTDATA {
    public static final String DEFAULT_BLOCK_WIDTH = "2";


    public static List<Pair<LongWritable, Text>> getVectorGeneratorMapperInput() {
        List<Pair<LongWritable, Text>> list = new ArrayList<Pair<LongWritable, Text>>();

        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text("1" + DEFAULT_FIELD_SEPARATOR + "2")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text("2" + DEFAULT_FIELD_SEPARATOR + "5")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text("3" + DEFAULT_FIELD_SEPARATOR + "4")));

        return list;
    }

    public static List<Pair<LongWritable, Text>> getVectorGeneratorMapperOutput() {
        List<Pair<LongWritable, Text>> list = new ArrayList<Pair<LongWritable, Text>>();

        list.add(new Pair<LongWritable, Text>(new LongWritable(2), new Text("1")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(1), new Text("2")));

        list.add(new Pair<LongWritable, Text>(new LongWritable(5), new Text("2")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(2), new Text("5")));

        list.add(new Pair<LongWritable, Text>(new LongWritable(4), new Text("3")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(3), new Text("4")));

        return list;
    }

    public static List<Pair<LongWritable, List<Text>>> getVectorGeneratorReducerInput() {
        List<Pair<LongWritable, List<Text>>> list = new ArrayList<Pair<LongWritable, List<Text>>>();
        List<Text> key_1 = new ArrayList<Text>();

        key_1.add(new Text("2"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(1), key_1));

        List<Text> key_2 = new ArrayList<Text>();

        key_2.add(new Text("1"));
        key_2.add(new Text("5"));
        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(2), key_2));

        List<Text> key_3 = new ArrayList<Text>();

        key_3.add(new Text("4"));
        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(3), key_3));

        List<Text> key_4 = new ArrayList<Text>();

        key_4.add(new Text("3"));
        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(4), key_4));

        List<Text> key_5 = new ArrayList<Text>();

        key_5.add(new Text("2"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(5), key_5));

        return list;
    }


    public static List<Pair<LongWritable, Text>> getVectorGeneratorReducerOutput() {

        //same as first round result after a join and merge which have been fed the input file directly
        return getMergeReducerOutput();
    }

    public static List<Pair<LongWritable, Text>> getJoinMapperInput() {
        List<Pair<LongWritable, Text>> list = new ArrayList<Pair<LongWritable, Text>>();

        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text("1" + DEFAULT_FIELD_SEPARATOR + "2")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text("2" + DEFAULT_FIELD_SEPARATOR + "5")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text("3" + DEFAULT_FIELD_SEPARATOR + "4")));

        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text("1" + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "1")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text("2" + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "2")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text("3" + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "3")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text("4" + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "4")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text("5" + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "5")));

        return list;
    }

    public static List<Pair<LongWritable, Text>> getJoinMapperOutput() {
        List<Pair<LongWritable, Text>> list = new ArrayList<Pair<LongWritable, Text>>();

        list.add(new Pair<LongWritable, Text>(new LongWritable(2), new Text("1")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(1), new Text("2")));

        list.add(new Pair<LongWritable, Text>(new LongWritable(5), new Text("2")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(2), new Text("5")));

        list.add(new Pair<LongWritable, Text>(new LongWritable(4), new Text("3")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(3), new Text("4")));

        list.add(new Pair<LongWritable, Text>(new LongWritable(1), new Text(DEFAULT_VECTOR_INDICATOR + "1")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(2), new Text(DEFAULT_VECTOR_INDICATOR + "2")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(3), new Text(DEFAULT_VECTOR_INDICATOR + "3")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(4), new Text(DEFAULT_VECTOR_INDICATOR + "4")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(5), new Text(DEFAULT_VECTOR_INDICATOR + "5")));

        return list;
    }

    public static List<Pair<LongWritable, List<Text>>> getJoinReducerInput() {
        List<Pair<LongWritable, List<Text>>> list = new ArrayList<Pair<LongWritable, List<Text>>>();
        List<Text> key_1 = new ArrayList<Text>();

        key_1.add(new Text("2"));
        key_1.add(new Text(DEFAULT_VECTOR_INDICATOR + "1"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(1), key_1));

        List<Text> key_2 = new ArrayList<Text>();

        key_2.add(new Text("1"));
        key_2.add(new Text("5"));
        key_2.add(new Text(DEFAULT_VECTOR_INDICATOR + "2"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(2), key_2));

        List<Text> key_3 = new ArrayList<Text>();

        key_3.add(new Text("4"));
        key_3.add(new Text(DEFAULT_VECTOR_INDICATOR + "3"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(3), key_3));

        List<Text> key_4 = new ArrayList<Text>();

        key_4.add(new Text("3"));
        key_4.add(new Text(DEFAULT_VECTOR_INDICATOR + "4"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(4), key_4));

        List<Text> key_5 = new ArrayList<Text>();

        key_5.add(new Text("2"));
        key_5.add(new Text(DEFAULT_VECTOR_INDICATOR + "5"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(5), key_5));

        return list;
    }


    public static List<Pair<LongWritable, Text>> getJoinReducerOutput() {
        List<Pair<LongWritable, Text>> list = new ArrayList<Pair<LongWritable, Text>>();

        list.add(new Pair<LongWritable, Text>(new LongWritable(1), new Text(DEFAULT_VECTOR_INDICATOR + "1")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(2), new Text(DEFAULT_VECTOR_INDICATOR + "1")));

        list.add(new Pair<LongWritable, Text>(new LongWritable(1), new Text(DEFAULT_VECTOR_INDICATOR + "2")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(5), new Text(DEFAULT_VECTOR_INDICATOR + "2")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(2), new Text(DEFAULT_VECTOR_INDICATOR + "2")));

        list.add(new Pair<LongWritable, Text>(new LongWritable(4), new Text(DEFAULT_VECTOR_INDICATOR + "3")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(3), new Text(DEFAULT_VECTOR_INDICATOR + "3")));

        list.add(new Pair<LongWritable, Text>(new LongWritable(3), new Text(DEFAULT_VECTOR_INDICATOR + "4")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(4), new Text(DEFAULT_VECTOR_INDICATOR + "4")));

        list.add(new Pair<LongWritable, Text>(new LongWritable(2), new Text(DEFAULT_VECTOR_INDICATOR + "5")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(5), new Text(DEFAULT_VECTOR_INDICATOR + "5")));

        return list;
    }

    public static List<Pair<LongWritable, Text>> getMergeMapperInput() {
        List<Pair<LongWritable, Text>> list = new ArrayList<Pair<LongWritable, Text>>();

        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(1 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "1")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(2 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "1")));

        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(1 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "2")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(5 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "2")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(2 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "2")));

        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(4 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "3")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(3 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "3")));

        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(3 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "4")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(4 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "4")));

        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(2 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "5")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(5 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "5")));

        return list;
    }

    public static List<Pair<LongWritable, List<Text>>> getMergeReducerInput() {
        List<Pair<LongWritable, List<Text>>> list = new ArrayList<Pair<LongWritable, List<Text>>>();
        List<Text> key_1 = new ArrayList<Text>();

        key_1.add(new Text(DEFAULT_VECTOR_INDICATOR + "1"));
        key_1.add(new Text(DEFAULT_VECTOR_INDICATOR + "2"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(1), key_1));

        List<Text> key_2 = new ArrayList<Text>();

        key_2.add(new Text(DEFAULT_VECTOR_INDICATOR + "1"));
        key_2.add(new Text(DEFAULT_VECTOR_INDICATOR + "2"));
        key_2.add(new Text(DEFAULT_VECTOR_INDICATOR + "5"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(2), key_2));

        List<Text> key_3 = new ArrayList<Text>();

        key_3.add(new Text(DEFAULT_VECTOR_INDICATOR + "3"));
        key_3.add(new Text(DEFAULT_VECTOR_INDICATOR + "4"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(3), key_3));

        List<Text> key_4 = new ArrayList<Text>();

        key_4.add(new Text(DEFAULT_VECTOR_INDICATOR + "3"));
        key_4.add(new Text(DEFAULT_VECTOR_INDICATOR + "4"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(4), key_4));

        List<Text> key_5 = new ArrayList<Text>();

        key_5.add(new Text(DEFAULT_VECTOR_INDICATOR + "2"));
        key_5.add(new Text(DEFAULT_VECTOR_INDICATOR + "5"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(5), key_5));

        return list;
    }

    public static List<Pair<LongWritable, Text>> getMergeReducerOutput() {
        List<Pair<LongWritable, Text>> list = new ArrayList<Pair<LongWritable, Text>>();

        list.add(new Pair<LongWritable, Text>(new LongWritable(1), new Text(DEFAULT_VECTOR_INDICATOR + "1")));

        list.add(new Pair<LongWritable, Text>(new LongWritable(2), new Text(DEFAULT_VECTOR_INDICATOR + "1")));

        list.add(new Pair<LongWritable, Text>(new LongWritable(3), new Text(DEFAULT_VECTOR_INDICATOR + "3")));

        list.add(new Pair<LongWritable, Text>(new LongWritable(4), new Text(DEFAULT_VECTOR_INDICATOR + "3")));

        list.add(new Pair<LongWritable, Text>(new LongWritable(5), new Text(DEFAULT_VECTOR_INDICATOR + "2")));

        return list;
    }

    public static List<Pair<LongWritable, Text>> getStateCheckMapperInput() {
        List<Pair<LongWritable, Text>> list = new ArrayList<Pair<LongWritable, Text>>();

        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(1 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "1")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(2 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "2")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(3 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "3")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(4 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "4")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(5 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "5")));


        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(1 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "1")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(2 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "1")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(3 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "3")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(4 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "3")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text(5 + DEFAULT_FIELD_SEPARATOR + DEFAULT_VECTOR_INDICATOR + "2")));

        return list;
    }

    public static List<Pair<LongWritable, Text>> getStateCheckerMapperOutput() {
        List<Pair<LongWritable, Text>> list = new ArrayList<Pair<LongWritable, Text>>();

        list.add(new Pair<LongWritable, Text>(new LongWritable(1), new Text(DEFAULT_VECTOR_INDICATOR + "1")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(2), new Text(DEFAULT_VECTOR_INDICATOR + "2")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(3), new Text(DEFAULT_VECTOR_INDICATOR + "3")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(4), new Text(DEFAULT_VECTOR_INDICATOR + "4")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(5), new Text(DEFAULT_VECTOR_INDICATOR + "5")));


        list.add(new Pair<LongWritable, Text>(new LongWritable(1), new Text(DEFAULT_VECTOR_INDICATOR + "1")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(2), new Text(DEFAULT_VECTOR_INDICATOR + "1")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(3), new Text(DEFAULT_VECTOR_INDICATOR + "3")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(4), new Text(DEFAULT_VECTOR_INDICATOR + "3")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(5), new Text(DEFAULT_VECTOR_INDICATOR + "2")));

        return list;
    }

    public static List<Pair<LongWritable, List<Text>>> getStateCheckReducerInput() {
        List<Pair<LongWritable, List<Text>>> list = new ArrayList<Pair<LongWritable, List<Text>>>();
        List<Text> key_1 = new ArrayList<Text>();

        key_1.add(new Text(DEFAULT_VECTOR_INDICATOR + "1"));
        key_1.add(new Text(DEFAULT_VECTOR_INDICATOR + "1"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(1), key_1));

        List<Text> key_2 = new ArrayList<Text>();

        key_2.add(new Text(DEFAULT_VECTOR_INDICATOR + "1"));
        key_2.add(new Text(DEFAULT_VECTOR_INDICATOR + "2"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(2), key_2));

        List<Text> key_3 = new ArrayList<Text>();

        key_3.add(new Text(DEFAULT_VECTOR_INDICATOR + "3"));
        key_3.add(new Text(DEFAULT_VECTOR_INDICATOR + "3"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(3), key_3));

        List<Text> key_4 = new ArrayList<Text>();

        key_4.add(new Text(DEFAULT_VECTOR_INDICATOR + "3"));
        key_4.add(new Text(DEFAULT_VECTOR_INDICATOR + "4"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(4), key_4));

        List<Text> key_5 = new ArrayList<Text>();

        key_5.add(new Text(DEFAULT_VECTOR_INDICATOR + "2"));
        key_5.add(new Text(DEFAULT_VECTOR_INDICATOR + "5"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(5), key_5));

        return list;
    }

    public static List<Pair<LongWritable, Text>> getStateCheckReducerOutput() {
        List<Pair<LongWritable, Text>> list = new ArrayList<Pair<LongWritable, Text>>();

        list.add(new Pair<LongWritable, Text>(new LongWritable(2), new Text(DEFAULT_VECTOR_INDICATOR + "1")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(4), new Text(DEFAULT_VECTOR_INDICATOR + "3")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(5), new Text(DEFAULT_VECTOR_INDICATOR + "2")));

        return list;
    }

    public static List<Pair<LongWritable, Text>> getBlockJoinMapperOutput() {
        List<Pair<LongWritable, Text>> list = new ArrayList<Pair<LongWritable, Text>>();

        list.add(new Pair<LongWritable, Text>(new LongWritable(1), new Text("0" + DEFAULT_FIELD_SEPARATOR + "0" + SPACE + "1" )));
        list.add(new Pair<LongWritable, Text>(new LongWritable(0), new Text("1" + DEFAULT_FIELD_SEPARATOR + "1" + SPACE + "0" )));

        list.add(new Pair<LongWritable, Text>(new LongWritable(2), new Text("1" + DEFAULT_FIELD_SEPARATOR + "1" + SPACE + "0" )));
        list.add(new Pair<LongWritable, Text>(new LongWritable(1), new Text("2" + DEFAULT_FIELD_SEPARATOR + "0" + SPACE + "1" )));

        list.add(new Pair<LongWritable, Text>(new LongWritable(2), new Text("1" + DEFAULT_FIELD_SEPARATOR + "0" + SPACE + "1" )));
        list.add(new Pair<LongWritable, Text>(new LongWritable(1), new Text("2" + DEFAULT_FIELD_SEPARATOR + "1" + SPACE + "0" )));

        list.add(new Pair<LongWritable, Text>(new LongWritable(0), new Text("1" + SPACE + "1")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(1), new Text("0" + SPACE + "2")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(1), new Text("1" + SPACE + "3")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(2), new Text("0" + SPACE + "4")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(2), new Text("1" + SPACE + "5")));

        return list;
    }

    public static List<Pair<LongWritable, List<Text>>> getBlockJoinReducerInput() {
        List<Pair<LongWritable, List<Text>>> list = new ArrayList<Pair<LongWritable, List<Text>>>();
        List<Text> list0 = new ArrayList<Text>();

        list0.add(new Text("1" + SPACE + "1"));
        list0.add(new Text("1" + DEFAULT_FIELD_SEPARATOR + "1" + SPACE + "0" ));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(0), list0));

        List<Text> list1 = new ArrayList<Text>();
        list1.add(new Text("0" + DEFAULT_FIELD_SEPARATOR + "0" + SPACE + "1" ));
        list1.add(new Text("2" + DEFAULT_FIELD_SEPARATOR + "0" + SPACE + "1" ));
        list1.add(new Text("2" + DEFAULT_FIELD_SEPARATOR + "1" + SPACE + "0" ));
        list1.add(new Text("0" + SPACE + "2"));
        list1.add(new Text("1" + SPACE + "3"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(1), list1));

        List<Text> list2 = new ArrayList<Text>();

        list2.add(new Text("1" + DEFAULT_FIELD_SEPARATOR + "1" + SPACE + "0" ));
        list2.add(new Text("1" + DEFAULT_FIELD_SEPARATOR + "0" + SPACE + "1" ));
        list2.add(new Text("0" + SPACE + "4"));
        list2.add(new Text("1" + SPACE + "5"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(2), list2));

        return list;
    }

    public static List<Pair<LongWritable, Text>> getBlockJoinReducerOutput() {
        List<Pair<LongWritable, Text>> list = new ArrayList<Pair<LongWritable, Text>>();

        list.add(new Pair<LongWritable, Text>(new LongWritable(0), new Text("1" + SPACE + "2")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(1), new Text("0" + SPACE + "1")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(1), new Text("0" + SPACE + "5" + SPACE + "1" + SPACE + "4")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(2), new Text("0" + SPACE + "3" + SPACE + "1" + SPACE + "2")));

        return list;
    }

    public static List<Pair<LongWritable, Text>> getBlockMergeMapperInput() {
        List<Pair<LongWritable, Text>> list = new ArrayList<Pair<LongWritable, Text>>();

        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text("0" + DEFAULT_FIELD_SEPARATOR + "1" + SPACE + "2")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text("1" + DEFAULT_FIELD_SEPARATOR + "0" + SPACE + "1")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text("1" + DEFAULT_FIELD_SEPARATOR + "0" + SPACE + "5" + SPACE + "1" + SPACE + "4")));
        list.add(new Pair<LongWritable, Text>(new LongWritable(), new Text("2" + DEFAULT_FIELD_SEPARATOR + "0" + SPACE + "3" + SPACE + "1" + SPACE + "2")));

        return list;
    }

    public static List<Pair<LongWritable, Text>> getBlockMergeMapperOutput() {
        return getBlockJoinReducerOutput();
    }

    public static List<Pair<LongWritable, List<Text>>> getBlockMergeReducerInput() {
        List<Pair<LongWritable, List<Text>>> list = new ArrayList<Pair<LongWritable, List<Text>>>();
        List<Text> list0 = new ArrayList<Text>();

        list0.add(new Text("1" + SPACE + "2"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(0), list0));

        List<Text> list1 = new ArrayList<Text>();
        list1.add(new Text("0" + SPACE + "1"));
        list1.add(new Text("0" + SPACE + "5" + SPACE + "1" + SPACE + "4"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(1), list1));

        List<Text> list2 = new ArrayList<Text>();

        list2.add(new Text("0" + SPACE + "3" + SPACE + "1" + SPACE + "2"));

        list.add(new Pair<LongWritable, List<Text>>(new LongWritable(2), list2));

        return list;
    }

    public static List<Pair<LongWritable, Text>> getBlockMergeReducerOutput() {
        return getMergeReducerOutput();
    }
}
