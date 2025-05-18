// Ngan Li Syuen
// Inheritance : Subclass of Piece

import java.util.ArrayList;

public class Tor extends Piece{

    public Tor(String team, String imagePath, int row, int col){
        super("Tor", team, imagePath, row, col);
    }

    // overriding abstract method, polymorphism
    @Override
    public Piece clone() {
        return new Tor(this.team, this.imagePath, this.row, this.col);
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

        ArrayList<Position> temp = new ArrayList<Position>();

        // check vertical
        for(int i=0; i<8; i++) {
            Piece p = model.getGameboard().getPieceAt(i, srcCol);

            if (p != null) {
                if (i < srcRow) // top
                {
                    temp.clear();
                    if (p.team.equals(currentTeam))
                        continue;
                } else if (i == srcRow) { // current piece
                    continue;
                } else { // bottom
                    if (p.team.equals(oppositeTeam))
                        temp.add(new Position(i, srcCol));
                    break;
                }
            }

            temp.add(new Position(i, srcCol));
        }
            possiblePos.addAll(temp);
            temp.clear();


        // check horizontal
        for (int i = 0; i < 5; i++) {
            Piece p = model.getGameboard().getPieceAt(srcRow, i);

            if (p != null) {
                if (i < srcCol) // left
                {
                    temp.clear();
                    if (p.team.equals(currentTeam))
                        continue;
                } else if (i == srcCol) // current piece
                {
                    continue;
                } else // right
                {
                    if (p.team.equals(oppositeTeam))
                        temp.add(new Position(srcRow, i));
                    break;
                }
            }

            temp.add(new Position(srcRow, i));
        }

        possiblePos.addAll(temp);
    }
}

