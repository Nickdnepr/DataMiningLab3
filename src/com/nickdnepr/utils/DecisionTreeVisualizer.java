package com.nickdnepr.utils;

import com.nickdnepr.core.DecisionTree;
import com.nickdnepr.core.Node;

public class DecisionTreeVisualizer {

    public static final String delimiter = "    ";

    public static void visualize(DecisionTree tree) {
        visualizeNode(tree.getRoot(), tree, 0);
    }

    private static void visualizeNode(Node node, DecisionTree tree, int embedding) {
        StringBuilder localDelimiter = new StringBuilder();
        for (int i = 0; i < embedding; i++) {
            localDelimiter.append(delimiter);
        }
        if (node.isLeaf()) {
            System.out.println(localDelimiter + "Class is " + node.getNodeClass());
        } else {
            System.out.println(localDelimiter + node.getAttributeName() + " <= " + node.getAttributeBorder() + " :");
            System.out.println(localDelimiter + "Yes:");
            visualizeNode(tree.getNodes().get(node.getNodeYesId()), tree, embedding + 1);
            System.out.println(localDelimiter + "No:");
            visualizeNode(tree.getNodes().get(node.getNodeNoId()), tree, embedding + 1);
        }

    }
}
