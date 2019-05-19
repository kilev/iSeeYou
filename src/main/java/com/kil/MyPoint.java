package com.kil;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

class MyPoint extends Circle {

    CityNode cityNode;

    MyPoint(CityNode node) {
        super(node.getFinPoint().getX(), node.getFinPoint().getY(), 6);
        this.cityNode = node;
        this.setFill(Color.GREEN);
        this.setOnMouseEntered(e -> {
            Logic.currentItem = cityNode;
            this.setRadius(8);
        });
        this.setOnMouseExited(e -> {
            if(Logic.currentItem == cityNode)
                Logic.currentItem = null;
            this.setRadius(6);
        });
    }
}
