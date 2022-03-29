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

    protected String createTrigramRight(String word) {
        return word.substring(2,5);
    }

    protected String createTrigramCenter(String word) {
        return word.substring(1,4);
    }

    protected String createTrigramLeft(String word) {
        // cares
        // index 0 = c
        // index 1 = a
        // index 2 = r
        // index 3 = e
        // index 4 = s
        return word.substring(0,3);
    }

    public List<String> wordListTrigramAnalyzer(List<String> wordList) {
        List<String> trigrams = new ArrayList<>();
        for (String word : wordList) {
            trigrams.add(createTrigramLeft(word));
            trigrams.add(createTrigramCenter(word));
            trigrams.add(createTrigramRight(word));
        }
        return trigrams;
    }

    protected Map<String, Integer> calculateTrigramFrequency(List<String> trigrams) {

        Map<String, Integer> trigramFrequencyMap = new HashMap<>();
        Set<String> uniqueTrigrams = new HashSet<>(trigrams);

        for (String trigram : uniqueTrigrams) {
            trigramFrequencyMap.put(trigram, Collections.frequency(trigrams, trigram));
        }
        return trigramFrequencyMap;
    }

    protected int getTrigramFrequencyValue(List<String> trigrams) {

        int value = 0;

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

        Comparator<String> trigramComparator = (String left, String right) -> {
            int leftScore = 0;
            int rightScore = 0;

            List<String> leftGrams = Arrays.asList(
                    createTrigramLeft(left), createTrigramCenter(left), createTrigramRight(left));

            List<String> rightGrams = Arrays.asList(
                    createTrigramLeft(right), createTrigramCenter(right), createTrigramRight(right));

            leftScore = getTrigramFrequencyValue(leftGrams);

            rightScore = getTrigramFrequencyValue(rightGrams);

            return rightScore-leftScore;
        };

        super.analyzePastGuesses(pastGuessResults);

        List<String> filteredWordList = super.filterWordList(wordList.stream().sorted(trigramComparator).toList());

        return filteredWordList.get(0);
    }



}
