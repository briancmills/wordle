package com.briancmills.wordle;

import com.briancmills.wordle.solver.HopefullySmartSolver;
import com.briancmills.wordle.solver.Solver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.briancmills.wordle.LetterGuessResult.CORRECT_POSITION;
import static com.briancmills.wordle.LetterGuessResult.LETTER_NOT_PRESENT;
import static org.junit.jupiter.api.Assertions.*;


 class HopefullySmartSolverTest {

    HopefullySmartSolver solver;

    @BeforeEach
    void setUp() {
        solver = new HopefullySmartSolver();
        System.out.println(solver.getWordList());
    }
    @Test
     void testInitialization() {
        solver.initialize(null);
        System.out.println(solver.getWordList());
    }
     @Test
     void testGuessFilter() {
        List<String> wordList = new ArrayList<String>();
        wordList.add("test");
        solver.initialize(wordList);
         PastWordGuessResult result = new PastWordGuessResult();
        PastLetterGuessResult letterResult = new PastLetterGuessResult(0, 't',CORRECT_POSITION);
        // assert(letterResult "t");
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
         testList.add("test");
         testList.add("apple");
         testList.add("orange");
         solver.initialize(testList);
         PastLetterGuessResult letterResult = new PastLetterGuessResult(0, 'a',LETTER_NOT_PRESENT);
         PastWordGuessResult result = new PastWordGuessResult();
         solver.wrongLetterFilter(testList);
         // assert(testList == null);
     }
     @Test
     void testCorrectLetterPositionFilter() {
         List<String> testList = new ArrayList<>();
         testList.add("test");
         testList.add("apple");
         testList.add("orange");
         solver.initialize(testList);
         PastLetterGuessResult letterResult = new PastLetterGuessResult(0, 'a',LETTER_NOT_PRESENT);
         PastWordGuessResult result = new PastWordGuessResult();
         solver.wrongLetterFilter(testList);
         // assert(testList == null);
     }
}
