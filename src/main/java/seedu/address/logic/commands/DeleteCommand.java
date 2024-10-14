package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list "
            + "or by phone number.\n"
            + "Parameters: INDEX (must be a positive integer) or PHONE_NUMBER\n"
            + "Example: " + COMMAND_WORD + " 1 or " + COMMAND_WORD + " 98765432";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    private final Index targetIndex;
    private final Phone phoneNumber;
    private final NameContainsKeywordsPredicate predicate;


    //overload with constructor to delete using name --> partial

    public DeleteCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
        this.phoneNumber = null;
        this.targetIndex = null;
    }

    /**
     * Initializes command to delete a person identified using it's displayed index
     * Number from the address book.
     */
    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
        this.phoneNumber = null;
        this.predicate = null;
    }

    /**
     * Initializes command to delete a person identified using it's displayed phone Number
     * from the address book.
     */
    public DeleteCommand(Phone phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.targetIndex = null;
        this.predicate = null;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        Person personToDelete;

        // deleting by index
        if (targetIndex != null) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            personToDelete = lastShownList.get(targetIndex.getZeroBased());

        // deleting by phone Number,
        // check for validity of arguments are done in DeleteCommandParser
        } else if (phoneNumber != null) {
            personToDelete = findPersonToDeleteByPhoneNumber(lastShownList, phoneNumber);

            // no person with given phone number
            if (personToDelete == null) {
                throw new CommandException(Messages.MESSAGE_INVALID_PHONE_NUMBER);
            }
        } else {
            model.updateFilteredPersonList(predicate);
            if (model.getFilteredPersonList().size() == 1) {
                personToDelete = model.getFilteredPersonList().get(0);
            } else {
                return new CommandResult(
                        String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
            }
        }

        model.deletePerson(personToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
    }

    /**
     * Finds a person in the list by their phone number.
     *
     * @param lastShownList The list of persons currently shown.
     * @param phoneNumber The phone number of the person to delete.
     * @return The person to delete, or null if no matching person is found.
     */
    private Person findPersonToDeleteByPhoneNumber(List<Person> lastShownList, Phone phoneNumber) {
        for (Person person : lastShownList) {
            Phone phoneNumberOfPerson = person.getPhone();
            if (phoneNumberOfPerson.equals(phoneNumber)) {
                return person;
            }
        }
        return null; //no person found with given phone number
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        if (targetIndex != null) {
            return targetIndex.equals(otherDeleteCommand.targetIndex);
        } else {
            return phoneNumber.equals(otherDeleteCommand.phoneNumber);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
