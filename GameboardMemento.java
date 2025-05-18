// Memento class
// Memento Design Pattern - Memento
/*
The Memento Design Pattern is used for saving and loading game states in Kwazam chess game
because it allows the game to capture and store the state of the board (e.g., positions of pieces, player turns, etc.)
at a specific point in time without exposing the internal details of the game objects.

This encapsulated state, or "memento," can be saved and later restored to resume the game exactly where it left off.
By using this pattern, the game ensures a clean separation between the state-saving logic and the game mechanics,
promoting maintainability and flexibility. Additionally, it provides a way to implement features like undo moves by reverting to a previously stored state.
*/

// Yap Sze Thin

public class GameboardMemento {
    private final String currentTeam;
    private final int turns;
    private final Piece[][] currentState;
    
    // Constructor for a memento
    public GameboardMemento(String currentTeam, int turns, Piece[][] currentState ) {
        this.currentTeam = currentTeam;
        this.turns = turns;
        this.currentState = new Piece[8][5]; // 2d array like gameboard
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 5; j++) {
                // Make a deep copy of each Piece object
                if (currentState[i][j] != null) {
                    this.currentState[i][j] = currentState[i][j].clone();
                } else {
                    this.currentState[i][j] = null; 
                }
            }
        }
    }
    
    // Getters
    public String getCurrentTeam() {
        return currentTeam;
    }
    
    public int getTurns() {
        return turns;
    }
    
    public Piece[][] getCurrentState() {
        return currentState;
    }
    
}
