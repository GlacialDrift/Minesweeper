package minesweeper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is a controller class for the start screen. It utilizes the ScreenBuilder tool for JavaFX and incorporates listeners and updaters for each of the important controls on the start screen.
 * Upon clicking the start button ("startGame" method), a new GamePane object is created, which will run the whole game, and that object is set to display on the stage
 */
public class StartScreenController implements Initializable{
	
	@FXML
	private Label gridWidth, gridHeight, numBombs;
	
	@FXML
	private Slider widthSlider, heightSlider, bombSlider;
	
	@FXML
	private RadioButton easyRB, mediumRB, hardRB, customRB;
	
	@FXML
	private Pane sliderPane;
	
	private int width;
	private int height;
	private int bombs;
	
	/**
	 * Create the game board and display the board to the screen.
	 * @param event button click event
	 */
	public void startGame(ActionEvent event){
		GamePane game = new GamePane(width, height, bombs);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(game);
		stage.setScene(scene);
		stage.show();
		
		System.out.println("game started");
	}
	
	/**
	 * Initializer method to allow for slider listeners. There may be a different way to do this, but this is what I found through quick internet searching
	 * @param url            unsure of purpose
	 * @param resourceBundle unsure of purpose
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		sliderPane.setDisable(true);
		width = (int) widthSlider.getValue();
		gridWidth.setText(Integer.toString(width));
		height = (int) heightSlider.getValue();
		gridHeight.setText(Integer.toString(height));
		bombs = (int) bombSlider.getValue();
		numBombs.setText(Integer.toString(bombs));
		
		widthSlider.valueProperty().addListener((observableValue, number, t1) -> {
			width = (int) widthSlider.getValue();
			gridWidth.setText(Integer.toString(width));
		});
		heightSlider.valueProperty().addListener((observableValue, number, t1) -> {
			height = (int) heightSlider.getValue();
			gridHeight.setText(Integer.toString(height));
		});
		bombSlider.valueProperty().addListener((observableValue, number, t1) -> {
			bombs = (int) bombSlider.getValue();
			numBombs.setText(Integer.toString(bombs));
		});
	}
	
	/**
	 * Listen for radio button selection for difficulty settings and update relevant information fields
	 * @param event selection event for any of the radio buttons
	 */
	public void setDifficulty(ActionEvent event){
		if(easyRB.isSelected()) {
			updateGameSettings(9, 9, 10);
			sliderPane.setDisable(true);
		} else if(mediumRB.isSelected()) {
			updateGameSettings(16, 16, 40);
			sliderPane.setDisable(true);
		} else if(hardRB.isSelected()) {
			updateGameSettings(30, 16, 99);
			sliderPane.setDisable(true);
		} else if(customRB.isSelected()) {
			sliderPane.setDisable(false);
			width = (int) widthSlider.getValue();
			height = (int) heightSlider.getValue();
			bombs = (int) bombSlider.getValue();
			updateGameSettings(width, height, bombs);
		}
	}
	
	/**
	 * Update the labels that show the relevant parameters
	 * @param width  game board width (# of tiles)
	 * @param height game board height (# of tiles)
	 * @param bombs  number of bombs to be included in the game
	 */
	private void updateGameSettings(int width, int height, int bombs){
		gridWidth.setText(Integer.toString(width));
		gridHeight.setText(Integer.toString(height));
		numBombs.setText(Integer.toString(bombs));
		heightSlider.setValue(height);
		widthSlider.setValue(width);
		bombSlider.setValue(bombs);
	}
}
