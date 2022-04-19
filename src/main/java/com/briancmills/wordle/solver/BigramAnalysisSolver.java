package com.briancmills.wordle.solver;

import com.briancmills.wordle.PastWordGuessResult;

import java.util.*;

public class BigramAnalysisSolver extends BasicFilteringSolver {
   
        private Map<String,Integer> bigramFrequencyMap;

        @Override
        public void initialize(List<String> wordList) {
            super.initialize(wordList);
            bigramFrequencyMap = calculateBigramFrequency(wordListBigramAnalyzer(wordList));
        }

        protected String createBigramOne(String word) {
            if (word == null) {
                return null;
            }
            return word.substring(0,2);
        }

        protected String createBigramTwo(String word) {
            if (word == null) {
                return null;
            }
            return word.substring(1,3);
        }

        protected String createBigramThree(String word) {
            if (word == null) {
                return null;
            }
            return word.substring(2,4);
        }

        protected String createBigramFour(String word) {
             if (word == null) {
                 return null;
            }
             return word.substring(3,5);
        }

        public List<String> wordListBigramAnalyzer(List<String> wordList) {
            if (wordList == null) {
                return null;
            }
            List<String> bigrams = new ArrayList<>();
            for (String word : wordList) {
                bigrams.add(createBigramFour(word));
                bigrams.add(createBigramThree(word));
                bigrams.add(createBigramTwo(word));
                bigrams.add(createBigramOne(word));

            }
            return bigrams;
        }

        protected Map<String, Integer> calculateBigramFrequency(List<String> bigrams) {

            if (bigrams == null) {
                return null;
            }

            Map<String, Integer> bigramFrequencyMap = new HashMap<>();
            Set<String> uniqueBigrams = new HashSet<>(bigrams);

            for (String bigram : uniqueBigrams) {
                bigramFrequencyMap.put(bigram, Collections.frequency(bigrams, bigram));
            }
            return bigramFrequencyMap;
        }

        protected int getBigramFrequencyValue(List<String> bigrams) {

            int value = 0;

            if (bigrams == null) {
                return value;
            }

            for (String bi: bigrams) {
                Integer frequencyValue = bigramFrequencyMap.get(bi);
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

            Comparator<String> bigramComparator = (String left, String right) -> {
                Integer leftScore = 0;
                Integer rightScore = 0;

                List<String> leftGrams = Arrays.asList(
                       createBigramFour(left), createBigramThree(left), createBigramTwo(left), createBigramOne(left));

                List<String> rightGrams = Arrays.asList(
                        createBigramFour(right),  createBigramThree(right), createBigramTwo(right), createBigramOne(right));

                leftScore = getBigramFrequencyValue(leftGrams);

                rightScore = getBigramFrequencyValue(rightGrams);

                return rightScore.compareTo(leftScore);
            };

            super.analyzePastGuesses(pastGuessResults);

            List<String> filteredWordList = super.filterWordList(wordList);
            List<String> bigramAnalyzedList = filteredWordList.stream().sorted(bigramComparator).toList();

            return bigramAnalyzedList.get(0);
        }

}
