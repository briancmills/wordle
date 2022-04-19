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
    
    private static final int NUMBER_OF_GAMES = 1000;
    
    public static void main(String[] args) {
        
        if (args.length < 1) {
            System.err.println("Please provide the path to the answers file:");
            System.err.println("java -cp target/wordle-1.0-SNAPSHOT.jar com.briancmills.wordle.WordleRunner /Users/brianmills/IdeaProjects/wordle/src/main/resources/answers.txt");
            System.exit(1);
        }
        
        System.out.println("Starting WordleRunner: " + args[0]);
        System.out.println("Will run " + NUMBER_OF_GAMES + " games.");
        
        String inputFileName = args[0];
        File file = FileUtils.getFile(inputFileName);

        List<String> wordList = getWordList(inputFileName);

        // Solver solver = new BasicFilteringSolver();
        // Solver solver = new BigramAnalysisSolver();
         Solver solver = new TrigramAnalysisSolver();
        // Solver solver = new TrigramOptimizedFilteringSolver();
        // Solver solver = new MostCommonWordAnalysisSolver();
        // give the solver an unmodifiable copy of the list
        solver.initialize(Collections.unmodifiableList(wordList));
        
        // run the game 1000 times and calculate how often the solver is correct
        int victories = 0;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < NUMBER_OF_GAMES; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, wordList.size());
            String answer = wordList.get(randomNum);
            if (runGame(answer, solver, false)) {
                victories++;
            }
            // remove the answer from the word list so we don't run the same answer twice
            wordList.remove(answer);
        }
    
        long endTime = System.currentTimeMillis();
    
        System.out.println("it took " + ((endTime - startTime) / 1000) + " seconds to run");
        
        System.out.println("solver produced " + victories + " victories");
        
        System.out.println(String.format("%.2f",((victories / Double.valueOf(NUMBER_OF_GAMES)) * 100)) + "% of games were victories");
    }
    
    /**
     * Factor out the logic of reading the lines from the input file and error handling. 
     * 
     * @param inputFileName The txt file that contains a list of 5-letter words for the puzzle.
     * @return The list of words for the puzzle. 
     */
    private static List<String> getWordList(String inputFileName) {
        File file = FileUtils.getFile(inputFileName);
    
        try {
            return FileUtils.readLines(file, "UTF-8");
        } catch (Exception e) {
            System.err.println("Error opening word file: " + inputFileName);
            e.printStackTrace(System.err);
        }  
        return Collections.emptyList();
    }
    
    /**
     * Runs a single instance of a wordle game using the given solver. 
     *
     * @param answer The correct answer.
     * @param solver The solver which is going to attempt to guess the word.  Must already be initialized. 
     * @return A true value indicates the solver guessed the word correctly. 
     */
    private static boolean runGame(String answer, Solver solver, boolean verbose) {
        
        if (verbose) {
            System.out.println("starting game");
        }
        boolean result = false;
        
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
                if (verbose)  {
                    System.out.println("The guess was correct: " + guess);
                }
                result = true;
                break;
            } else {
                if (verbose) {
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
        }
        if (verbose) {
            System.out.println("\nThe answer was: " + answer);
        }
        return result;
    }
}
