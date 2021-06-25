package minesweeper;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane {
	
	private boolean bomb;
	private boolean hidden;
	private boolean flagged;
	private int bombNeighbors;
	private int xpos;
	private int ypos;
	
	public Tile(int x, int y) {
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
	
	public void addImage(ImageView i) {
		this.getChildren().add(i);
	}
	
	@Override
	public int hashCode() {
		int result = (bomb ? 1 : 0);
		result = 31 * result + (hidden ? 1 : 0);
		result = 31 * result + (flagged ? 1 : 0);
		result = 31 * result + bombNeighbors;
		result = 31 * result + xpos;
		result = 31 * result + ypos;
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		Tile tile = (Tile) o;
		
		if (bomb != tile.bomb) return false;
		if (hidden != tile.hidden) return false;
		if (flagged != tile.flagged) return false;
		if (bombNeighbors != tile.bombNeighbors) return false;
		if (xpos != tile.xpos) return false;
		return ypos == tile.ypos;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	public boolean isFlagged() {
		return flagged;
	}
	
	public void setFlagged(boolean flagged) {
		this.flagged = flagged;
	}
	
	public int getBombNeighbors() {
		return bombNeighbors;
	}
	
	public void setBombNeighbors(int bombNeighbors) {
		this.bombNeighbors = bombNeighbors;
	}
	
	public int getXpos() {
		return xpos;
	}
	
	public int getYpos() {
		return ypos;
	}
	
	public boolean isBomb() {
		return bomb;
	}
	
	public void setBomb(boolean b) {
		bomb = b;
	}
}
