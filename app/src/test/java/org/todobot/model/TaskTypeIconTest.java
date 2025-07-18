package org.todobot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TaskTypeIconTest {
    
    @Test
    void shouldReturnCorrectIconsForAllTypes() {
        assertEquals("T", TaskTypeIcon.TODO.getIcon());
        assertEquals("D", TaskTypeIcon.DEADLINE.getIcon());
        assertEquals("E", TaskTypeIcon.EVENT.getIcon());
    }
}