import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Stack;
import java.io.*;

// MVC Design Pattern - Controller
/* Memento Design Pattern - Caretaker
   Manages Memento objects by saving them to storage or memory and retrieving them when needed to restore the game state.
*/
// Yap Sze Thin, Ngan Li Syuen, Yap Yan Ting, Kuan Chee Ling

public class KwazamController {
    // Create Model & View objects and a memento stack
    private KwazamView view;
    private static KwazamModel model;
    private Stack<GameboardMemento> mementoStack = new Stack<>();

    // Attributes to track selected piece
    private Piece selectedPiece = null;

    // Constructor to link the view and model
    public KwazamController (KwazamView view, KwazamModel model) {
        this.view = view;
        this.model = model;
        
        // Attach listeners
        addListeners();
        
        SwingUtilities.invokeLater(() -> showMenu()); // Show main menu at the start of the game
    }

    // Add listeners for all buttons and certain components
    private void addListeners() {
        // Add blocks listener
        JButton[] boardbtns = view.getBoardButtons();
        for(JButton btn: boardbtns)
        {
            btn.addActionListener(new BlockClickListener());
        }

        // Add resize listener
        ResizeListener resizeListener = new ResizeListener(view);
        view.addComponentListener(resizeListener);
        
        // Trigger resizeListener right after the frame becomes visible
        SwingUtilities.invokeLater(() -> resizeListener.componentResized(null));

        // Other listeners...
        view.getMenuButtons()[0].addActionListener(new MenuListener());
        view.getMenuButtons()[1].addActionListener(new SaveListener());
        view.getMenuButtons()[2].addActionListener(new RestartListener());
    }
    
    public void showMenu()
    {
        String menuChoice = view.showMenuDialog(); // Receive user's choice from KwazamView
        
        switch (menuChoice)
        {
            case "load":
                loadGame();
                break;
            case "save":
                saveGame();
                System.exit(0);
                break;
            case "noSave":
                System.exit(0); // close the window
            default:
                break;
        }
    }

    // When menu button is clicked
    private class MenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt){
            showMenu();
        }
    }
    
    // Save current game state (triggered by in-game menu - "Save" button)
    /* Memento design pattern is used here to capture and store the state of the board
       (e.g., positions of pieces, player turns, etc.) at a specific point
       without exposing the internal details of the game objects.
    */
    public void saveGame() {
        String filename = "Kwazam.txt";
        // Call saveState() from gameboard to get a snapshot of gamestate
        GameboardMemento memento = model.getGameboard().saveState(model.getCurrentTeam(), model.getTurns());
        mementoStack.push(memento); // Push the memento onto the stack

        // Write game state in text file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Current Team: " + memento.getCurrentTeam()); // Save current team
            writer.newLine();
            writer.write("Current Turn: " + String.valueOf(memento.getTurns()) + "(Round " + String.valueOf((memento.getTurns()+1)/2) + ")"); // Save current turn
            writer.newLine();
            writer.write("Current Position: ");
            writer.newLine();
            writer.write("Name | Team | Row | Column | ImagePath | isMovingForward");
            writer.newLine();
            
            // Save the gameboard state
            Piece[][] board = memento.getCurrentState();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 5; j++) {
                    Piece piece = board[i][j];
                    if (piece != null) {
                        String isMovingForward = "-";
                        if(piece instanceof Ram) {  // if piece is string, record its isMovingForward value
                            Ram r = (Ram) piece;
                            isMovingForward = String.valueOf(r.isMovingForward);
                        }
                        // Format: name,team,row,col,imagePath
                        writer.write(piece.getName() + ", " + piece.getTeam() + ", " + i + ", " + j + ", " + piece.getimagePath() + ", " + isMovingForward);
                        writer.newLine();
                    }
                }
            }
            
            JOptionPane.showMessageDialog(view, "Game saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view, "Error saving game!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // When save button is clicked
    private class SaveListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent evt)
        {
            saveGame(); // Calls save game
        }
    }
    
    // Load latest game state (triggered by main menu - "Load" button)
    /* Memento design pattern is used here to resume the game exactly where it left off
       by restoring the memento (containing game state) saved previously
    */
    public void loadGame() {
        String filename = "Kwazam.txt";
        
        // Check if the saved file exists
        File file = new File(filename);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(view, "No saved game to load!");
            return;
        }

        // Read the text file containing saved game state
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String currentTeam = reader.readLine().split(": ")[1];
            int currentTurn = Integer.parseInt(reader.readLine().split(": ")[1].split("\\(")[0]);
            
            Piece[][] restoredState = new Piece[8][5];
            reader.readLine();
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                String name = parts[0];
                String team = parts[1];
                int row = Integer.parseInt(parts[2]);
                int col = Integer.parseInt(parts[3]);
                String imagePath = parts[4];
                String isMovingForward = parts[5];
                
                Piece piece = null;
                if (name.equals("Ram")) {
                    piece = new Ram(team, imagePath, row, col, true);
                    Ram r = (Ram) piece;
                    if (isMovingForward.equals("true"))
                        r.isMovingForward = true;
                    else
                        r.isMovingForward = false;

                } else if (name.equals("Biz")) {
                    piece = new Biz(team, imagePath, row, col);
                } else if (name.equals("Tor")) {
                    piece = new Tor(team, imagePath, row, col);
                } else if (name.equals("Xor")) {
                    piece = new Xor(team, imagePath, row, col);
                } else if (name.equals("Sau")) {
                    piece = new Sau(team, imagePath, row, col);
                }


                if (piece != null) {
                    restoredState[row][col] = piece;
                }
            }  
                        
            GameboardMemento memento;
            
            if (!mementoStack.isEmpty()) { // if mementoStack not empty
                // Pop the most recently saved memento
                memento = mementoStack.pop();
            } else { 
                // If the user hasn't saved any states during the current session, but a saved txt file exists,
                // create a new memento using the game state loaded from the text file and push it onto the stack.
                memento = new GameboardMemento(currentTeam, currentTurn, restoredState);
                mementoStack.push(memento);
            }
            
            model.getGameboard().restoreState(memento);
            model.setCurrentTeam(currentTeam);
            model.setTurns(currentTurn);

            // If a new game is halfway played when player decide to load old game,
            // clear the selectedPiece and highlights
            if(selectedPiece != null) {
                selectedPiece.possiblePos.clear();
                selectedPiece = null;
            }
            view.clearHighlights();

            // Refresh in the view...
            view.addBlockImg(model.getGameboard());
            view.updateTurnLabel(currentTeam);
            
            JOptionPane.showMessageDialog(view, "Game loaded successfully!");
        
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view, "Error loading game!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    
    }   
    
    public void restartGame()
    {
        model.restartModel(); // Reset the model state
        view.restartView(model.getGameboard()); // Refresh the view
         if (selectedPiece != null) {
            selectedPiece.possiblePos.clear(); 
            selectedPiece = null;
        }
        JOptionPane.showMessageDialog(view, "Game has been restarted!");
    }
    
    // Listener classes...
    private class RestartListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent evt)
        {
            if (view.newGameConfirmation() == true) // if isNewGame is true
                restartGame();
        }
    }
    
    // ResizeListener : handles screenPanel, turnLabel font size, & block images' resizing when the window is resized
    // Yap Sze Thin
    private static class ResizeListener extends ComponentAdapter {
        private final KwazamView view;

        public ResizeListener(KwazamView view) {
            this.view = view;
        }

        @Override
        public void componentResized(ComponentEvent evt) {
            // Get window's width and height
            int screenWidth = view.getWidth();
            int screenHeight = view.getHeight();

            // Calculation of screenPanel
            // Calculate 27% width and 82% height of the frame size
            int screenPanelWidth = (int) (screenWidth * 0.27);
            int screenPanelHeight = (int) (screenHeight * 0.82);
            
            // Calculate position of the frame
            int centeredX = (screenWidth - screenPanelWidth) / 2;
            int offsetY = (int) (screenPanelHeight * 0.03); // 3% of panel height
            int centeredY = ((screenHeight - screenPanelHeight) / 2)- offsetY;
            
            // Calculation of menuPanel
            int menuPanelWidth = (int) (screenWidth * 0.12);
            int menuPanelHeight = (int) (screenHeight * 0.3255);
            int menuPanelX = (int)((screenWidth-centeredX)*0.315);
            int menuPanelY = (int) (screenHeight*0.065);

            // 1. Reset size for screenPanel & menuPanel and position them manually
            // screenPanel
            view.screenPanel.setPreferredSize(new Dimension(screenPanelWidth, screenPanelHeight));
            view.screenPanel.setBounds(
                    centeredX, // Center horizontally
                    centeredY, // Center vertically
                    screenPanelWidth, screenPanelHeight // Set width and height
            );
            
            // menuPanel 
            view.menuPanel.setPreferredSize(new Dimension(menuPanelWidth, screenPanelHeight));
            view.menuPanel.setBounds(menuPanelX, menuPanelY, menuPanelWidth, menuPanelHeight);

            // 2. Dynamic font size for turnLabel & menuButtons (based on screen height)
            // turnLabel
            int fontSize = Math.max(20, (int) (screenHeight * 0.05));  // 5% of screen height
            view.turnLabel.setFont(new Font("Arial", Font.BOLD, fontSize));

            // menuButtons
            int buttonFontSize = Math.max(1, (int) (screenHeight * 0.023));  
            for (JButton button : view.getMenuButtons()) {
                button.setFont(new Font("Tahoma", Font.BOLD, buttonFontSize));
            }

            view.screenPanel.revalidate(); // Revalidate the layout to apply the size changes
            view.screenPanel.repaint();
            view.menuPanel.revalidate();
            view.menuPanel.repaint();

            // 3. Resize block images
            SwingUtilities.invokeLater(()->{
                view.addBlockImg(model.getGameboard());
            });
        }
    }
    
    // BlockClickListener : Handles block clicks to set background color to green (testing only)
    // Yap Sze Thin, Ngan Li Syuen, Yap Yan Ting, Kuan Chee Ling
    private class BlockClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 1. Get the row & col of the selected block
            String position = e.getActionCommand(); // get the button's "row, col"
            String[] coordinates = position.split(","); // split by comma and add to string array
            // Convert coordinates to int
            int row = Integer.parseInt(coordinates[0]);
            int col = Integer.parseInt(coordinates[1]);
            
            // Case 1: First click of the turn (selecting the piece to move)
            if (selectedPiece == null) { // No selected piece for this round yet
                // Case 1A: First click of the turn is a block with piece
                
                // Get the piece object at that gameboard position
                selectedPiece = model.getGameboard().getPieceAt(row,col); 
                
                if (selectedPiece != null) {  
                    
                    Piece clickedPiece = selectedPiece;
                    
                    if (!clickedPiece.getTeam().equals(model.getCurrentTeam())) {
                        JOptionPane.showMessageDialog(view, "You cannot select this piece!");
                        selectedPiece = null;
                    } else {
                        // Calculate the possible valid moves for the selected piece
                        selectedPiece.getPossiblePos(row, col, model);
                        
                        if(selectedPiece.possiblePos.isEmpty()){
                            JOptionPane.showMessageDialog(view, "No possible move right now. \nPlease choose another piece!");
                            selectedPiece = null;
                        } else {
                            // Highlight the possible moves in green
                            view.highlightBlocks(selectedPiece.possiblePos);
                        }
                    }

                } else {    // Case 1B: First click of the turn is an empty block
                    JOptionPane.showMessageDialog(view, "Please select a piece!");
                }
                
            }
            else {
                // Case 2: Second click (unselect current piece)
                if(row == selectedPiece.row && col == selectedPiece.col)
                {
                    view.clearHighlights();
                }
                else
                {
                    // Case 2: Second click of the turn (click the destination)
                    // Determine if the clicked block is a valid destination
                    boolean validMove = selectedPiece.isValidMove(row, col);

                    // Case 2A: If valid, change the piece's position
                    if (validMove) {
                        // 0. Display message for captured piece
                        Piece capturedPiece = model.getGameboard().getPieceAt(row,col);
                        
                        if (capturedPiece != null && selectedPiece.isEnemy(capturedPiece)) {
                            // If the captured piece is Sau, game over
                            if (capturedPiece.getName().equals("Sau")) {
                                JOptionPane.showMessageDialog(
                                    view,
                                    capturedPiece.getTeam().toUpperCase() + "'s Sau has been captured! \n               " +
                                    model.getCurrentTeam().toUpperCase() + " won!",
                                    "GAME OVER!",
                                    JOptionPane.INFORMATION_MESSAGE
                                );
                    
                                restartGame(); // To be replaced with game reset logic
                                selectedPiece.possiblePos.clear();
                                selectedPiece = null;
                                return; // Skip the rest of the code and exit the method
                            }
                            
                            // Else just display normal capture message
                            JOptionPane.showMessageDialog(
                                view,
                                model.getCurrentTeam().toUpperCase() + "'s " + selectedPiece.getName() + " captured " 
                                + capturedPiece.getTeam().toUpperCase() + "'s " + capturedPiece.getName() + "!",
                                "Piece Captured",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                            
                        }
                        
                        // 1. Update in KwazamModel -> gameboard
                        model.getGameboard().movePiece(selectedPiece.row, selectedPiece.col, row, col);

                        // 2. Update in KwazamView
                        view.clearHighlights();
                        view.updateBlock(selectedPiece.row, selectedPiece.col, row, col, model);

                        // 3. Modify position attributes in piece
                        selectedPiece.move(row, col);
                        
                        // 4. Increment turns
                        model.setTurns();

                        // 5. Switch tor and xor every 2 turns
                        if(model.getTurns()!=0 && model.getTurns()%4 == 1)
                        {
                            ArrayList<Piece> switchList = model.getGameboard().switchPieces();
                            if(switchList != null)
                                view.switchBlocks(switchList);
                        }

                        // 6. Switch team 
                        if(model.getCurrentTeam().equals("red"))
                            model.setCurrentTeam("blue");
                        else
                            model.setCurrentTeam("red");
                        view.updateTurnLabel(model.getCurrentTeam());

                        // 7. Flip board
                        model.getGameboard().flipBoard(model);
                        view.addBlockImg(model.getGameboard());
                        
                    } else {    // Case 2B: Button clicked is not a valid move
                        JOptionPane.showMessageDialog(view, "Invalid move!");
                        view.clearHighlights();
                    }
                }

                // Reset attributes to null for the next move
                selectedPiece.possiblePos.clear();
                selectedPiece = null;
            }
        }
    }    
}
