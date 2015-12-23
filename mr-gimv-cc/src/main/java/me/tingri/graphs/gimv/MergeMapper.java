package me.tingri.graphs.gimv;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.IdentityMapper;

/**
 * Created by sandeep on 12/23/15.
 */
public class MergeMapper<K1, V1, K2, V2> extends IdentityMapper<LongWritable, Text> {

}
