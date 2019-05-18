package com.kil;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

class MyPoint extends Circle {

    CityNode cityNode;

    MyPoint(CityNode node) {
        super(node.getPoint().getX(), node.getPoint().getY(), 4);
        this.cityNode = node;
        this.setOnMouseEntered(e -> {
            Logic.currentItem = cityNode;
            this.setRadius(8);
            this.setFill(Color.BLUE);
        });
        this.setOnMouseExited(e -> {
            if(Logic.currentItem == cityNode)
                Logic.currentItem = null;
            this.setRadius(4);
            this.setFill(Color.BLACK);
        });
    }
}
