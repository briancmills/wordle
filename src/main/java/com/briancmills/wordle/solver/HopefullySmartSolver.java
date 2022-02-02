package com.briancmills.wordle.solver;

import com.briancmills.wordle.LetterGuessResult;
import com.briancmills.wordle.PastLetterGuessResult;
import com.briancmills.wordle.PastWordGuessResult;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class HopefullySmartSolver implements Solver {

    private List<String> wordList;

    @Override
    public void initialize(List<String> wordList) {
        this.wordList = wordList;
    }

    @Override
    public String guess(List<PastWordGuessResult> pastGuessResults) {

      List<String> foundLetterCorrectPosition = new ArrayList<>();
      List<String> foundLetterIncorrectPosition = new ArrayList<>();

      Set<String> wrongLetter = new HashSet<>();

      Map<Integer, Character> incorrectPositionMap = new HashMap<>();
      Map<Integer, Character> correctPositionMap = new HashMap<>();

      for (PastWordGuessResult pastResult: pastGuessResults) {
          for (PastLetterGuessResult pastLetterResult: pastResult.getPastLetterResults()) {
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
      List<String> filteredWordList = wordList.stream()
              .filter(
                      word -> {
                          // I am going to use a map
                          int matchCount = 0;
                          for (String letter: foundLetterCorrectPosition) {
                              if (word.contains(letter)) {
                                  matchCount++;
                              }
                          }
                          return matchCount == foundLetterCorrectPosition.size();
                      }
              ).filter(
                      word -> {
                          int matchCount = 0;
                          for (String letter: foundLetterIncorrectPosition) {
                              if (word.contains(letter)){
                                  matchCount++;
                              }
                          }
                          return matchCount == foundLetterIncorrectPosition.size();
                      }

              ).filter(
                      word -> {
                          boolean match = true;
                          for (String letter : wrongLetter) {
                              if (word.contains(letter)) {
                                   match = false;
                                   break;
                              }
                          }
                          return match;
                      }

              ).filter(
                      word -> {

                          boolean match = true;

                          for (Integer position : correctPositionMap.keySet()) {
                             if (word.charAt(position) != correctPositionMap.get(position)) {
                                 match = false;
                             }
                          }
                          return match;
                      }

              ).filter(
                word -> {

                    boolean match = true;

                    for (Integer position : incorrectPositionMap.keySet()) {
                        if (word.charAt(position) == incorrectPositionMap.get(position)) {
                            match = false;
                        }
                    }
                    return match;
                }

        ).collect(Collectors.toList());

            if (pastGuessResults.isEmpty()) {
                String firstWord = "tares";
                return firstWord;
            }
            else {
                int randomNum = ThreadLocalRandom.current().nextInt(0, filteredWordList.size());
                return filteredWordList.get(randomNum);
            }
    }
}
