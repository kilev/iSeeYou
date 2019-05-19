package com.kil;

import javafx.geometry.Point2D;
import javafx.scene.transform.Scale;

import java.util.ArrayList;
import java.util.List;

class Logic {

    static int infoPaneOffset = 10;
    static int startOffset = 50;
    static double sizeCoef = 1;
    static boolean enableAutoReSize;
    static double maxPos;
    static Scale scale =  new Scale();


    static Object currentItem = null;
    static List<Object> drawedInfoObjects = new ArrayList<>();


    static List<CityNode> nodeList = new ArrayList<>();
    static List<Branch> branchList = new ArrayList<>();

    private static void computeRightPos(){
        double maxX = 0;
        double maxY = 0;
        for (CityNode node : nodeList) {
            if(node.getPoint().getX() > maxX)
                maxX = node.getPoint().getX();
            if(node.getPoint().getY() > maxY)
                maxY = node.getPoint().getY();
        }
        if(maxX > maxY)
            maxPos = maxX;
        else
            maxPos = maxY;
    }

    public static void computeFinPoint() {
//Setting the dimensions for the transformation
        scale.setX(sizeCoef);
        scale.setY(sizeCoef);

        computeRightPos();

//Setting the pivot point for the transformation
        scale.setPivotX(maxPos / 2);
        scale.setPivotY(maxPos / 2);

        for (CityNode node : nodeList) {
            if (enableAutoReSize){
                node.setFinPoint(new Point2D(node.getPoint().getX() + startOffset, node.getPoint().getY() + startOffset));
            }
            else{
                node.setFinPoint(new Point2D(node.getPoint().getX() + startOffset, node.getPoint().getY() + startOffset));
            }
        }
    }
}
