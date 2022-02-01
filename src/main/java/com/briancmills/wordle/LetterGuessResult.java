package com.briancmills.wordle;

public enum LetterGuessResult {
    CORRECT_POSITION, // green
    INCORRECT_POSITION, // yellow
    LETTER_NOT_PRESENT // gray
}
