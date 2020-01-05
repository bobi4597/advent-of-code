package com.github.bobi4597.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.github.bobi4597.adventofcode.y2018.Year2018Day1.FOLDER_PATH;

public class Year2018Day7 {

    public static void main(String[] args) {
        List<String> a = readInput();
        Graph graph = parseInput(a);
        System.out.printf("Part 1: %s\n", solve1(graph));
    }

    private static String solve1(Graph graph) {
        TopologicalSort ts = new TopologicalSort(graph);
        return ts.order;
    }

    static class TopologicalSort {

        Map<String, Integer> counts = new HashMap<>();
        String order = new String();
        Set<String> visited = new HashSet<>();
        Graph g;

        TopologicalSort(Graph g) {
            this.g = g;
            g.nodes
                .forEach(node -> counts.put(node, g.edges.getOrDefault(node, new HashSet<>()).size()));

            while (true) {
                List<String> freeNodes = new ArrayList<>();
                for (String node : g.nodes) {
                    if (counts.get(node) == 0 && !visited.contains(node)) {
                        freeNodes.add(node);
                    }
                }
                if (freeNodes.isEmpty()) {
                    break;
                }
                freeNodes.sort(String::compareTo);
                String nextNode = freeNodes.get(0);
                visited.add(nextNode);
                for (String otherNode : g.nodes) {
                    if (g.edges.get(otherNode) != null && g.edges.get(otherNode).contains(nextNode)) {
                        counts.put(otherNode, counts.get(otherNode) - 1);
                    }
                }
                order = order + nextNode;
            }

        }
    }

    static class Graph {
        Map<String, Set<String>> edges = new HashMap<>();
        Set<String> nodes = new HashSet<>();

        Graph () {
        }

        void addDirectedEdge(String from, String to) {
            Set<String> e = edges.getOrDefault(from, new HashSet<>());
            e.add(to);
            edges.put(from, e);
            nodes.add(from);
            nodes.add(to);
        }
    }

    private static Graph parseInput(List<String> a) {
        Pattern pattern = Pattern.compile("^Step ([A-Z]) must be finished before step ([A-Z]) can begin.");
        Graph graph = new Graph();
        a.stream()
            .map(line -> {
                Matcher m = pattern.matcher(line);
                String from = "", to = "";
                if (m.find()) {
                    to = m.group(1);
                    from = m.group(2);
                }
                return new String[] {from, to};
            })
            .forEach(e -> graph.addDirectedEdge(e[0], e[1]));

        return graph;
    }

    private static List<String> readInput() {
        try {
            return Files.lines(Paths.get(FOLDER_PATH + "day7.txt"))
                .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
