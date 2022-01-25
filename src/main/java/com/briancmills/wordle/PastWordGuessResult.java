package com.briancmills.wordle;

import java.util.*;

public class PastWordGuessResult {
    private List<PastLetterGuessResult> pastLetterResults = new ArrayList<>();
    
    public void addLetterResult(PastLetterGuessResult pastLetterGuessResult) {
        this.pastLetterResults.add(pastLetterGuessResult);
    }
    
    public List<PastLetterGuessResult> getPastLetterResults() {
        return this.pastLetterResults;
    }
}
