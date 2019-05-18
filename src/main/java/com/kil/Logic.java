package com.kil;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

class Logic {

    static int infoPaneOffset = 10;
    static int startOffset = 50;
    static double sizeCoef = 1;
    static boolean enableAutoReSize;


    static Object currentItem = null;
    static List<Object> drawedInfoObjects = new ArrayList<>();


    static List<CityNode> nodeList = new ArrayList<>();
    static List<Branch> branchList = new ArrayList<>();

    public static void computeFinPoint() {
        for (CityNode node : nodeList) {
            if (enableAutoReSize){
                int sr = 0;
                for(CityNode nd: nodeList){

                }
                node.setFinPoint(new Point2D(node.getPoint().getX() * Logic.sizeCoef + startOffset, node.getPoint().getY() * Logic.sizeCoef + startOffset));
            }
            else{
                node.setFinPoint(new Point2D(node.getPoint().getX(), node.getPoint().getY()));
            }
        }
    }
}
