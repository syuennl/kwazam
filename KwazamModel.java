// MVC Design Pattern - Model
// Yap Sze Thin, Ngan Li Syuen
public class KwazamModel {
    // Gameboard instance
    private Gameboard gameboard;
    private String currentTeam = "red";
    private int turns = 1;
    
    // Model constructor
    public KwazamModel() {
        gameboard = new Gameboard();
    }

    // Getters
    public Gameboard getGameboard() {return gameboard;}
    public String getCurrentTeam(){ return currentTeam; }
    public String getOppositeTeam()
    {
        if(currentTeam == "red")
            return "blue";
        else
            return "red";
    }
    
    public int getTurns(){ return turns; }

    // Setters
    public void setCurrentTeam(String s) { currentTeam = s; }
    public void setTurns(){ turns++; }
    public void setTurns(int turns) { 
        this.turns = turns; 
    }

    // Method called when game is restarted
    // Kuan Chee Ling
    public void restartModel() {
        gameboard = new Gameboard(); // Reset the gameboard
        currentTeam = "red";         // Reset the starting team
        turns = 1;                   // Reset the turn counter
    }
}
