package com.briancmills.wordle;

import com.briancmills.wordle.solver.*;
import org.apache.commons.io.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A simple command line application that runs a Wordle Puzzle 
 * and allows us to test implementations of our Solver interface 
 * to try out different approaches to having software solve the puzzle. 
 */
public class WordleRunner {
    
    public static void main(String[] args) {
        
        if (args.length < 1) {
            System.err.println("Please provide the path to the answers file:");
            System.err.println("java -cp target/wordle-1.0-SNAPSHOT.jar com.briancmills.wordle.WordleRunner /Users/brianmills/IdeaProjects/wordle/src/main/resources/answers.txt");
            System.exit(1);
        }
    
        System.out.println("Starting WordleRunner: " + args[0]);
        
        String inputFileName = args[0];
        File file = FileUtils.getFile(inputFileName);
        
        try {
            List<String> wordList = FileUtils.readLines(file, "UTF-8");
            //lines.forEach(l->System.out.println(l));
    
            int randomNum = ThreadLocalRandom.current().nextInt(0, wordList.size() + 1);
            String answer = wordList.get(randomNum);
            Solver solver = new NaiveRandomGuessSolver();
            solver.initialize(wordList);
            List<PastWordGuessResult> pastGuessResults = new ArrayList<>();
            // the solver gets 6 guesses
            for (int i = 0; i <= 5; i++) {
                String guess = solver.guess(pastGuessResults).toLowerCase(Locale.ROOT);
                PastWordGuessResult wordGuessResult = new PastWordGuessResult();
                // check each letter against the answer
                for (int j = 0; j <= 4; j++) {
                    char guessLetter = guess.charAt(j);
                    char correctLetter = answer.charAt(j);
                    LetterGuessResult letterGuessResult = LetterGuessResult.LETTER_NOT_PRESENT;
                    if (guessLetter == correctLetter) {
                        letterGuessResult = LetterGuessResult.CORRECT_POSITION;
                    } else if (answer.contains(""+guessLetter)) {
                        letterGuessResult = LetterGuessResult.INCORRECT_POSITION;
                    }
                    wordGuessResult.addLetterResult(new PastLetterGuessResult(j, guessLetter, letterGuessResult));
                }
                
                pastGuessResults.add(wordGuessResult);
                
                if (guess.equalsIgnoreCase(answer)) {
                    System.out.println("The guess was correct: " + guess);
                    break;
                } else {
                    System.out.println(guess);
                    wordGuessResult.getPastLetterResults().forEach(r-> {
                        switch (r.getResult()) {
                            case CORRECT_POSITION -> System.out.print("+");
                            case INCORRECT_POSITION -> System.out.print("*");
                            case LETTER_NOT_PRESENT -> System.out.print("-");
                        }
                    });
                    System.out.println();
                }
            }
    
            System.out.println("\nThe answer was: "+ answer);
        } catch (Exception e) {
            System.err.println("runtime error");
            e.printStackTrace(System.err);
        }
    }
}
