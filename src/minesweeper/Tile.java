package minesweeper;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane {
    
    public Tile() {
        double size = 25;
        Rectangle r = new Rectangle(size, size);
        r.setFill(Color.web("C0C0C0"));
        r.setStroke(Color.web("797979"));
        this.getChildren().add(r);
    }
    
    
}
