package com.kil;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import lombok.extern.java.Log;

public class FXMLController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem itemNew;

    @FXML
    private MenuItem itemOpen;

    @FXML
    private MenuItem itemSave;

    @FXML
    private MenuItem itemSaveAs;

    @FXML
    private MenuItem itemExit;

    @FXML
    private MenuItem itemCopy;

    @FXML
    private MenuItem itemPast;

    @FXML
    private MenuItem itemDelete;

    @FXML
    private MenuItem itemReference;

    @FXML
    private MenuItem itemAbout;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonEdit;

    @FXML
    private Label labelTest;

    @FXML
    private Button sizeMinusButton;

    @FXML
    private Slider sizeSlider;

    @FXML
    private Button sizePlusButton;


    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label labelSize;

    @FXML
    private Pane holst;

    @FXML
    private ComboBox<String> selectCableBox;

    @FXML
    private TextArea outPutText;

    @FXML
    void initialize() {

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                showInfo();
            }
        };
        timer.start();

        sizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                labelSize.setText("Size: " + String.format("%.0f", new_val) + "%");
            }
        });

        fillLists();
        loadTestBD();
        draw();

    }

    private void loadTestBD(){
        Logic.nodeList.add(new CityNode("Novosibirsk", Logic.startOffset, 20, 50, 100));
        Logic.nodeList.add(new CityNode("Moscow", 100 + Logic.startOffset, 20, 50, 100));
        Logic.nodeList.add(new CityNode("Kiev", 200 + Logic.startOffset, 20, 50, 100));
    }

    private void draw(){
        holst.getChildren().clear();
        for (CityNode node : Logic.nodeList) {
            MyPoint point = new MyPoint(node);
            holst.getChildren().add(point);
        }
    }



    //fill lists and setup listener for them
    private void fillLists() {
        //for cables
        ObservableList<String> listC = FXCollections.observableArrayList("КГ-50", "КГ-70", "КГ-100");
        selectCableBox.setItems(listC);
    }

    private void showInfo(){
        if(Logic.currentItem == null){
            if(Logic.drawedInfoObjects != null){
                for (Object obj : Logic.drawedInfoObjects) {
                    holst.getChildren().remove(obj);
                    Logic.drawedInfoObjects = new ArrayList<>();
                }
            }
            return;
        }

        List<String> list = new ArrayList();

        if(Logic.currentItem instanceof CityNode){
            CityNode node = (CityNode) Logic.currentItem; 
            list = node.getInfo();

            double maxLabelWidthChar = 0;
            List<Label> labelList = new ArrayList<>();
            for (String str : list) {
                Label label = new Label(str);
                label.setTranslateY(node.getPoint().getY() + (15 * list.indexOf(str)) + Logic.infoPaneOffset);
                labelList.add(label);
                if(str.length() * 7 > maxLabelWidthChar)
                    maxLabelWidthChar = str.length() * 7;
            }

            Polygon polygon = new Polygon();
            polygon.getPoints().addAll(
                    node.getPoint().getX() - maxLabelWidthChar/2, node.getPoint().getY() + Logic.infoPaneOffset,
                    node.getPoint().getX() + maxLabelWidthChar/2, node.getPoint().getY() + Logic.infoPaneOffset,
                    node.getPoint().getX() + maxLabelWidthChar/2, node.getPoint().getY() + list.size() * 15 + Logic.infoPaneOffset,
                    node.getPoint().getX() - maxLabelWidthChar/2, node.getPoint().getY() + list.size() * 15 + Logic.infoPaneOffset);
            polygon.setFill(Color.WHITE);
            polygon.setStroke(Color.BLACK);
            holst.getChildren().add(polygon);
            Logic.drawedInfoObjects.add(polygon);

            for (Label label : labelList) {
                label.setTranslateX(node.getPoint().getX() - maxLabelWidthChar/2 + 2);
                holst.getChildren().add(label);
                Logic.drawedInfoObjects.add(label);
            }
        }
        //else
        //fillProperties(((Branch) Logic.currentItem).cityNode.getInfo());
    }
}

