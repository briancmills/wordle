package com.briancmills.wordle;

public class PastLetterGuessResult {
    
    private int position;
    private char letter;
    private LetterGuessResult result;
    
    public PastLetterGuessResult(int position, char letter, LetterGuessResult result) {
        this.position = position;
        this.letter = letter;
        this.result = result;
    }
    
    public int getPosition() {
        return position;
    }
    
    public char getLetter() {
        return letter;
    }
    
    public LetterGuessResult getResult() {
        return result;
    }
}
