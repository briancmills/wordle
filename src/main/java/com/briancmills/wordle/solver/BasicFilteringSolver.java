package com.briancmills.wordle.solver;

import com.briancmills.wordle.LetterGuessResult;
import com.briancmills.wordle.PastLetterGuessResult;
import com.briancmills.wordle.PastWordGuessResult;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BasicFilteringSolver implements Solver {

    protected List<String> wordList;
    protected List<String> foundLetterCorrectPosition = new ArrayList<>();
    protected List<String> foundLetterIncorrectPosition = new ArrayList<>();

    protected Set<String> wrongLetter = new HashSet<>();

    protected Map<Integer, Character> incorrectPositionMap = new HashMap<>();
    protected Map<Integer, Character> correctPositionMap = new HashMap<>();
    protected List<String> filteredWordList = new ArrayList<>();

    @Override
    public void initialize(List<String> wordList) {
        if (wordList == null) {
            return;
        }
        this.wordList = wordList;
    }

    // TODO: write a unit test to prove that when the method is given no past results it resets the maps

    protected void analyzePastGuesses(List<PastWordGuessResult> pastGuessResults) {
        if (pastGuessResults == null || pastGuessResults.isEmpty()) {
            // if we are running a new game, we will have no past guesses
            // so we reset this data
            foundLetterCorrectPosition.clear();
            foundLetterIncorrectPosition.clear();
            correctPositionMap.clear();
            incorrectPositionMap.clear();
            wrongLetter.clear();
            return;
        }

        for (PastWordGuessResult pastResult : pastGuessResults) {
            for (PastLetterGuessResult pastLetterResult : pastResult.getPastLetterResults()) {
                if (pastLetterResult.getResult() == LetterGuessResult.CORRECT_POSITION) {
                    foundLetterCorrectPosition.add("" + pastLetterResult.getLetter());
                    correctPositionMap.put(pastLetterResult.getPosition(), pastLetterResult.getLetter());
                }
                if (pastLetterResult.getResult() == LetterGuessResult.INCORRECT_POSITION) {
                    foundLetterIncorrectPosition.add("" + pastLetterResult.getLetter());
                    incorrectPositionMap.put(pastLetterResult.getPosition(), pastLetterResult.getLetter());
                }
                if (pastLetterResult.getResult() == LetterGuessResult.LETTER_NOT_PRESENT) {
                    wrongLetter.add("" + pastLetterResult.getLetter());
                }
            }
        }
    }

     public Stream<String> wrongLetterFilter(Stream<String> wordList) {
        Predicate<? super String> wrongLetterFilter = word -> {
            boolean match = true;
            for (String letter : wrongLetter) {
                if (word.contains(letter)) {
                    match = false;
                    break;
                }
            }
            return match;
        };
         if (wordList == null){
             return null;
         }
        return wordList.filter(wrongLetterFilter);
    }

    public Stream<String> correctLetterPositionFilter(Stream<String> wordList) {
        Predicate<? super String> correctLetterFilter = word -> {
            int matchCount = 0;
            for (String letter : foundLetterCorrectPosition) {
                if (word.contains(letter)) {
                    matchCount++;
                }
            }
            return matchCount == foundLetterCorrectPosition.size();
        };
        if (wordList == null){
            return null;
        }
        return wordList.filter(correctLetterFilter);
    }

    public Stream<String> incorrectLetterPositionFilter(Stream<String> wordList) {
        Predicate<? super String> incorrectLetterFilter = word -> {
            int matchCount = 0;
            for (String letter : foundLetterIncorrectPosition) {
                if (word.contains(letter)) {
                    matchCount++;
                }
            }
            return matchCount == foundLetterIncorrectPosition.size();
        };
        if (wordList == null){
            return null;
        }
        return wordList.filter(incorrectLetterFilter);
    }

    public Stream<String> correctPositionMapFilter(Stream<String> wordList) {
        Predicate<? super String> correctPositionFilter = word -> {
            boolean match = true;
            for (Integer position : correctPositionMap.keySet()) {
                if (word.charAt(position) != correctPositionMap.get(position)) {
                    match = false;
                }
            }
            return match;
        };
        if (wordList == null){
            return null;
        }
        return wordList.filter(correctPositionFilter);
    }

    public Stream<String> incorrectPositionMapFilter(Stream<String> wordList) {
        Predicate<? super String> incorrectPositionFilter = word -> {
            boolean match = true;
            for (Integer position : incorrectPositionMap.keySet()) {
            if (word.charAt(position) == incorrectPositionMap.get(position)) {
                    match = false;
                }
            }
            return match;
        };
        if (wordList == null){
            return null;
        }
        return wordList.filter(incorrectPositionFilter);
    }

    protected List<String> filterWordList(List<String> wordList) {

        Stream<String> wrongLetterFilteredList = wrongLetterFilter(wordList.stream());
        Stream<String> correctLetterPositionFilteredList = correctLetterPositionFilter(wrongLetterFilteredList);
        Stream<String> incorrectLetterPositionFilteredList = incorrectLetterPositionFilter(correctLetterPositionFilteredList);
        Stream<String> correctPositionMapFilterList = correctPositionMapFilter(incorrectLetterPositionFilteredList);
        Stream<String> incorrectPositionMapFilterList = incorrectPositionMapFilter(correctPositionMapFilterList);
        return incorrectPositionMapFilterList.collect(Collectors.toList());
    }

    @Override
    public String guess(List<PastWordGuessResult> pastGuessResults) {

        analyzePastGuesses(pastGuessResults);

        if (wordList == null) {
            return null;
        }

       filteredWordList = filterWordList(wordList);

      {
            int randomNum = ThreadLocalRandom.current().nextInt(0, filteredWordList.size());
            return filteredWordList.get(randomNum);
        }
    }


    public List<String> getWordList() {
        return wordList;
    }
}

