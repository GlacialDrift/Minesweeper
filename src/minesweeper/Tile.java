package minesweeper;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * The tile class acts as the individual tiles and is, in reality, a stack of images on top of each other. Each tile store information:
 * whether it is hidden, flagged, and/or is a bomb, and the number of adjacent bombs (0-8 for normal tiles, -1 for a bomb tile). Each
 * tile should store x-y position so that it can be more easily searched.
 */
public class Tile extends StackPane{
	
	private final int xpos;
	private final int ypos;
	private final ArrayList<Pair<Integer, Integer>> neighbors;
	private int size;
	private boolean bomb;
	private boolean hidden;
	private boolean flagged;
	private int bombNeighbors;
	
	/**
	 * Constructor that defines the x-y position of the tile. Adds the base image/rectangle
	 * @param x x-position of the tile (index starts at 0)
	 * @param y y-position of the tile (index starts at 0)
	 */
	public Tile(int x, int y){
		size = 25;
		addRectangle("808080", "808080", "C0C0C0", 0.08);
		addRectangle("FFFFFF", "808080", "C0C0C0", 0.12);
		bomb = false;
		xpos = x;
		ypos = y;
		hidden = true;
		flagged = false;
		bombNeighbors = 0;
		neighbors = new ArrayList<>();
	}
	
	public void addRectangle(String color1, String color2, String color3, double scaling){
		StackPane backing = new StackPane();
		Polygon topTriangle = new Polygon(0, 0, 0, size, size, 0);
		topTriangle.setFill(Color.web(color1));
		Polygon bottomTriangle = new Polygon(size, 0, 0, size, size, size);
		bottomTriangle.setFill(Color.web(color2));
		double offset = scaling * size;
		double rSize = size - 2 * offset;
		Rectangle r = new Rectangle(rSize, rSize);
		r.setFill(Color.web(color3));
		r.setX(offset);
		r.setY(offset);
		backing.getChildren().addAll(topTriangle, bottomTriangle, r);
		this.getChildren().add(backing);
	}
	
	/**
	 * Add an image to the tile. Since the tile is a StackPane, should add image directly to the top??
	 * @param i image to be added
	 */
	public void addImage(Image i){
		ImageView imageView = new ImageView(i);
		imageView.setFitHeight(25);
		imageView.setFitWidth(25);
		imageView.setSmooth(true);
		this.getChildren().add(imageView);
	}
	
	/**
	 * Create an ArrayList of Tile Locations that are adjacent to this tile. Uses the Pair class to create an x-y pair for each adjacent tile position
	 * @param width  the width of the game board to check for boundaries
	 * @param height the height of the game board to check for boundaries
	 */
	public void buildNeighbors(int width, int height){
		int x, y;
		Pair<Integer, Integer> p;
		for(int i = -1; i < 2; i++) {
			x = xpos + i;
			for(int j = -1; j < 2; j++) {
				y = ypos + j;
				if(!(x < 0 || y < 0 || x >= width || y >= height || (x == xpos && y == ypos))) {
					p = new Pair<>(x, y);
					neighbors.add(p);
				}
			}
		}
		
	}
	
	/**
	 * Calculate the number of neighbors that are bombs and update the bombNeighbors field
	 * @param game provide the GamePane to reference the correct grid instance
	 */
	public void calculateNeighbors(GamePane game){
		if(isBomb()) {
			bombNeighbors = -1;
			return;
		}
		int x, y;
		Tile t;
		for(Pair<Integer, Integer> p : neighbors) {
			x = p.getKey();
			y = p.getValue();
			t = game.getNode(y, x, game.getGrid());
			if(t.isBomb()) {
				bombNeighbors++;
			}
		}
		
	}
	
	public boolean isBomb(){
		return bomb;
	}
	
	public void setBomb(boolean b){
		bomb = b;
	}
	
	/**
	 * Add a label to the tile indicating the number of adjacent bombs. Color the text according to the number of adjacent bombs
	 */
	public void addNeighborsText(){
		Label label = new Label("" + bombNeighbors);
		label.setAlignment(Pos.CENTER);
		label.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 20));
		switch(bombNeighbors) {
			case 1 -> label.setTextFill(Color.web("0100FE"));
			case 2 -> label.setTextFill(Color.web("008000"));
			case 3 -> label.setTextFill(Color.web("FE0000"));
			case 4 -> label.setTextFill(Color.web("010080"));
			case 5 -> label.setTextFill(Color.web("810102"));
			case 6 -> label.setTextFill(Color.web("008081"));
			case 7 -> label.setTextFill(Color.BLACK);
			case 8 -> label.setTextFill(Color.web("808080"));
			case 0 -> {
				label.setTextFill(Color.DARKGREEN);
				label.setText("");
			}
		}
		//label.setBackground(new Background(new BackgroundFill(null, null, null)));
		this.getChildren().add(label);
	}
	
	/**
	 * Create the artwork to act as a hidden box and add that to the tile StackPane
	 */
	/*public void addHiddenBox(){
		StackPane hidden = new StackPane();
		Polygon topTriangle = new Polygon(0, 0, 0, size, size, 0);
		topTriangle.setFill(Color.web("FFFFFF"));
		Polygon bottomTriangle = new Polygon(size, 0, 0, size, size, size);
		bottomTriangle.setFill(Color.web("808080"));
		double offset = 0.12d * size;
		double rSize = size - 2 * offset;
		Rectangle r = new Rectangle(rSize, rSize);
		r.setFill(Color.web("C0C0C0"));
		r.setX(offset);
		r.setY(offset);
		hidden.getChildren().addAll(topTriangle, bottomTriangle, r);
		this.getChildren().add(hidden);
		
	}*/
	@Override
	public int hashCode(){
		int result = xpos;
		result = 31 * result + ypos;
		return result;
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		
		Tile tile = (Tile) o;
		
		if(xpos != tile.xpos) return false;
		return ypos == tile.ypos;
	}
	
	public int getSize(){
		return size;
	}
	
	public void setSize(int size){
		this.size = size;
	}
	
	public ArrayList<Pair<Integer, Integer>> getNeighbors(){
		return neighbors;
	}
	
	public boolean isHidden(){
		return hidden;
	}
	
	public void setHidden(boolean hidden){
		this.hidden = hidden;
	}
	
	public boolean isFlagged(){
		return flagged;
	}
	
	public void setFlagged(boolean flagged){
		this.flagged = flagged;
	}
	
	public int getBombNeighbors(){
		return bombNeighbors;
	}
}
