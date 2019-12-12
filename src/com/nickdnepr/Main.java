package com.nickdnepr;

import com.nickdnepr.core.DecisionTree;
import com.nickdnepr.core.Item;
import com.nickdnepr.utils.CsvParser;
import com.nickdnepr.utils.DecisionTreeBuilder;
import com.nickdnepr.utils.DecisionTreeVisualizer;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
//        System.out.println(Math.log(0));

//        System.out.println(CsvParser.parseCsv("csv.csv"));
        ArrayList<Item> items = CsvParser.parseCsv("csv.csv");
        DecisionTreeBuilder.getEntropy(items);
//        System.out.println( DecisionTreeBuilder.build(items));
        DecisionTree tree = DecisionTreeBuilder.build(items);
//        System.out.println(tree.accuracy(items));

        DecisionTreeVisualizer.visualize(tree);
        DecisionTree validTree = DecisionTreeBuilder.buildAndCrossValidate(items,2,2);
//        DecisionTreeVisualizer.visualize(validTree);
        validTree.confusionMatrix(items);
    }
}
