# Class: MainActivity

## 1. General Information
*   **Class Name:** `MainActivity`
*   **Type:** `Activity`
*   **Purpose:** This is the main controller of the application. It manages the entire game logic, UI updates, level transitions, and user interactions. It is responsible for generating the game sequence and handling the "Tap Chain" mechanics.
*   **Interactions:** It interacts with the Android System (for lifecycle and UI rendering), `RelativeLayout` (as a container), and dynamically created `TextView` objects (representing the game elements).

---

## 2. Variables (Class Fields)

| Name | Type | Purpose | Usage in Logic |
| :--- | :--- | :--- | :--- |
| `level` | `int` | Tracks the current game level. | Used to display the level number and increment difficulty. |
| `n` | `int` | Number of elements to tap in the current level. | Determines how many numbers are generated and placed on the screen. |
| `k` | `int` | The starting number of the sequence. | Used as the base value for the list of numbers in each level. |
| `nextNumber` | `int` | The number the player is expected to tap next. | Used to validate if the player's tap is correct. |
| `numbers` | `ArrayList<Integer>` | A list of numbers to be displayed. | Stores the sequence of numbers which is then shuffled. |
| `numberViews` | `ArrayList<TextView>` | A list of visual elements on the screen. | Stores references to the `TextView` objects to manage their visibility or removal. |
| `mainLayout` | `RelativeLayout` | The main UI container. | The "canvas" where number bubbles are added or removed. |
| `levelTextView` | `TextView` | The label showing the current level. | Updates the text on the screen to show "Level: X". |
| `random` | `Random` | Random number generator. | Used to generate random start numbers (`k`) and random positions on the screen. |
| `handler` | `Handler` | A tool for scheduling tasks. | Used to delay the level restart after an error happens. |

---

## 3. Classroom Methods

### Method Name: `onCreate`
*   **Type:** `protected`
*   **Return value:** `void` (nothing)
*   **Parameters:**
    | Name | Type | Description |
    | :--- | :--- | :--- |
    | `savedInstanceState` | `Bundle` | Container for data if the Activity is recreated. |
*   **What it does:**
    1.  Enables "Edge-to-Edge" display (making the app take up the whole screen).
    2.  Sets the visual layout from `activity_main.xml`.
    3.  Initializes `mainLayout` and `levelTextView` by finding them in the XML.
    4.  Sets a listener for window insets (system bars) to ensure the game starts only after the screen dimensions are known.
*   **When called:** Automatically by the Android system when the app starts.
*   **Important:** This is the entry point. We use `v.post(this::startLevel)` to ensure the layout is measured before we start placing bubbles.

### Method Name: `startLevel`
*   **Type:** `private`
*   **Return value:** `void`
*   **Parameters:** None.
*   **What it does:**
    1.  Checks if `n > 50` (the win condition). If so, shows a "You Win!" toast and closes the app.
    2.  Generates a random starting number `k` (1 to 100).
    3.  Sets `nextNumber` to `k`.
    4.  Updates the level text.
    5.  Removes all old number views from the screen.
    6.  Populates the `numbers` list from `k` to `k + n - 1` and shuffles them.
    7.  Calls `createNumberView` for each number in the list.
*   **When called:** Manually at the start of the app, after completing a level, or after an error.
*   **Important:** Shuffling the numbers ensures that the sequence is not predictable by position.

### Method Name: `createNumberView`
*   **Type:** `private`
*   **Return value:** `void`
*   **Parameters:**
    | Name | Type | Description |
    | :--- | :--- | :--- |
    | `number` | `int` | The specific number to display in this bubble. |
*   **What it does:**
    1.  Creates a new `TextView` object.
    2.  Sets its text, size, color, and background (circle).
    3.  Attaches a "Click Listener" to handle player taps.
    4.  Calculates a random X and Y position within the screen boundaries.
    5.  Adds the view to the `mainLayout`.
    6.  Starts a "Fade In" animation to make the bubble appear smoothly.
*   **When called:** Inside the loop in `startLevel` for every number in the sequence.
*   **Important:** We must calculate the maximum X and Y carefully to prevent bubbles from going off-screen.

### Method Name: `onNumberClick`
*   **Type:** `private`
*   **Return value:** `void`
*   **Parameters:**
    | Name | Type | Description |
    | :--- | :--- | :--- |
    | `number` | `int` | The value of the number that was clicked. |
    | `numberView` | `TextView` | The visual element that was clicked. |
*   **What it does:**
    1.  Checks if the clicked `number` equals `nextNumber`.
    2.  If correct:
        - Makes the bubble unclickable.
        - Increments `nextNumber`.
        - Starts a "Fade Out" animation.
        - When animation ends, it hides the view.
        - If it was the last number in the sequence, it increments the level and starts the next one.
    3.  If incorrect:
        - Calls `flashErrorAndRestart()`.
*   **When called:** Whenever the user taps a number bubble.

### Method Name: `flashErrorAndRestart`
*   **Type:** `private`
*   **Return value:** `void`
*   **Parameters:** None.
*   **What it does:**
    1.  Disables clicking on all bubbles.
    2.  Turns all visible bubbles red using a color filter.
    3.  Uses the `handler` to wait 500 milliseconds, then calls `startLevel()` to reset.
*   **When called:** When the user taps the wrong number in the sequence.

---

## 4. Lifecycle

*   **onCreate():** Called when the Activity is first created. It sets up the UI and prepares the initial state.
*   **Other methods:** This class doesn't override other lifecycle methods (like `onPause`), so it uses the default behavior.

---

## 5. Interface Interaction (UI)

*   **Elements:** `RelativeLayout` (id: `main`) and `TextView` (id: `levelTextView`).
*   **Binding:** Uses `findViewById(R.id.name)` to link XML elements to Java variables.
*   **Events:** Handles `setOnClickListener` for every dynamically created bubble.

---

## 6. Interaction with Other Components

*   **Intent/Transitions:** No other Activities are used.
*   **Data:** No database or API; all data is managed in memory.

---

## 7. General Logic of the Class

The class works as a game loop. It starts by generating a set of numbers. It places them randomly on a screen. The user must find the smallest number and tap it, then the next, and so on. If the user makes a mistake, the whole screen flashes red and the level starts over. With each successful level, the number of bubbles increases, making the game harder.

---

## 8. Simplified Explanation

**Explanation in simple words:**
Imagine a table covered with shuffled cards numbered 1 to 5. Your job is to pick them up in order: 1, then 2, then 3... If you pick 3 before 2, the teacher says "Wrong!" and puts all cards back on the table in new random spots. Each time you succeed, the teacher adds one more card (1 to 6, then 1 to 7) to make it harder. This `MainActivity` is like the teacher: it prepares the cards, checks your choices, and sets up the next round.

---

## 🛠 Possible Improvements
*   **Bug/Bad Practice:** Using `random.nextInt(xMax)` can throw an error if `xMax` is 0 or negative (which might happen on very small screens or during rotation). It's better to check if `xMax > 0`. (Note: The code actually has this check: `xMax > 0 ? random.nextInt(xMax) : 0`).
*   **Improvement:** The coordinates calculation doesn't prevent bubbles from overlapping. A better approach would be to check for occupied space before placing a new bubble.
*   **State:** If you rotate the phone, the game resets to Level 1. It would be better to save the level in `onSaveInstanceState`.
