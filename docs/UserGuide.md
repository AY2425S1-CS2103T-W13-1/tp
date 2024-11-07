---
layout: page
title: User Guide
---

Cher is a **desktop app for managing contacts, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, Cher can get your contact management tasks done faster than traditional GUI apps.

* Table of Contents
{:toc}
--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.

1. Download the latest `.jar` file from [here](https://github.com/AY2425S1-CS2103T-W13-1/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for Cher.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar cher.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe r/student p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to Cher.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaning how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a person to Cher.

Format: `add n/NAME r/ROLE p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​`

Role can either be ```student``` or ```parent```, case-insensitive.

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A person can have any number of tags (including 0)
</div>

Examples:
* `add n/John Doe r/student p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy Crowe r/parent e/betsycrowe@example.com a/Newgate Prison p/1234567`

### Listing all persons : `list`

Shows a list of all persons in Cher.

Format: `list`

### Editing a person : `edit`

Edits an existing person in the Cher.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without
    specifying any tags after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

### Locating persons by name: `find`

Finds persons whose names contain any of the given keywords.

Format: `find [INDEX] [n/KEYWORDS] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

* Searches for the person using the given attribute
* For search by name,
  - The name search is case-insensitive. e.g `hans` will match `Hans`
  - The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
  - Partial words will be matched if they appear at the start of any word in the name. For example:
    - Searching for `Han` will match `Hans Bo`
    - Searching for `Bo` will match `Hans Bo`
    - However, searching for `ans` will not match `Hans Bo`, as `ans` does not appear at the start of a word.
  - Persons matching at least one keyword will be returned (i.e. `OR` search).
    - e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find p/98765432` Finds the person with phone number `98765432`
* `find n/alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Sorting persons by name: `sort`

Sorts list of persons alphabetically by specified predicate.

Format: `sort PREDICATE`

* Sorts the list alphabetically by given predicate.
* The predicate refers to attributes used to sort the list by.
* The predicate **must be a valid attribute** name, role, phone, email and address only

Examples:
* `sort name` sorts the list alphabetically by name.
  ![result for 'sort name'](images/sortMessage.png)
* `sort role` sorts the list alphabetically by role.
* `sort phone` sorts the list alphabetically by phone.
* `sort email` sorts the list alphabetically by email.
* `sort address` sorts the list alphabetically by address.

### Deleting a person : `delete`

Deletes the specified person from Cher.

Format: `delete [INDEX] [n/KEYWORDS] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…`

* Deletes the person with the specified attribute
* The constraints for keywords are the same as find
* If duplicates are found, a list of duplicates is returned

Examples:
* `delete 1` Delete the first person from the list
* `delete n/mithuna john` Returns a list showing persons containing the words `Mithuna` or `John`

### Deleting in a batch : `batch-delete`

Delete all contacts from Cher that contain **all** the specified tags.

Format: `batch-delete t/TAG [t/TAG]...`

Examples:
![Batch delete example data](images/ForBatchDeleteExampleData.png)
* `batch-delete t/friends` will delete both `Alex Yeoh` and `Bernice Yu`.
* `batch-delete t/friends t/colleagues` will delete only `Bernice Yu`.

### Selecting persons by index: `select`
Select contacts from the address book by specifying their index numbers in the currently displayed list. Only the specified contacts will remain in view.

Format: select INDEX [MORE_INDEXES]...

Examples:

* `select 1 2` will select the contacts at index `1` and `2` in the displayed list, showing only those contacts.
* `select 3 5 7` will select the contacts at indexes `3`, `5`, and `7` in the displayed list, filtering to show only these selected contacts.

### Editing tag in a batch: `batch-edit`
Changes all contacts from cher with containing the specified tags with a new tag.

Format: `batch-edit t/OLDTAG t/NEWTAG`

Examples:
![Batch delete example data](images/ForBatchDeleteExampleData.png)
* `batch-edit t/friends t/fren` will change the `friends` tag of both `Alex Yeoh` and `Bernice Yu` to `fren`.



### Clearing all entries : `clear`

Clears all entries from Cher.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

Data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

Data are saved automatically as a JSON file `[JAR file location]/data/cher.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, Cher will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the Cher to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous Cher home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME r/ROLE p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `add n/James Ho r/student p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/Secondary 1`
**Clear** | `clear`
**Delete** | `delete INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g., `delete 3`
**Batch-Delete**| `batch-delete t/TAG [t/TAG]...`<br> e.g. `batch-delete t/friends t/colleagues t/owesmoney t/...`
**Batch-Edit**| `batch-edit t/OLDTAG t/NEWTAG`<br> e.g. `batch-delete t/friends t/frens`
**Edit** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Find** | `find INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g., `find n/James Jake`
**List** | `list`
**Help** | `help`
