package com.nickdnepr.utils;

import com.nickdnepr.core.DecisionTree;
import com.nickdnepr.core.Item;
import com.nickdnepr.core.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DecisionTreeBuilder {

    public static DecisionTree buildAndCrossValidate(ArrayList<Item> items) {
        return buildAndCrossValidate(items, 10);
    }

    public static DecisionTree buildAndCrossValidate(ArrayList<Item> items, int selectionPacks) {
        return buildAndCrossValidate(items, 2, selectionPacks);
    }

    public static DecisionTree buildAndCrossValidate(ArrayList<Item> items, int minimalCapacity, int selectionPacks) {
        ArrayList<Item> copyItems = new ArrayList<>(items);
        ArrayList<ArrayList<Item>> packs = new ArrayList<>();
        while (!copyItems.isEmpty()) {
//            System.out.println("New pack");
            ArrayList<Item> pack = new ArrayList<>();
            for (int i = 0; i < selectionPacks; i++) {
                if (i >= copyItems.size()) {
                    break;
                }
//                System.out.println("Add " + copyItems.get(0));
                pack.add(copyItems.get(i));
            }
            copyItems.removeAll(pack);
            packs.add(pack);
        }
//        System.out.println(packs.size());
//        for (ArrayList<Item> p : packs) {
//            System.out.println(p);
//        }
        ArrayList<Pair<ArrayList<Item>, ArrayList<Item>>> learnAndTestPacks = new ArrayList<>();
        for (int i = 0; i < packs.size(); i++) {
            Pair<ArrayList<Item>, ArrayList<Item>> learnAndTestPair;
            ArrayList<Item> learnPack = new ArrayList<>();
            ArrayList<Item> testPack = new ArrayList<>();
            for (int j = 0; j < packs.size(); j++) {
                if (i == j) {
                    testPack.addAll(packs.get(j));
                } else {
                    learnPack.addAll(packs.get(j));
                }
            }
            learnAndTestPair = new Pair<>(learnPack, testPack);
            learnAndTestPacks.add(learnAndTestPair);
        }
//        System.out.println(learnAndTestPacks);
//        ArrayList<DecisionTree> trees = new ArrayList<>();
//        HashMap<DecisionTree, Double> accurateTrees = new HashMap<>();
        ArrayList<Pair<DecisionTree, Double>> accurateTrees = new ArrayList<>();
        for (Pair<ArrayList<Item>, ArrayList<Item>> pair : learnAndTestPacks) {
//            System.out.println(pair);
//            trees.add(build(pair.getKey(), minimalCapacity));
            DecisionTree tree = build(pair.getKey(), minimalCapacity);
//            accurateTrees.put(tree.accuracy(pair.getValue()), tree);
            accurateTrees.add(new Pair<>(tree, tree.accuracy(pair.getValue())));
        }
        Double maxAccuracy = null;
        DecisionTree maxTree = null;
//        System.out.println("-------------");
        for (Pair<DecisionTree, Double> p : accurateTrees) {
            if (maxAccuracy == null) {
                maxAccuracy = p.getValue();
                maxTree = p.getKey();
            } else {
                if (p.getValue() > maxAccuracy) {
                    maxAccuracy = p.getValue();
                    maxTree = p.getKey();
                }
            }
//            System.out.println(p.getValue());
        }
//        System.out.println("-------------");
//        System.out.println(maxTree);
//        System.out.println(maxAccuracy);
        return maxTree;
    }

    public static DecisionTree build(ArrayList<Item> items) {
        return build(items, 2);
    }

    public static DecisionTree build(ArrayList<Item> items, int minimalCapacity) {

        Node rootNode = buildNode(items);
        HashMap<Integer, Node> nodes = new HashMap<>();
        nodes.put(rootNode.getNodeId(), rootNode);
        ArrayList<Node> notProcessedNodes = new ArrayList<>();
        HashMap<Node, ArrayList<Item>> associatedSelection = new HashMap<>();
        associatedSelection.put(rootNode, items);
        notProcessedNodes.add(rootNode);

        while (!notProcessedNodes.isEmpty()) {
            Node node = notProcessedNodes.get(0);
            ArrayList<Item> nodeSet = associatedSelection.get(node);
//            System.out.println("Set: " + nodeSet);
//            System.out.println(node.toString());
            Pair<ArrayList<Item>, ArrayList<Item>> split = node.split(nodeSet);
            ArrayList<Item> yesSet = split.getKey();
            ArrayList<Item> noSet = split.getValue();
            Node yesNode = buildNode(yesSet);
            Node noNode = buildNode(noSet);
            node.setNodeYesId(yesNode.getNodeId());
            node.setNodeNoId(noNode.getNodeId());
//            if (yesSet.size() < minimalCapacity) {
//                yesNode.setLeaf(true);
//                yesNode.setNodeClass(getMaxClassValue(yesSet));
//            }
//            if (noSet.size() < minimalCapacity) {
//                noNode.setLeaf(true);
//                noNode.setNodeClass(getMaxClassValue(noSet));
//            }
            notProcessedNodes.remove(node);
            if (!yesNode.isLeaf()) {
                notProcessedNodes.add(yesNode);
            }
            if (!noNode.isLeaf()) {
                notProcessedNodes.add(noNode);
            }
            nodes.put(yesNode.getNodeId(), yesNode);
            nodes.put(noNode.getNodeId(), noNode);
            associatedSelection.put(yesNode, yesSet);
            associatedSelection.put(noNode, noSet);
        }
        return new DecisionTree(rootNode, nodes);
    }

    private static Node buildNode(ArrayList<Item> items) {
        ArrayList<String> attributes = new ArrayList<>(items.get(0).getAttributes().keySet());
        boolean shouldBeLeaf = true;
        int itemsClass = items.get(0).getClassBelong();
        for (Item item : items) {
            if (item.getClassBelong() != itemsClass) {
                shouldBeLeaf = false;
            }
        }
        Pair<Double, Node> minEntropyPair = null;
        for (String attribute : attributes) {
            Pair<Double, Node> pair = getMinEntropyBorderByAttribute(items, attribute);
            if (minEntropyPair == null) {
                minEntropyPair = pair;
            } else {
                if (minEntropyPair.getKey() > pair.getKey()) {
                    minEntropyPair = pair;
                }
            }
        }
        Node resultNode;
        if (minEntropyPair == null) {
            resultNode = new Node("", 0.0);
            shouldBeLeaf = true;
        } else {
            resultNode = minEntropyPair.getValue();
        }
        if (shouldBeLeaf) {
            resultNode.setNodeClass(itemsClass);
            resultNode.setLeaf(true);
        }
        return resultNode;
    }

    private static Pair<Double, Node> getMinEntropyBorderByAttribute(ArrayList<Item> items, String attribute) {
        Set<Double> uniqueValues = new HashSet<>();
        for (Item item : items) {
            uniqueValues.add(item.getAttributes().get(attribute));
        }
        ArrayList<Double> uniqueValuesList = new ArrayList<>(uniqueValues);
        uniqueValuesList.sort(Double::compareTo);
        ArrayList<Double> possibleBorders = new ArrayList<>();
        for (int i = 1; i < uniqueValuesList.size(); i++) {
            possibleBorders.add((uniqueValuesList.get(i) + uniqueValuesList.get(i - 1)) / 2.0);
        }
        Double minimalEntropy = null;
        Node minimalEntropyNode = null;
        Node node = null;
        for (Double border : possibleBorders) {
            node = new Node(attribute, border);
            Pair<ArrayList<Item>, ArrayList<Item>> split = node.split(items);
            double entropy = getEntropy(split.getKey()) + getEntropy(split.getValue());
            if (minimalEntropy == null) {
                minimalEntropy = entropy;
                minimalEntropyNode = node;
            } else {
                if (entropy < minimalEntropy) {
                    minimalEntropy = entropy;
                    minimalEntropyNode = node;
                }
            }
        }
        if (minimalEntropy == null) {
            return null;
        }
        return new Pair<>(minimalEntropy, minimalEntropyNode);
    }

    public static double getEntropy(ArrayList<Item> items) {
        double entropy = 0;
        HashMap<Integer, Integer> classFrequencies = getClassFrequencies(items);
        for (Integer i : classFrequencies.keySet()) {
            double p = classFrequencies.get(i) / (double) items.size();
            entropy += -1.0 * p * (Math.log(p) / Math.log(2));
        }
        return entropy;
    }

    private static int getMaxClassValue(ArrayList<Item> items) {
        HashMap<Integer, Integer> classFrequencies = getClassFrequencies(items);
        int maxFrequency = 0;
        for (int i : classFrequencies.values()) {
            if (i > maxFrequency) {
                maxFrequency = i;
            }
        }
        return maxFrequency;
    }

    private static HashMap<Integer, Integer> getClassFrequencies(ArrayList<Item> items) {
        Set<Integer> classes = new HashSet<>();
        HashMap<Integer, Integer> classFrequencies = new HashMap<>();
        for (Item i : items) {
            classes.add(i.getClassBelong());
        }
        for (Integer i : classes) {
            classFrequencies.put(i, 0);
        }
        for (Item item : items) {
            classFrequencies.put(item.getClassBelong(), classFrequencies.get(item.getClassBelong()) + 1);
        }
        return classFrequencies;
    }
}
