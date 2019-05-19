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
import javafx.print.*;
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
    private CheckMenuItem worldCoord;

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
    private CheckMenuItem showInfoEnable;

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
            PageLayout pageLayout = Printer.getDefaultPrinter().createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);

            PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null) {
                job.showPrintDialog(holst.getScene().getWindow()); // Window must be your main Stage
                job.printPage(pageLayout, holst);
                job.endJob();
            }
        });

        //switch autoResize
        autoReSize.setOnAction(actionEvent -> {
            Logic.enableAutoReSize = autoReSize.isSelected();
            Logic.computeFinPoint();
            draw();
        });

        worldCoord.setOnAction(actionEvent -> {
            Logic.worldCoord = !Logic.worldCoord;
        });

        showInfoEnable.setOnAction(actionEvent -> {
            if(showInfoEnable.isSelected())
                for (CityNode node : Logic.nodeList) {
                    showMessage(node);
                }
            else{
                deleteMessage();
            }
        });
        draw();
    }

    //draw components
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
            line.setStrokeWidth(2);
            holst.getChildren().add(line);
        }
    }


    private void showMessage(CityNode node) {
        if(Logic.sizeCoef != 1.0f)
            return;

        List<String> list = new ArrayList();

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
        labelName.getTransforms().add(Logic.scale);
        labelName.setTranslateY(node.getFinPoint().getY() - Logic.infoPaneOffset - 15);
        labelName.setTranslateX(node.getFinPoint().getX() - node.getCityName().length() * 3);
        holst.getChildren().add(labelName);
        Logic.drawedInfoObjects.add(labelName);
        //info
        for (Label label : labelList) {
            label.getTransforms().add(Logic.scale);
            label.setTranslateX(node.getFinPoint().getX() - maxLabelWidthChar / 2 + 2);
            holst.getChildren().add(label);
            Logic.drawedInfoObjects.add(label);
        }
    }

    private void deleteMessage(){
        if (Logic.currentItem == null) {
            if (Logic.drawedInfoObjects != null) {
                for (Object obj : Logic.drawedInfoObjects) {
                    holst.getChildren().remove(obj);
                    Logic.drawedInfoObjects = new ArrayList<>();
                }
            }
            return;
        }
    }


    private void showInfo() {
        if (showInfoEnable.isSelected())
            return;

        deleteMessage();

        if (Logic.currentItem instanceof CityNode) {
            CityNode node = (CityNode) Logic.currentItem;
            showMessage(node);
        }

    }
}

