package me.tingri.graphs.gimv.block;

/**
 * Created by sandeep on 2/1/16.
 */
public class VectorElem<T> {
    public short row;
    public T val;

    public VectorElem(short in_row, T in_val) {
        row = in_row;
        val = in_val;
    }
}
