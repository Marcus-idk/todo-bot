package org.todobot.parsers.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeParser {
    private static final DateTimeFormatter INPUT_DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter INPUT_DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private static final DateTimeFormatter OUTPUT_DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy, HHmm");
    private static final DateTimeFormatter OUTPUT_DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");
    
    public static class DateTimeResult {
        private final LocalDateTime dateTime;
        private final boolean hasTime;
        
        public DateTimeResult(LocalDateTime dateTime, boolean hasTime) {
            this.dateTime = dateTime;
            this.hasTime = hasTime;
        }
        
        public LocalDateTime getDateTime() {
            return dateTime;
        }
        
        public boolean hasTime() {
            return hasTime;
        }
    }
    
    public static DateTimeResult parseDateTime(String input) throws DateTimeParseException {
         if (input == null) {
            throw new DateTimeParseException("Date/time input cannot be empty", "", 0);
        }
        if (input.trim().isEmpty()) {
            throw new DateTimeParseException("Date/time input cannot be empty", input, 0);
        }             
        
        String trimmedInput = input.trim();
        
        // Try to parse as date with time first (DD-MM-YYYY HH:MM)
        // Regex: Matches 2 digits-2 digits-4 digits, followed by whitespace, then 2 digits:2 digits (date with time format)
        if (trimmedInput.matches("\\d{2}-\\d{2}-\\d{4}\\s+\\d{2}:\\d{2}")) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(trimmedInput, INPUT_DATETIME_FORMAT);
                return new DateTimeResult(dateTime, true);
            } catch (DateTimeParseException e) {
                throw new DateTimeParseException("Invalid date/time format. Expected: DD-MM-YYYY HH:MM", 
                    trimmedInput, e.getErrorIndex());
            }
        }
        
        // Try to parse as date only (DD-MM-YYYY)
        // Regex: Matches exactly 2 digits-2 digits-4 digits (date only format)
        if (trimmedInput.matches("\\d{2}-\\d{2}-\\d{4}")) {
            try {
                LocalDate date = LocalDate.parse(trimmedInput, INPUT_DATE_FORMAT);
                LocalDateTime dateTime = date.atStartOfDay();
                return new DateTimeResult(dateTime, false);
            } catch (DateTimeParseException e) {
                throw new DateTimeParseException("Invalid date format. Expected: DD-MM-YYYY", 
                    trimmedInput, e.getErrorIndex());
            }
        }
        
        throw new DateTimeParseException("Invalid format. Expected: DD-MM-YYYY or DD-MM-YYYY HH:MM", 
            trimmedInput, 0);
    }
    
    public static String formatDateTime(LocalDateTime dateTime, boolean hasTime) {
        if (hasTime) {
            return dateTime.format(OUTPUT_DATETIME_FORMAT);
        } else {
            return dateTime.format(OUTPUT_DATE_FORMAT);
        }
    }
}