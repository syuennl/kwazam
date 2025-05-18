
import javax.lang.model.type.ArrayType;
import java.util.ArrayList;

// Abstract piece class
// Yap Sze Thin
abstract class Piece {
    protected String name;  // Ram/ Biz/ Tor/ Xor/ Sau
    protected String team;  // "Red" or "Blue"
    protected String imagePath;  // File name for piece image
    protected int row, col; // Current position
    protected ArrayList<Position> possiblePos = new ArrayList<Position>();

    // Constructor
    public Piece (String name, String team, String imagePath, int row, int col) {
        this.name = name;
        this.team = team;
        this.imagePath = imagePath;
        this.row = row;
        this.col = col;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getTeam() {
        return team;
    }

    public String getimagePath() {
        return imagePath;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    // Update piece position
    public void move(int destRow, int destCol) {
        this.row = destRow;
        this.col = destCol;
    }
    
    // Get a list of possible valid destinations based on the piece's current position
    // abstract method for polymorphism
    abstract public void getPossiblePos(int srcRow, int srcCol, KwazamModel model);
    
    // Determine if the destination selected is valid by comparing with the possiblePos list
    public boolean isValidMove(int destRow, int destCol){
        for (Position pos : possiblePos) {
            // Check if the destination matches any position in the possiblePos list
            if (pos.getRow() == destRow && pos.getCol() == destCol) {
                return true; // match found, valid move
            }
        }
        return false; // no match found, invalid move
    };


    // Check if another piece is an enemy
    public boolean isEnemy(Piece other) {
        // A piece is an enemy if it is not null && the piece is not of the same team of current piece
        if ( other != null && !(other.getTeam().equals(this.team)) ) {
            return true;
        } else {
            return false;
        }
    }

    // Deep copies of Piece objects to fix GameboardMemento shallow copy issues
    // abstract method for polymorphism
    // Yap Yan Ting
    public abstract Piece clone();
}

