package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.AttendanceCount;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

public class ResetAttendanceCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_resetAttendance_success() {
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        for (Person p : expectedModel.getFilteredPersonList()) {
            Person personWithResetAttendance = new Person(p.getName(), p.getRole(),
                    p.getPhone(), p.getEmail(), p.getAddress(), p.getTags(), new AttendanceCount("0"));
            expectedModel.setPerson(p, personWithResetAttendance);
        }
        ResetAttendanceCommand resetAttendanceCommandCommand = new ResetAttendanceCommand();

        String expectedMessage = String.format(ResetAttendanceCommand.MESSAGE_RESET_ATTENDANCE_SUCCESS);

        assertCommandSuccess(resetAttendanceCommandCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void createNewPersonWithZeroAttendance_success() {
        Student newAmy = ResetAttendanceCommand.createNewStudentWithZeroAttendance(AMY);
        assertNotEquals(new AttendanceCount("0"), AMY.getAttendanceCount());
        assertEquals(new AttendanceCount("0"), newAmy.getAttendanceCount());

    }
}
