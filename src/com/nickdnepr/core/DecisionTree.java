package com.nickdnepr.core;

import java.util.ArrayList;
import java.util.HashMap;

public class DecisionTree {

    private Node root;
    private HashMap<Integer, Node> nodes;

    public DecisionTree(Node root, HashMap<Integer, Node> nodes) {
        this.root = root;
        this.nodes = nodes;
    }

    public Integer classify(Item item) {
        Node node = root;
        while (!node.isLeaf()) {
            node = nodes.get(node.step(item));
        }
        return node.getNodeClass();
    }

    public Node getRoot() {
        return root;
    }

    public double accuracy(ArrayList<Item> items) {
        double yes = 0;
        double all = items.size();
        for (Item item : items) {
            if (classify(item) == item.getClassBelong()) {
                yes++;
            }
        }
        return yes / all;
    }

    public void confusionMatrix(ArrayList<Item> items) {
        int yesC1 = 0;
        int yesC2 = 0;
        int allC1 = 0;
        int allC2 = 0;
        for (Item item : items) {
            if (item.getClassBelong() == 1) {
                if (classify(item) == item.getClassBelong()) {
                    yesC1++;
                }
                allC1++;
            } else {
                if (classify(item) == item.getClassBelong()) {
                    yesC2++;
                }
                allC2++;
            }
        }
        System.out.println(yesC1 + "  " + allC1);
        System.out.println(allC2 + "  " + yesC2);
    }

    public HashMap<Integer, Node> getNodes() {
        return nodes;
    }

    @Override
    public String toString() {
        return "DecisionTree{" +
                "root=" + root +
                ", nodes=" + nodes +
                '}';
    }
}
