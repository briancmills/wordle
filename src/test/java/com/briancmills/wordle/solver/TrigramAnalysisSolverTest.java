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
    void testWordListTrigramAnalyzer() {
        solver.wordListTrigramAnalyzer(null);
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
    void testCalculateTrigramFrequencyNull() {
        solver.calculateTrigramFrequency(null);
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
    void testFrequencyValueNull() {
        solver.getTrigramFrequencyValue(null);
    }

    @Test
    void testTrigramLeft(){
        String word = "match";
        String result = solver.createTrigramLeft(word);
        assert(Objects.equals(result, "mat"));
        System.out.println(result);
    }

    @Test
    void testTrigramCenter() {
        String word = "match";
        String result = solver.createTrigramCenter(word);
        assert(Objects.equals(result, "atc"));
        System.out.println(result);
    }

    @Test
    void testTrigramRight() {
        String word = "match";
        String result = solver.createTrigramRight(word);
        assert(Objects.equals(result, "tch"));
        System.out.println(result);
    }
    @Test
    void testTrigramNull() {
        String word = null;
        String result = solver.createTrigramRight(word);
        String result1 = solver.createTrigramCenter(word);
        String result2 = solver.createTrigramLeft(word);
        assert(result == null);
        assert(result1 == null);
        assert(result2 == null);
    }
    
    
    @Test
    void testGuess_nullHandling() {
        String result = solver.guess(null);
        assert(result == null);
    }
}
