package minesweeper;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TitlePage implements Initializable {
    
    private Stage stage;
    private Parent root;
    private Scene scene;
    
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
    private long startTime;
    
    public void startGame(ActionEvent event) {
        root = Main.buildGameScene(width, height, bombs);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        startTime = System.currentTimeMillis();
        stage.show();
        
        
        System.out.println("game started");
    }
    
    public void setDifficulty(ActionEvent event) {
        if (easyRB.isSelected()) {
            updateGameSettings(9, 9, 10);
            sliderPane.setDisable(true);
        } else if (mediumRB.isSelected()) {
            updateGameSettings(16, 16, 40);
            sliderPane.setDisable(true);
        } else if (hardRB.isSelected()) {
            updateGameSettings(30, 16, 99);
            sliderPane.setDisable(true);
        } else if (customRB.isSelected()) {
            sliderPane.setDisable(false);
            width = (int) widthSlider.getValue();
            height = (int) heightSlider.getValue();
            bombs = (int) bombSlider.getValue();
            updateGameSettings(width, height, bombs);
        }
    }
    
    private void updateGameSettings(int width, int height, int bombs) {
        gridWidth.setText(Integer.toString(width));
        gridHeight.setText(Integer.toString(height));
        numBombs.setText(Integer.toString(bombs));
        heightSlider.setValue(height);
        widthSlider.setValue(width);
        bombSlider.setValue(bombs);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sliderPane.setDisable(true);
        width = (int) widthSlider.getValue();
        gridWidth.setText(Integer.toString(width));
        height = (int) heightSlider.getValue();
        gridHeight.setText(Integer.toString(height));
        bombs = (int) bombSlider.getValue();
        numBombs.setText(Integer.toString(bombs));
        
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
    }
}
