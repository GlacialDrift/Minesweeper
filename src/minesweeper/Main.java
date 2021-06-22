package minesweeper;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;

public class Main extends Application implements Serializable {
	
	private static int gamesPlayed;
	private static int gamesWon;
	private static float percentWon;
	
	private static Node createVSpacer() {
		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		return spacer;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
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
