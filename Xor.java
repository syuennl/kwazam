// Ngan Li Syuen
// Inheritance : Subclass of Piece

import java.util.ArrayList;

public class Xor extends Piece{
    public Xor(String team, String imagePath, int row, int col){
        super("Xor", team, imagePath, row, col);
    }

    // overriding abstract method, polymorphism
    @Override
    public Piece clone() {
        return new Xor(this.team, this.imagePath, this.row, this.col);
    }

    // overriding abstract method, polymorphism
    @Override
    public void getPossiblePos(int srcRow, int srcCol, KwazamModel model) {
        String currentTeam = model.getCurrentTeam();
        String oppositeTeam;
        if(currentTeam.equals("red"))
            oppositeTeam = "blue";
        else
            oppositeTeam = "red";

        int startRow = srcRow - srcCol;

        ArrayList<Position> temp = new ArrayList<Position>();

        // check left diagonal (\)
        for(int i=0; i<5; i++)
        {
            if(startRow < 0) // out of bound
            {
                startRow++;
                continue;
            }

            if(startRow > 7)
                break;

            Piece p = model.getGameboard().getPieceAt(startRow, i);

            if(p != null) {
                if(i<srcCol) { // top left
                    temp.clear();
                    if(p.team.equals(currentTeam))
                    {
                        startRow++;
                        continue;
                    }
                }
                else if(i == srcCol) // current piece
                {
                    startRow++;
                    continue;
                }
                else // bottom right
                {
                    if(p.team.equals(oppositeTeam))
                        temp.add(new Position(startRow, i));
                    break;
                }
            }

            temp.add(new Position(startRow, i));
            startRow++;
        }

        possiblePos.addAll(temp);
        temp.clear();

        // check right diagonal (/)
        startRow = srcRow + srcCol;

        for(int i=0; i<5; i++)
        {
            if(startRow > 7) // out of bound
            {
                startRow--;
                continue;
            }

            if(startRow < 0)
                break;

            Piece p = model.getGameboard().getPieceAt(startRow, i);
            if(p != null) {
                if(i<srcCol) { // bottom left
                    temp.clear();
                    if(p.team.equals(currentTeam))
                    {
                        startRow--;
                        continue;
                    }
                }
                else if(i == srcCol) // current piece
                {
                    startRow--;
                    continue;
                }
                else // top right
                {
                    if(p.team.equals(oppositeTeam))
                        temp.add(new Position(startRow, i));
                    break;
                }
            }

            temp.add(new Position(startRow, i));
            startRow--;
        }

        possiblePos.addAll(temp);
    }
}
