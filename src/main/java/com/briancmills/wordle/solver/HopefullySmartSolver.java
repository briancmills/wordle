package com.briancmills.wordle.solver;

import com.briancmills.wordle.LetterGuessResult;
import com.briancmills.wordle.PastLetterGuessResult;
import com.briancmills.wordle.PastWordGuessResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
      List exactPosition = new ArrayList<>();
      List<String> foundLetterIncorrectPosition = new ArrayList<>();
      Set<String> wrongLetter = new HashSet<>();
      for (PastWordGuessResult pastResult: pastGuessResults) {
          for (PastLetterGuessResult pastLetterResult: pastResult.getPastLetterResults()) {
              if (pastLetterResult.getResult() == LetterGuessResult.CORRECT_POSITION) {
                  foundLetterCorrectPosition.add("" + pastLetterResult.getLetter());
                  exactPosition.add(pastLetterResult.getPosition());
              }
              if (pastLetterResult.getResult() == LetterGuessResult.INCORRECT_POSITION) {
                  foundLetterIncorrectPosition.add("" + pastLetterResult.getLetter());
              }
              if (pastLetterResult.getResult() == LetterGuessResult.LETTER_NOT_PRESENT) {
                  wrongLetter.add("" + pastLetterResult.getLetter());
              }
          }
      }
      List<String> filteredWordList = wordList.stream()
              .filter(
                      word -> {
                          // what I need to do is associate the FLCP with the EP, they
                          // both are arraylists, I need to learn how to
                          // set the position of the letter so that with this filter
                          // words with letters in the position would be 0-6
                          // I'm not sure if I need an array for each, or if I keep it at one and
                          // just split it after each letter for the number
                          int MatchCount = 0;
                          for (String letter: foundLetterCorrectPosition) {
                              if (word.contains(letter)) {
                                  MatchCount++;
                              }
                          }
                          return MatchCount == foundLetterCorrectPosition.size();
                      }
              ).filter(
                      word -> {
                          int MatchCount = 0;
                          for (String letter: foundLetterIncorrectPosition) {
                              if (word.contains(letter)){
                                  MatchCount++;
                              }
                          }
                          return MatchCount == foundLetterIncorrectPosition.size();
                      }

              ).filter(
                      word -> {
                          boolean match = true;
                          for (String letter : wrongLetter) {
                              if (word.contains(letter)) {
                                   match = false;
                              }
                          }
                          return match;
                      }

              ).collect(Collectors.toList());

            if (wrongLetter.isEmpty()) {
                String firstWord = "tares";
                return firstWord;
            }
            else {
                int randomNum = ThreadLocalRandom.current().nextInt(0, filteredWordList.size());
                return filteredWordList.get(randomNum);
            }
    }
}
