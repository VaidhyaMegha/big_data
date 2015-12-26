package me.tingri.util;

/**
 * Created by sandeep on 12/23/15.
 */
public enum FLAGS{
    YES(1),
    CHANGED(1),
    UNCHANGED(0),
    NO(0);


    private final int value;

    public int getValue() {
        return value;
    }


    FLAGS(int value) {
        this.value = value;
    }
}
