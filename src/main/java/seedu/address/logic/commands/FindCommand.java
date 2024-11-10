package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.prefix.PrefixHandler;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.person.predicates.FilterListPredicate;
import seedu.address.model.person.predicates.NameContainsKeywordsPredicate;
import seedu.address.model.tag.Tag;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds all persons that match the specified attributes "
            + "and displays them as a list with index numbers.\n"
            + "Parameters: [INDEX] "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_PHONE + "91234567 ";

    private Index targetIndex = null;
    private Phone phoneNumber = null;
    private NameContainsKeywordsPredicate predicate = null;
    private Role role = null;
    private Address address = null;
    private Email email = null;
    private Set<Tag> tags = null;

    private PrefixHandler prefixHandler = new PrefixHandler();
    public FindCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public FindCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    public FindCommand(Phone phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public FindCommand(Address address) {
        this.address = address;
    }

    public FindCommand(Email email) {
        this.email = email;
    }

    public FindCommand(Set<Tag> tags) {
        this.tags = tags;
    }

    public FindCommand(Role role) {
        this.role = role;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        FilterListPredicate filter;
        List<Person> personsToFind = new ArrayList<>();
        if (predicate != null) {
            model.updateFilteredPersonList(predicate);
            return new CommandResult(
                    String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
        } else if (targetIndex != null) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            Person personToFind = lastShownList.get(targetIndex.getZeroBased());
            personsToFind.add(personToFind);
        } else if (phoneNumber != null) {
            personsToFind = prefixHandler.findPersonByAttribute(lastShownList, phoneNumber);
        } else if (address != null) {
            personsToFind = prefixHandler.findPersonByAttribute(lastShownList, address);
        ;
        } else if (email != null) {
            personsToFind = prefixHandler.findPersonByAttribute(lastShownList, email);

        } else if (role != null) {
            personsToFind = prefixHandler.findPersonByAttribute(lastShownList, role);
        } else {
            personsToFind = prefixHandler.findPersonByAttribute(lastShownList, tags);

        }

        filter = new FilterListPredicate(personsToFind);
        model.updateFilteredPersonList(filter);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        if (targetIndex != null) {
            return targetIndex.equals(otherFindCommand.targetIndex);
        } else if (phoneNumber != null) {
            return phoneNumber.equals(otherFindCommand.phoneNumber);
        } else if (address != null) {
            return address.equals(otherFindCommand.address);
        } else if (email != null) {
            return email.equals(otherFindCommand.email);
        } else if (tags != null) {
            return tags.equals(otherFindCommand.tags);
        } else if (predicate != null) {
            return predicate.equals(otherFindCommand.predicate);
        } else {
            return role.equals(otherFindCommand.role);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
