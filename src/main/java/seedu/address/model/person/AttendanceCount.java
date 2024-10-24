package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's Attendance Count in the address book.
 */
public class AttendanceCount {
    public static final String MESSAGE_CONSTRAINTS = "The attendance count is invalid.";
    public static final String VALIDATION_REGEX = "\\d+";
    private Integer count;


    /**
     * Constructs a {@code AttendanceCount}.
     *
     * @param count A valid attendance count.
     */
    public AttendanceCount(String count) {
        requireNonNull(count);
        checkArgument(isValidAttendanceCount(count), MESSAGE_CONSTRAINTS);
        this.count = Integer.parseInt(count);
    }

    public static boolean isValidAttendanceCount(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns a new AttendanceCount object with incremented attendance count.
     */
    public AttendanceCount increment() {
        Integer newCount = this.count + 1;
        return new AttendanceCount(newCount.toString());
    }

    /**
     * Returns a new AttendanceCount object with decremented attendance count.
     */
    public AttendanceCount decrement() {
        if (this.count == 0) {
            return this;
        } else {
            Integer newCount = this.count - 1;
            return new AttendanceCount(newCount.toString());
        }
    }

    /**
     * Returns count as an int.
     */
    public int integerCount() {
        return this.count;
    }

    @Override
    public String toString() {
        return String.valueOf(count);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AttendanceCount)) {
            return false;
        }

        AttendanceCount otherAttendenceCount = (AttendanceCount) other;
        return count.equals(otherAttendenceCount.count);
    }

    @Override
    public int hashCode() {
        return count.hashCode();
    }

}
