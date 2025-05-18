// Yap Yan Ting
// Inheritance : Subclass of Piece

public class Biz extends Piece{

    public Biz(String team, String imagePath, int row, int col){
        super("Biz", team, imagePath, row, col);
    }

    // overriding abstract method, polymorphism
    @Override
    public Piece clone() {
        return new Biz(this.team, this.imagePath, this.row, this.col);
    }

    // overriding abstract method, polymorphism
    @Override
    public void getPossiblePos(int srcRow, int srcCol, KwazamModel model) {
         // Clear old possible positions in the list before looking for new possible positions
        if (possiblePos.size() > 0) {
            possiblePos.clear();
        }

        // Define the L-shaped moves
        int[][] moves = {
            {-2, -1}, {-2, 1}, // Upward L-moves
            {-1, -2}, {-1, 2}, // Leftward L-moves
            {1, -2}, {1, 2},   // Rightward L-moves
            {2, -1}, {2, 1}    // Downward L-moves
        };

        for (int[] move : moves) {
            int newRow = srcRow + move[0];
            int newCol = srcCol + move[1];
            Position tempPos = new Position(newRow, newCol);

            // Check if the position is within bounds
            
            if (tempPos.isInBound()) {
                Piece targetPiece = model.getGameboard().getPieceAt(newRow, newCol);
                 // If the position is empty or contains an enemy, it's valid
                if (targetPiece == null || this.isEnemy(targetPiece)) {
                    possiblePos.add(tempPos);
                }
            }
            
        }

    }

}
