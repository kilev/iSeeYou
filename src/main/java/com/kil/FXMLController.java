package com.kil;

import java.io.File;
import java.io.IOException;
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
import javafx.geometry.Point2D;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FXMLController {

    @FXML
    private MenuItem itemOpen;

    @FXML
    private MenuItem itemPDF;

    @FXML
    private CheckMenuItem autoReSize;

    @FXML
    private MenuItem itemReference;

    @FXML
    private MenuItem itemAbout;

    @FXML
    private Button buttonRefresh;

    @FXML
    private Button buttonEdit;

    @FXML
    private Label labelTest;

    @FXML
    private Label labelSize;

    @FXML
    private Button sizeMinusButton;

    @FXML
    private Slider sizeSlider;

    @FXML
    private Button sizePlusButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pane holst;

    @FXML
    void initialize() {

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                showInfo();
            }
        };
        timer.start();

        //size slider
        sizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                labelSize.setText(String.format("%.0f", new_val));
                Logic.sizeCoef = Double.parseDouble(labelSize.getText()) / 100;
                for (CityNode node : Logic.nodeList)
                    Logic.computeFinPoint();
                draw();
            }
        });

        //open file
        itemOpen.setOnAction(actionEvent -> {
            FileChooser fc = new FileChooser();
            File selectedFile = fc.showOpenDialog(null);

            if (selectedFile != null) {
                try {
                    ReadController readController = new ReadController(selectedFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    throw new IOException();
                } catch (IOException e) {
                    System.out.println("не указан файл!");
                }
            }
            Logic.computeFinPoint();
            draw();
        });

        buttonRefresh.setOnAction(actionEvent -> {
            draw();
        });

        //export in PDF
        itemPDF.setOnAction(actionEvent -> {
            PrinterJob job = PrinterJob.createPrinterJob();
            if(job != null){
                job.showPrintDialog(holst.getScene().getWindow()); // Window must be your main Stage
                job.printPage(holst);
                job.endJob();
            }
        });

        //switch autoResize
        autoReSize.setOnAction(actionEvent -> {
            Logic.enableAutoReSize = autoReSize.isSelected();
            Logic.computeFinPoint();
            draw();
        });

        //loadTestBD();
        draw();

    }

    private void loadTestBD() {
        Logic.nodeList.add(new CityNode("Novosibirsk", 0, 50, 50, 100, 10, 10));
        Logic.nodeList.add(new CityNode("Moscow", 100 , 20, 50, 100, 10, 10));
        Logic.nodeList.add(new CityNode("Kiev", 200 , 20, 50, 100, 1, 10));
        Logic.computeFinPoint();
    }

    private void draw() {
        holst.getChildren().clear();
        for (CityNode node : Logic.nodeList) {
            MyPoint point = new MyPoint(node);
            point.getTransforms().addAll(Logic.scale);
            holst.getChildren().add(point);
        }
        for (Branch branch : Logic.branchList) {
            Line line = new Line();
            line.setStartX(Logic.nodeList.get(branch.getNodes()[0]).getPoint().getX() + Logic.startOffset);
            line.setStartY(Logic.nodeList.get(branch.getNodes()[0]).getPoint().getY() + Logic.startOffset);
            line.setEndX(Logic.nodeList.get(branch.getNodes()[1]).getPoint().getX() + Logic.startOffset);
            line.setEndY(Logic.nodeList.get(branch.getNodes()[1]).getPoint().getY() + Logic.startOffset);
            line.getTransforms().add(Logic.scale);
            holst.getChildren().add(line);
        }
    }


    private void showInfo() {
        if (Logic.currentItem == null) {
            if (Logic.drawedInfoObjects != null) {
                for (Object obj : Logic.drawedInfoObjects) {
                    holst.getChildren().remove(obj);
                    Logic.drawedInfoObjects = new ArrayList<>();
                }
            }
            return;
        }

        List<String> list = new ArrayList();

        if (Logic.currentItem instanceof CityNode) {
            CityNode node = (CityNode) Logic.currentItem;
            list = node.getInfo();

            double maxLabelWidthChar = 0;
            List<Label> labelList = new ArrayList<>();
            for (String str : list) {
                Label label = new Label(str);
                label.setTranslateY(node.getFinPoint().getY() + (15 * list.indexOf(str)) + Logic.infoPaneOffset);
                labelList.add(label);
                if (str.length() * 7 > maxLabelWidthChar)
                    maxLabelWidthChar = str.length() * 7;
            }

            Polygon polygon = new Polygon();
            polygon.getPoints().addAll(
                    node.getFinPoint().getX() - maxLabelWidthChar / 2, node.getFinPoint().getY() + Logic.infoPaneOffset,
                    node.getFinPoint().getX() + maxLabelWidthChar / 2, node.getFinPoint().getY() + Logic.infoPaneOffset,
                    node.getFinPoint().getX() + maxLabelWidthChar / 2, node.getFinPoint().getY() + list.size() * 15 + Logic.infoPaneOffset,
                    node.getFinPoint().getX() - maxLabelWidthChar / 2, node.getFinPoint().getY() + list.size() * 15 + Logic.infoPaneOffset);
            polygon.setFill(Color.WHITE);
            polygon.setStroke(Color.BLACK);
            polygon.getTransforms().add(Logic.scale);
            holst.getChildren().add(polygon);
            Logic.drawedInfoObjects.add(polygon);

            //name
            Label labelName = new Label(node.getCityName());
            labelName.setTranslateY(node.getFinPoint().getY() - Logic.infoPaneOffset - 15);
            labelName.setTranslateX(node.getFinPoint().getX() - node.getCityName().length() * 3);
            labelName.getTransforms().add(Logic.scale);
            holst.getChildren().add(labelName);
            Logic.drawedInfoObjects.add(labelName);
            //info
            for (Label label : labelList) {
                label.setTranslateX(node.getFinPoint().getX() - maxLabelWidthChar / 2 + 2);
                label.getTransforms().add(Logic.scale);
                holst.getChildren().add(label);
                Logic.drawedInfoObjects.add(label);
            }
        }
        //else
        //fillProperties(((Branch) Logic.currentItem).cityNode.getInfo());
    }
}

