package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.AttendanceCount;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.person.Student;
import seedu.address.model.tag.Tag;

/**
 * Unmarks the attendance of an existing person.
 */
public class UnmarkCommand extends Command {
    public static final String COMMAND_WORD = "unmark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unmarks the attendance of person by index number";

    public static final String MESSAGE_UNMARK_PERSON_SUCCESS = "Unmarked attendance for: %1$s";
    public static final String MESSAGE_CANNOT_MARK_PARENT = "You can't unmark attendance for a parent";
    public static final String MESSAGE_CANNOT_UNMARK_FURTHER = "attendance count is already at 0";
    private final Index targetIndex;

    /**
     * @param targetIndex of the person in the filtered person list to edit
     */
    public UnmarkCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToUnmark = lastShownList.get(targetIndex.getZeroBased());
        if (!(personToUnmark instanceof Student)) {
            return new CommandResult(String.format(MESSAGE_CANNOT_MARK_PARENT));
        } else {
            Student studentToUnmark = (Student) personToUnmark;
            if (studentToUnmark.getAttendanceCount().integerCount() == 0) {
                return new CommandResult(Messages.getNameOnly(personToUnmark) + "'s "
                        + String.format(MESSAGE_CANNOT_UNMARK_FURTHER));
            } else {
                Student unmarkedStudent = createNewUnmarkedStudent(studentToUnmark);
                model.setPerson(personToUnmark, unmarkedStudent);
                model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
                return new CommandResult(String.format(MESSAGE_UNMARK_PERSON_SUCCESS,
                        Messages.getNameOnly(unmarkedStudent)));
            }
        }
    }

    private Student createNewUnmarkedStudent(Student selectedStudent) {
        assert selectedStudent != null;
        Name name = selectedStudent.getName();
        Role role = selectedStudent.getRole();
        Phone phone = selectedStudent.getPhone();
        Email email = selectedStudent.getEmail();
        Address address = selectedStudent.getAddress();
        Set<Tag> tags = selectedStudent.getTags();
        AttendanceCount attendanceCount = selectedStudent.getAttendanceCount().decrement();
        return new Student(name, role, phone, email, address, tags, attendanceCount);

    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnmarkCommand)) {
            return false;
        }

        return Objects.equals(targetIndex, ((UnmarkCommand) other).targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
