import java.util.ArrayList;

// MVC Design Pattern - Model
/* Memento Design Pattern - Originator
   Responsible for creating a Memento object that captures the current state of the game,
   including piece positions, player turns, and other relevant data. */
// Ngan Li Syuen
public class Gameboard {
    private final Piece[][] gameboard = new Piece[8][5]; // 2D array to store gameboard pieces

    // Gameboard constructor
    public Gameboard(){
        gameboard[0][0] = new Tor("blue", "/pieces/blueTor.png", 0, 0);
        gameboard[0][1] = new Biz("blue", "/pieces/blueBiz.png", 0, 1);
        gameboard[0][2] = new Sau("blue", "/pieces/blueSau2.png", 0, 2);
        gameboard[0][3] = new Biz("blue", "/pieces/blueBiz.png", 0, 3);
        gameboard[0][4] = new Xor("blue", "/pieces/blueXor.png", 0, 4);

        gameboard[7][0] = new Xor("red", "/pieces/redXor.png", 7, 0);
        gameboard[7][1] = new Biz("red", "/pieces/redBiz.png", 7, 1);
        gameboard[7][2] = new Sau("red", "/pieces/redSau.png", 7, 2);
        gameboard[7][3] = new Biz("red", "/pieces/redBiz.png", 7, 3);
        gameboard[7][4] = new Tor("red", "/pieces/redTor.png", 7, 4);

        for(int i=0; i<5; i++)
        {
            gameboard[1][i] = new Ram("blue", "/pieces/blueRam2.png", 1, i, true);
            gameboard[6][i] = new Ram("red", "/pieces/redRam.png", 6, i, true);
        }
    }

    // Get the piece at a position
    public Piece getPieceAt(int r, int c){
        return gameboard[r][c];
    }

    // Move the piece from source position to destination position in gameboard
    public void movePiece(int srcRow, int srcCol, int row, int col)
    {
        gameboard[row][col] = gameboard[srcRow][srcCol];
        gameboard[srcRow][srcCol] = null;
    }
    
    // Switch Xor and Tor
    public ArrayList<Piece> switchPieces()
    {
        ArrayList<Piece> switchList = new ArrayList<Piece>();
        for(int i=0; i<8; i++) // Loop for 8 rows
        {
            for(int j=0; j<5; j++) // Loop for 5 columns
            {
                Piece p = gameboard[i][j];
                if(p!=null){
                    if(p.getName().equals("Tor")) // If piece is Tor
                    {
                        String path = "/pieces/" + p.team + "Xor.png";
                        Xor newXor = new Xor(p.team, path, p.row, p.col); // Create new Xor object
                        gameboard[i][j] = newXor; // Replace Tor in gameboard
                        switchList.add(newXor);
                    }
                    else if(p.getName().equals("Xor")) // If piece is Xor
                    {
                        String path = "/pieces/" + p.team + "Tor.png";
                        Tor newTor = new Tor(p.team, path, p.row, p.col); // Create new Tor object
                        gameboard[i][j] = newTor; // Replace Xor in gameboard
                        switchList.add(newTor);
                    }
                }
            }
        }
        return switchList;
    }

    public void flipBoard(KwazamModel model)
    {
        // Flip horizontally
        for(int row=0; row<8; row++)
        {
            for(int col=0; col<2; col++)
            {
                // flip pieces
                Piece temp = gameboard[row][col]; // piece on left end
                gameboard[row][col] = gameboard[row][4-col]; // swap pieces on 2 ends
                gameboard[row][4-col] = temp;

                // update piece's position
                if(gameboard[row][col] != null)
                    gameboard[row][col].move(row, col);

                if(gameboard[row][4-col] != null)
                    gameboard[row][4-col].move(row, 4-col);
            }
        }

        // Flip vertically
        for(int col=0; col<5; col++)
        {
            for(int row=0; row<4; row++)
            {
                Piece p1 = gameboard[row][col]; // piece on top end
                Piece p2 = gameboard[7-row][col]; // piece on bottom end

                // flip pieces
                gameboard[row][col] = p2;
                gameboard[7-row][col] = p1;

                // update piece's position and img paths
                // top pieces
                if(p1 != null)
                {
                    p1.move(7 - row, col);
                    if(p1.name.equals("Ram") || p1.name.equals("Sau"))
                    {
                        /* p1 Ram (at top half) has 4 flipping scenarios:
                           1. pieces == CurrentTeam, which are the next player's pieces to be flipped down, uses .png path (face upward)
                           2. pieces reaches top border and u-turn (row 0, after flip = row 7), regardless of team, uses .png path (face upward)
                              2.1 next player's piece (piece == CurrentTeam)
                              2.2 current player's piece (piece == OppositeTeam)
                           3. pieces that has u-turn previously and is returning to team members, not reached top border
                              scenario 3 avoid conflicts with scenario 1, where all pieces of next player are flipped to face upward
                              3.1 next player's piece (piece == CurrentTeam), uses 2.png path (face downward)
                              3.2 current player's piece (piece == OppositeTeam), uses .png path (face upward)
                           4. pieces == OppositeTeam, which are the current player's pieces to be flipped up, uses 2.png path (face downward)
                        */
                        if (p1.team.equals(model.getCurrentTeam()) || row == 7) { // next player pieces (on top, to be flipped down)
                            try{                                                     // or any team's pieces that reached the top border (row 0, after flip will be row 7)
                                Ram r = (Ram) p1; // check if piece is ram, if it is sau, catch block will handle its flipping logic
                                if(!r.isMovingForward) // u-turned
                                {
                                    if(row == 7) // scenario 2.1: piece returned to team members, reaches top border & u-turn
                                        r.imagePath = "/pieces/" + p1.team + p1.name + ".png";
                                    else // scenario 3.1: next player piece already u-turned and is heading back to team members, but not yet reach top border
                                        r.imagePath = "/pieces/" + p1.team + p1.name + "2.png";
                                }
                                else // moving forward (normal direction)
                                     // scenario 1: next player's piece to be flipped down
                                     // scenario 2.2: current player's piece has moved forward to opponent's field and reached top border, u-turn
                                    r.imagePath = "/pieces/" + p1.team + p1.name + ".png";
                            }catch(ClassCastException e) // handle sau
                            {
                                p1.imagePath = "/pieces/" + p1.team + p1.name + ".png";
                            }
                        }
                        else { // current player pieces (on top, to be flipped down)
                            try{
                                Ram r = (Ram) p1;
                                if(!r.isMovingForward) // scenario 3.2: current player piece already u-turned and is heading back to team members
                                    r.imagePath = "/pieces/" + p1.team + p1.name + ".png";
                                else // moving forward (normal direction)
                                     // scenario 4: current player's piece to be flipped down
                                    r.imagePath = "/pieces/" + p1.team + p1.name + "2.png";
                            }
                            catch(ClassCastException e) // handle sau
                            {
                                p1.imagePath = "/pieces/" + p1.team + p1.name + "2.png";
                            }
                        }
                    }
                }

                // bottom pieces
                if(p2 != null)
                {
                    p2.move(row, col);
                    if(p2.name.equals("Ram") || p2.name.equals("Sau"))
                    {
                        if (p2.team.equals(model.getOppositeTeam()) || row == 0) { // current player pieces (at bottom, to be flipped up)
                            try{                                                      // or any team's pieces that reached the bottom border (row 7, after flip will be row 0)
                                Ram r = (Ram) p2; // check if piece is ram, if it is sau, catch block will handle its flipping logic
                                if(!r.isMovingForward) // u-turned
                                {
                                    if(row == 0) // piece returned to team members, reaches bottom border & u-turn
                                        r.imagePath = "/pieces/" + p2.team + p2.name + "2.png";
                                    else // current player piece already u-turned and is heading back to team members, but not yet reach bottom border
                                        r.imagePath = "/pieces/" + p2.team + p2.name + ".png";
                                }
                                else // current player's piece to be flipped up or
                                     // next player's piece has moved forward to opponent's field and reached bottom border, u-turn
                                    r.imagePath = "/pieces/" + p2.team + p2.name + "2.png";
                            }
                            catch(ClassCastException e) { // handle sau
                                p2.imagePath = "/pieces/" + p2.team + p2.name + "2.png";
                            }
                        }
                        else { // next player pieces (at bottom, to be flipped up)
                            try{
                                Ram r = (Ram) p2;
                                if(!r.isMovingForward) // next player piece already u-turned and is heading back to team members
                                    r.imagePath = "/pieces/" + p2.team + p2.name + "2.png";
                                else // next player's piece to be flipped up
                                    r.imagePath = "/pieces/" + p2.team + p2.name + ".png";
                            }
                            catch(ClassCastException e) // handle sau
                            {
                                p2.imagePath = "/pieces/" + p2.team + p2.name + ".png";
                            }
                        }
                    }
                }
            }
        }
    }

    // Save current state to a memento (called by Controller)
    /* Memento design pattern is used here to capture and store the state of the board
       (e.g., positions of pieces, player turns, etc.) at a specific point
       without exposing the internal details of the game objects.
    */
    // Yap Yan Ting
    public GameboardMemento saveState (String currentTeam, int currentTurn) {
        // currentTeam is passed-in by Controller due to being declared under Model
        
        // Create new memento object, return to Controller, & pushed into memento stack
        return new GameboardMemento(currentTeam, currentTurn, gameboard);
    }
    
    // Restore a state from a memento (called by Controller)
    /* Memento design pattern is used here to resume the game exactly where it left off
       by restoring the memento (containing game state) saved previously
    */
    // Yap Yan Ting
    public void restoreState(GameboardMemento memento) {
        // Memento's team is restored at Controller due to being declared under Model
        
        Piece[][] restoredState = memento.getCurrentState();
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 5; j++) {
                // Restore all the positions "snapshot" by the memento
                gameboard[i][j] = restoredState[i][j];
            }
        }
    }

}
