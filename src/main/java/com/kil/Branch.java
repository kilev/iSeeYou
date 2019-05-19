package com.kil;

import lombok.Getter;

@Getter
class Branch {
    private int[] nodes = new int[2];
    private int[] finNodes;
    private double factual_voltage_Re1;
    private double factual_voltage_Re2;
    private double factual_voltage_Im1;
    private double factual_voltage_Im2;

    private int max_amperage;


    public Branch(int node1, int node2, double factual_voltage_Re1, double factual_voltage_Re2, double factual_voltage_Im1, double factual_voltage_Im2,  int max_amperage) {
        this.nodes[0] = node1;
        this.nodes[1] = node2;
        finNodes = nodes;
        this.factual_voltage_Re1 = factual_voltage_Re1;
        this.factual_voltage_Im1 = factual_voltage_Im1;
        this.factual_voltage_Re2 = factual_voltage_Re2;
        this.factual_voltage_Im2 = factual_voltage_Im2;
        this.max_amperage = max_amperage;
    }
}
