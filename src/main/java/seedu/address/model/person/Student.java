package seedu.address.model.person;

import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Student in the address book. Student is a subclass of Person and has AttendanceCount.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Student extends Person {
    private final AttendanceCount attendanceCount;

    /**
     * Constructs a new Student.
     * Every field must be present and not null.
     */
    public Student(Name name, Role role, Phone phone, Email email, Address address,
                  Set<Tag> tags, AttendanceCount attendanceCount) {
        super(name, role, phone, email, address, tags);
        this.attendanceCount = attendanceCount;
    }

    /**
     * Constructs a new Student with attendanceCount of 0.
     */
    public Student(Name name, Role role, Phone phone, Email email, Address address,
                   Set<Tag> tags) {
        super(name, role, phone, email, address, tags);
        this.attendanceCount = new AttendanceCount("0");
    }
    public AttendanceCount getAttendanceCount() {
        return attendanceCount;
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(getName(), getRole(), getPhone(), getEmail(), getAddress(),
                getTags(), attendanceCount);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", getName())
                .add("role", getRole())
                .add("phone", getPhone())
                .add("email", getEmail())
                .add("address", getAddress())
                .add("tags", getTags())
                .add("attendanceCount", attendanceCount)
                .toString();
    }

}
