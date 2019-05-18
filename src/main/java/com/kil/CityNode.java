package com.kil;


import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;


import java.util.ArrayList;
import java.util.List;

@Getter
public class CityNode {
    private Point2D point;
    @Setter
    private Point2D finPoint;
    private String cityName;
    private double factual_voltage;
    private double frequency;
    private double nominalPower_Re;
    private double nominalPower_Im;

    public CityNode(String cityName, double x_coord, double y_coord, double nominalPower_Re, double nominalPower_Im, double factual_voltage, int frequency) {
        this.point = new Point2D(x_coord,y_coord);
        finPoint = point;
        //always
        this.cityName = cityName;
        //window
        this.nominalPower_Re = nominalPower_Re;
        this.nominalPower_Im = nominalPower_Im;
        this.frequency = frequency;
        this.factual_voltage = factual_voltage;
    }

    public List getInfo(){
        List<String> list = new ArrayList<>();
        list.add(cityName);
        list.add("S nom " + nominalPower_Re + " + " + nominalPower_Im + "i");
        list.add("f " + frequency);
        list.add("V fact " + factual_voltage);
        return list;
    }
}
