package com.nickdnepr.utils;

import com.nickdnepr.core.Item;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CsvParser {


    public static ArrayList<Item> parseCsv(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File does not exist");
            return null;
        }
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while (line != null) {
            lines.add(line);
            line = reader.readLine();
        }
        if (lines.size() < 2) {
            System.out.println("File is empty");
            return null;
        }
        if (!validate(lines)) {
            System.out.println("Input file is invalid");
            return null;
        }
        ArrayList<Item> items = new ArrayList<>();
        String[] names = lines.get(0).split(",");
        for (int i = 1; i < lines.size(); i++) {
            HashMap<String, Double> attributes = new HashMap<>();
            Integer classBelong = -1;
            String[] attrArr = lines.get(i).split(",");
            for (int j = 0; j < names.length; j++) {
                String attrName = names[j];
                if (attrName.equals("class")){
                    classBelong = Integer.valueOf(attrArr[j]);
                    continue;
                }
                attributes.put(attrName, Double.valueOf(attrArr[j]));
            }
            items.add(new Item(attributes, classBelong));
        }
        return items;
    }

    private static boolean validate(ArrayList<String> lines) {
        String[] splits = lines.get(0).split(",");
        boolean containsClass = false;
        for (String s : splits) {
            if (s.equals("class")) {
                if (containsClass) {
                    System.out.println("Class link met twice");
                    return false;
                } else {
                    containsClass = true;
                }
            }
        }
        if (!containsClass) {
            System.out.println("File does not have class link");
            return false;
        }
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i).split(",").length != splits.length) {
                return false;
            }
        }
        return true;
    }
}
