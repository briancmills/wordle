package com.briancmills.wordle.solver;

import com.briancmills.wordle.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Uses basic filters and knowledge of past guesses to solve the puzzle, but also
 * analyzes the three letter sequences found in the words (Trigrams) to prioritize guesses
 * that contain the most common sequences and hopefully increase the odds of success. 
 */
public class TrigramOptimizedFilteringSolver implements Solver {
    
    private List<String> wordList;
    // as we go, we filter down the list of words based on our answers
    // continuing to filter down a previously filtered list has performance benefits
    private List<String> filteredWordList;
    
    Map<String, Integer> gramMap = new HashMap<>();
    
    @Override
    public void initialize(List<String> wordList) {
        this.wordList = wordList;
        this.filteredWordList = new ArrayList<>(wordList);
        
        // keep track of each trigram found in the word list and the number of occurrences
        this.wordList.forEach(word-> {
            // since each word is 5 letters long we can get trigrams 
            // by splitting on known length index
            
            trackGram(word.substring(0, 3));
            trackGram(word.substring(1, 4));
            trackGram(word.substring(2, 5));
            //trackGram(word.substring(3, 5));
            
        });
    }
    
    private void trackGram(String gram) {
        // we never want repeated letters since it doesn't add value to the guessing
        if (gram.charAt(0) == gram.charAt(1) || gram.charAt(0) == gram.charAt(2) || gram.charAt(1) == gram.charAt(2)) {
            return;
        }
        gramMap.compute(gram, (key, val) ->
                val == null ? 1 : val + 1
        );
    }
    
    private List<String> getGrams(String word) {
        List<String> grams = new ArrayList<>();
        grams.add(word.substring(0, 3));
        grams.add(word.substring(1, 4));
        grams.add(word.substring(2, 5));
        //grams.add(word.substring(3, 5));
        return grams;
        
    }
    
    @Override
    public String guess(List<PastWordGuessResult> pastGuessResults) {
        Set<String> foundLetters = new HashSet<>();
        Set<String> wrongLetters = new HashSet<>();
        
        // tracks the index and corresponding letter of any correct position guesses
        Map<Integer, Character> correctPositionLetters = new HashMap<>();
        Map<Integer, Character> incorrectPositionLetters = new HashMap<>();
        
        for (PastWordGuessResult pastResult: pastGuessResults) {
            for (PastLetterGuessResult pastLetterResult: pastResult.getPastLetterResults()) {
                if (pastLetterResult.getResult() == LetterGuessResult.CORRECT_POSITION) {
                    foundLetters.add("" + pastLetterResult.getLetter());
                    correctPositionLetters.put(pastLetterResult.getPosition(), pastLetterResult.getLetter());
                } else if (pastLetterResult.getResult() == LetterGuessResult.INCORRECT_POSITION) {
                    foundLetters.add("" + pastLetterResult.getLetter());
                    incorrectPositionLetters.put(pastLetterResult.getPosition(), pastLetterResult.getLetter());
                } else if (pastLetterResult.getResult() == LetterGuessResult.LETTER_NOT_PRESENT) {
                    wrongLetters.add("" + pastLetterResult.getLetter());
                }
            }
        }
        
        List<String> localFilteredWordList;
        
        if (pastGuessResults.isEmpty()) {
            localFilteredWordList = new ArrayList<>(wordList);
        } else {
            localFilteredWordList = filteredWordList.stream()
                    // first filter out any letters known to be wrong        
                    .filter(
                            word -> {
                                boolean match = true;
                                for (String letter: wrongLetters) {
                                    if (word.contains(letter)) {
                                        match = false;
                                    }
                                }
                                return match;
                            }
                    )
                    // then make sure we only are targeting words that have letters we know to be in the answer
                    .filter(
                            word -> {
                                int matchCount = 0;
                                for (String letter: foundLetters) {
                                    if (word.contains(letter)) {
                                        matchCount++;
                                    }
                                }
                                return matchCount == foundLetters.size();
                            }
                    )
                    // narrow down further to letters we know are in a certain position
                    .filter(
                            word -> {
                                boolean match = true;
                                
                                for (Integer position: correctPositionLetters.keySet()) {
                                    Character knownLetter = correctPositionLetters.get(position);
                                    Character letterAtIndex = word.charAt(position);
                                    if (!letterAtIndex.equals(knownLetter)) {
                                        match = false;
                                    }
                                }
                                return match;
                            }
                    )
                    // now use the incorrect position data to remove words that have found letters but in the wrong spot
                    .filter(
                            word -> {
                                boolean match = true;
                                
                                for (Integer position: incorrectPositionLetters.keySet()) {
                                    Character wrongPositionLetter = incorrectPositionLetters.get(position);
                                    Character letterAtIndex = word.charAt(position);
                                    if (letterAtIndex.equals(wrongPositionLetter)) {
                                        match = false;
                                    }
                                }
                                return match;
                            }
                    )
                    .collect(Collectors.toList());
            
            if (localFilteredWordList.isEmpty()) {
                localFilteredWordList = new ArrayList<>(wordList);
            }
        }
        
        
        filteredWordList = localFilteredWordList;
        
        
        // now try to prioritize a guess that contains the highest count of the most frequent bigrams
        
        Comparator<String> trigramCount = new Comparator<>() {
            @Override
            public int compare(String o1, String o2) {
                Integer leftCount = 0;
                Integer rightCount = 0;
                List<String> leftGrams = getGrams(o1);
                List<String> rightGrams = getGrams(o2);
                
                for (String gram : leftGrams) {
                    Integer frequency = gramMap.get(gram);
                    leftCount = leftCount + (frequency == null ? 0 : frequency);
                }
                for (String gram : rightGrams) {
                    Integer frequency = gramMap.get(gram);
                    rightCount = rightCount + (frequency == null ? 0 : frequency);
                }
                return rightCount.compareTo(leftCount);
            }
        };
        
        // TODO make sure first guess does not contain multiple of the same letter
        Optional<String> topChoice = filteredWordList.stream().sorted(trigramCount).findFirst();
        int randomNum = ThreadLocalRandom.current().nextInt(0, filteredWordList.size());
        String randomGuess = filteredWordList.get(randomNum);
        return topChoice.orElse(randomGuess);
    }
}
