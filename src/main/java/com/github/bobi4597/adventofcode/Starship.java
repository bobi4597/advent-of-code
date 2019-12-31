package com.github.bobi4597.adventofcode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Starship {

    public static Map<String, String> opositeDirection = new HashMap<>();

    static {
        opositeDirection.put("south", "north");
        opositeDirection.put("north", "south");
        opositeDirection.put("west", "east");
        opositeDirection.put("east", "west");
    }

    Map<String, StarshipCell> cells;
    Set<String> items;
    Set<String> visited;

    void addCell(StarshipCell cell) {
        cells.put(cell.name, cell);
        items.addAll(cell.items);
    }

    Starship() {
        cells = new HashMap<>();
        items = new HashSet<>();
        visited = new HashSet<>();
    }
    void print() {

        System.out.printf("Printing total of %d starship cells: \n", cells.size());

        for (StarshipCell sc: cells.values()) {
            System.out.printf("%s\n", sc.text.replace("\n", " "));
            // Edges
            sc.edges.forEach((key, value) -> System.out.printf("\t%s: %s\n", key, value.name));
            System.out.println();
            // Items
            sc.items.forEach(item -> System.out.printf("\t%s\n", item));
            System.out.println();
        }
        System.out.printf("\nPrinting total of %d items:\n", items.size());
        for (String item: items) {
            System.out.println(item);
        }
    }

    public void addEdge(StarshipCell cell, StarshipCell newCell, String direction) {
        cell.edges.put(direction, newCell);
        newCell.edges.put(opositeDirection.get(direction), cell);
    }
}
