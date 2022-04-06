package com.briancmills.wordle.solver;

// currently, the solver will correctly identify and track the frequency of five-letter words
// I need to implement a comparator and try to figure out how to make the file reader work,
// I don't know if it needs to be passed in as an argument like Brian has his or not

import com.briancmills.wordle.PastWordGuessResult;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MostCommonWordAnalysisSolver extends BasicFilteringSolver {

    protected List<String> allFiveLetterWordList = new ArrayList<>();
    protected Map<String, Integer> wordFrequencyMap = new HashMap<>();

    @Override
    public void initialize(List<String> wordList) {
        File file = new File("/Users/josh/IdeaProjects/wordle/src/main/resources/english_text.txt");
        List<String> textFile = getTextFile(file);
        textFileAnalyzer(textFile);
        calculateFiveLetterWordFrequency(allFiveLetterWordList);
        super.initialize(wordList);
    }

    private static List<String> getTextFile(File inputFileName) {

        File file = FileUtils.getFile(inputFileName);

        try {
            return FileUtils.readLines(file, "UTF-8");
        } catch (Exception e) {
            System.err.println("Error opening word file: " + inputFileName);
            e.printStackTrace(System.err);
        }
        return Collections.emptyList();
    }


   protected boolean isAlphabetic(String word) {

        if (word == null) {
            return false;
        }

        Pattern pattern = Pattern.compile("^[a-zA-Z]*$",Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(word);

        return matcher.find();
    }

    protected void wordChecker(String word) {

        if (word == null) {
            return;
        }

        if (isAlphabetic(word)) {
            if (word.length() == 5) {
                allFiveLetterWordList.add(word);
            }
        }
    }

    protected void textFileAnalyzer(List<String> textFile) {
        if (textFile == null) {
            return;
        }
        for (String line : textFile) {
            String[] arrayOfLine = line.split(" ", 0);
            for (String word : arrayOfLine) {
                wordChecker(word.toLowerCase(Locale.ROOT));
            }
        }
    }

    protected void calculateFiveLetterWordFrequency(List<String> words) {
        Set<String> frequencyOfWords = new HashSet<>(words);
        for (String word : frequencyOfWords) {
            wordFrequencyMap.put(word, Collections.frequency(words, word));
        }
    }

    protected int getFrequencyValue(String word) {

        int value = 0;

        if (word == null) {
            return value;
        }
        if (!wordFrequencyMap.containsKey(word)) {
            return value;
        }
        value = wordFrequencyMap.get(word);

        return value;
    }

    @Override
    public String guess(List<PastWordGuessResult> pastGuessResults) {

        Comparator<String> fiveLetterWordComparator = (String left, String right) -> {

            int leftScore = 0;
            int rightScore = 0;

            leftScore = getFrequencyValue(left);
            rightScore = getFrequencyValue(right);

            return rightScore - leftScore;
        };

        super.analyzePastGuesses(pastGuessResults);

        List<String> filteredWordList = super.filterWordList(wordList.stream().sorted(fiveLetterWordComparator).toList());

        return filteredWordList.get(0);
    }
}