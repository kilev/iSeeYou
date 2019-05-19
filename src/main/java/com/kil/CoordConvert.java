package com.kil;


import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.StrictMath.*;

public class CoordConvert {
    private static final double Earth_Radius = 6371.0;

    private List<RealPoint> changedCoordinates = new ArrayList<>();
    private List<Point> realCoordinates = new ArrayList<>();

    public CoordConvert(List<CityNode> cityNodes) throws NullPointerException {
        for (CityNode cityNode : cityNodes) {
            Point point = new Point(cityNode.getPoint().getX(), cityNode.getPoint().getY());
            realCoordinates.add(point);
        }
    }

    private RealPoint translateToNormal(Point point) {
        return new RealPoint(Earth_Radius * cos(point.getX() * Math.PI / 180) * cos(point.getY() * Math.PI / 180),
                Earth_Radius * cos(point.getX() * Math.PI / 180) * sin(point.getY() * Math.PI / 180),
                Earth_Radius * sin(point.getX() * Math.PI / 180));
    }

    public double calculateLenght(Point begin, Point end) {
        RealPoint P1 = translateToNormal(begin);
        RealPoint P2 = translateToNormal(end);
        return sqrt(pow(P2.X - P1.X, 2) + pow(P2.Y - P1.Y, 2) + pow(P2.Z - P1.Z, 2));
    }


    public void changeCoords() {      //создает список новых координат относительно самого левого и верхнего узла
        Point leftUp = new Point(10000000.0, -999999999.0);   // условная самая левая верхняя точка
        for (Point realPoint : realCoordinates) {
            RealPoint temp = translateToNormal(realPoint);
            changedCoordinates.add(temp);
            if (temp.X < leftUp.getX()) {
                leftUp.setX(temp.X);
            }
            if (temp.Y > leftUp.getY()) {
                leftUp.setY(temp.Y);
            }
        }
        for (int i = 0; i < changedCoordinates.size(); i++) {
            changedCoordinates.set(i ,new RealPoint(changedCoordinates.get(i).X - leftUp.getX(),
                    changedCoordinates.get(i).Y - leftUp.getY(), 0.0));
        }
    }

    public String newCoordsByIndex(int index) {           //возвращает новую координату узла по заданному индексу
        return changedCoordinates.get(index).X + " " + changedCoordinates.get(index).Y;
    }
}

class Point {
    private double x;
    private double y;

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }

    void setX(double x) {
        this.x = x;
    }

    void setY(double y) {
        this.y = y;
    }

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

class RealPoint{
    double X;
    double Y;
    double Z;

    RealPoint(double x, double y, double z) {
        X = x;
        Y = y;
        Z = z;
    }
}
