package test.me.tingri.qcf;

import me.tingri.qcf.QCF;
import org.junit.Test;

/**
 * Created by sandeepkunkunuru on 5/15/16.
 */
public class QCFTest {

    /**
     * 22K  -- access.log.4.gz
     * 225K -- access.log.4
     * @throws Exception
     */
    @Test
    public void runSmall() throws Exception {
        double diff = new QCF().run(new String[]{
                "-inputFile","/home/sandeepkunkunuru/projects/big_data/datasets/tingri/access.log.4.gz",
                "-outputFile","/tmp/access.log",
                "-numOfTrials", "1000",
                "-regExp", "bot"});

        System.out.println("Time diff uncompressed - compressed : " + diff);
    }

    /**
     * 147K -- access.log.6.gz
     * 2.3M -- access.log.6
     * @throws Exception
     */
    @Test
    public void runMedium() throws Exception {
        double diff = new QCF().run(new String[]{
                "-inputFile","/home/sandeepkunkunuru/projects/big_data/datasets/tingri/access.log.6.gz",
                "-outputFile","/tmp/access.log",
                "-numOfTrials", "100",
                "-regExp", "bot"});

        System.out.println("Average Time diff uncompressed - compressed : " + diff);
    }

    /**
     * 34M -- /home/sandeepkunkunuru/Downloads/weather_data/weather_data.gz
     * 229M -- /home/sandeepkunkunuru/Downloads/weather_data
     * @throws Exception
     */
    @Test
    public void runLarge() throws Exception {
        double diff = new QCF().run(new String[]{
                "-inputFile","/home/sandeepkunkunuru/Downloads/weather_data.gz",
                "-outputFile","/tmp/weather_data",
                "-numOfTrials", "10",
                "-regExp", "DAY365"});

        System.out.println("Average Time diff uncompressed - compressed : " + diff);
    }

}