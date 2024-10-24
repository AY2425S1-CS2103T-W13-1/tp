package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AttendanceCountTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AttendanceCount(null));
    }

    @Test
    public void constructor_invalidAttendanceCount_throwsIllegalArgumentException() {
        String invalidCount = "";
        assertThrows(IllegalArgumentException.class, () -> new AttendanceCount(invalidCount));
    }

    @Test
    public void isValidAttendanceCount() {
        // null count
        assertThrows(NullPointerException.class, () -> AttendanceCount.isValidAttendanceCount(null));

        // invalid attendance count
        assertFalse(AttendanceCount.isValidAttendanceCount("asdfasdf")); // not numeric
        assertFalse(AttendanceCount.isValidAttendanceCount(" ")); // spaces only


        // valid attendance count
        assertTrue(AttendanceCount.isValidAttendanceCount("1"));
        assertTrue(AttendanceCount.isValidAttendanceCount("123"));
    }

    @Test
    public void equals() {
        AttendanceCount count = new AttendanceCount("3");

        // same values -> returns true
        assertTrue(count.equals(new AttendanceCount("3")));

        // same object -> returns true
        assertTrue(count.equals(count));

        // null -> returns false
        assertFalse(count.equals(null));

        // different types -> returns false
        assertFalse(count.equals(5.0f));

        // different values -> returns false
        assertFalse(count.equals(new AttendanceCount("4")));
    }
}
