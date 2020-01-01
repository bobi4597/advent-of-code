package com.github.bobi4597.adventofcode.y2019;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StarshipCell {
    String name;
    String text;
    List<String> items;
    List<String> directions;
    Map<String, StarshipCell> edges;

    StarshipCell () {
        items = new ArrayList<>();
        directions = new ArrayList<>();
        edges = new HashMap<>();
    }

    /**
     * == Stables ==
     * Reindeer-sized. They're all empty.
     *
     * Doors here lead:
     * - north
     * - south
     * - west
     *
     * Items here:
     * - infinite loop
     *
     * Command?
     */
    public static StarshipCell fromRunResult(BigRunResult runResult) {
        String output = runResult.toAscii();
        String[] lines = output.split("\n");
        StarshipCell cell = new StarshipCell();
        cell.text = output;

        int i = 0;
        while (i < lines.length && !lines[i].startsWith("==")) {
            ++i;
        }
        if (i < lines.length) {
            cell.name = lines[i];
            ++i;
        }
        while (i < lines.length && !lines[i].equals("Doors here lead:")) {
            ++i;
        }
        ++i;
        while (i < lines.length && lines[i].startsWith("-")) {
            cell.directions.add(lines[i].substring(2));
            ++i;
        }

        while (i < lines.length && !lines[i].equals("Items here:")) {
            ++i;
        }
        ++i;
        while (i < lines.length && lines[i].startsWith("-")) {
            cell.items.add(lines[i].substring(2));
            ++i;
        }
        return cell;

    }

}
