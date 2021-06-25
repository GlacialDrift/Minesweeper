package minesweeper;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ThreadLocalRandom;

public class GamePane extends AnchorPane{
	
	private static GridPane grid;
	private Font digital;
	private Font numbers;
	private ImageView bomb;
	private ImageView bombRed;
	private ImageView bombWrong;
	private ImageView smile;
	private boolean initialized;
	
	private int width;
	private int height;
	private int bombs;
	
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
		System.out.println(bombs);
		grid = new GridPane();
		
		BorderPane game = new BorderPane();
		game.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
		Label bombCount = new Label(Integer.toString(bombs));
		Label timer = new Label("000");
		Button face = new Button();
		face.setGraphic(smile);
		
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
		
		Tile t;
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				t = new Tile(i, j);
				t.addEventFilter(MouseEvent.MOUSE_CLICKED, click);
				grid.add(t, i, j);
			}
		}
		grid.setAlignment(Pos.CENTER);
		game.setCenter(grid);
		
		this.getChildren().add(game);
	}
	
	// TODO fix the way the gridpane displays bombs once they are built
	// TODO generate list of neighbors for each tile??
	// TODO calculate number of neighbors that are bombs to set value
	// TODO add label field of this value to the Tile
	
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
			if((selected.getXpos() == t.getXpos() && selected.getYpos() == t.getYpos()) || selected.isBomb()) {
				row = ThreadLocalRandom.current().nextInt(height);
				col = ThreadLocalRandom.current().nextInt(width);
			} else {
				count++;
				grid.getChildren().remove(selected);
				selected.setBomb(true);
				grid.add(selected, col, row);
			}
			limit++;
			for(int i = 0; i < width * height; i++) {
				Tile tempo = getNode(i % height, i / width, grid);
				
			}
		}
		
		/*int count = 0;
		int row;
		int col;
		Tile temp;
		while (count < bombs) {
			row = ThreadLocalRandom.current().nextInt(height);
			col = ThreadLocalRandom.current().nextInt(width);
			temp = getNode(row, col, grid);
			if (!temp.isBomb() && !temp.equals(t)) {
				count++;
				grid.getChildren().remove(temp);
				temp.getChildren().add(bomb);
				grid.add(temp, col, row);
			}
		}*/
		System.out.println("Board Built");
	}
	
	private Tile getNode(int row, int col, GridPane grid){
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
	
	private void leftClick(Object source){
		if(source instanceof Tile) {
			Tile t = (Tile) source;
			System.out.println("Clicked at " + t.getXpos() + ", " + t.getYpos() + ": " + t.isBomb());
		}
	}
	
	private void rightClick(Object source){
		if(source instanceof Tile) {
			Tile t = (Tile) source;
			System.out.println("Right clicked at " + t.getXpos() + ", " + t.getYpos());
		}
	}
	
	private void loadResources() throws IOException{
		InputStream FIS = new FileInputStream("Resources/Fonts/DSEG7Modern-Bold.ttf");
		digital = Font.loadFont(FIS, 24);
		FIS = new FileInputStream("Resources/Fonts/numbers.ttf");
		numbers = Font.loadFont(FIS, 16);
		FIS = new FileInputStream("Resources/Images/bomb.png");
		bomb = new ImageView(new Image(FIS));
		bomb.setFitHeight(25);
		bomb.setFitWidth(25);
		bomb.setSmooth(true);
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
	
	private Node createHSpacer(){
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		return spacer;
	}
}