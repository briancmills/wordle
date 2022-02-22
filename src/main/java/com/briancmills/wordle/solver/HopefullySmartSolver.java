package com.briancmills.wordle.solver;

import com.briancmills.wordle.LetterGuessResult;
import com.briancmills.wordle.PastLetterGuessResult;
import com.briancmills.wordle.PastWordGuessResult;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HopefullySmartSolver implements Solver {

    private List<String> wordList;
    private List<String> foundLetterCorrectPosition = new ArrayList<>();
    private List<String> foundLetterIncorrectPosition = new ArrayList<>();

    private Set<String> wrongLetter = new HashSet<>();

    private Map<Integer, Character> incorrectPositionMap = new HashMap<>();
    private Map<Integer, Character> correctPositionMap = new HashMap<>();

    @Override
    public void initialize(List<String> wordList) {
        if (wordList == null) {
            return;
        }
        this.wordList = wordList;
    }

    protected void guessFilter(List<PastWordGuessResult> pastGuessResults) {
        if (pastGuessResults == null) {
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

     public List<String> wrongLetterFilter(List<String> wordList) {
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
        return wordList.stream().filter(wrongLetterFilter).toList();
    }

    public List<String> correctLetterPositionFilter(List<String> wordList) {
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
        return wordList.stream().filter(correctLetterFilter).toList();
    }

    public List<String> incorrectLetterPositionFilter(List<String> wordList) {
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
        return wordList.stream().filter(incorrectLetterFilter).toList();
    }

    public List<String> correctPositionMapFilter(List<String> wordList) {
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
        return wordList.stream().filter(correctPositionFilter).toList();
    }

    public List<String> incorrectPositionMapFilter(List<String> wordList) {
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
        return wordList.stream().filter(incorrectPositionFilter).toList();
    }

    @Override
    public String guess(List<PastWordGuessResult> pastGuessResults) {

        guessFilter(pastGuessResults);

        if (wordList == null) {
            return null;
        }


        List<String> wrongLetterFilteredList = wrongLetterFilter(wordList);
        List<String> correctLetterPositionFilteredList = correctLetterPositionFilter(wrongLetterFilteredList);
        List<String> incorrectLetterPositionFilteredList = incorrectLetterPositionFilter(correctLetterPositionFilteredList);
        List<String> correctPositionMapFilterList = correctPositionMapFilter(incorrectLetterPositionFilteredList);
        List<String> incorrectPositionMapFilterList = incorrectPositionMapFilter(correctPositionMapFilterList);
        List<String> filteredWordList = incorrectPositionMapFilterList.stream().collect(Collectors.toList());

        if (pastGuessResults.isEmpty()) {
            String firstWord = "tares";
            return firstWord;
        } else {
            int randomNum = ThreadLocalRandom.current().nextInt(0, filteredWordList.size());
            return filteredWordList.get(randomNum);
        }
    }


    public List<String> getWordList() {
        return wordList;
    }
}

