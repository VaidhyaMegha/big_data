package me.tingri.hive.udaf;

import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testSetOfSets() throws Exception {
          assert new ConnectedComponents.CCEvaluator.SetOfSets() != null;

    }
}