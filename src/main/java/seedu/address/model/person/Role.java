package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class Role {
    public static final String MESSAGE_CONSTRAINTS =
            "The role should be either student, parent, teacher or staff, and it should not be blank";

    public final String roleName;

    public Role(String role) {
        requireNonNull(role);
        checkArgument(isValidRole(role), MESSAGE_CONSTRAINTS);
        roleName = role;
    }

    public static boolean isValidRole(String test) {
        return test.toLowerCase().equals("student")
                || test.toLowerCase().equals("parent")
                || test.toLowerCase().equals("teacher")
                || test.toLowerCase().equals("staff");
    }

    @Override
    public String toString() {
        return roleName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Role otherRole = (Role) other;
        return roleName.equals(otherRole.roleName);
    }

    @Override
    public int hashCode() {
        return roleName.hashCode();
    }



}
