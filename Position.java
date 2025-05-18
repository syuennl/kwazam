// Class to store positions (row, col) as an object
// Ngan Li Syuen, Yap Sze Thin, Kuan Chee Ling
public class Position {
    private int row;
    private int col;

    // Constructor
    public Position (int row, int col) {
        this.row = row;
        this.col = col;
    }

    // Getters
    public int getRow(){return row;}
    public int getCol(){return col;}

    // Setters
    public void setRow(int row){ this.row = row;}
    public void setCol(int col){ this.col = col;}

    // Method that make sure possible position is not out of bound 
    public boolean isInBound() { // 8 rows and 5 columns
        return (row >= 0 && row <= 7 && col >= 0 && col <= 4)? true : false;
    }
}
