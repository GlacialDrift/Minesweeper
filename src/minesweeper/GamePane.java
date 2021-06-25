package minesweeper;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The GamePane class acts as the parent of everything on the game board. Therefore, extending the AnchorPane class will allow for everything to be added to this class for control
 * Creation of a new GamePane object should initialize a new board, which contains a grid of tiles. The tiles are each essentially a StackPane for stacking images and text labels.
 * Each tile will be given an eventFilter to listen for mouse clicks. If it is clicked, it performs various actions based on what type of click, whether its a bomb, if it hidden
 * or shown, etc.
 */
public class GamePane extends AnchorPane{
	
	private static GridPane grid;
	private Font digital;
	private Font numbers;
	private Image bomb;
	private ImageView bombRed;
	private ImageView bombWrong;
	private ImageView smile;
	private boolean initialized;
	
	private int width;
	private int height;
	private int bombs;
	
	/**
	 * Anonymous EventHandler class that calls other methods on a tile mouse click. It passes in the source node of the event to modify the correct tile on the board
	 */
	EventHandler<MouseEvent> click = e -> {
		if(!initialized) {
			if(e.getButton() == MouseButton.PRIMARY) {
				buildBoard(e.getSource());
				leftClick(e.getSource());
			}
		} else {
			if(e.getButton() == MouseButton.PRIMARY) {
				leftClick(e.getSource());
			} else if(e.getButton() == MouseButton.SECONDARY) {
				rightClick(e.getSource());
			}
		}
	};
	
	/**
	 * Anonymous EventHandler that creates a new game, and sets it to the main scene of the stage. Perform these actions such that no new scenes or stages are created;
	 */
	EventHandler<ActionEvent> newGame = e -> {
		
		GamePane game = new GamePane(width, height, bombs);
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		Scene scene = ((Node) e.getSource()).getScene();
		scene.setRoot(game);
		stage.setScene(scene);
		stage.show();
	};
	
	/**
	 * Constructor for the game board. It creates a timer, bomb counter, face button (to start a new game), and the board itself. The board is defined by the tile width, the tile height
	 * and the number of bombs that are on the board
	 * @param width  the width, in tiles, of the game board
	 * @param height the height, in tiles, of the game board
	 * @param bombs  the number of hidden bombs to be included on the game board
	 */
	public GamePane(int width, int height, int bombs){
		
		initialized = false;
		try {
			loadResources();
		} catch(Exception e) {
			e.printStackTrace();
		}
		this.width = width;
		this.height = height;
		this.bombs = bombs;
		grid = new GridPane();
		
		BorderPane game = new BorderPane();
		game.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
		Label bombCount = new Label(Integer.toString(bombs));
		Label timer = new Label("000");
		Button face = new Button();
		face.setGraphic(smile);
		face.setOnAction(newGame);
		
		Background boxes = new Background(new BackgroundFill(Color.rgb(25, 25, 25), null, null));
		timer.setFont(digital);
		timer.setTextFill(Color.RED);
		timer.setBackground(boxes);
		timer.setBorder(new Border(new BorderStroke(Color.BLACK, null, null, null)));
		bombCount.setFont(digital);
		bombCount.setTextFill(Color.RED);
		bombCount.setBackground(boxes);
		bombCount.setBorder(new Border(new BorderStroke(Color.BLACK, null, null, null)));
		HBox titleBar = new HBox(bombCount, createHSpacer(), face, createHSpacer(), timer);
		titleBar.setAlignment(Pos.CENTER);
		game.setTop(titleBar);
		
		Tile t;
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				t = new Tile(i, j);
				t.addEventFilter(MouseEvent.MOUSE_CLICKED, click);
				t.addHiddenBox();
				grid.add(t, i, j);
			}
		}
		grid.setAlignment(Pos.CENTER);
		game.setCenter(grid);
		
		this.getChildren().add(game);
	}
	
	/**
	 * Load images and fonts to the object for future use during click events, etc.
	 * @throws IOException Exception thrown in event of an issue during file loading
	 */
	private void loadResources() throws IOException{
		InputStream FIS = new FileInputStream("Resources/Fonts/DSEG7Modern-Bold.ttf");
		digital = Font.loadFont(FIS, 24);
		FIS = new FileInputStream("Resources/Images/bomb.png");
		bomb = new Image(FIS);
		FIS = new FileInputStream("Resources/Images/bomb-exploded.png");
		bombRed = new ImageView(new Image(FIS));
		bombRed.setFitHeight(25);
		bombRed.setFitWidth(25);
		bombRed.setSmooth(true);
		FIS = new FileInputStream("Resources/Images/bomb-wrong.png");
		bombWrong = new ImageView(new Image(FIS));
		bombWrong.setFitHeight(25);
		bombWrong.setFitWidth(25);
		bombWrong.setSmooth(true);
		FIS = new FileInputStream("Resources/Images/minesweeper-smile.png");
		smile = new ImageView(new Image(FIS));
		smile.setPreserveRatio(true);
		smile.setFitHeight(30);
		smile.setSmooth(true);
		FIS.close();
	}
	
	/**
	 * Creates a "spacer" node that fills space within an HBox and a VBox
	 * @return Spacer object for HBox and VBox
	 */
	private Node createHSpacer(){
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		VBox.setVgrow(spacer, Priority.ALWAYS);
		return spacer;
	}
	
	/**
	 * Function that builds the game board, i.e. it fills the board with the correct number of bombs in a random location. It should also add the bomb image to each bomb=true tile so that when
	 * rectangles or other "hiding" objects are removed on clicking, it shows an actual bomb. Also useful for testing that the board is built correctly. After adding bombs, calculate adjacent
	 * bomb counts for each tile and display those values. This is where showing bomb images can be useful in debugging. Boards in minecraft are only built after the first click. This allows
	 * us to prevent the first clicked tile from being a bomb. Therefore, feed this source tile into the method and ensure the source tile is not set to a bomb.
	 * @param source the source node that cannot be a bomb
	 */
	private void buildBoard(Object source){
		initialized = true;
		Tile t = (Tile) source;
		
		int row = ThreadLocalRandom.current().nextInt(height);
		int col = ThreadLocalRandom.current().nextInt(width);
		Tile selected;
		
		int count = 0;
		int limit = 0;
		
		while (count < bombs && limit < 1000) {
			selected = getNode(row, col, grid);
			if(selected.equals(t) || selected.isBomb()) {
				row = ThreadLocalRandom.current().nextInt(height);
				col = ThreadLocalRandom.current().nextInt(width);
			} else {
				count++;
				selected.setBomb(true);
				selected.addImage(bomb);
			}
			limit++;
		}
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				selected = getNode(j, i, grid);
				selected.buildNeighbors(width, height);
				selected.calculateNeighbors(this);
				selected.addNeighborsText();
				selected.getChildren().get(1).toFront();
			}
		}
	}
	
	/**
	 * Fetch and return the Tile at row-r and col-c in the grid
	 * @param row  row to retrieve
	 * @param col  column to retrieve
	 * @param grid the grid to search
	 * @return the Tile at that position in that grid
	 */
	public Tile getNode(int row, int col, GridPane grid){
		ObservableList<Node> children = grid.getChildren();
		Tile result = null;
		for(Node n : children) {
			if(GridPane.getRowIndex(n) == row && GridPane.getColumnIndex(n) == col) {
				result = (Tile) n;
				break;
			}
		}
		return result;
	}
	
	// TODO fix showing adjacent tiles when the currently clicked tile has no bombs
	
	/**
	 * Action to perform on a left-click. This will break out into ~3 methods:
	 * 1. the tile clicked is hidden and not a bomb -> show
	 * 2. the tile clicked is hidden and is a bomb -> game over
	 * 3. the tile clicked is not hidden -> if # of flags is correct, show all adjacent tiles
	 * @param source the source tile that is being clicked.
	 */
	private void leftClick(Object source){
		
		Tile t = (Tile) source;
		//show the current tile
		StackPane box = (StackPane) t.getChildren().get(t.getChildren().size() - 1);
		t.getChildren().remove(box);
		
		//show adjacent neighbors if this tile = 0
		if(t.getBombNeighbors() == 0) {
			Tile temp;
			ArrayList<Pair<Integer, Integer>> n = t.getNeighbors();
			for(Pair<Integer, Integer> pair : n) {
				temp = getNode(pair.getKey(), pair.getValue(), grid);
				if(temp.getBombNeighbors() == 0 && temp.isHidden()) {
					leftClick(temp);
				}
			}
		}
		
		System.out.println("Left clicked at " + t.getXpos() + ", " + t.getYpos() + " bomb=" + t.isBomb());
	}
	
	/**
	 * Action to perform on a right click:
	 * if hidden:
	 * toggle the flag on that tile
	 * @param source the source tile that is being clicked
	 */
	private void rightClick(Object source){
		if(source instanceof Tile t) {
		
		}
	}
	
	public GridPane getGrid(){
		return grid;
	}
	
	public void setGrid(GridPane grid){
		GamePane.grid = grid;
	}
}