package minesweeper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Objects;

/**
 * Main class for entering the minesweeper game. This class should load in old saved stats data. On launch, the game loads into a pre-made scene defined in the StartScreen.fxml file. This is a file
 * used to set up a new game and to display the total game stats. Upon setting selection, the start button will generate a new grid for a game and display that on the screen.
 */
public class Main extends Application implements Serializable{
	
	private static int gamesPlayed;
	private static int gamesWon;
	private static float percentWon;
	
	public static void main(String[] args){
		launch(args);
	}
	
	/**
	 * Load the starting screen and window. This should include loading stats from a saved file
	 * @param stage stage passed into the start method by JavaFX
	 * @throws Exception Throw an exception in the event that the game icon image cannot be loaded
	 */
	@Override
	public void start(Stage stage) throws Exception{
		
		/* TODO
		    implement stat saving and loading for display on the start screen
		    Implement game-save in MenuBar
		    implement updates for total games played, number of games won, high scores, etc.
		*/
		
		Parent startRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("StartScreen.fxml")));
		Scene startScene = new Scene(startRoot);
		
		stage.setScene(startScene);
		stage.setTitle("Minesweeper");
		InputStream FIS = new FileInputStream("Resources/Images/bomb.png");
		ImageView bomb = new ImageView(new Image(FIS));
		FIS.close();
		stage.getIcons().add(bomb.getImage());
		stage.show();
	}
}
