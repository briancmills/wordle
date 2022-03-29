package com.briancmills.wordle.solver;

import com.briancmills.wordle.PastLetterGuessResult;
import com.briancmills.wordle.PastWordGuessResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.briancmills.wordle.LetterGuessResult.LETTER_NOT_PRESENT;

public class MostCommonWordAnalysisSolverTest {

    MostCommonWordAnalysisSolver solver;

    @BeforeEach
    void setUp() {
        solver = new MostCommonWordAnalysisSolver();
    }

    @Test
    void testLargeTextAnalysis() {
        List<String> testDoc = new ArrayList<>();
        testDoc.add("This eBook is for the use of anyone anywhere in the United States and");
        testDoc.add("most other parts of the world at no cost and with almost no restrictions");
        testDoc.add("Release Date: March 8, 2022 [eBook #67584]");
        testDoc.add("www.gutenberg.org. If you are not located in the United States, you");
        solver.textDocAnalyzer(testDoc);
        System.out.println(solver.allFiveLetterWordList);
        assert(!solver.allFiveLetterWordList.isEmpty());
    }

    @Test
    void testWordChecker() {
        String word = "seven";
        solver.wordChecker(word);
        System.out.println(solver.allFiveLetterWordList);
        assert(!solver.allFiveLetterWordList.isEmpty());
    }

    @Test
    void testFrequency() {
        List<String> testDoc = new ArrayList<>();
        testDoc.add("This eBook is for the use of anyone anywhere in the United States and");
        testDoc.add("most other parts of the world at no cost and with almost no restrictions");
        testDoc.add("Release Date: March 8, 2022 [eBook #67584]");
        testDoc.add("www.gutenberg.org. If you are not located in the United States, you");
        testDoc.add("In the twenty-four hours’ run ending the 14th, according to the posted\n" +
                "reckoning, the ship had covered 546 miles, and we were told that the\n" +
                "next twenty-four hours would see even a better record made.\n" +
                "\n" +
                "Towards evening the report, which I heard, was spread that wireless\n" +
                "messages from passing steamers had been received advising the officers\n" +
                "of our ship of the presence of icebergs and ice-floes. The increasing\n" +
                "cold and the necessity of being more warmly clad when appearing on\n" +
                "deck were outward and visible signs in corroboration of these\n" +
                "warnings. But despite them all no diminution of speed was indicated\n" +
                "and the engines kept up their steady running.\n" +
                "\n" +
                "Not for fifty years, the old sailors tell us, had so great a mass of\n" +
                "ice and icebergs at this time of the year been seen so far south.\n" +
                "\n" +
                "The pleasure and comfort which all of us enjoyed upon this floating\n" +
                "palace, with its extraordinary provisions for such purposes, seemed an\n" +
                "ominous feature to many of us, including myself, who felt it almost\n" +
                "too good to last without some terrible retribution inflicted by the\n" +
                "hand of an angry omnipotence. Our sentiment in this respect was voiced\n" +
                "by one of the most able and distinguished of our fellow passengers,\n" +
                "Mr. Charles M. Hays, President of the Canadian Grand Trunk Railroad.\n" +
                "Engaged as he then was in studying and providing the hotel equipment\n" +
                "along the line of new extensions to his own great railroad system, the\n" +
                "consideration of the subject and of the magnificence of the\n" +
                "_Titanic’s_ accommodations was thus brought home to him. This was the\n" +
                "prophetic utterance with which, alas, he sealed his fate a few hours\n" +
                "thereafter: “The White Star, the Cunard and the Hamburg-American\n" +
                "lines,” said he, “are now devoting their attention to a struggle for\n" +
                "supremacy in obtaining the most luxurious appointments for their\n" +
                "ships, but the time will soon come when the greatest and most\n" +
                "appalling of all disasters at sea will be the result.”");
        solver.textDocAnalyzer(testDoc);
        solver.calculateFiveLetterWordFrequency(solver.allFiveLetterWordList);
        System.out.println(solver.wordFrequencyMap);
    }

    @Test
    void testComparator() {
        List<String> testDoc = new ArrayList<>();
        testDoc.add("ILLUSTRATIONS\n" +
                "\n" +
                "\n" +
                "  Colonel Archibald Gracie                          _Frontispiece_\n" +
                "\n" +
                "                                                       FACING PAGE\n" +
                "  The _Titanic_                                                  2\n" +
                "\n" +
                "  The Promenade Deck of the _Titanic_                           12\n" +
                "\n" +
                "  Mr. and Mrs. Isidor Straus                                    24\n" +
                "\n" +
                "  First-Class Smoking Room                                      28\n" +
                "\n" +
                "  Bedroom of Parlor Suite                                       40\n" +
                "\n" +
                "  Suite Bedroom                                                 40\n" +
                "\n" +
                "  James Clinch Smith                                            48\n" +
                "\n" +
                "  Boilers of the _Titanic_ arranged in Messrs. Harland\n" +
                "    & Wolff’s Works                                             52\n" +
                "\n" +
                "  Thomas Andrews, Jr., Designer of the _Titanic_                58\n" +
                "\n" +
                "  Joseph Bell, Chief Engineer                                   58\n" +
                "\n" +
                "  The Last Photograph of the _Titanic’s_ Commander and\n" +
                "    Three Officers                                              60\n" +
                "\n" +
                "  Passengers of the _Olympic_ awaiting Events                  104\n" +
                "\n" +
                "  The Overturned Engelhardt Boat B                             110\n" +
                "\n" +
                "  The _Titanic_ narrowly Escapes Collision at Southampton      134\n" +
                "\n" +
                "  Fifth Officer Lowe Towing the Canvas Collapsible             158\n" +
                "\n" +
                "  The Canvas Collapsible                                       158\n" +
                "\n" +
                "  Captain Rostron of the S. S. _Carpathia_                     180\n" +
                "\n" +
                "  Photographed from the _Carpathia_                            242\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "THE TRUTH ABOUT THE “TITANIC”\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "CHAPTER I\n" +
                "\n" +
                "THE LAST DAY ABOARD SHIP\n" +
                "\n" +
                "    “There is that Leviathan.”--Ps. 104:26.\n" +
                "\n" +
                "\n" +
                "AS the sole survivor of all the men passengers of the _Titanic_\n" +
                "stationed during the loading of six or more lifeboats with women and\n" +
                "children on the port side of the ship, forward on the glass-sheltered\n" +
                "Deck A, and later on the Boat Deck above, it is my duty to bear\n" +
                "testimony to the heroism on the part of all concerned. First, to my\n" +
                "men companions who calmly stood by until the lifeboats had departed\n" +
                "loaded with women and the available complement of crew, and who,\n" +
                "fifteen to twenty minutes later, sank with the ship, conscious of\n" +
                "giving up their lives to save the weak and the helpless.\n" +
                "\n" +
                "Second, to Second Officer Lightoller and his ship’s crew, who did\n" +
                "their duty as if similar occurrences were matters of daily routine;\n" +
                "and thirdly, to the women, who showed no signs of fear or panic\n" +
                "whatsoever under conditions more appalling than were ever recorded\n" +
                "before in the history of disasters at sea.\n" +
                "\n" +
                "I think those of my readers who are accustomed to tales of thrilling\n" +
                "adventure will be glad to learn first-hand of the heroism displayed on\n" +
                "the _Titanic_ by those to whom it is my privilege and sad duty to pay\n" +
                "this tribute. I will confine the details of my narrative for the most\n" +
                "part to what I personally saw, and did, and heard during that\n" +
                "never-to-be-forgotten maiden trip of the _Titanic_, which ended with\n" +
                "shipwreck and her foundering about 2.22 a. m., Monday, April 15, 1912,\n" +
                "after striking an iceberg “in or near latitude 41 degrees, 46 minutes\n" +
                "N., longitude 50 degrees, 14 minutes W., North Atlantic Ocean,”\n" +
                "whereby the loss of 1490 lives ensued.\n" +
                "\n" +
                "On Sunday morning, April 14th, this marvellous ship, the perfection of\n" +
                "all vessels hitherto conceived by the brain of man, had, for three and\n" +
                "one-half days, proceeded on her way from Southampton to New York over\n" +
                "a sea of glass, so level it appeared, without encountering a ripple\n" +
                "brought on the surface of the water by a storm.\n" +
                "\n" +
                "  [Illustration: THE TITANIC\n" +
                "    (Photographed in Southampton Water the day she sailed)]\n" +
                "\n" +
                "The Captain had each day improved upon the previous day’s speed,\n" +
                "and prophesied that, with continued fair weather, we should make an\n" +
                "early arrival record for this maiden trip. But his reckoning never\n" +
                "took into consideration that Protean monster of the Northern seas\n" +
                "which, even before this, had been so fatal to the navigator’s\n" +
                "calculations and so formidable a weapon of destruction.\n" +
                "\n" +
                "Our explorers have pierced to the furthest north and south of the\n" +
                "icebergs’ retreat, but the knowledge of their habitat, insuring our\n" +
                "great ocean liners in their successful efforts to elude them, has not\n" +
                "reached the detail of time and place where they become detached and\n" +
                "obstruct their path.\n" +
                "\n" +
                "In the twenty-four hours’ run ending the 14th, according to the posted\n" +
                "reckoning, the ship had covered 546 miles, and we were told that the\n" +
                "next twenty-four hours would see even a better record made.\n" +
                "\n" +
                "Towards evening the report, which I heard, was spread that wireless\n" +
                "messages from passing steamers had been received advising the officers\n" +
                "of our ship of the presence of icebergs and ice-floes. The increasing\n" +
                "cold and the necessity of being more warmly clad when appearing on\n" +
                "deck were outward and visible signs in corroboration of these\n" +
                "warnings. But despite them all no diminution of speed was indicated\n" +
                "and the engines kept up their steady running.\n" +
                "\n" +
                "Not for fifty years, the old sailors tell us, had so great a mass of\n" +
                "ice and icebergs at this time of the year been seen so far south.\n" +
                "\n" +
                "The pleasure and comfort which all of us enjoyed upon this floating\n" +
                "palace, with its extraordinary provisions for such purposes, seemed an\n" +
                "ominous feature to many of us, including myself, who felt it almost\n" +
                "too good to last without some terrible retribution inflicted by the\n" +
                "hand of an angry omnipotence. Our sentiment in this respect was voiced\n" +
                "by one of the most able and distinguished of our fellow passengers,\n" +
                "Mr. Charles M. Hays, President of the Canadian Grand Trunk Railroad.\n" +
                "Engaged as he then was in studying and providing the hotel equipment\n" +
                "along the line of new extensions to his own great railroad system, the\n" +
                "consideration of the subject and of the magnificence of the\n" +
                "_Titanic’s_ accommodations was thus brought home to him. This was the\n" +
                "prophetic utterance with which, alas, he sealed his fate a few hours\n" +
                "thereafter: “The White Star, the Cunard and the Hamburg-American\n" +
                "lines,” said he, “are now devoting their attention to a struggle for\n" +
                "supremacy in obtaining the most luxurious appointments for their\n" +
                "ships, but the time will soon come when the greatest and most\n" +
                "appalling of all disasters at sea will be the result.”\n" +
                "\n" +
                "In the various trips which I have made across the Atlantic, it has\n" +
                "been my custom aboard ship, whenever the weather permitted, to take as\n" +
                "much exercise every day as might be needful to put myself in prime\n" +
                "physical condition, but on board the _Titanic_, during the first days\n" +
                "of the voyage, from Wednesday to Saturday, I had departed from this,\n" +
                "my usual self-imposed regimen, for during this interval I had devoted\n" +
                "my time to social enjoyment and to the reading of books taken from the\n" +
                "ship’s well-supplied library. I enjoyed myself as if I were in a\n" +
                "summer palace on the seashore, surrounded with every comfort--there\n" +
                "was nothing to indicate or suggest that we were on the stormy Atlantic\n" +
                "Ocean. The motion of the ship and the noise of its machinery were\n" +
                "scarcely discernible on deck or in the saloons, either day or night.\n" +
                "But when Sunday morning came, I considered it high time to begin my\n" +
                "customary exercises, and determined for the rest of the voyage to\n" +
                "patronize the squash racquet court, the gymnasium, the swimming pool,\n" +
                "etc. I was up early before breakfast and met the professional racquet\n" +
                "player in a half hour’s warming up, preparatory for a swim in the\n" +
                "six-foot deep tank of salt water, heated to a refreshing temperature.\n" +
                "In no swimming bath had I ever enjoyed such pleasure before. How\n" +
                "curtailed that enjoyment would have been had the presentiment come to\n" +
                "me telling how near it was to being my last plunge, and that before\n" +
                "dawn of another day I would be swimming for my life in mid-ocean,\n" +
                "under water and on the surface, in a temperature of 28 degrees\n" +
                "Fahrenheit!\n");
        testDoc.add("This eBook is for the use of anyone anywhere in the United States and");
        testDoc.add("most other parts of the world at no cost and with almost no restrictions");
        testDoc.add("Release Date: March 8, 2022 [eBook #67584]");
        testDoc.add("www.gutenberg.org. If you are not located in the United States, you");
        testDoc.add("In the twenty-four hours’ run ending the 14th, according to the posted\n" +
                "reckoning, the ship had covered 546 miles, and we were told that the\n" +
                "next twenty-four hours would see even a better record made.\n" +
                "\n" +
                "Towards evening the report, which I heard, was spread that wireless\n" +
                "messages from passing steamers had been received advising the officers\n" +
                "of our ship of the presence of icebergs and ice-floes. The increasing\n" +
                "cold and the necessity of being more warmly clad when appearing on\n" +
                "deck were outward and visible signs in corroboration of these\n" +
                "warnings. But despite them all no diminution of speed was indicated\n" +
                "and the engines kept up their steady running.\n" +
                "\n" +
                "Not for fifty years, the old sailors tell us, had so great a mass of\n" +
                "ice and icebergs at this time of the year been seen so far south.\n" +
                "\n" +
                "The pleasure and comfort which all of us enjoyed upon this floating\n" +
                "palace, with its extraordinary provisions for such purposes, seemed an\n" +
                "ominous feature to many of us, including myself, who felt it almost\n" +
                "too good to last without some terrible retribution inflicted by the\n" +
                "hand of an angry omnipotence. Our sentiment in this respect was voiced\n" +
                "by one of the most able and distinguished of our fellow passengers,\n" +
                "Mr. Charles M. Hays, President of the Canadian Grand Trunk Railroad.\n" +
                "Engaged as he then was in studying and providing the hotel equipment\n" +
                "along the line of new extensions to his own great railroad system, the\n" +
                "consideration of the subject and of the magnificence of the\n" +
                "_Titanic’s_ accommodations was thus brought home to him. This was the\n" +
                "prophetic utterance with which, alas, he sealed his fate a few hours\n" +
                "thereafter: “The White Star, the Cunard and the Hamburg-American\n" +
                "lines,” said he, “are now devoting their attention to a struggle for\n" +
                "supremacy in obtaining the most luxurious appointments for their\n" +
                "ships, but the time will soon come when the greatest and most\n" +
                "appalling of all disasters at sea will be the result.”");
        List<String> testListToSort = new ArrayList<>();
        testListToSort.add("march");
        testListToSort.add("would");
        testListToSort.add("which");
        testListToSort.add("other");
        testListToSort.add("their");
        solver.textDocAnalyzer(testDoc);
        solver.calculateFiveLetterWordFrequency(solver.allFiveLetterWordList);
       // testListToSort.sort(solver.guess(testList).fiveLetterWordComparator);
        System.out.println(testListToSort);
    }

}
