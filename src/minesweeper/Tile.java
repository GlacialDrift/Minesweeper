package minesweeper;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The tile class acts as the individual tiles and is, in reality, a stack of images on top of each other. Each tile store information:
 * whether it is hidden, flagged, and/or is a bomb, and the number of adjacent bombs (0-8 for normal tiles, -1 for a bomb tile). Each
 * tile should store x-y position so that it can be more easily searched.
 */
public class Tile extends StackPane{
	
	private boolean bomb;
	private boolean hidden;
	private boolean flagged;
	private int bombNeighbors;
	private final int xpos;
	private final int ypos;
	
	/**
	 * Constructor that defines the x-y position of the tile. Adds the base image/rectangle
	 * @param x x-position of the tile (index starts at 0)
	 * @param y y-position of the tile (index starts at 0)
	 */
	public Tile(int x, int y){
		double size = 25;
		Rectangle r = new Rectangle(size, size);
		r.setFill(Color.web("C0C0C0"));
		r.setStroke(Color.web("797979"));
		this.getChildren().add(r);
		bomb = false;
		xpos = x;
		ypos = y;
		hidden = true;
		flagged = false;
		bombNeighbors = 0;
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
	
	public void setBombNeighbors(int bombNeighbors){
		this.bombNeighbors = bombNeighbors;
	}
	
	public int getXpos(){
		return xpos;
	}
	
	public int getYpos(){
		return ypos;
	}
	
	public boolean isBomb(){
		return bomb;
	}
	
	public void setBomb(boolean b){
		bomb = b;
	}
}
