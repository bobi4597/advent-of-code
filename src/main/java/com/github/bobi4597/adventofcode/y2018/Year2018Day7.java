package com.github.bobi4597.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.github.bobi4597.adventofcode.y2018.Year2018Day1.FOLDER_PATH;

public class Year2018Day7 {

    public static void main(String[] args) {
        List<String> a = readInput();
        Graph graph = parseInput(a);
        String order = solve1(graph);
        System.out.printf("Part 1: %s\n", order);
        System.out.printf("Part 2: %d\n", solve2(graph, order));
    }

    private static int solve2(Graph graph, String order) {
        PriorityQueue<Step> pq = new PriorityQueue<>(Comparator.comparingInt(s -> (s.startTime + s.duration)));
        int time = 0;

        Set<String> visited = new HashSet<>();

        for (int i = 0; i < order.length(); ++i) {
            String name = "" + order.charAt(i);
            int duration = 0 + (order.charAt(i) - 'A') + 1;
            while (true) {
                if (graph.edges.get(name) == null || graph.edges.get(name).size() == 0) {
                    // no dependencies to wait for:
                    // 1. check if there is enough space in the priority queue
                    if (pq.size() == 5) {
                        Step top = pq.poll();
                        removeNode(graph, top.name);
                        time = (top.startTime + top.duration);
                    }
                    pq.add(new Step(name, time, duration));
                    break;
                } else {
                    // there are dependencies
                    Step top = pq.poll();
                    removeNode(graph, top.name);
                    time = (top.startTime + top.duration);
                }
            }
        }

        while (!pq.isEmpty()) {
            Step top = pq.poll();
            time = top.startTime + top.duration;
        }

        return time;
    }

    private static void removeNode(Graph graph, String nodeToRemove) {
        for (String node: graph.nodes) {
            if (graph.edges.get(node) != null) {
                graph.edges.get(node).remove(nodeToRemove);
            }
        }
    }

    static class Step {
        String name;
        int startTime;
        int duration;
        Step (String name, int startTime, int duration) {
            this.name = name; this.startTime = startTime; this.duration = duration;
        }
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
