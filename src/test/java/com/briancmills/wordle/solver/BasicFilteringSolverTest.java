package com.briancmills.wordle.solver;

import com.briancmills.wordle.PastLetterGuessResult;
import com.briancmills.wordle.PastWordGuessResult;
import com.briancmills.wordle.solver.BasicFilteringSolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.briancmills.wordle.LetterGuessResult.*;


public class BasicFilteringSolverTest {

    BasicFilteringSolver solver;

    @BeforeEach
    void setUp() {
        solver = new BasicFilteringSolver();
    }

    @Test
     void testInitializationNull() {
        solver.initialize(null);
        System.out.println(solver.getWordList());
    }

    @Test
    void testInitialization() {
        List<String> testList = new ArrayList<>();
        testList.add("testy");
        testList.add("apple");
        testList.add("range");
        solver.initialize(testList);
        System.out.println(solver.getWordList());
    }

     @Test
     void testAnalyzePastGuesses() {
         solver.analyzePastGuesses(null);
     }

     @Test
    void testAnalyzePastGuessesClearing() {
        List<String> result = solver.foundLetterCorrectPosition;
        solver.foundLetterCorrectPosition.add("a");
        solver.foundLetterCorrectPosition.add("d");
        solver.foundLetterCorrectPosition.add("c");
        System.out.println(solver.foundLetterCorrectPosition);
        solver.analyzePastGuesses(null);
        assert(result.isEmpty());
    }

     @Test
     void testGuessFilterNull() {
        solver.initialize(null);
        solver.guess(null);
     }

     @Test
     void testWrongLetterFilterNull() {
         solver.initialize(null);
         solver.wrongLetterFilter(null);
     }

     @Test
     void testCorrectLetterPositionFilterNull() {
         solver.initialize(null);
         solver.correctLetterPositionFilter(null);
     }

     @Test
     void testIncorrectLetterPositionFilterNull() {
         solver.initialize(null);
         solver.correctLetterPositionFilter(null);
     }

     @Test
     void testCorrectPositionMapFilterNull() {
         solver.initialize(null);
         solver.correctLetterPositionFilter(null);
     }

     @Test
     void testIncorrectPositionMapFilterNull() {
         solver.initialize(null);
         solver.correctLetterPositionFilter(null);
     }

     @Test
     void testWrongLetterFilter() {
         List<String> testList = new ArrayList<>();
         testList.add("testy");
         testList.add("apple");
         testList.add("range");
         solver.initialize(testList);
         PastLetterGuessResult letterResult = new PastLetterGuessResult(0, 'a',LETTER_NOT_PRESENT);
         PastWordGuessResult result = new PastWordGuessResult();
         result.addLetterResult(letterResult);
         solver.analyzePastGuesses(Arrays.asList(result));
         List<String> results = solver.wrongLetterFilter(testList.stream()).collect(Collectors.toList());
         assert(results.size() == 1);

         String guess = solver.guess(Arrays.asList(result));
         assert(guess.equals("testy"));
     }

     @Test
     void testCorrectLetterPositionFilter() {
         List<String> testList = new ArrayList<>();
         testList.add("testy");
         testList.add("test");
         testList.add("apple");
         testList.add("range");
         solver.initialize(testList);
         PastLetterGuessResult letterResult = new PastLetterGuessResult(0, 'a',CORRECT_POSITION);
         PastWordGuessResult result = new PastWordGuessResult();
         result.addLetterResult(letterResult);
         solver.analyzePastGuesses(Arrays.asList(result));
         List<String> results = solver.correctLetterPositionFilter(testList.stream()).collect(Collectors.toList());
         assert(results.size() == 2);
     }

     @Test
     void testIncorrectLetterPositionFilter() {
         List<String> testList = new ArrayList<>();
         testList.add("testy");
         testList.add("test");
         testList.add("apple");
         testList.add("range");
         testList.add("mange");
         solver.initialize(testList);
         PastLetterGuessResult letterResult = new PastLetterGuessResult(0, 'a',INCORRECT_POSITION);
         PastWordGuessResult result = new PastWordGuessResult();
         result.addLetterResult(letterResult);
         solver.analyzePastGuesses(Arrays.asList(result));
         List<String> results = solver.incorrectLetterPositionFilter(testList.stream()).collect(Collectors.toList());
         assert(results.size() == 3);
         System.out.println(results);
     }

    @Test
    void testIncorrectPositionMapFilter() {
        List<String> testList = new ArrayList<>();
        testList.add("testy");
        testList.add("apple");
        testList.add("range");
        testList.add("mange");
        testList.add("arse");
        solver.initialize(testList);
        PastLetterGuessResult letterResult = new PastLetterGuessResult(0, 'a', INCORRECT_POSITION);
        PastWordGuessResult result = new PastWordGuessResult();
        result.addLetterResult(letterResult);
        solver.analyzePastGuesses(Arrays.asList(result));
        List<String> results = solver.incorrectPositionMapFilter(testList.stream()).collect(Collectors.toList());
        assert (results.size() == 3);
        System.out.println(results);
    }

    @Test
    void testCorrectPositionMapFilter() {
        List<String> testList = new ArrayList<>();
        testList.add("testy");
        testList.add("apple");
        testList.add("range");
        testList.add("mange");
        testList.add("arse");
        solver.initialize(testList);
        PastLetterGuessResult letterResult = new PastLetterGuessResult(0, 'r', CORRECT_POSITION);
        PastWordGuessResult result = new PastWordGuessResult();
        result.addLetterResult(letterResult);
        solver.analyzePastGuesses(Arrays.asList(result));
        List<String> results = solver.correctPositionMapFilter(testList.stream()).collect(Collectors.toList());
        assert (results.size() == 1);
        System.out.println(results);
    }

    @Test
    void testFilteringWordlist() {
        List<String> testList = new ArrayList<>();
        testList.add("testy");
        testList.add("apple");
        testList.add("range");
        testList.add("mange");
        testList.add("arse");
        solver.initialize(testList);
        PastLetterGuessResult letterResult = new PastLetterGuessResult(0, 'r', CORRECT_POSITION);
        PastWordGuessResult result = new PastWordGuessResult();
        result.addLetterResult(letterResult);
        solver.analyzePastGuesses(Arrays.asList(result));
        List<String> filteredList = solver.filterWordList(testList);
        assert(!filteredList.contains("apple"));
        System.out.println(filteredList);
    }
}
