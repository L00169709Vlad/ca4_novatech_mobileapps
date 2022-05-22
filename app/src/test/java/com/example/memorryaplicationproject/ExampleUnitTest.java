package com.example.memorryaplicationproject;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void shuffle_word_does_not_change_size() {
        String originalString = "originalString";
        String shuffledString = ArrangeLetterActivity.shuffledWord(originalString);

        assertEquals(originalString.length(), shuffledString.length());
    }

    @Test
    public void matchColourActivity_setButtonPosition_returns_correct_position_under_9() {
        int originalPosition = 4;

        assertEquals(MatchColourActivity.setButtonPosition(originalPosition), originalPosition);
    }

    @Test
    public void matchNumberActivity_generateNumber_returns_string_with_correct_number_of_digits() {
        int digits = 9;

        assertEquals(MatchNumberActivity.generateNumber(digits).length(), digits);
    }

    @Test
    public void matchNumberActivity_generateNumber_returns_empty_string_when_given_negative_number() {
        int digits = -9;

        assertEquals(MatchNumberActivity.generateNumber(digits).length(), 0);
    }
}