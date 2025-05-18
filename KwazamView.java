import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.io.InputStream;
import java.util.*;

// MVC Design Pattern - View
// Yap Sze Thin, Ngan Li Syuen, Yap Yan Ting, Kuan Chee Ling
public class KwazamView extends JFrame {
    private Image backgroundImage;
    public JPanel screenPanel;
    public JPanel menuPanel;
    private JButton mainMenuButton, saveButton, restartButton;
    private JButton[] menuButtons;
    private JPanel boardPanel;
    public JLabel turnLabel;
    private JButton[] boardButtons;

    // View Constructor
    public KwazamView() {
        // Set up frame
        setTitle("Kwazam Chess");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Set minimum size to 45% of the original size
        int minWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.45);
        int minHeight = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.45);
        setMinimumSize(new Dimension(minWidth, minHeight));

        // Load background image
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading background image.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Add the custom panel to JFrame
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null); // Disable layout manager
        setContentPane(backgroundPanel);

        // Panel for the chess game screen -> added to backgroundPanel
        screenPanel = new JPanel(new BorderLayout()); // Use BorderLayout for screenPanel
        screenPanel.setOpaque(false);
        screenPanel.setBounds(0, 0, 100, 100); // Temporary values, will be overridden after frame is fully rendered
        backgroundPanel.add(screenPanel);
        
        // Panel for menu -> added to backgroundPanel
        menuPanel = new JPanel(new GridLayout(3, 1, 0, 15));
        menuPanel.setOpaque(false);
        menuPanel.setBounds(0, 0, 100, 100);
        backgroundPanel.add(menuPanel);

        // Set initial size & bounds based on initial frame size
        // Ensure calculations is only carried out after the frame is fully rendered to avoid returning incorrect dimensions
        SwingUtilities.invokeLater(() -> {
            Dimension screenSize = getSize(); // Get frame size only after the frame is fully rendered
            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;

            // Calculation of screenPanel
            int screenPanelWidth = (int) (screenWidth * 0.27);
            int screenPanelHeight = (int) (screenHeight * 0.82);
            int screenPanelX = (screenWidth - screenPanelWidth) / 2;
            int screenPanelY = (screenHeight - screenPanelHeight) / 2 - (int) (screenPanelHeight * 0.10);
            
            // Calculation of menuPanel
            int menuPanelWidth = (int) (screenWidth * 0.27);
            int menuPanelHeight = (int) (screenHeight * 0.7);
            int menuPanelX = (int)((screenWidth-screenPanelX)*0.7);
            int menuPanelY = (int) (screenHeight*0.14);
            
            // Set initial bounds on start of program
            screenPanel.setBounds(screenPanelX, screenPanelY, screenPanelWidth, screenPanelHeight);
            menuPanel.setBounds(menuPanelX, menuPanelY, menuPanelWidth, menuPanelHeight);
            
            // Revalidate and repaint the frame
            backgroundPanel.revalidate();
            backgroundPanel.repaint();
        });

        // Components on screenPanel
        // Label for player's turn -> added to screenPanel
        turnLabel = new JLabel(" RED ");
        turnLabel.setFont(new Font("Arial", Font.BOLD, 40));
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setBackground(new Color(245,83,71));
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        turnLabel.setOpaque(true);
        screenPanel.add(turnLabel, BorderLayout.NORTH);

        // Panel for the chess board -> added to screenPanel
        boardPanel = new JPanel(new GridLayout(8, 5, 5, 5));
        addBoardBlocks(); // Add the board blocks (JButtons)
        screenPanel.add(boardPanel, BorderLayout.CENTER);
        
        // Components on menuPanel
        mainMenuButton = new JButton("Menu");
        saveButton = new JButton("Save");
        restartButton = new JButton("Restart");
        
        menuButtons = new JButton[3];
        menuButtons[0] = mainMenuButton;
        menuButtons[1] = saveButton;
        menuButtons[2] = restartButton;
        
        for (JButton button : menuButtons) {
            button.setFont(new Font("Tahoma", Font.BOLD, 24));
            menuPanel.add(button);
        }
    }

    // Yap Sze Thin
    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Paint the panel's background

            // Draw the backgroundImage if it exists
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    // Method to add blocks to the board panel
    // Ngan Li Syuen
    private void addBoardBlocks() {
        boardButtons = new JButton[40]; // Initialize the boardButtons array with 40 buttons
        int index = 0; // Index to track position in the boardButtons array

        for (int row = 0; row < 8; row++) { // Loop for 8 rows
            for (int col = 0; col < 5; col++) {  // Loop for 5 columns
                // Create a button for each block
                JButton block = new JButton();
                block.setBackground(Color.WHITE); // Set default background color
                block.setFocusPainted(false); // Remove focus border
                block.setActionCommand(row + "," + col); // Set action command as "row,column" (used at KwazamController to get a block's coordinates)

                // Add the button to the array and board panel
                boardButtons[index++] = block; // Increment the index after assignment
                boardPanel.add(block);  // Add the button to the boardPanel
            }
        }
    }

    // Method to add icons into blocks
    // Ngan Li Syuen
    public void addBlockImg(Gameboard g)
    {
        int btnWidth = boardPanel.getWidth() / 5;
        int btnHeight = boardPanel.getHeight() / 8;
        int btnSize = Math.min(btnWidth, btnHeight);
        int iconSize = (int) (btnSize * 0.8); // Size of the icon

        int index = 0;

        for(int row = 0; row < 8; row++)
        {
            for(int col = 0; col < 5; col++)
            {
                Piece p = g.getPieceAt(row, col);
                if(p != null && p.getimagePath() != null) {
                    try {
                        String path = p.getimagePath();
                        InputStream inputStream = getClass().getResourceAsStream(path);
                        if(inputStream == null) {
                            System.err.println("Could not find resource: " + path);
                            continue;
                        }

                        Image icon = ImageIO.read(inputStream); // Read image path as image
                        if(icon != null){
                            Image resizedIcon = icon.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH); // Scale image
                            Icon blockIcon = new ImageIcon(resizedIcon); // Transform image to icon
                            boardButtons[index].setIcon(blockIcon); // Set icon
                        }
                        else{
                            System.err.println("Failed to load image: " + path);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error loading icon image.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if(p == null) // The block does not contain chess pieces, hence no icon
                    boardButtons[index].setIcon(null);

                index++;
            }
        }
    }

    // Highlight possible positions in pastel green
    // Yap Sze Thin
    public void highlightBlocks (ArrayList<Position> possiblePos) {
        Color pastelGreen = new Color(195,255,229);
        
        for (Position pos : possiblePos) {
            // Calculate index of block in boardButtons array [0-39]
            int index = pos.getRow()*5 + pos.getCol();
            // Set possible position blocks to pastel green background
            boardButtons[index].setBackground(pastelGreen);
        }
    }
    
    // Clear highlight color on all blocks
    // Yap Sze Thin
    public void clearHighlights() {
        for (JButton button: boardButtons) {
            button.setBackground(Color.WHITE);
        }
    }
    
    // Update turn label with background color
    public void updateTurnLabel(String color)
    {
        if (color.equals("red")) {
            turnLabel.setBackground(new Color(245,83,71));
            turnLabel.setText("RED");
        }
        else {
            turnLabel.setBackground(new Color(4,185,217));
            turnLabel.setText("BLUE");
        }
    }

    // Update block images after piece movement
    // Ngan Li Syuen
    public void updateBlock(int srcRow, int srcCol, int destRow, int destCol, KwazamModel model) {
        int srcIndex = srcRow*5 + srcCol;
        int destIndex = destRow*5 + destCol;

        // 1. Get source icon and assign to destination block
        Icon srcIcon = boardButtons[srcIndex].getIcon();
        boardButtons[destIndex].setIcon(srcIcon);

        // 2. Clear source block image
        boardButtons[srcIndex].setIcon(null);
        
        // 3. Check if the piece at the destination has an updated imagePath and reload the icon
        Piece piece = model.getGameboard().getPieceAt(destRow, destCol);
        if (piece != null && piece.getimagePath() != null) {
            try {
                InputStream inputStream = getClass().getResourceAsStream(piece.getimagePath());
                if (inputStream != null) {
                    Image icon = ImageIO.read(inputStream);
                    int btnWidth = boardPanel.getWidth() / 5;
                    int btnHeight = boardPanel.getHeight() / 8;
                    int iconSize = (int) (Math.min(btnWidth, btnHeight) * 0.8); // Size of icon
    
                    Image resizedIcon = icon.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
                    boardButtons[destIndex].setIcon(new ImageIcon(resizedIcon)); // Set icon
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading icon image.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        boardButtons[destIndex].revalidate();
        boardButtons[destIndex].repaint();
        boardPanel.revalidate();
        boardPanel.repaint();
    }
    
    // Method to change Xor Tor icons when they transform
    // Ngan Li Syuen
    public void switchBlocks(ArrayList<Piece> switchList)
    {
        int btnWidth = boardPanel.getWidth() / 5;
        int btnHeight = boardPanel.getHeight() / 8;
        int btnSize = Math.min(btnWidth, btnHeight);
        int iconSize = (int) (btnSize * 0.8); // Size of icon

        for(int i=0; i<switchList.size(); i++) // Loop the pieces to be changed
        {
            Piece p = switchList.get(i);
            int index = p.row * 5 + p.col;
            String path = p.imagePath;
            try {
                InputStream inputStream = getClass().getResourceAsStream(path);
                if (inputStream == null) {
                    System.err.println("Could not find resource: " + path);
                    continue;
                }

                Image icon = ImageIO.read(inputStream); // Read image path as image
                if (icon != null)
                {
                    Image resizedIcon = icon.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH); // Scale image
                    Icon blockIcon = new ImageIcon(resizedIcon); // Transform image to icon
                    boardButtons[index].setIcon(blockIcon); // Set icon
                }
                else{
                    System.err.println("Failed to load image: " + path);
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading icon image.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            boardButtons[index].revalidate();
            boardButtons[index].repaint();
            boardPanel.revalidate();
            boardPanel.repaint();
        }
    }

    // Yap Yan Ting
    public String showMenuDialog() {
        final String[] confirmationChoice = {"doNothing"}; // Array to hold user's choice
        // Menu pop up window
        JDialog menuDialog = new JDialog(this, "Menu", true);
        menuDialog.setLayout(new GridLayout(3, 1, 10, 10)); 
        JButton playButton = new JButton("Play");
        JButton loadButton = new JButton("Load");
        JButton exitButton = new JButton("Exit");
        menuDialog.add(playButton);
        menuDialog.add(loadButton);
        menuDialog.add(exitButton);

        // Play
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuDialog.dispose(); // Do nothing and close the dialog
            }
        });

        // Load
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuDialog.dispose(); 
                confirmationChoice[0] = loadConfirmation(); // Get user's choice from load confirmation dialog
            }
        });

        // Exit
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuDialog.dispose();
                confirmationChoice[0] = exitConfirmation(); // Get user's choice from exit confirmation dialog
            }
        });
        
        menuDialog.setSize(400, 200);
        menuDialog.setLocationRelativeTo(this);
        menuDialog.setVisible(true);
        
        return confirmationChoice[0]; // Return user's choice to KwazamController
    }

    // Method that is called when restart button is clicked
    // Yap Yan Ting
    public boolean newGameConfirmation() {
        final boolean[] isNewGame = {false}; // Array to hold the result due to variables modification
                                             // within an inner class or lambda must be final
        
        JDialog newGameDialog = new JDialog(this, "New Game", true);
        newGameDialog.setLayout(new GridLayout(2, 1));
        JLabel message = new JLabel("Are you sure? Current progress will be lost.", SwingConstants.CENTER);
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));
        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");
        
        // Inner class
        // Confirm new game
        confirmButton.addActionListener(new ActionListener() { 
            @Override           
            public void actionPerformed(ActionEvent e) {
                isNewGame[0] = true; 
                newGameDialog.dispose();
            }
        });

        // Cancel new game
        cancelButton.addActionListener(new ActionListener() {
            @Override           
            public void actionPerformed(ActionEvent e) {
                newGameDialog.dispose(); // Do nothing and close the dialog
            }
        });
        
        buttonsPanel.add(confirmButton);
        buttonsPanel.add(cancelButton);
        newGameDialog.add(message);
        newGameDialog.add(buttonsPanel);
        newGameDialog.setSize(400, 200);
        newGameDialog.setLocationRelativeTo(this);
        newGameDialog.setVisible(true);
        
        return isNewGame[0]; // Return user's choice to KwazamController
    }

    // Method that is called when load button is clicked
    // Yap Yan Ting
    private String loadConfirmation() {
        final String[] loadChoice = {"doNothing"}; // Array to hold user's choice
        
        JDialog loadDialog = new JDialog(this, "Load", true);
        loadDialog.setLayout(new GridLayout(2, 1));
        JLabel message = new JLabel("Are you sure to load the previous game? Current progress will be lost.", SwingConstants.CENTER);
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));
        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");

        // Confirm load game
        confirmButton.addActionListener(new ActionListener() {
            @Override           
            public void actionPerformed(ActionEvent e) {
                loadChoice[0] = "load";
                loadDialog.dispose();
            }
        });

        // Cancel load game
        cancelButton.addActionListener(new ActionListener() {
            @Override           
            public void actionPerformed(ActionEvent e) {
                loadDialog.dispose(); // Do nothing and close the dialog
            }
        });
        
        buttonsPanel.add(confirmButton);
        buttonsPanel.add(cancelButton);
        loadDialog.add(message);
        loadDialog.add(buttonsPanel);
        loadDialog.setSize(500, 200);
        loadDialog.setLocationRelativeTo(this);
        loadDialog.setVisible(true);
        
        return loadChoice[0]; // Return user's choice to menu dialog
    }

    // Method that is called when exit button is clicked
    // Yap Yan Ting
    private String exitConfirmation() {
        final String[] exitChoice = {"doNothing"};
        
        JDialog exitDialog = new JDialog(this, "Exit", true);
        exitDialog.setLayout(new GridLayout(2, 1));
        JLabel message = new JLabel("Save your progress before exiting?", SwingConstants.CENTER);
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 3));
        JButton saveButton = new JButton("Save");
        JButton dontSaveButton = new JButton("Don't Save");
        JButton cancelButton = new JButton("Cancel");

        // Save progress and exit
        saveButton.addActionListener(new ActionListener() {
            @Override           
            public void actionPerformed(ActionEvent e) {
                exitChoice[0] = "save";
                exitDialog.dispose(); 
            }
        });

        // Don't save progress and exit
        dontSaveButton.addActionListener(new ActionListener() {
            @Override           
            public void actionPerformed(ActionEvent e) {
                exitChoice[0] = "noSave";
                exitDialog.dispose();
            }
        });

        // Cancel exit
        cancelButton.addActionListener(new ActionListener() {
            @Override           
            public void actionPerformed(ActionEvent e) {
                exitDialog.dispose(); // Do nothing and close the dialog
            }
        });
        
        buttonsPanel.add(saveButton);
        buttonsPanel.add(dontSaveButton);
        buttonsPanel.add(cancelButton);
        exitDialog.add(message);
        exitDialog.add(buttonsPanel);
        exitDialog.setSize(400, 200);
        exitDialog.setLocationRelativeTo(this);
        exitDialog.setVisible(true);
        
        return exitChoice[0]; // Return user's choice to menu dialog
    }

    // Method to reset view when restart button clicked
    // Kuan Chee Ling
    public void restartView(Gameboard gameboard) {
        clearHighlights(); // Remove any highlights
        addBlockImg(gameboard); // Redraw the board with the initial pieces
        updateTurnLabel("red");
    }

    // Getter
    public JButton[] getBoardButtons(){return boardButtons;}
    public JButton[] getMenuButtons() {return menuButtons;}
}
