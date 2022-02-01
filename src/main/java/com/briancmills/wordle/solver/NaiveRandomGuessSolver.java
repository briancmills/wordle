package com.briancmills.wordle.solver;

import com.briancmills.wordle.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This is the dumbest solver I could think of.  It just takes a complete random guess. 
 */
public class NaiveRandomGuessSolver implements Solver {
    
    private List<String> wordList;
    
    @Override
    public void initialize(List<String> wordList) {
        this.wordList = wordList;
    }
    
    @Override
    public String guess(List<PastWordGuessResult> pastGuessResults) {
        int randomNum = ThreadLocalRandom.current().nextInt(0, wordList.size());
        return wordList.get(randomNum);
    }
}
