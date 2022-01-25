package com.briancmills.wordle.solver;

import com.briancmills.wordle.*;

import java.util.List;

public interface Solver {
    
    /**
     * The entire word list (5 letter words is provided to the Solver
     * to do any initialization that is needed.  The Wordle engine
     * will ultimately pick a word from this list at random.
     * @param wordList The list of words considered valid.
     */
    void initialize(List<String> wordList);
    
    /**
     * The main interface for the solver to provide a guess.  The past guess results
     * will be in the order of your prior guesses and will contain specific results 
     * for each letter that was guessed. 
     * @return The guess made by the solver using the data available. 
     */
    String guess(List<PastWordGuessResult> pastGuessResults);
}
