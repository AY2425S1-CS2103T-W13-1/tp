---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2425S1-CS2103T-W13-1/tp/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2425S1-CS2103T-W13-1/tp/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2425S1-CS2103T-W13-1/tp/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2425S1-CS2103T-W13-1/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2425S1-CS2103T-W13-1/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S1-CS2103T-W13-1/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2425S1-CS2103T-W13-1/tp/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S1-CS2103T-W13-1/tp/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user**: Private School teachers, administrators.

**Value proposition**: Simplifies contact management by providing an all-in-one user-friendly interface for teachers/staff in private education institutions. Ease their pain of manually tracking things like attendance and parents/students’ contact.

**Target user profile 1**: Independent Private School Teachers

* private school teachers, often working independently, managing their classes and responsibilities on their own.
* private school teachers who manage multiple classes of different levels
* have a need to track and organise details of students and parents/guardians (e.g. phone numbers, emails, addresses)
* have a need to efficiently access information regarding students and parents/guardians for communication during various situations (e.g. parent-teacher meetings, emergencies, administrative tasks, payment)
* have a need to edit data regarding students and parents/guardians in case of updates

**Target user profile 2**: Private School Teachers working under administrations

* private school teachers who work alongside or under the supervision of education administrators or managers.
* private school teachers who manage multiple classes across various subjects
* have a need to track and organise details of students (e.g. phone numbers, emails, addresses)
* Parent/Guardian contacts are overseen by institution admin staff
* have a need to efficiently access information regarding students for communication during various situations (e.g. emergencies, administrative tasks, class schedules)
* have a need to edit data regarding students in case of updates

**Target user profile 3**: Private Education Institution Managers

* Managing the overall administration of the institution
* have a need to track and organise details of students, parents/guardians, teachers and vendors (e.g. phone numbers, emails, addresses)
* have a need to efficiently access information regarding students, parents/guardians, teachers and vendors for communication during various situations (e.g. parent-teacher meetings, emergencies, administrative tasks)
* have a need to edit data regarding students, parents/guardians, teachers and vendors in case of updates in contact information


### User stories


Priorities: High (Must-Have), Medium (Nice-to-Have), Low (Could-Have), Trivial (Won't-Have))


| Priority      | As a/an           | I want to …                                                      | So that I can…                                                     |
|---------------|-------------------|------------------------------------------------------------------|--------------------------------------------------------------------|
| **Must-Have** | Novice user       | add new contact information (phone-number, email)                | keep the contact information in one place                          |
| **Must-Have** | Novice user       | view contact information                                         | view the contact information of my student, Peter                  |
| **Must-Have** | Novice user       | delete existing contact                                          | ensure all contacts stored are relevant                            |
| **Must-Have** | Any user          | have my data automatically saved                                 | access my information seamlessly the next time I open the software |
| Nice-to-Have  | New user          | view the help guide easily                                       | learn how to use the app                                           |
| Nice-to-Have  | Novice user       | edit the existing contact of Peter                               | correct the contact typo                                           |
| Nice-to-Have  | Any user          | filter the students by lesson day and time                       | find all students in a class at once                               |
| Nice-to-Have  | Any user          | sort students by last name                                       | have the contacts sorted                                           |
| Nice-to-Have  | Any user          | group students by class timings                                  | send class cancellations out quicker                               |
| Nice-to-Have  | Any user          | find Peter by his last name                                      | inform Peter of homework-related matters                           |
| Nice-to-Have  | Any user          | filter the students by subjects                                  | view the students taking the subject                               |
| Nice-to-Have  | Any user          | edit timing of lessons of students                               | allow students to change their lesson timing to fit their schedule |
| Nice-to-Have  | Any user          | find Peter's parents' contact info by Peter's name               | retrieve parents' contact information quicker                      |
| Nice-to-Have  | Elderly user      | zoom in on the smaller texts                                     | see the contact information easily                                 |
| Nice-to-Have  | Intermediate user | tag certain contacts                                             | know the contacts' roles (e.g., admin)                             |
| Nice-to-Have  | Any user          | find the admin user by the role                                  | discuss matters with him/her                                       |
| Nice-to-Have  | Any user          | group some students                                              | contact students having classes at the same time slot easily       |
| Nice-to-Have  | Any user          | find exact student based on NRIC                                 | find the correct person with many similar names                    |
| Nice-to-Have  | Intermediate user | retrieve students' profile information (pic, name, school, etc.) | verify/track them for security purposes                            |
| Nice-to-Have  | Intermediate user | retrieve staffs' profile information (pic, name, etc.)           | verify/track them for security purposes                            |
| Nice-to-Have  | Admin user        | import data from my previous system                              | use this new app quickly without loss of data                      |
| Nice-to-Have  | Admin user        | export data from the app                                         | switch over to another app or view in Excel                        |
| Nice-to-Have  | Teacher           | create a note of student performance                             | discuss it with parents later                                      |
| Could-Have    | Conservative user | set up an access password                                        | safeguard the information                                          |
| Could-Have    | Advance user      | provide feedback to the developer                                | add desired features                                               |
| Could-Have    | Intermediate user | use shortcuts (del instead of delete)                            | navigate the app quicker                                           |
| Could-Have    | Teacher           | take note of a temporary class change                            | prepare accordingly                                                |
| Could-Have    | Any user          | find a student's parents                                         | discuss the student's performance with his/her parents             |
| Could-Have    | Admin user        | contact the vendors working for the tuition center               | settle admin matters                                               |
| Won't-Have    | new user          | create notes for the features                                    | understand and remind myself what each feature does                |
| Won't-Have    | Teacher           | swap class lesson with my colleagues                             | ensure someone can cover in my absence                             |

### Use cases

(For all use cases below, the **System** is the `Cher` and the **Actor** is the `User`, unless specified otherwise)

#### Use case: UC1 - Add contact
**MSS**
1. User enters add contact command with the contact details.
2. Cher add contact to memory.
3. Cher [<u>Save to disk</u>](#use-case-uc4---save-to-disk).
4. Cher show success message. <br>
   Use case ends.

**Extensions**
* 1a. Cher detects error in user input.
    - 1a1. Cher raises error.
    - 1a2. Cher shows correct input format. <br>
      Use case ends.
* 1b. Cher detects duplicate contacts.
    - 1b1. Cher raises error. <br>
      Use case ends.

#### Use case: UC2 - Delete a contact

**MSS**
1. User enters delete contact command with the contact's detail (full name or phone number).
2. Cher shows list of contacts with name that matches user input.
3. User re-enters delete command with phone number of the contact to delete.
4. Cher prompts for delete confirmation.
5. User confirms deletion.
6. Cher deletes the contact from memory.
7. Cher [<u>Save to disk</u>](#use-case-uc4---save-to-disk).
8. Cher shows a success message. <br>
   Use case ends.

**Extensions**
* 1a. Cher detects error in user input.
    - 1a1. Cher shows correct input format.<br>
      Use case ends.
* 1b. Cher detects only 1 contact
    - Use Case jumps to step 5
* 3a. Cher detects error in user input.
    - 3a1. Cher shows error message. <br>
      Use case ends.

#### Use case: UC3 - List contacts
**MSS**
1. User enters List command.
2. Cher displays all contacts in memory to user. <br>
   Use case ends.

**Extensions**
* 1a. Cher detects error in user input.
    - 1a1. Cher shows correct input format. <br>
      Use case ends.
  
#### Use case: UC4 - Save to disk
**MSS**
1. Cher opens a local file.
2. Cher saves contacts in memory into local file. <br>
   Use case ends.

#### Use case: UC5 - Sync disk and memory
**MSS**
1. Cher reads from local file.
2. Cher store contact data to memory. <br>
   Use case ends.

### Non-Functional Requirements
1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed (>50 words per minute) for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  The user interface should be intuitive for users with minimal technical expertise.
5.  Commands should be intuitive and simple. 
6.  Command structures should be logical, consistent and memorable. 
7.  All texts should be of appropriate size and easily-readable for users of all ages.
8.  The color scheme of the user interface should be high-contrast to accomodate for users of all accessibility levels. 
9.  Invalid inputs should be handled gracefully, and the user should be informed clearly of their mistake.
10.  The user should be promptly informed of the outcome of their action (sucess/failure) via the feedback box. 


*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Command-Line Interface (CLI)**: A text-based interface that allows users to interact with the system by typing commands
* **Case-insensitive**: Refers to functionality where uppercase and lowercase letters are treated as the same (e.g., "Peter Tan" is the same as "peter tan")
* **Novice user**: A user with limited experience or familiarity with the system, requiring guidance and simple, intuitive interfaces to perform tasks effectively
* **Intermediate user**: A user with some experience and familiarity with the system, capable of performing tasks with minimal guidance but not yet an expert
* **Use case**: A description of a sequences of actions that the user or system performs, resulting in an observable outcome
* **Actor**: In the context of use cases, actor refers to the role played by the user
* **Main Success Scenario (MSS)**: The sequence of interactions that is the most straightforward and assumes that nothing goes wrong
* **Non-Functional Requirements**: A set of specifications that describes the system's operation capabilities, instead of its function


--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
