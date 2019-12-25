package com.github.bobi4597.adventofcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2019/day/14
 */
public class Year2019Day14 {

    static class Edge {
        String to;
        Integer value;
        Edge (String to, Integer value) {
            this.to = to; this.value = value;
        }
    }

    static class Graph {
        Map<String, List<Edge>> edges;
        Map<String, Integer> values;
        Graph () {
            edges = new HashMap<>();
            values = new HashMap<>();
        }
    }

    static class TopologicalSort {
        Graph g;
        Map<String, Integer> sortOrder;
        List<String> orderedList;
        Set<String> visited;

        TopologicalSort(Graph g) {
            this.g = g;
            sortOrder = new HashMap<>();
            visited = new HashSet<>();
            orderedList = new ArrayList<>();

            doTopologicalSort();
        }

        private void doTopologicalSort() {
            dfs("FUEL");
        }

        private void dfs(String node) {
            visited.add(node);
            if (g.edges.containsKey(node)) {
                for (Edge edge : g.edges.get(node)) {
                    if (!visited.contains(edge.to)) {
                        dfs(edge.to);
                    }
                }
            }
            orderedList.add(node);
            sortOrder.put(node, orderedList.size());
        }
    }

    static class State {
        String node;
        Integer order;
        State (String node, Integer order) {
            this.node = node; this.order = order;
        }
    }

    private static Graph g;
    private static TopologicalSort ts;

    public static void main(String[] args) {
        readInput();
        System.out.printf("%d\n", solve2());
    }

    private static void readInput() {
        g = new Graph();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            processLine(line);
        }
        // printGraph();
        ts = new TopologicalSort(g);
        // System.out.printf("%s\n", ts.orderedList.toString());
    }

    private static void processLine(String line) {
        String[] parts = line.split("=>");

        String nodeStr = parts[1].trim();
        String[] nodeParts = nodeStr.split(" ");
        Integer nodeValue = Integer.parseInt(nodeParts[0].trim());
        String nodeName = nodeParts[1].trim();

        String[] edgesStr = parts[0].split(",");
        List<Edge> edges = new ArrayList<>();
        for (String edgeStr: edgesStr) {
            String[] edgeParts = edgeStr.trim().split(" ");
            Integer edgeValue = Integer.parseInt(edgeParts[0].trim());
            String edgeName = edgeParts[1].trim();
            edges.add(new Edge(edgeName, edgeValue));
        }
        g.values.put(nodeName, nodeValue);
        g.edges.put(nodeName, edges);
    }

    private static void printGraph() {
        for (Map.Entry<String, List<Edge>> entry: g.edges.entrySet()) {
            System.out.printf("%d %s <= ", g.values.get(entry.getKey()), entry.getKey());
            String edgesString = entry.getValue()
                .stream()
                .map(e -> e.value + " " + e.to)
                .collect(Collectors.joining(", "));
            System.out.printf("%s\n", edgesString);
        }
    }

    /////////////// part 1 /////////////////////

    private static long solve2() {
        long totalORE = 1_000_000_000_000L;
        Map<String, Integer> extraMap = new HashMap<>();
        long count = 0L;

        while (totalORE >= 0) {
            Set<String> visited = new HashSet<>();

            PriorityQueue<State> pq = new PriorityQueue<>((s1, s2) -> s2.order - s1.order);
            pq.add(new State("FUEL", ts.sortOrder.get("FUEL")));
            Map<String, Integer> requiredMap = new HashMap<>();
            requiredMap.put("FUEL", 1);

            while (!pq.isEmpty()) {
                State state = pq.poll();
                if (state.node.equals("ORE")) {
                    totalORE -= requiredMap.get(state.node);
                    ++count;
                    break;
                }
                if (visited.contains(state.node)) {
                    continue;
                }
                visited.add(state.node);

                Integer extraAmount = extraMap.getOrDefault(state.node, 0);
                Integer producedAmount = g.values.get(state.node);
                Integer requiredAmount = requiredMap.get(state.node);
                Integer factor = (requiredAmount - extraAmount) / producedAmount;
                if ((requiredAmount - extraAmount) % producedAmount != 0) {
                    factor += 1;
                }

                extraMap.put(state.node, factor * producedAmount - (requiredAmount - extraAmount));

                for (Edge edge : g.edges.get(state.node)) {
                    pq.add(new State(edge.to, ts.sortOrder.get(edge.to)));
                    Integer edgeRequiredAmount = requiredMap.getOrDefault(edge.to, 0);
                    edgeRequiredAmount += edge.value * factor;
                    requiredMap.put(edge.to, edgeRequiredAmount);
                }
            }
        }

        return count - 1;
    }
}
