package com.kil;


import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.extern.java.Log;


import java.util.ArrayList;
import java.util.List;

@Getter
public class CityNode {
    private Point2D point;
    private String cityName;
    private double voltage;
    private double maxAmperage;

    public CityNode(String cityName, double x_coord, double y_coord, double voltage, double maxAmperage) {
        this.cityName = cityName;
        this.point = new Point2D(x_coord,y_coord);
        this.voltage = voltage;
        this.maxAmperage = maxAmperage;
    }

    public List getInfo(){
        List<String> list = new ArrayList<>();
        list.add(cityName);
        list.add("Voltage" + String.valueOf(voltage));
        list.add("maxA" + String.valueOf(maxAmperage));
        return list;
    }
}
