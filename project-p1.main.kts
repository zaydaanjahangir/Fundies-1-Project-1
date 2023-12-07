// -----------------------------------------------------------------
// Project: Part 1, Summary
// -----------------------------------------------------------------
import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.fileReadAsList
import khoury.isAnInteger
import khoury.linesToString
import khoury.reactConsole
import khoury.runEnabledTests
import khoury.testSame
import java.io.File
// TODO: Add a comment here saying whom you worked with (people or
// AI), and how it helped. If you did not work with anyone (which
// would be surprising), say that.

// Discussed react console functions chooseOption and studyDeck with Rohan Mehta
// We drew out the react console diagram and discussed how the helper functions wouuld wor
// Also discussed possible solutions for debugging especiialy with indexOutOfBounds
// on the studyDeck function
// I verbally explained to him how the captureResults worked with the helper test
// which was given on hw 3
// Rest of code done by myself

// You are going to design an application to allow a user to
// self-study using flash cards. In this part of the project,
// a user will...

// 1. Be prompted to choose from a menu of available flash
//    card decks; this menu will repeat until a valid
//    selection is made.
//
// 2. Proceed through each card in the selected deck,
//    one-by-one. For each card, the front is displayed,
//    and the user is allowed time to reflect; then the
//    back is displayed; and the user is asked if they
//    got the correct answer.
//
// 3. Once the deck is exhausted, the program outputs the
//    number of self-reported correct answers and ends.
//

// Of course, we'll design this program step-by-step, AND
// you've already done pieces of this in homework!!
// (Note: you are welcome to leverage your prior work and/or
// code found in the sample solutions & lecture notes.)
//

// Lastly, here are a few overall project requirements...
// - Since mutation hasn't been covered in class, your design is
//   NOT allowed to make use of mutable variables and/or lists.
// - As included in the instructions, all interactive parts of
//   this program MUST make effective use of the reactConsole
//   framework.
// - Staying consistent with our Style Guide...
//   * All functions must have:
//     a) a preceding comment specifying what it does
//     b) an associated @EnabledTest function with sufficient
//        tests using testSame
//   * All data must have:
//     a) a preceding comment specifying what it represents
//     b) associated representative examples
// - You will be evaluated on a number of criteria, including...
//   * Adherence to instructions and the Style Guide
//   * Correctly producing the functionality of the program
//   * Design decisions that include choice of tests, appropriate
//     application of list abstractions, and task/type-driven
//     decomposition of functions.
//

// -----------------------------------------------------------------
// Data design
// (Hint: see Homework 3, Problem 2)
// -----------------------------------------------------------------

// TODO 1/2: Design the data type FlashCard to represent a single
//           flash card. You should be able to represent the text
//           prompt on the front of the card as well as the text
//           answer on the back. Include at least 3 example cards
//           (which will come in handy later for tests!).
//

data class FlashCard(val front: String, val back: String)

val cardOne = FlashCard("What is the neumothorax?", "A collection of air outside the lung but within the pleural cavity")
val cardTwo = FlashCard("What does CRM stand for?", "Customer Relationship Management")
val cardThree = FlashCard("What does GAAP stand for?", "Generally Accepted Accounting Principles")
val cardFour = FlashCard("What is the epidermis?", "The top layer of skin in your body")
val cardFive = FlashCard("Has Purdue Univeristy won an NCAA Basketball Champiopnship?", "No")
val cardSix = FlashCard("Has Duke Univeristy won an NCAA Basketball Champiopnship?", "Yes")

// TODO 2/2: Design the data type Deck to represent a deck of
//           flash cards. The deck should have a name, as well
//           as a Kotlin list of flash cards.
//
//           Include at least 2 example decks based upon the
//           card examples above.
//

data class Deck(val name: String, val cardList: List<FlashCard>)

val deckOne = Deck("Evens", listOf(cardTwo, cardFour, cardSix))
val deckTwo = Deck("Odds", listOf(cardOne, cardThree, cardFive))

// -----------------------------------------------------------------
// Generating flash cards
// -----------------------------------------------------------------

// One benefit of digital flash cards is that sometimes we can
// use code to produce cards that match a known pattern without
// having to write all the fronts/backs by hand!
//

// TODO 1/1: Design the function perfectSquares that takes a
//           count (assumed to be positive) and produces the
//           list of flash cards that tests that number of the
//           first squares.
//
//           For example, the first three perfect squares...
//
//            1. front (1^2 = ?), back (1)
//            2. front (2^2 = ?), back (4)
//            3. front (3^2 = ?), back (9)
//
//           have been supplied as named values.
//
//           Hint: you might consider combining your
//                 kthPerfectSquare function from Homework 1
//                 with the list constructor in Homework 3.
//

val square1Front = "1^2 = ?"
val square2Front = "2^2 = ?"
val square3Front = "3^2 = ?"

val square1Back = "1"
val square2Back = "4"
val square3Back = "9"

val i = 1

// Returns a list of flashcards testing the perfect squares
fun perfectSquares(count: Int): List<FlashCard> {
    if (count < 1) {
        return emptyList()
    }
    return (i..count).map { i ->
        val square = i * i
        FlashCard("$i^2 = ?", "$square")
    }
}

@EnabledTest
fun testPerfectSquares() {
    testSame(
        perfectSquares(3),
        listOf(FlashCard("1^2 = ?", "1"), FlashCard("2^2 = ?", "4"), FlashCard("3^2 = ?", "9")),
        "1,2,3",
    )
    testSame(
        perfectSquares(0),
        emptyList(),
        "0",
    )
    testSame(
        perfectSquares(-1),
        emptyList(),
        "-1",
    )
    testSame(
        perfectSquares(5),
        listOf(
            FlashCard("1^2 = ?", "1"),
            FlashCard("2^2 = ?", "4"),
            FlashCard("3^2 = ?", "9"),
            FlashCard("4^2 = ?", "16"),
            FlashCard("5^2 = ?", "25"),
        ),
        "1,2,3",
    )
}

// Files of cards
// -----------------------------------------------------------------

// Consider a simple format for storing flash cards in a file:
// each card is a line in the file, where the front comes first,
// separated by a "pipe" character ('|'), followed by the text
// on the back of the card.
//

val charSep = "|"

// TODO 1/3: Design the function cardToString that takes a flash
//           card as input and produces a string according to the
//           specification above ("front|back"). Make sure to
//           test all your card examples!
//

// Returns the given flashcards and returns the string version
fun cardToString(inputCard: FlashCard): String {
    return inputCard.front + charSep + inputCard.back
}

@EnabledTest
fun testCardtoString() {
    testSame(
        cardToString(cardOne),
        "What is the neumothorax?|A collection of air outside the lung but within the pleural cavity",
        "Card 1",
    )
    testSame(
        cardToString(cardTwo),
        "What does CRM stand for?|Customer Relationship Management",
        "Card 2",
    )
    testSame(
        cardToString(cardThree),
        "What does GAAP stand for?|Generally Accepted Accounting Principles",
        "Card 3",
    )
    testSame(
        cardToString(cardFour),
        "What is the epidermis?|The top layer of skin in your body",
        "Card 4",
    )
    testSame(
        cardToString(cardFive),
        "Has Purdue Univeristy won an NCAA Basketball Champiopnship?|No",
        "Card 5",
    )
    testSame(
        cardToString(cardSix),
        "Has Duke Univeristy won an NCAA Basketball Champiopnship?|Yes",
        "Card 6",
    )
}

// TODO 2/3: Design the function stringToCard that takes a string,
//           assumed to be in the format described above, and
//           produces the corresponding flash card.
//
//           Hints:
//           - look back to how we extracted data from CSV
//             (comma-separated value) files (such as in
//             Homework 3)!
//           - a great way to test: for each of your card
//             examples, pass them through the function in TODO
//             1 to convert them to a string; then, pass that
//             result to this function... you *should* get your
//             original flash card back :)
//

// Takes a given string and returns the flashcard version
fun stringToCard(inputString: String): FlashCard {
    val parts = inputString.split('|')
    if (parts.contains("|") == false) {
        return FlashCard("", "") // empty flashcard because invalid
    }
    if (parts.size == 2) {
        val flashcardFront = parts[0]
        val flashcardBack = parts[1]
        return FlashCard(flashcardFront, flashcardBack)
    } else {
        return FlashCard("", "") // empty flashcard because invalid
    }
}

@EnabledTest
fun testStringToCard() {
    testSame(
        stringToCard("What is the neumothorax?|A collection of air outside the lung but within the pleural cavity"),
        stringToCard(cardToString(cardOne)),
        "Card 1",
    )
    testSame(
        stringToCard("What does CRM stand for?|Customer Relationship Management"),
        stringToCard(cardToString(cardTwo)),
        "Card 2",
    )
    testSame(
        stringToCard("What does GAAP stand for?|Generally Accepted Accounting Principles"),
        stringToCard(cardToString(cardThree)),
        "Card 3",
    )
    testSame(
        stringToCard("What is the epidermis?|The top layer of skin in your body"),
        stringToCard(cardToString(cardFour)),
        "Card 4",
    )
    testSame(
        stringToCard("Has Purdue Univeristy won an NCAA Basketball Champiopnship?|No"),
        stringToCard(cardToString(cardFive)),
        "Card 5",
    )
    testSame(
        stringToCard("Has Duke Univeristy won an NCAA Basketball Champiopnship?|Yes"),
        stringToCard(cardToString(cardSix)),
        "Card 6",
    )
    testSame(
        stringToCard("What is the neumothorax?|A collection of air outside the lung but within the pleural cavity"),
        FlashCard("", ""),
        "No Char Separator",
    )
    testSame(
        stringToCard("What is the neumothorax?|A collection of air outside the lung but within the pleural cavity"),
        FlashCard("", ""),
        "Multiple Char Separators",
    )
}

// TODO 3/3: Design the function readCardsFile that takes a path
//           to a file and produces the corresponding list of
//           flash cards found in the file.
//
//           If the file does not exist, return an empty list.
//           Otherwise, you can assume that every line is
//           formatted in the string format we just worked with.
//
//           Hint:
//           - Think about how HW3-P1 effectively used an
//             abstraction to process all the lines in a
//             file assuming a known pattern.
//           - We've provided an "example.txt" file that you can
//             use for testing if you'd like; also make sure to
//             test your function when the supplied file does not
//             exist!
//

// Takes the given filepath and returns a list of flashcards
fun readCardsFile(filePath: String): List<FlashCard> {
    val file = File(filePath)

    if (!file.exists()) {
        return emptyList()
    }

    val flashcardList = fileReadAsList(filePath)

    return flashcardList.map { line ->
        val newParts = line.split('|')
        if (newParts.size == 2) {
            val newFlashCardFront = newParts[0].trim()
            val newFlashCardBack = newParts[1].trim()
            FlashCard(newFlashCardFront, newFlashCardBack)
        } else {
            FlashCard("", "")
        }
    }
}

@EnabledTest
fun testReadCardsFile() {
    val fileDeckTest = listOf(FlashCard("front 1", "back 1"), FlashCard("front 2", "back 2"))
    testSame(
        readCardsFile("example.txt"),
        fileDeckTest,
        "example.txt",
    )
}

// -----------------------------------------------------------------
// Processing a self-report
// (Hint: see Homework 2)
// -----------------------------------------------------------------

// In our program, we will ask for a self-report as to whether
// the user got the correct answer for a card, SO...

// TODO 1/1: Finish designing the function isPositive that
//           determines if the supplied string starts with
//           the letter "y" (either upper or lowercase).
//
//           You've been supplied with a number of tests - make
//           sure you understand what they are doing!
//

// Determines if the supplied string starts with the letter y
fun isPositive(word: String): Boolean {
    if ((word.lowercase()).startsWith("y")) {
        return true
    }
    return false
}

@EnabledTest
fun testIsPositive() {
    fun helpTest(
        str: String,
        expected: Boolean,
    ) {
        testSame(isPositive(str), expected, str)
    }

    helpTest("yes", true)
    helpTest("Yes", true)
    helpTest("YES", true)
    helpTest("yup", true)

    helpTest("nope", false)
    helpTest("NO", false)
    helpTest("nah", false)
    helpTest("not a chance", false)

    // should pass,
    // despite doing the wrong thing
    helpTest("indeed", false)
}

// -----------------------------------------------------------------
// Choosing a deck from a menu
// -----------------------------------------------------------------

// Now let's work on providing a menu of decks from which a user
// can choose what they want to study.

// TODO 1/2: Finish design the function choicesToText that takes
//           a list of strings (assumed to be non-empty) and
//           produces the textual representation of a menu of
//           those options.

//           For example, given...

//           ["a", "b", "c"]

//           The menu would be...

//           "1. a
//            2. b
//            3. c

//            Enter your choice"

//            As you have probably guessed, this will be a key
//            piece of our rendering function :)

//            Hints:
//            - Think back to Homework 3 when we used a list
//              constructor to generate list elements based
//              upon an index.
//            - If you can produce a list of strings, the
//              linesToString function in the Khoury library
//              will bring them together into a single string.
//            - Make sure to understand the supplied tests!
//

val promptMenu = "Enter your choice"

// takes a list of Strings and outputs a menu representaion of those strings
fun choicesToText(options: List<String>): String {
    val menuText = options.mapIndexed { index, option -> "${index + 1}. $option" }.joinToString("\n")
    return "$menuText\n\n$promptMenu"
}

@EnabledTest
fun testChoicesToText() {
    val optA = "apple"
    val optB = "banana"
    val optC = "carrot"

    testSame(
        choicesToText(listOf(optA)),
        linesToString(
            "1. $optA",
            "",
            promptMenu,
        ),
        "one",
    )

    testSame(
        choicesToText(listOf(optA, optB, optC)),
        linesToString(
            "1. $optA",
            "2. $optB",
            "3. $optC",
            "",
            promptMenu,
        ),
        "three",
    )
}

// TODO 2/2: Finish designing the program chooseOption that takes
//           a list of decks, produces a corresponding numbered
//           menu (1-# of decks, each showing its name), and
//           returns the deck corresponding to the number entered.
//           (Of course, keep displaying the menu until a valid
//           number is entered.)
//
//           Hints:
//            - Review the "Valid Number Example" of reactConsole
//              as one example of how to validate input. In this
//              case, however, since we know that we have a valid
//              range of integers, we can simplify the state
//              representation significantly :)
//            - To help you get started, the chooseOption function
//              has been written, but you must complete the helper
//              functions; look to the comments below for guidance.
//              You can then play "signature detective" to figure
//              out the parameters/return type of the functions you
//              need to write :)
//            - Lastly, as always, don't forget to sufficiently
//              test all the functions you write in this problem!
//

// a program to allow the user to interactively select
// a deck from the supplied, non-empty list of decks

// Returns the name of the supplied deck
fun getDeckName(suppliedDeck: Deck): String {
    return suppliedDeck.name
}

@EnabledTest
fun testGetDeckName() {
    testSame(
        getDeckName(deckOne),
        "Evens",
        "Deck 1",
    )
    testSame(
        getDeckName(deckTwo),
        "Odds",
        "Deck 2",
    )
}

// Takes a string integer and int range and keeps the string
// if it is a valid int and is within the int range
fun keepIfValid(
    input: String,
    validIndices: IntRange,
): Int {
    val userInputValidity = isAnInteger(input)
    val isWithinValidRange = (input.toInt() >= 1) && (input.toInt() in validIndices)

    if (userInputValidity && isWithinValidRange) {
        return input.toInt() - 1
    }

    return -1
}

@EnabledTest
fun testKeepIfValid() {
    testSame(
        keepIfValid("10", 1..20),
        9,
        "10 and 1-20",
    )
    testSame(
        keepIfValid("4", 1..5),
        3,
        "4 and 1-5",
    )
    testSame(
        keepIfValid("10", 1..5),
        -1,
        "10 and 1-5 (not in valid range)",
    )
}

// Returns a string announncing the chosen deck
fun choiceAnnouncement(deckName: String): String {
    return "You chose: $deckName"
}

@EnabledTest
fun testChoiceAnnouncement() {
    testSame(
        choiceAnnouncement("Basketball Cards"),
        "You chose: Basketball Cards",
        "Basketball Cards",
    )
    testSame(
        choiceAnnouncement("Poker Cards"),
        "You chose: Poker Cards",
        "Poker Cards",
    )
    testSame(
        choiceAnnouncement("UNO"),
        "You chose: UNO",
        "UNO",
    )
    testSame(
        choiceAnnouncement("Pokemon Cards"),
        "You chose: Pokemon Cards",
        "Pokemon Cards",
    )
}

// Runs a react console that with helper function gives the user the option
// to choose a deck from the supplied list
fun chooseOption(decks: List<Deck>): Deck {
    // since the event handlers will need some info about
    // the supplied decks, the functions inside
    // chooseOption provide info about them while the
    // parameter is in scope

    // TODO: Above chooseOption, design the function
    //       getDeckName, which returns the name of
    //       a supplied deck.

    // Renders the supplied deck list as options to choose from
    fun renderDeckOptions(state: Int): String {
        return choicesToText(decks.map(::getDeckName))
    }

    // TODO: Above chooseOption, design the function
    //       keepIfValid, that takes the typed input
    //       as a string, as well as the valid
    //       indices of the decks; note that the list indices
    //       will be in the range [0, size), whereas the
    //       user will see and work with [1, size].
    //
    //       If the user did not type a valid integer,
    //       or not one in [1, size], return -1; otherwise
    //       return the string converted to an integer, but
    //       subtract 1, which makes it a valid list index.

    // Returns the choice of the user if it is valid
    fun transitionOptionChoice(
        ignoredState: Int,
        kbInput: String,
    ): Int {
        return keepIfValid(kbInput, decks.indices)
    }

    // TODO: nothing, but understand this :)
    fun validChoiceEntered(state: Int): Boolean {
        return state in decks.indices
    }

    // TODO: Above chooseOption, design the function
    //       choiceAnnouncement that takes the selected
    //       deck name and returns an announcement that
    //       makes you happy. For a simple example, given
    //       "fundies" as the chosen deck name, you might
    //       return "you chose: fundies"

    // Prints out the users choice
    fun renderChoice(state: Int): String {
        return choiceAnnouncement(getDeckName(decks[state]))
    }

    return decks[
        reactConsole(
            initialState = -1,
            stateToText = ::renderDeckOptions,
            nextState = ::transitionOptionChoice,
            isTerminalState = ::validChoiceEntered,
            terminalStateToText = ::renderChoice,
        ),
    ]
}

@EnabledTest
fun testChooseOption() {
    val testDeck = listOf(deckOne, deckTwo)

    fun helpTest(deckList: List<Deck>): () -> Unit {
        fun showMyOptions() {
            chooseOption(deckList)
        }

        return ::showMyOptions
    }

    testSame(
        captureResults(
            helpTest(testDeck),
            "1",
            "1",
        ),
        CapturedResult(
            Unit,
            "1. Evens",
            "2. Odds",
            "",
            "Enter your choice",
            "You chose: Evens",
        ),
        "Evens",
    )

    testSame(
        captureResults(
            helpTest(testDeck),
            "2",
            "1",
        ),
        CapturedResult(
            Unit,
            "1. Evens",
            "2. Odds",
            "",
            "Enter your choice",
            "You chose: Odds",
        ),
        "Odds",
    )
}

// -----------------------------------------------------------------
// Studying a deck
// -----------------------------------------------------------------

// Now let's design a program to allow a user to study through a
// supplied deck of flash cards.

// TODO 1/2: Design the data type StudyState to keep track of...
//           - which card you are currently studying in the deck
//           - are you looking at the front or back
//           - how many correct answers have been self-reported
//             thus far
//
//           Create sufficient examples so that you convince
//           yourself that you can represent any situation that
//           might arise when studying a deck.
//
//           Hints:
//           - Look back to the reactConsole problems in HW2 and
//             HW3; the former involved keeping track of a count
//             of loops (similar to the count of correct answers),
//             and the latter involved options for keeping track
//             of where you are in a list with reactConsole.
//

data class StudyState(
    val currentCardIndex: Int,
    val frontOrBack: Boolean,
    val correctAnswers: Int,
)

val startingStudying = StudyState(3, true, 2)
val middleofStudyingBad = StudyState(25, true, 12)
val middleofStudyingGood = StudyState(25, false, 23)
val endOfStudyingBad = StudyState(50, true, 31)
val endOfStudyingGood = StudyState(50, true, 49)

// TODO 2/2: Now, using reactConsole, design the program studyDeck
//           that for each card in a supplied deck, allows the
//           user to...
//
//           1. see the front (pause and think)
//           2. see the back
//           3. respond as to whether they got the answer
//
//           At the end, the user is told how many they self-
//           reported as correct (and this number is returned).
//
//           You have been supplied some prompts for steps #1
//           and #2 - feel free to change them if you'd like :)
//
//           Suggestions...
//           - Review the reactConsole videos/examples
//           - Start with studyDeck:
//             * write some tests to convince yourself you know
//               what your program is supposed to do!
//             * figure out how you'll create the initial state
//             * give names to the handlers you'll need
//             * how will you return the number correct?
//             * now comment-out this function, so that you can
//               design/test the handlers without interference :)
//           - For each handler...
//             * Play signature detective: based upon how it's
//               being used with reactConsole, what data will it
//               be given and what does it produce?
//             * Write some tests to convince yourself you know
//               its job.
//             * Write the code and don't move on till your tests
//               pass.
//            - Suggested ordering...
//              1. Am I done studying yet?
//              2. Rendering
//                 - It's a bit simpler to have a separate
//                   function for the terminal state.
//                 - The linesToString function is your friend to
//                   combine the card with the prompts.
//                 - Think about good decomposition when making
//                   the decision about front vs back content.
//              3. Transition
//                 - Start with the two main situations
//                   you'll find yourself in...
//                   > front->back
//                   > back->front
//                 - Then let a helper figure out how to handle
//                   the details of self-report
//
//            You've got this :-)
//

val studyThink = "Think of the result? Press enter to continue"
val studyCheck = "Correct? (Y)es/(N)o"

// React console which simulates a study session with the given deck
fun studyDeck(deck: Deck): Int {
    data class StudyState(
        val currentCardIndex: Int,
        val frontOrBack: Boolean,
        val numCorrect: Int,
    )

    // Determines if user is finished studying by the current index and the list size
    fun isFinishedStudying(state: StudyState): Boolean {
        return state.currentCardIndex >= deck.cardList.size
    }

    // Prints front of card
    fun renderFront(card: FlashCard): String {
        return "Front: ${card.front}\n$studyThink"
    }

    // Prints back of card
    fun renderBack(card: FlashCard): String {
        return "Back: ${card.back}\n$studyCheck"
    }

    // Prints out results or front/back of card based on the state
    fun renderStudyState(state: StudyState): String {
        if (isFinishedStudying(state)) {
            return "You got ${state.numCorrect} out of ${deck.cardList.size}"
        }

        val currentFlashCard = deck.cardList[state.currentCardIndex]

        return if (state.frontOrBack) {
            renderFront(currentFlashCard)
        } else {
            renderBack(currentFlashCard)
        }
    }

    // transition study state which determines if user is done and updates
    // all values based on what the user has answered
    fun transitionStudyState(
        state: StudyState,
        input: String,
    ): StudyState {
        if (isFinishedStudying(state)) {
            return state
        }

        val currentCardIndex = state.currentCardIndex
        val cardListSize = deck.cardList.size

        if (currentCardIndex < cardListSize) {
            val currentFlashCard = deck.cardList[currentCardIndex]

            if (state.frontOrBack) {
                return StudyState(currentCardIndex, false, state.numCorrect)
            }

            val isAnswerCorrect = isPositive(input)
            val newNumCorrect = if (isAnswerCorrect) state.numCorrect + 1 else state.numCorrect
            val newCurrentCardIndex = currentCardIndex + 1

            return StudyState(newCurrentCardIndex, true, newNumCorrect)
        } else {
            return state
        }
    }

    val studySessionOutcome =
        reactConsole(
            initialState = StudyState(0, true, 0),
            stateToText = { renderStudyState(it) },
            nextState = { state, input -> transitionStudyState(state, input) },
            isTerminalState = ::isFinishedStudying,
            terminalStateToText = { renderStudyState(it) },
        )

    return studySessionOutcome.numCorrect
}

@EnabledTest
fun testStudyDeck() {
    val cardOne = FlashCard("What is the neumothorax?", "A collection of air outside the lung but within the pleural cavity")
    val cardThree = FlashCard("What does GAAP stand for?", "Generally Accepted Accounting Principles")
    val testDeckOne = Deck("Odds", listOf(cardOne, cardThree))

    val cardFour = FlashCard("What is the epidermis?", "The top layer of skin in your body")
    val cardFive = FlashCard("Has Purdue Univeristy won an NCAA Basketball Champiopnship?", "No")
    val cardSix = FlashCard("Has Duke Univeristy won an NCAA Basketball Champiopnship?", "Yes")
    val testDeckTwo = Deck("Odds", listOf(cardFour, cardFive, cardSix))

    fun helpTest(decko: Deck): () -> Unit {
        fun showMyStudyDeck() {
            studyDeck(decko)
        }

        return ::showMyStudyDeck
    }

    testSame(
        captureResults(
            helpTest(testDeckOne),
            "2",
            "2",
            "A collection of air outside the lung but within the pleural cavity",
            "Y",
            "No idea",
            "N",
        ),
        CapturedResult(
            Unit,
            "Front: What is the neumothorax?", "Think of the result? Press enter to continue",
            "Back: A collection of air outside the lung but within the pleural cavity", "Correct? (Y)es/(N)o",
            "Front: What does GAAP stand for?", "Think of the result? Press enter to continue",
            "Back: Generally Accepted Accounting Principles",
            "Correct? (Y)es/(N)o",
            "You got 1 out of 2",
        ),
        "Deck 1 1/2",
    )

    testSame(
        captureResults(
            helpTest(testDeckOne),
            "2",
            "2",
            "A type of ladybug",
            "N",
            "The clothing store with nice hoodies",
            "N",
        ),
        CapturedResult(
            Unit,
            "Front: What is the neumothorax?", "Think of the result? Press enter to continue",
            "Back: A collection of air outside the lung but within the pleural cavity", "Correct? (Y)es/(N)o",
            "Front: What does GAAP stand for?", "Think of the result? Press enter to continue",
            "Back: Generally Accepted Accounting Principles",
            "Correct? (Y)es/(N)o",
            "You got 0 out of 2",
        ),
        "Deck 1 0/2",
    )

    testSame(
        captureResults(
            helpTest(testDeckTwo),
            "2",
            "2",
            "The top layer of skin in your body",
            "Y",
            "Yes",
            "N",
            "Yes",
            "N",
        ),
        CapturedResult(
            Unit,
            "Front: What is the epidermis?", "Think of the result? Press enter to continue",
            "Back: The top layer of skin in your body", "Correct? (Y)es/(N)o",
            "Front: Has Purdue Univeristy won an NCAA Basketball Champiopnship?", "Think of the result? Press enter to continue",
            "Back: No", "Correct? (Y)es/(N)o",
            "Front: Has Duke Univeristy won an NCAA Basketball Champiopnship?", "Think of the result? Press enter to continue",
            "Back: Yes", "Correct? (Y)es/(N)o",
            "You got 1 out of 3",
        ),
        "Deck 2 1/3",
    )

    testSame(
        captureResults(
            helpTest(testDeckTwo),
            "2",
            "2",
            "The bottom layer of skin in your body",
            "N",
            "Yes",
            "N",
            "Yes",
            "N",
        ),
        CapturedResult(
            Unit,
            "Front: What is the epidermis?", "Think of the result? Press enter to continue",
            "Back: The top layer of skin in your body", "Correct? (Y)es/(N)o",
            "Front: Has Purdue Univeristy won an NCAA Basketball Champiopnship?", "Think of the result? Press enter to continue",
            "Back: No", "Correct? (Y)es/(N)o",
            "Front: Has Duke Univeristy won an NCAA Basketball Champiopnship?", "Think of the result? Press enter to continue",
            "Back: Yes", "Correct? (Y)es/(N)o",
            "You got 0 out of 3",
        ),
        "Deck 2 0/3",
    )

    testSame(
        captureResults(
            helpTest(testDeckTwo),
            "2",
            "2",
            "The top layer of skin in your body",
            "Y",
            "No",
            "Y",
            "No",
            "N",
        ),
        CapturedResult(
            Unit,
            "Front: What is the epidermis?", "Think of the result? Press enter to continue",
            "Back: The top layer of skin in your body", "Correct? (Y)es/(N)o",
            "Front: Has Purdue Univeristy won an NCAA Basketball Champiopnship?", "Think of the result? Press enter to continue",
            "Back: No", "Correct? (Y)es/(N)o",
            "Front: Has Duke Univeristy won an NCAA Basketball Champiopnship?", "Think of the result? Press enter to continue",
            "Back: Yes", "Correct? (Y)es/(N)o",
            "You got 2 out of 3",
        ),
        "Deck 2 2/3",
    )
}

// -----------------------------------------------------------------
// Final app!
// -----------------------------------------------------------------

// Now you just get to put this all together 💃

// TODO 1/1: Design the function chooseAndStudy, where you'll
//           follow the comments in the supplied code to leverage
//           your prior work to allow the user to choose a deck,
//           study it, and return the number of correct self-
//           reports.
//
//           Your deck options MUST include at least one from each
//           of the following categories...
//
//           - Coded by hand (such as an example in data design)
//           - Read from a file (ala readCardsFile)
//           - Generated by code (ala perfectSquares)
//
//           Note: while this is an interactive program, you won't
//                 directly use reactConsole - instead, just call
//                 the programs you already designed above :)
//
//           And of course, don't forget to test at least two runs
//           of this completed program!
//
//           (And, consider adding this to main so you can see the
//           results of all your hard work so far this semester!)
//

// Runs everything giving the user an option to choose decks
// Allows the user to conduct a full study session on the chosen deck
fun chooseAndStudy(): Int {
    // 1. Construct a list of options
    // (ala the instructions above)
    val deckOptions =
        listOf(deckOne, Deck("File Derived Deck", readCardsFile("example.txt")), Deck("Perfect Squares Deck", perfectSquares(3)))
    // TODO: at least...
    // deck from file via readCardsFile,

    // deck from code via perfectSquares

    // deck hand-coded
    // 2. Use chooseOption to let the user
    //    select a deck
    val deckChosen = chooseOption(deckOptions)
    // 3. Let the user study, return the
    //    number correctly answered
    val studySessionOutcome = studyDeck(deckChosen)
    println("The total amount of correct answers is $studySessionOutcome")
    return studySessionOutcome
}

@EnabledTest
fun testChooseAndStudy() {
    fun helpTest(): () -> Unit {
        fun chooseAndStudyMyDecks() {
            chooseAndStudy()
        }

        return ::chooseAndStudyMyDecks
    }
// val cardOne = FlashCard("What is the neumothorax?", "A collection of air outside the lung but within the pleural cavity")
// val cardTwo = FlashCard("What does CRM stand for?", "Customer Relationship Management")
// val cardThree = FlashCard("What does GAAP stand for?", "Generally Accepted Accounting Principles")
// val cardFour = FlashCard("What is the epidermis?", "The top layer of skin in your body")
// val cardFive = FlashCard("Has Purdue Univeristy won an NCAA Basketball Champiopnship?", "No")
// val cardSix = FlashCard("Has Duke Univeristy won an NCAA Basketball Champiopnship?", "Yes")

    testSame(
        captureResults(
            helpTest(),
            "1",
            "Y",
            "Y",
            "N",
        ),
        CapturedResult(
            Unit,
            "1. Evens",
            "2. File Derived Deck",
            "3. Perfect Squares Deck",
            "",
            "Enter your choice",
            "You chose: Evens",
            "Front: What does CRM stand for?",
            "Think of the result? Press enter to continue",
            "Back: Customer Relationship Management",
            "Correct? (Y)es/(N)o",
            "Front: What is the epidermis?",
            "Think of the result? Press enter to continue",
            "Back: The top layer of skin in your body",
            "Correct? (Y)es/(N)o",
            "Front: Has Duke Univeristy won an NCAA Basketball Champiopnship?",
            "Think of the result? Press enter to continue",
            "Back: Yes",
            "Correct? (Y)es/(N)o",
            "You got 1 out of 3",
            "The total amount of correct answers is 1",
        ),
        "Evens",
    )

    testSame(
        captureResults(
            helpTest(),
            "2",
            "Y",
            "Y",
            "N",
        ),
        CapturedResult(
            Unit,
            "1. Evens",
            "2. File Derived Deck",
            "3. Perfect Squares Deck",
            "",
            "Enter your choice",
            "You chose: File Derived Deck",
            "Front: front 1",
            "Think of the result? Press enter to continue",
            "Back: back 1",
            "Correct? (Y)es/(N)o",
            "Front: front 2",
            "Think of the result? Press enter to continue",
            "Back: back 2",
            "Correct? (Y)es/(N)o",
            "You got 1 out of 2",
            "The total amount of correct answers is 1",
        ),
        "File Derived Deck",
    )
}

fun main() {
    chooseAndStudy()
}
runEnabledTests(this)
main()

// -----------------------------------------------------------------
