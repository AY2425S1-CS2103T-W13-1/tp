package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.HOON;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(HOON.isSamePerson(HOON));

        // null -> returns false
        assertFalse(HOON.isSamePerson(null));

        // same phone number, all other attributes different -> returns true
        Person differentPersonSamePhone = new PersonBuilder(BOB).withPhone(HOON.getPhone().value).build();
        assertTrue(HOON.isSamePerson(differentPersonSamePhone));

        // different phone number, all other attributes same -> false
        Person differentPhone = new PersonBuilder(HOON).withPhone(VALID_PHONE_BOB).build();
        assertFalse(HOON.isSamePerson(differentPhone));

        // different person -> false
        assertFalse(HOON.isSamePerson(AMY));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(HOON).build();
        assertTrue(HOON.equals(aliceCopy));

        // same object -> returns true
        assertTrue(HOON.equals(HOON));

        // null -> returns false
        assertFalse(HOON.equals(null));

        // different type -> returns false
        assertFalse(HOON.equals(5));

        // different person -> returns false
        assertFalse(HOON.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(HOON).withName(VALID_NAME_BOB).build();
        assertFalse(HOON.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(HOON).withPhone(VALID_PHONE_BOB).build();
        assertFalse(HOON.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(HOON).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(HOON.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(HOON).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(HOON.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(HOON).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(HOON.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + HOON.getName() + ", role=" + HOON.getRole()
                + ", phone=" + HOON.getPhone() + ", email=" + HOON.getEmail() + ", address=" + HOON.getAddress()
                + ", tags=" + HOON.getTags() + "}";
        assertEquals(expected, HOON.toString());
    }
}
