package org.todobot.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.todobot.common.BotMessages;
import org.todobot.common.CommandType;

public class NumericParserTest {
    
    private MarkParser markParser;
    private DeleteParser deleteParser;
    
    private static final String VALID_NUMBER_1 = "1";
    private static final String VALID_NUMBER_42 = "42";
    private static final String VALID_NUMBER_999 = "999";
    private static final String VALID_NUMBER_WITH_SPACES = "  5  ";
    
    private static final String INVALID_ZERO = "0";
    private static final String INVALID_NEGATIVE = "-1";
    private static final String INVALID_DECIMAL = "1.5";
    private static final String INVALID_ALPHANUMERIC = "1a";
    private static final String INVALID_TEXT = "abc";
    private static final String INVALID_MIXED = "a1b";
    private static final String EMPTY_STRING = "";
    private static final String WHITESPACE_ONLY = "   ";
    
    @BeforeEach
    void setUp() {
        markParser = new MarkParser();
        deleteParser = new DeleteParser();
    }
    
    // Valid number formats
    @Test
    void shouldParseValidSingleDigitNumber() {
        ParseResult result = markParser.parse(VALID_NUMBER_1);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.MARK, result.getCommandType());
        assertEquals("1", result.getArguments()[0]);
    }
    
    @Test
    void shouldParseValidTwoDigitNumber() {
        ParseResult result = markParser.parse(VALID_NUMBER_42);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.MARK, result.getCommandType());
        assertEquals("42", result.getArguments()[0]);
    }
    
    @Test
    void shouldParseValidThreeDigitNumber() {
        ParseResult result = markParser.parse(VALID_NUMBER_999);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.MARK, result.getCommandType());
        assertEquals("999", result.getArguments()[0]);
    }
    
    @Test
    void shouldParseNumberWithLeadingAndTrailingSpaces() {
        ParseResult result = markParser.parse(VALID_NUMBER_WITH_SPACES);
        
        assertTrue(result.isValid());
        assertEquals(CommandType.MARK, result.getCommandType());
        assertEquals("5", result.getArguments()[0]);
    }
    
    @Test
    void shouldWorkWithDifferentNumericParserSubclasses() {
        ParseResult markResult = markParser.parse(VALID_NUMBER_42);
        ParseResult deleteResult = deleteParser.parse(VALID_NUMBER_42);
        
        assertTrue(markResult.isValid());
        assertTrue(deleteResult.isValid());
        assertEquals(CommandType.MARK, markResult.getCommandType());
        assertEquals(CommandType.DELETE, deleteResult.getCommandType());
        assertEquals("42", markResult.getArguments()[0]);
        assertEquals("42", deleteResult.getArguments()[0]);
    }
    
    // Invalid number formats
    @Test
    void shouldRejectZero() {
        ParseResult result = markParser.parse(INVALID_ZERO);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_NUMBER_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectNegativeNumber() {
        ParseResult result = markParser.parse(INVALID_NEGATIVE);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_NUMBER_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectDecimalNumber() {
        ParseResult result = markParser.parse(INVALID_DECIMAL);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_NUMBER_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectAlphanumericString() {
        ParseResult result = markParser.parse(INVALID_ALPHANUMERIC);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_NUMBER_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectTextString() {
        ParseResult result = markParser.parse(INVALID_TEXT);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_NUMBER_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectMixedAlphanumericString() {
        ParseResult result = markParser.parse(INVALID_MIXED);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_NUMBER_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectEmptyString() {
        ParseResult result = markParser.parse(EMPTY_STRING);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_NUMBER_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectWhitespaceOnlyString() {
        ParseResult result = markParser.parse(WHITESPACE_ONLY);
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_NUMBER_FORMAT, result.getErrorMessage());
    }
    
    // Edge cases
    @Test
    void shouldRejectNumberStartingWithZero() {
        ParseResult result = markParser.parse("01");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_NUMBER_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectNumberWithInternalSpaces() {
        ParseResult result = markParser.parse("1 2");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_NUMBER_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectNumberWithPlusSign() {
        ParseResult result = markParser.parse("+5");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_NUMBER_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectVeryLargeNumber() {
        // Test with a very large number to ensure regex handles it
        ParseResult result = markParser.parse("123456789012345");
        
        assertTrue(result.isValid());
        assertEquals("123456789012345", result.getArguments()[0]);
    }
    
    @Test
    void shouldRejectSpecialCharacters() {
        ParseResult result = markParser.parse("1!");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_NUMBER_FORMAT, result.getErrorMessage());
    }
    
    @Test
    void shouldRejectHexNumber() {
        ParseResult result = markParser.parse("0x5");
        
        assertFalse(result.isValid());
        assertEquals(BotMessages.INVALID_NUMBER_FORMAT, result.getErrorMessage());
    }
}