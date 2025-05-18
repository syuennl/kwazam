# Kwazam Chess

**CCP6224 Object Oriented Analysis and Design Project (Term 2430)**
**Group F\_TT1L**

* **Group Leader**: Ngan Li Syuen (1211108613)
* **Group Members**:

  * Kuan Chee Ling (1211112202)
  * Yap Sze Thin (1211108889)
  * Yap Yan Ting (1211111321)

---
[▶️ Watch Demo Video]([https://www.youtube.com/watch?v=your_video_id](https://youtu.be/2K_3BtAVkg4))
---

## 📝 Description

**Kwazam Chess** is a GUI-based, two-player chess variant played on a 5x8 board. Players control uniquely designed pieces with specific movement rules, including the **Ram**, **Biz**, **Tor**, **Xor**, and **Sau**.

The objective: **Capture the opponent’s Sau piece to win**.
It features dynamic mechanics such as:

* Piece transformations
* Move highlighting for strategic gameplay

---

## 💡 Design Patterns Used

### 1. Model-View-Controller (MVC)

Ensures clear separation of concerns and modular code organization.

#### Model

* Handles core logic and data.
* Key classes: `KwazamModel`, `Gameboard`, `Piece`, and subclasses like `Ram`, `Biz`, `Xor`, `Tor`, `Sau`.
* `Gameboard` manages a 2D array of pieces and provides methods such as `movePiece()`, `getPieceAt()`, `switchPieces()`, and `flipBoard()`.
* Supports game state saving and restoration.

#### View

* GUI logic is handled by `KwazamView`.
* Responsibilities:

  * Displaying gameboard and menu buttons (e.g., Save, Load, Restart)
  * Highlighting moves, showing turn indicators
  * Updating visuals during transformations or flips
  * Displaying pop-ups for invalid actions or game-over events

#### Controller

* Manages user interactions via listeners:

  * `BlockClickListener`, `ResizeListener`, `MenuListener`, etc.
* Validates user input through Model
* Updates the View
* Manages game saving/loading with mementos

---

### 2. Memento Design Pattern

Implements save/load functionality to restore game states.

* **Originator**: `Gameboard`

  * Creates/restores game states using `saveState()` and `restoreState()`

* **Memento**: `GameboardMemento`

  * Stores snapshot of the board, current team, and turn count

* **Caretaker**: `KwazamController`

  * Manages stack of mementos
  * Handles file saving/loading operations

---

## ⚙️ Compile and Run Instructions

### 🔸 Using Command Line

1. Open terminal and navigate to the project folder:

   ```bash
   cd /path/to/your/files
   ```

2. Compile all Java files:

   ```bash
   javac *.java
   ```

3. Ensure `KwazamChessProgram.java` (with `main()` function) is present. Run the program:

   ```bash
   java KwazamChessProgram
   ```

> ✅ Ensure that **Java Development Kit (JDK)** is installed and added to your PATH. <br>
> ⚠️ Commands are case-sensitive.

---

### 🔸 Using BlueJ

1. Download the **Kwazam** project folder
2. Add `package.bluej` file to the folder
3. Open **BlueJ**
4. Go to `Project` → `Open Project`
5. Navigate to your downloaded folder and open it
6. Click **Compile**
7. Right-click `KwazamChessProgram.java` and select **Run**
