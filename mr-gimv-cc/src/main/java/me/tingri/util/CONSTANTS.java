package me.tingri.util;


import org.apache.hadoop.fs.Path;

/**
 * Created by sandeep on 12/23/15.
 */
public class CONSTANTS {
    public static final String MAKE_SYMMETRIC = "makesym";
    public static final String VECTOR_INDICATOR = "VECTOR_INDICATOR";
    public static final String FIELD_SEPARATOR = "FIELD_SEPARATOR";

    public static final String DEFAULT_FIELD_SEPARATOR = "\t";
    public static final String DEFAULT_VECTOR_INDICATOR = "v";
    public static final String DEFAULT_MAKE_SYMMETRIC = "1" ;

    public static final Path DEFAULT_EDGE_PATH = new Path("/edge");
    public static final Path DEFAULT_VECTOR_PATH = new Path("/vector") ;
    public static final Path DEFAULT_TEMP_VECTOR_PATH = new Path("/temp_join_output") ;

    public static final int MAX_ITERATIONS = 15;
}
