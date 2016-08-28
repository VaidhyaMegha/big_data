package me.tingri.big_data.util;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by sandeep on 4/23/16.
 */
public class TimeIt {

    public static <T> T time(Callable<T> task, List<Double> timings) {
        T returnValue = null;
        try {
            long startTime = System.currentTimeMillis();
            returnValue = task.call();
            timings.add((System.currentTimeMillis() - startTime) / 1000d);
        } catch (Exception e) {
            //...
        }
        return returnValue;
    }

    public static <T> T time(Callable<T> task) {
        T returnValue = null;
        try {
            long startTime = System.currentTimeMillis();
            returnValue = task.call();
            System.out.println("Time taken is " + (System.currentTimeMillis() - startTime) / 1000d);
        } catch (Exception e) {
            //...
        }
        return returnValue;
    }
}
