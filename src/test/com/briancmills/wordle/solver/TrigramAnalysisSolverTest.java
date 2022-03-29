package com.briancmills.wordle.solver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TrigramAnalysisSolverTest {

    TrigramAnalysisSolver solver;

    @BeforeEach
    void setUp() {
        solver = new TrigramAnalysisSolver();
    }

    @Test
    void testCalculateTrigramFrequency() {
        List<String> trigramTestList = new ArrayList<>();
        trigramTestList.add("map");
        trigramTestList.add("map");
        trigramTestList.add("map");
        trigramTestList.add("map");
        trigramTestList.add("mat");
        trigramTestList.add("mar");
        trigramTestList.add("ple");
        trigramTestList.add("ple");
        Map<String,Integer> result = solver.calculateTrigramFrequency(trigramTestList);
        assert(result.get("map").equals(4));
        assert(result.get("mar").equals(1));
        assert(result.get("foo") == null);
    }

    @Test
    void testFrequencyValue() {
        List<String> trigramTestList = new ArrayList<>();
        trigramTestList.add("mar");
        trigramTestList.add("ple");
        trigramTestList.add("mar");
        trigramTestList.add("ple");
        solver.initialize(Arrays.asList("march", "cares"));
        int result = solver.getTrigramFrequencyValue(trigramTestList);
        assert(result == 2);
    }

    @Test
    void testComparator() {
        List<String> trigramTestList = new ArrayList<>();
        trigramTestList.add("map");
        trigramTestList.add("map");
        trigramTestList.add("map");
        trigramTestList.add("map");
        trigramTestList.add("mat");
        trigramTestList.add("mar");
        trigramTestList.add("ple");
        trigramTestList.add("ple");
        solver.calculateTrigramFrequency(trigramTestList);
        List<String> testList = new ArrayList<>();
        testList.add("maple");
        testList.add("testy");
        testList.add("testy");
        testList.add("testy");

    }

}
