package com.nickdnepr.core;


import com.nickdnepr.utils.Pair;

import java.util.ArrayList;
import java.util.Objects;

public class Node {

    private static int id = 0;

    private boolean isLeaf;
    private String attributeName;
    private Double attributeBorder;
    private int nodeId;
    private int nodeNoId;
    private int nodeYesId;
    private int nodeClass;

    public Node(String attributeName, Double attributeBorder) {
        this.attributeName = attributeName;
        this.attributeBorder = attributeBorder;
        this.isLeaf = false;
        this.nodeId = getId();
        this.nodeNoId = -2;
        this.nodeYesId = -1;
        this.nodeClass = -1;
    }

    public int step(Item item) {
        if (item.getAttributes().get(attributeName) > attributeBorder) {
            return nodeNoId;
        } else {
            return nodeYesId;
        }
    }

    public Pair<ArrayList<Item>, ArrayList<Item>> split(ArrayList<Item> items) {
        ArrayList<Item> yesItems = new ArrayList<>();
        ArrayList<Item> noItems = new ArrayList<>();
        for (Item item : items) {
            if (step(item) == nodeYesId) {
                yesItems.add(item);
            } else {
                noItems.add(item);
            }
        }
        return new Pair<>(yesItems, noItems);
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public Double getAttributeBorder() {
        return attributeBorder;
    }

    public int getNodeId() {
        return nodeId;
    }

    public int getNodeNoId() {
        return nodeNoId;
    }

    public int getNodeYesId() {
        return nodeYesId;
    }

    public int getNodeClass() {
        return nodeClass;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public void setNodeNoId(int nodeNoId) {
        this.nodeNoId = nodeNoId;
    }

    public void setNodeYesId(int nodeYesId) {
        this.nodeYesId = nodeYesId;
    }

    public void setNodeClass(int nodeClass) {
        this.nodeClass = nodeClass;
    }

    private static int getId() {
        return id++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return isLeaf == node.isLeaf &&
                nodeId == node.nodeId &&
                nodeNoId == node.nodeNoId &&
                nodeYesId == node.nodeYesId &&
                Objects.equals(attributeName, node.attributeName) &&
                Objects.equals(attributeBorder, node.attributeBorder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isLeaf, attributeName, attributeBorder, nodeId, nodeNoId, nodeYesId);
    }

    @Override
    public String toString() {
        return "Node{" +
                "isLeaf=" + isLeaf +
                ", attributeName='" + attributeName + '\'' +
                ", attributeBorder=" + attributeBorder +
                ", nodeId=" + nodeId +
                ", nodeNoId=" + nodeNoId +
                ", nodeYesId=" + nodeYesId +
                ", nodeClass=" + nodeClass +
                '}';
    }
}
