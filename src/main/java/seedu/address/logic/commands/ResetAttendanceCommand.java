package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Set;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.person.Student;
import seedu.address.model.tag.Tag;

/**
 * Resets the AttendanceCount of a person to 0.
 */
public class ResetAttendanceCommand extends Command {

    public static final String COMMAND_WORD = "reset-att";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Resets all attendance to 0.";

    public static final String MESSAGE_RESET_ATTENDANCE_SUCCESS = "Attendance set to 0 for all students";



    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        for (Person p : lastShownList) {
            if (p instanceof Student) {
                Student studentToReset = (Student) p;
                Student resetStudent = createNewStudentWithZeroAttendance(studentToReset);
                model.setPerson(p, resetStudent);
            }
        }

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_RESET_ATTENDANCE_SUCCESS));
    }

    /**
     * Creates and returns a {@code Person} with AttendanceCount of 0.
     * edited with {@code editPersonDescriptor}.
     */
    static Student createNewStudentWithZeroAttendance(Student studentToReset) {
        assert studentToReset != null;

        Name name = studentToReset.getName();
        Role role = studentToReset.getRole();
        Phone phone = studentToReset.getPhone();
        Email email = studentToReset.getEmail();
        Address address = studentToReset.getAddress();
        Set<Tag> tags = studentToReset.getTags();
        return new Student(name, role, phone, email, address, tags);
    }
}
