// Starting File
// Yap Sze Thin
public class KwazamChessProgram {
    // Main function
    public static void main(String[] args) {
        // View
        KwazamView view = new KwazamView();
        view.setVisible(true);

        //Model
        KwazamModel model = new KwazamModel();

        // Controller
        KwazamController controller = new KwazamController(view,model);
    }
}
