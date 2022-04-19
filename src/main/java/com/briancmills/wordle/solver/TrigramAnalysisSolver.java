package com.briancmills.wordle.solver;

// this has all the basic filtering logic, it is just going to implement trigrams,
// which analyze the word list for the most common three-letter runs in the five-letter words

// so far I have logic that will take a list of five-letter words and then return three lists
// of all the trigrams for each position

import com.briancmills.wordle.PastWordGuessResult;

import java.util.*;

public class TrigramAnalysisSolver extends BasicFilteringSolver {

    private Map<String,Integer> trigramFrequencyMap;

    @Override
    public void initialize(List<String> wordList) {
        super.initialize(wordList);
        trigramFrequencyMap = calculateTrigramFrequency(wordListTrigramAnalyzer(wordList));
    }

    /**
     * Creates a trigram of the five-letter word that is passed in
     * @param word A single five-letter word
     * @return A Trigram of letters 2-4
     */

    protected String createTrigramRight(String word) {
        if (word == null) {
            return null;
        }
        return word.substring(2,5);
    }

    /**
     * Creates a trigram of the five-letter word that is passed in
     * @param word A single five-letter word
     * @return A Trigram of letters 1-3
     */

    protected String createTrigramCenter(String word) {
        if (word == null) {
            return null;
        }
        return word.substring(1,4);
    }

    /**
     * Creates a trigram of the five-letter word that is passed in
     * @param word A single five-letter word
     * @return A trigram of letters 0-2
     */

    protected String createTrigramLeft(String word) {
        if (word == null) {
            return null;
        }
        return word.substring(0,3);
    }

    /**
     * This consolidates the createTrigram methods and processes the entire word list
     * by passing in each word in a for each loop
     * @param wordList The entire word-bank of five-letter words
     * @return A List of all the possible trigrams
     */

    public List<String> wordListTrigramAnalyzer(List<String> wordList) {
        if (wordList == null) {
            return null;
        }
        List<String> trigrams = new ArrayList<>();
        for (String word : wordList) {
            trigrams.add(createTrigramLeft(word));
            trigrams.add(createTrigramCenter(word));
            trigrams.add(createTrigramRight(word));
        }
        return trigrams;
    }

    /**
     * This method calculates the frequency of each unique trigram by creating a Set of all the unique trigrams,
     * to avoid duplicates and then calculates the amount of times each unique trigram appeared in the list.
     * This method also skips over trigrams that contain repeated letters
     * @param trigrams List of all the trigrams
     * @return A Map with the key being the unique trigram and the value being its frequency
     */

    protected Map<String, Integer> calculateTrigramFrequency(List<String> trigrams) {

        if (trigrams == null) {
            return null;
        }

        Map<String, Integer> trigramFrequencyMap = new HashMap<>();
        Set<String> uniqueTrigrams = new HashSet<>(trigrams);

        for (String trigram : uniqueTrigrams) {
            if (trigram.charAt(0) == trigram.charAt(1)
                    || trigram.charAt(1) == trigram.charAt(2)
                    || trigram.charAt(0) == trigram.charAt(2)) {
                continue;
            }
            trigramFrequencyMap.put(trigram, Collections.frequency(trigrams, trigram));
        }
        return trigramFrequencyMap;
    }

    /**
     * This passes in the three trigrams for a word and then uses the trigramFrequencyMap to get the frequency of
     * each trigram present in the word, it then adds each of the values together.
     * This total value is representative of how valuable this word is for guessing.
     * @param trigrams The three trigrams of an individual word
     * @return A combined numerical frequency value for specified word
     */

    protected int getTrigramFrequencyValue(List<String> trigrams) {

        int value = 0;

        if (trigrams == null) {
            return value;
        }

        for (String tri: trigrams) {
          Integer frequencyValue = trigramFrequencyMap.get(tri);
          if (frequencyValue != null) {
              value = frequencyValue + value;
          }
        }
        return value;
    }

    @Override
    public String guess(List<PastWordGuessResult> pastGuessResults) {

        if (pastGuessResults == null) {
            return null;
        }
        
        Comparator<String> trigramComparator = (String left, String right) -> {
            int leftScore = 0;
            int rightScore = 0;

            List<String> leftGrams = Arrays.asList(
                    createTrigramLeft(left), createTrigramCenter(left), createTrigramRight(left));

            List<String> rightGrams = Arrays.asList(
                    createTrigramLeft(right), createTrigramCenter(right), createTrigramRight(right));

            leftScore = getTrigramFrequencyValue(leftGrams);

            rightScore = getTrigramFrequencyValue(rightGrams);

             return rightScore - leftScore;
        };

        super.analyzePastGuesses(pastGuessResults);


        List<String> filteredWordList = super.filterWordList(wordList);
        List<String> trigramAnalyzedList = filteredWordList.stream().sorted(trigramComparator).toList();

        return trigramAnalyzedList.get(0);
    }



}
