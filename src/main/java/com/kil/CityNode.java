package com.kil;


import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;

@Getter
public class CityNode {
    private Point2D point;
    @Setter
    private Point2D finPoint;
    private String cityName;
    private double Umax1;
//    private double Umax2;
//    private double Umax3;
//    private double Umax4;
    private double frequency;
    private double nominalPower_Re;
    private double nominalPower_Im;

    public CityNode(String cityName, double x_coord, double y_coord, double nominalPower_Re, double nominalPower_Im, double Umax1, double frequency) {
        this.point = new Point2D(x_coord,y_coord);
        finPoint = point;
        //always
        this.cityName = cityName;
        //window
        this.nominalPower_Re = nominalPower_Re;
        this.nominalPower_Im = nominalPower_Im;
        this.frequency = frequency;
        this.Umax1 = Umax1;
//        this.Umax2 = Umax2;
//        this.Umax3 = Umax3;
//        this.Umax4 = Umax4;
    }

    public List getInfo(){
        List<String> list = new ArrayList<>();
        if(!Logic.worldCoord)
            list.add(point.getX() + " : " + point.getY());
        else
            list.add(Logic.coordConvert.newCoordsByIndex(Logic.nodeList.indexOf(this)));
        list.add("S nom " + nominalPower_Re + " + " + nominalPower_Im + "i");
        list.add("f " + frequency);
        list.add("V fact: " + Umax1);
//        list.add("V fact 2: " + Umax1);
//        list.add("V fact 3: " + Umax1);
//        list.add("V fact 4: " + Umax1);
        return list;
    }
}
