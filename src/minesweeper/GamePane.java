package minesweeper;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GamePane extends AnchorPane {
    
    private Font digital;
    private Font numbers;
    private ImageView bomb;
    private ImageView bombRed;
    private ImageView bombWrong;
    private ImageView smile;
    private static GridPane grid;
    
    private int width;
    private int height;
    private int bombs;
    
    public GamePane(int width, int height, int bombs) {
        try {
            loadResources();
        } catch (Exception e) {
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
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                t = new Tile();
                grid.add(t, i, j);
            }
        }
        grid.setAlignment(Pos.CENTER);
        game.setCenter(grid);
        
        this.getChildren().add(game);
    }
    
    private void loadResources() throws IOException {
        InputStream FIS = new FileInputStream("Resources/Fonts/DSEG7Modern-Bold.ttf");
        digital = Font.loadFont(FIS, 24);
        FIS = new FileInputStream("Resources/Fonts/numbers.ttf");
        numbers = Font.loadFont(FIS,16);
        FIS = new FileInputStream("Resources/Images/bomb.png");
        bomb = new ImageView(new Image(FIS));
        FIS = new FileInputStream("Resources/Images/bomb-exploded.png");
        bombRed = new ImageView(new Image(FIS));
        FIS = new FileInputStream("Resources/Images/bomb-wrong.png");
        bombWrong = new ImageView(new Image(FIS));
        FIS = new FileInputStream("Resources/Images/minesweeper-smile.png");
        smile = new ImageView(new Image(FIS));
        smile.setPreserveRatio(true);
        smile.setFitHeight(30);
        smile.setSmooth(true);
        FIS.close();
    }
    
    private Node createHSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
}