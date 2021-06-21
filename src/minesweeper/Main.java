package minesweeper;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class Main extends Application implements Serializable {
    
    private static Font digital;
    private static Font numbers;
    private static ImageView bomb;
    private static ImageView bombRed;
    private static ImageView bombWrong;
    private static ImageView smile;
    private static GridPane grid;
    private static int gamesPlayed;
    private static int gamesWon;
    private static float percentWon;
    
    private static Pane sliderPane;
    private static Label widthLabel;
    private static Label gridWidth;
    private static Label heightLabel;
    private static Label gridHeight;
    private static Label bombCount;
    private static Label numBombs;
    private static Slider widthSlider;
    private static Slider heightSlider;
    private static Slider bombSlider;
    
    private static Button startGame;
    
    private int width;
    private int height;
    private int bombs;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public static Parent buildGameScene(int width, int height, int bombs) {
        
        BorderPane gameRoot = new BorderPane();
        gameRoot.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
        Label bombCount = new Label(Integer.toString(bombs));
        Label timer = new Label("000");
        Button newGame = new Button();
        newGame.setGraphic(smile);
        
        EventHandler<ActionEvent> smilePress = actionEvent -> boardSetup();
        newGame.setOnAction(smilePress);
        
        Background blackBackground = new Background(new BackgroundFill(Color.BLACK, null, null));
        timer.setFont(digital);
        timer.setTextFill(Color.RED);
        timer.setBackground(blackBackground);
        bombCount.setFont(digital);
        bombCount.setTextFill(Color.RED);
        bombCount.setBackground(blackBackground);
        
        HBox titleGroup = new HBox(bombCount, createHSpacer(), newGame, createHSpacer(), timer);
        titleGroup.setBackground(new Background(new BackgroundFill(Color.rgb(200, 200, 200), null, null)));
        titleGroup.setAlignment(Pos.TOP_CENTER);
        titleGroup.setMinWidth(width * 25);
        gameRoot.setTop(titleGroup);
        
        Rectangle r;
        grid = new GridPane();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                r = new Rectangle(25, 25, Color.web("C0C0C0"));
                r.setStroke(Color.web("797979"));
                grid.add(new StackPane(r), i, j);
            }
        }
        grid.setAlignment(Pos.CENTER);
        gameRoot.setCenter(grid);
        return gameRoot;
    }
    
    private static Node createHSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
    
    private static void boardSetup() {
    
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        loadResources();
        
        final Parent[] gameRoot = new Parent[1];
        Parent titleRoot = buildTitleScene();
        Scene startup = new Scene(titleRoot);
        
        stage.setScene(startup);
        stage.setTitle("Minesweeper");
        stage.getIcons().add(bomb.getImage());
        stage.show();
        
        Scene game = new Scene(gameRoot[0]);
        
        startGame.setOnAction(e -> stage.setScene(game));
        
        stage.setScene(game);
        
        /*Parent startRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("OpeningScreen.fxml")));
        
        Scene startScene = new Scene(startRoot);
        
        stage.setScene(startScene);
        */
    }
    
    public static Parent buildTitleScene() {
        AnchorPane root = new AnchorPane();
        BorderPane border = new BorderPane();
        Label title = new Label("Minesweeper");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        title.setPrefWidth(600);
        title.setAlignment(Pos.CENTER);
        
        startGame = new Button("Start Game");
        startGame.setAlignment(Pos.CENTER);
        startGame.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        startGame.setTranslateX(225);
        
        VBox gameSetup = new VBox();
        gameSetup.setPrefSize(300, 350);
        Label gameSettings = new Label("Game Settings");
        gameSettings.setFont(Font.font("Verdana", 20));
        gameSetup.getChildren().add(gameSettings);
        gameSettings.setTranslateX(50);
        
        ToggleGroup difficulty = new ToggleGroup();
        RadioButton easyRB = new RadioButton("Beginner");
        RadioButton mediumRB = new RadioButton("Intermediate");
        RadioButton hardRB = new RadioButton("Expert");
        RadioButton customRB = new RadioButton("Custom");
        easyRB.setToggleGroup(difficulty);
        easyRB.setSelected(true);
        mediumRB.setToggleGroup(difficulty);
        hardRB.setToggleGroup(difficulty);
        customRB.setToggleGroup(difficulty);
        easyRB.setTranslateX(75);
        mediumRB.setTranslateX(75);
        hardRB.setTranslateX(75);
        customRB.setTranslateX(75);
        gameSetup.getChildren().addAll(createVSpacer(), easyRB, mediumRB, hardRB, customRB, createVSpacer());
        
        easyRB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                updateGameSettings(9, 9, 10);
                sliderPane.setDisable(true);
            }
        });
        
        mediumRB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                updateGameSettings(16, 16, 40);
                sliderPane.setDisable(true);
            }
        });
        
        hardRB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                updateGameSettings(30, 16, 99);
                sliderPane.setDisable(true);
            }
        });
        
        customRB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                sliderPane.setDisable(false);
                width = (int) widthSlider.getValue();
                height = (int) heightSlider.getValue();
                bombs = (int) bombSlider.getValue();
                updateGameSettings(width, height, bombs);
            }
        });
        
        sliderPane = new Pane();
        widthLabel = new Label("Width");
        gridWidth = new Label("9");
        heightLabel = new Label("Height");
        gridHeight = new Label("9");
        bombCount = new Label("Bomb Count");
        numBombs = new Label("10");
        widthSlider = new Slider(0, 50, 9);
        heightSlider = new Slider(0, 50, 9);
        bombSlider = new Slider(0, 200, 10);
        widthSlider.setMinorTickCount(5);
        widthSlider.setShowTickLabels(true);
        widthSlider.setShowTickMarks(true);
        widthSlider.setMajorTickUnit(10);
        heightSlider.setMinorTickCount(5);
        heightSlider.setShowTickLabels(true);
        heightSlider.setShowTickMarks(true);
        heightSlider.setMajorTickUnit(10);
        bombSlider.setMinorTickCount(5);
        bombSlider.setShowTickLabels(true);
        bombSlider.setShowTickMarks(true);
        bombSlider.setMajorTickUnit(50);
        
        widthLabel.setLayoutX(75);
        widthLabel.setLayoutY(20);
        gridWidth.setLayoutX(150);
        gridWidth.setLayoutY(20);
        widthSlider.setLayoutX(50);
        widthSlider.setLayoutY(40);
        
        heightLabel.setLayoutX(75);
        heightLabel.setLayoutY(80);
        gridHeight.setLayoutX(150);
        gridHeight.setLayoutY(80);
        heightSlider.setLayoutX(50);
        heightSlider.setLayoutY(100);
        
        bombCount.setLayoutX(75);
        bombCount.setLayoutY(140);
        numBombs.setLayoutX(150);
        numBombs.setLayoutY(140);
        bombSlider.setLayoutX(50);
        bombSlider.setLayoutY(160);
        
        widthSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                width = (int) widthSlider.getValue();
                gridWidth.setText(Integer.toString(width));
            }
        });
        heightSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                height = (int) heightSlider.getValue();
                gridHeight.setText(Integer.toString(height));
            }
        });
        bombSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                bombs = (int) bombSlider.getValue();
                numBombs.setText(Integer.toString(bombs));
            }
        });
        
        sliderPane.getChildren().addAll(widthLabel, gridWidth, widthSlider, heightLabel, gridHeight, heightSlider, bombCount, numBombs
                , bombSlider);
        sliderPane.setDisable(true);
        gameSetup.getChildren().addAll(sliderPane, createVSpacer());
        
        VBox gameStats = new VBox();
        Label stats = new Label("Game Stats");
        stats.setTranslateX(50);
        stats.setFont(Font.font("Verdana", 20));
        GridPane games = new GridPane();
        Label gP = new Label("Games Played");
        Label gamecount = new Label("");
        Label gW = new Label("Games Won");
        Label wincount = new Label("");
        Label pW = new Label("Percent Won");
        Label percentcount = new Label("");
        games.add(gP, 0, 0);
        games.add(gamecount, 1, 0);
        games.add(gW, 0, 1);
        games.add(wincount, 1, 1);
        games.add(pW, 0, 2);
        games.add(percentcount, 1, 2);
        
        GridPane highScores = new GridPane();
        Label topScore = new Label("First Place");
        Label score1 = new Label("");
        Label secondScore = new Label("Second Place");
        Label score2 = new Label("");
        Label thirdScore = new Label("Third Place");
        Label score3 = new Label("");
        highScores.add(topScore, 0, 0);
        highScores.add(score1, 1, 0);
        highScores.add(secondScore, 0, 1);
        highScores.add(score2, 1, 1);
        highScores.add(thirdScore, 0, 2);
        highScores.add(score3, 1, 2);
        games.setTranslateX(50);
        highScores.setTranslateX(50);
        
        gameStats.getChildren().addAll(stats, games, highScores);
        gameStats.setPrefSize(300, 200);
        gameStats.setSpacing(25);
        
        border.setTop(title);
        border.setLeft(gameSetup);
        border.setBottom(startGame);
        border.setRight(gameStats);
        root.getChildren().add(border);
        return root;
    }
    
    private static void updateGameSettings(int width, int height, int bombs) {
        gridWidth.setText(Integer.toString(width));
        gridHeight.setText(Integer.toString(height));
        numBombs.setText(Integer.toString(bombs));
        heightSlider.setValue(height);
        widthSlider.setValue(width);
        bombSlider.setValue(bombs);
    }
    
    private static Node createVSpacer() {
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
    
    public void loadResources() throws IOException {
        InputStream FIS = new FileInputStream("Resources/Fonts/DSEG7Modern-Bold.ttf");
        digital = Font.loadFont(FIS, 24);
        FIS = new FileInputStream("Resources/Fonts/numbers.ttf");
        numbers = Font.loadFont(FIS, 12);
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
}
