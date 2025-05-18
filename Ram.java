// Yap Sze Thin
// Inheritance : Subclass of Piece
// Ram : Moves forward 1 step, reverse when reached the end of board
public class Ram extends Piece {

    protected boolean isMovingForward;  // True = Moving upwards; False = Moving downwards

    // Constructor
    public Ram(String team, String imagePath, int row, int col, boolean isMovingForward) {
        super("Ram", team, imagePath, row, col);
        this.isMovingForward = isMovingForward;
    }

    // overriding abstract method, polymorphism
    @Override
    public Piece clone() {
        return new Ram(this.team, this.imagePath, this.row, this.col, this.isMovingForward);
    }

    // overriding abstract method, polymorphism
    @Override
    public void move(int destRow, int destCol) {
        // 1. Move first
        super.move(destRow, destCol); // Update the piece's position

        // 2. Reverse direction right after Ram reaches the end of the board 
        if (destRow == 0 || destRow == 7) { // 0: Top-most, 7: Bottom
            isMovingForward = !isMovingForward; // Toggle direction
        }
    }

    // overriding abstract method, polymorphism
    @Override
    public void getPossiblePos(int srcRow, int srcCol, KwazamModel model) {
        // If Ram is moving upwards, destination row = the row above it;
        // If Ram is moving downwards, destination row = the row below it.
        int possibleRow = isMovingForward ? srcRow - 1 : srcRow + 1;

        // Column of destination position = same as source column
        int possibleCol = srcCol;
        
        // Create position object for the possible position
        Position tempPos = new Position(possibleRow, possibleCol);
        
        if(tempPos.isInBound()) {
            Piece pieceAtPos = model.getGameboard().getPieceAt(possibleRow, possibleCol); // Get the piece on tempPos
        
            // Add tempPos to possiblePos only if the block is empty or contains an enemy
            // Will not add to possiblePos if the tempPos contains same team's piece
            if (pieceAtPos == null || isEnemy(pieceAtPos)) {
                possiblePos.add(tempPos); 
            }
        }
    }
}
