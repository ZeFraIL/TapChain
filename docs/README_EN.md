# 📱 Android Application Documentation: TapChain

## 🧾 General Information
**Project Name:** TapChain
**Author(s):** Zeev Fraiman
**Date:** July 2026
**Language:** Java
**Development Environment:** Android Studio
**Android Version:** minSdk 28 / targetSdk 36

---

## 🎯 Project Goal
• **Problem Solved:** Training attention, memory, and reaction speed through a sequence-finding game mechanic.
• **Why it's important:** Cognitive training helps keep the brain active and improves concentration.
• **Target Audience:** Casual gamers of all ages, individuals looking to improve focus.

---

## 📌 Requirements
**Functional Requirements**
• Generation of a sequence of random numbers.
• Dynamic placement of elements on the screen without overlap.
• Verification of click correctness (in sequential order).
• Difficulty progression (increasing the count of numbers per level).
• Error handling (restarting the level on a wrong click).

**Non-functional Requirements**
• **Performance:** Smooth fade-in and fade-out animations.
• **Usability:** Intuitive interface without unnecessary clutter.
• **Reliability:** Stable performance across different screen sizes and system insets.

---

## 🧠 General Architecture
• **Approach:** Simple Activity Pattern (similar to MVC, where the Activity handles both logic and display).
• **Why chosen:** The project is small-scale with a single game scene, making complex patterns (MVVM/MVI) redundant.
• **Key Components:**
  – `MainActivity`: manages the game loop.
  – `RelativeLayout`: dynamic container for game elements.
  – `TextView`: visual representation of the numbers.

---

## 🧩 UML Diagram
`[MainActivity]` -> manages -> `[TextView (numbers)]`
`[MainActivity]` -> uses -> `[Handler]` (for delays)
`[MainActivity]` -> uses -> `[Random]` (for coordinate and number generation)

---

## 📁 Package Structure
Currently, a flat structure is used within the `zeev.fraiman.tapchain` package.
- **Purpose:** Minimizing complexity for a small project.
- **Scaling:** Upon expansion (adding a database or settings), logic will be moved to a `ViewModel` and `Repository`.

---

## 🧩 Detailed Class Description
### 📌 Class: MainActivity
**Role:** Main screen and game controller.
**Responsibility:**
- UI initialization and system inset handling (Edge-to-Edge).
- Management of levels and game state.
- Creation and placement of game elements.
**Main Methods:**
- `onCreate()` — screen setup and launching the first level.
- `startLevel()` — preparing data for a new level, clearing the screen.
- `createNumberView()` — programmatic creation of "bubbles" with numbers and calculating their random coordinates.
- `onNumberClick()` — sequence validation and animation triggers.
- `flashErrorAndRestart()` — visual error indication and resetting level progress.

---

## 🔄 App Workflow Diagram
1. App Launch -> `onCreate`.
2. Generation of start number `k` and count of elements `n`.
3. Placement of `n` numbers on the screen at random positions.
4. Click detection:
   - Correct number (next in sequence) -> fade-out animation -> level completion check.
   - Wrong number -> red flash -> level restart after 500ms.
5. Goal reached when `n > 50` -> "You Win!"

---

## 🎨 UI/UX Analysis
• **Why the interface is designed this way:** Maximum focus on game elements. Centered level header and ample space for numbers.
• **Principles used:**
  – **Simplicity:** No distracting menus.
  – **Logic:** Numbers are visually distinct as interactive objects.
  – **Accessibility:** Large font (24sp) and high-contrast colors.
• **Improvements:** Add dark mode, sound effects, and a high-score leaderboard.

---

## ⚙️ Threading
• **Used:**
  – `Handler`: for creating a time delay before restarting the level after an error.
  – `Main Thread`: all coordinate calculations and UI operations are performed on the main thread due to their lightweight nature.
• **Why chosen:** Ease of implementation for UI-delayed tasks.
• **Prevention of ANR/Leaks:** Compact methods, no long-running blocking operations.

---

## 💾 Data Handling
• **Storage:** In-memory (variables within the `MainActivity` class).
• **Why chosen:** Game state does not require persistence between sessions at this stage.

---

## 🌐 Networking
• The application operates completely offline.

---

## 🔐 Security
• No sensitive data is involved.

---

## 🧪 Testing
• **Manual Testing:** Verifying level generation correctness, click handling, and adaptation to various screen sizes.

---

## 🐞 Error Handling
• **Logic Errors:** Handled via the `flashErrorAndRestart()` method.
• **Runtime Errors:** Using null checks and layout size validation before calculating coordinates.

---

## ⚡ Performance
• **Optimizations:** Use of `AlphaAnimation` for lightweight effects.
• **Bottlenecks:** With a very large number of elements (over 100), random position calculation might require an optimized collision detection algorithm.

---

## 🚀 Expansion Possibilities
• Adding a level timer.
• Global leaderboard via Firebase.
• Various game modes (descending order, even/odd only).
