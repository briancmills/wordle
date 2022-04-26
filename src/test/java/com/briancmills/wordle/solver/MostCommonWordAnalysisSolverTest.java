package com.briancmills.wordle.solver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class MostCommonWordAnalysisSolverTest {

    MostCommonWordAnalysisSolver solver;

    @BeforeEach
    void setUp() {
        solver = new MostCommonWordAnalysisSolver();
    }

    @Test
    void testLargeTextAnalysisNull() {
        solver.textFileAnalyzer(null);
    }

    @Test
    void testLargeTextAnalysis() {
        List<String> testDoc = new ArrayList<>();
        testDoc.add("This eBook is for the use of anyone anywhere in the United States and");
        testDoc.add("most other parts of the world at no cost and with almost no restrictions");
        testDoc.add("Release Date: March 8, 2022 [eBook #67584]");
        testDoc.add("www.gutenberg.org. If you are not located in the United States, you");
        solver.textFileAnalyzer(testDoc);
        System.out.println(solver.allFiveLetterWordList);
        assert(!solver.allFiveLetterWordList.isEmpty());
    }

    @Test
    void testWordCheckerNull() {
        solver.wordChecker(null);
    }

    @Test
    void testNullInitialize() {
        solver.initialize(null);
    }

    @Test
    void testWordChecker() {
        String word = "seven";
        solver.wordChecker(word);
        System.out.println(solver.allFiveLetterWordList);
        assert(!solver.allFiveLetterWordList.isEmpty());
    }

    @Test
    void testNullGuess() {
        solver.guess(null);
    }

    @Test
    void testIsAlphabetic() {
        String word = "there";
        String word1 = "this2";
        String word2 = "$$$$$";
        boolean test = solver.isAlphabetic(word);
        boolean test1 = solver.isAlphabetic(word1);
        boolean test2 = solver.isAlphabetic(word2);
        assert(test);
        assert(!test1);
        assert(!test2);
    }

    @Test
    void testIsAlphabeticNull() {
        solver.isAlphabetic(null);}

    @Test
    void testGetFrequencyValueNull() {
        solver.getFrequencyValue(null);
    }

    @Test
    void testFrequencyValue() {
        String testWord = "there";
        String testWord1 = "march";
        String testWord2 = "touch";
        String testNull = null;
        solver.wordFrequencyMap.put("there", 54);
        solver.wordFrequencyMap.put("march", 13);
        solver.wordFrequencyMap.put("touch", 1);
        int result = solver.getFrequencyValue(testWord);
        int result1 = solver.getFrequencyValue(testWord1);
        int result2 = solver.getFrequencyValue(testWord2);
        int nullResult = solver.getFrequencyValue(testNull);
        assert(result == 54);
        assert(result1 == 13);
        assert(result2 == 1);
        assert(nullResult == 0);
    }

    @Test
    void testFrequency() {
        List<String> testDoc = new ArrayList<>();
        testDoc.add("This eBook is for the use of anyone anywhere in the United States and");
        testDoc.add("most other parts of the world at no cost and with almost no restrictions");
        testDoc.add("Release Date: March 8, 2022 [eBook #67584]");
        testDoc.add("www.gutenberg.org. If you are not located in the United States, you");
        testDoc.add("In the twenty-four hours’ run ending the 14th, according to the posted\n" +
                "reckoning, the ship had covered 546 miles, and we were told that the\n" +
                "next twenty-four hours would see even a better record made.\n" +
                "\n" +
                "Towards evening the report, which I heard, was spread that wireless\n" +
                "messages from passing steamers had been received advising the officers\n" +
                "of our ship of the presence of icebergs and ice-floes. The increasing\n" +
                "cold and the necessity of being more warmly clad when appearing on\n" +
                "deck were outward and visible signs in corroboration of these\n" +
                "warnings. But despite them all no diminution of speed was indicated\n" +
                "and the engines kept up their steady running.\n" +
                "\n" +
                "Not for fifty years, the old sailors tell us, had so great a mass of\n" +
                "ice and icebergs at this time of the year been seen so far south.\n" +
                "\n" +
                "The pleasure and comfort which all of us enjoyed upon this floating\n" +
                "palace, with its extraordinary provisions for such purposes, seemed an\n" +
                "ominous feature to many of us, including myself, who felt it almost\n" +
                "too good to last without some terrible retribution inflicted by the\n" +
                "hand of an angry omnipotence. Our sentiment in this respect was voiced\n" +
                "by one of the most able and distinguished of our fellow passengers,\n" +
                "Mr. Charles M. Hays, President of the Canadian Grand Trunk Railroad.\n" +
                "Engaged as he then was in studying and providing the hotel equipment\n" +
                "along the line of new extensions to his own great railroad system, the\n" +
                "consideration of the subject and of the magnificence of the\n" +
                "_Titanic’s_ accommodations was thus brought home to him. This was the\n" +
                "prophetic utterance with which, alas, he sealed his fate a few hours\n" +
                "thereafter: “The White Star, the Cunard and the Hamburg-American\n" +
                "lines,” said he, “are now devoting their attention to a struggle for\n" +
                "supremacy in obtaining the most luxurious appointments for their\n" +
                "ships, but the time will soon come when the greatest and most\n" +
                "appalling of all disasters at sea will be the result.”");
        solver.textFileAnalyzer(testDoc);
        solver.calculateFiveLetterWordFrequency(solver.allFiveLetterWordList);
        System.out.println(solver.wordFrequencyMap);
    }

}
