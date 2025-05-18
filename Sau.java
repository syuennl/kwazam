// Kuan Chee Ling
// Inheritance : Subclass of Piece

public class Sau extends Piece{

    public Sau(String team, String imagePath, int row, int col){
        super("Sau", team, imagePath, row, col);
    }

    // overriding abstract method, polymorphism
    @Override
    public Piece clone() {
        return new Sau(this.team, this.imagePath, this.row, this.col);
    }

    // overriding abstract method, polymorphism
    @Override
    public void getPossiblePos(int srcRow, int srcCol, KwazamModel model) {
        // clear old possible position in the list before looking for new possible position
        possiblePos.clear();
        
        // 3 row range
        for (int r = -1; r <= 1; r++) {
            // 3 column range
            for (int c = -1; c <= 1; c++) {
                Position tempPos = new Position(srcRow+r, srcCol+c);
                
                if (tempPos.isInBound()) {
                    Piece pieceAtPos = model.getGameboard().getPieceAt(tempPos.getRow(), tempPos.getCol()); // Get the piece on tempPos
        
                    // Add tempPos to possiblePos only if the block is empty or contains an enemy
                    // Will not add to possiblePos if the tempPos contains same team's piece
                    if (pieceAtPos == null || isEnemy(pieceAtPos)) {
                        possiblePos.add(tempPos); 
                    }
                }
            }
        }
    }
}
