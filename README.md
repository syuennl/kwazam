#CCP6224 Object Oriented Analysis and Design Project Term 2430
By Group F_TT1L 
Group Leader: Ngan Li Syuen 1211108613
Group Member: Kuan Chee Ling 1211112202
              Yap Sze Thin 1211108889
              Yap Yan Ting 1211111321

Description:
Kwazam Chess is a GUI-based, two-player game played on a 5x8 board.
The game involves players controlling unique pieces with specific movement rules,
such as the Ram, Biz, Tor, Xor, and Sau.
The objective is to capture the opponent's Sau piece, leading to a win.
The game includes dynamic mechanics like transforming pieces and
highlights possible moves for selected pieces, providing a strategic and engaging chess variant.

Usage of Design Patterns:
1. Model-View-Controller (MVC) design pattern
- Ensure clear separation of concerns and modular code organization.
- The 3 components of MVC are Model, View, and Controller.
  i. The Model handles the core logic and data of the program.
     In Kwazam Chess, classes such as ‘KwazamModel’, ‘Gameboard’, ‘Piece’, and its subclasses (‘Ram’, ‘Biz’, ‘Xor’, ‘Tor’, ‘Sau’ ) represent the model.
     The ‘KwazamModel’ class manages the overall game state, including the active team, turn count, and the state of all pieces via the Gameboard.
     The ‘Gameboard’ maintains a 2D array of Piece objects and provides methods to manipulate the gameboard, such as movePiece, getPieceAt(), switchPieces(), and flipBoard().
     It also supports game state saving and restoration logic. The ‘Piece’ class and its subclasses represent individual chess pieces and define their movement logics through methods like move(), getPossiblePos, and isValidMove().

  ii. The View manages the graphical user interface.
      In KwazamChess, ‘KwazamView’ is responsible for displaying the gameboard, menu buttons (such as ‘Save Game’, ‘Load Game’, ‘Restart’) and turn label that indicates the active team.
      It dynamically updates the board during gameplay, such as highlighting valid moves, updating turn labels, handling piece movements, Xor-Tor switching, and board flips.
      It also displays notifications via pop-ups for invalid moves, captured pieces, and game-over events.

  iii. The Controller acts as the intermediary between the Model and the View.
       The ‘KwazamController’ processes user inputs such as block clicks, menu button selections, and window resizing through listener classes like ‘BlockClickListener’, ‘ResizeListener’, ‘MenuListener’, ‘SaveListener’, and ‘RestartListener’.
       It validates user actions using the Model and ensures that the View reflects the resulting updates.
       Additionally, the KwazamController also handles saving and loading game states by managing mementos created by the Gameboard, enabling seamless game restoration.

2. Memento Design Pattern
- The Memento Design Pattern is used in Kwazam Chess to implement the save and load functionality, allowing players to return to a previous state of the game.
  This pattern is ideal for capturing and restoring an object's state without violating encapsulation.
- It involves three key components:
  i. Originator, which creates and restores the state.
     ‘Gameboard’ class acts as the Originator, responsible for creating (saveState) and restoring (restoreState) mementos that capture the gameboard's configuration, current team, and turn count.

  ii. Memento, which stores the state.
      ‘GameboardMemento’ class stores a snapshot of the chessboard's state.
      It captures the state of the gameboard, current team, and turn count when a save is initiated.

  iii. Caretaker, which manages the mementos.
       ‘KwazamController’ class acts as the caretaker. It maintains a stack of GameboardMemento objects to keep track of saved game states.
       It also manages save and load operations where:
       - Save: Pushes a memento onto the stack and writes the game state to a file.
       - Load: Pops the latest memento from the stack then restores the game state via the Gameboard.


Compile and Run Instructions:
Command Prompt
1. Open terminal and navigate to directory.
   Replace '/path/to/your/files' with the actual path where your Java source files are located:
   cd /path/to/your/files

2. Compile the source codes:
   javac *.java

3. Ensure that the folder includes KwazamChessProgram.java as this is the starting file
   containing the main function.
   Execute the compiled program:
   java KwazamChessProgram

* Ensure that Java Development Kit (JDK) is installed and properly configured.
* All commands above are case sensitive

BlueJ
1. Download the Kwazam project folder 
2. Add a BlueJ package (package.bluej) in downloaded Kwazam project folder 
3. Open BlueJ 
4. Click 'Project' -> 'Open Project' 
5. Navigate to the downloaded folder and open it 
6. The project will load in BlueJ 
7. Compile and run the KwazamChessProgram.java 

