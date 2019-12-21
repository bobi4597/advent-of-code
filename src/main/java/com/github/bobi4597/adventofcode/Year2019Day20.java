package com.github.bobi4597.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

/**
 * https://adventofcode.com/2019/day/20
 */
public class Year2019Day20 {

    private static List<String> input = new ArrayList<>();
    private static int m, n;
    private static Map<Integer, String> coordMap;
    private static int[] di = {-1, 0, 0, 1}, dj = {0, -1, 1, 0};
    private static Graph g;

    static class Edge {
        String from, to;
        int distance;

        Edge(String from, String to, int distance) {
            this.from = from;
            this.to = to;
            this.distance = distance;
        }
    }

    static class Graph {
        Map<String, List<Edge>> edges;

        Graph() {
            edges = new HashMap<>();
        }

        void addEdge(String from, String to, int distance) {
            List<Edge> e = edges.getOrDefault(from, new ArrayList<>());
            e.add(new Edge(from, to, distance));
            edges.put(from, e);
        }
    }

    static class State {
        String node;
        int distance;
        int level;

        State(String node, int distance, int level) {
            this.node = node;
            this.distance = distance;
            this.level = level;
        }
    }

    public static void main(String[] args) {
        System.out.printf("%d\n", solve1());
        System.out.printf("%d\n", solve2());
    }

    private static int solve1() {
        readInput1();
        buildGraph1();
        return findShortestPath1();
    }

    private static void readInput1() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            input.add(scanner.nextLine());
        }
        m = input.size();
        n = input.get(0).length();

        coordMap = new HashMap<>();
        // find the coordinates of the gates
        for (int i = 0; i < m - 1; ++i) {
            for (int j = 0; j < n - 1; ++j) {
                if (isChar(i, j) && isChar(i, j + 1)) {
                    // horizontal gate
                    String name = input.get(i).substring(j, j + 2);
                    if (j - 1 >= 0 && isMaze(i, j - 1)) {
                        coordMap.put(i * n + j, name);
                    } else if (j + 2 < n && isMaze(i, j + 2)) {
                        coordMap.put(i * n + (j + 1), name);
                    }
                } else if (isChar(i, j) && isChar(i + 1, j)) {
                    // vertical gate
                    String name = "" + input.get(i).charAt(j) + input.get(i + 1).charAt(j);
                    if (i - 1 >= 0 && isMaze(i - 1, j)) {
                        coordMap.put(i * n + j, name);
                    } else if (i + 2 < m && isMaze(i + 2, j)) {
                        coordMap.put((i + 1) * n + j, name);
                    }
                }
            }
        }
    }

    private static void buildGraph1() {
        g = new Graph();
        for (Map.Entry<Integer, String> entry : coordMap.entrySet()) {
            Integer x = entry.getKey();
            int si = x / n, sj = x % n;
            String startNode = entry.getValue();

            int[][] visited = new int[m][n];
            for (int t = 0; t < m; ++t) {
                Arrays.fill(visited[t], -1);
            }

            Queue<Integer> queue = new LinkedList<>();
            queue.add(si * n + sj);
            visited[si][sj] = 0;

            while (!queue.isEmpty()) {
                Integer node = queue.poll();
                int i = node / n, j = node % n;

                if (coordMap.containsKey(node) && !coordMap.get(node).equals(startNode)) {
                    g.addEdge(startNode, coordMap.get(node), visited[i][j]);
                    continue;
                }


                for (int dir = 0; dir < 4; ++dir) {
                    int ni = i + di[dir], nj = j + dj[dir];
                    if (ni >= 0 && ni < m && nj >= 0 && nj < n && visited[ni][nj] == -1 &&
                        (input.get(ni).charAt(nj) != '#' && input.get(ni).charAt(nj) != ' ')) {
                        queue.add(ni * n + nj);
                        visited[ni][nj] = visited[i][j] + 1;
                    }
                }
            }
        }
    }

    private static int findShortestPath1() {
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> s.distance));
        pq.add(new State("AA", -1, 0));
        Set<String> visited = new HashSet<>();
        while (!pq.isEmpty()) {
            State state = pq.poll();
            if (visited.contains(state.node)) {
                continue;
            }
            visited.add(state.node);
            if (state.node.equals("ZZ")) {
                return state.distance;
            }
            for (Edge edge: g.edges.get(state.node)) {
                State newState = new State(edge.to, edge.distance + state.distance - 1, 0);
                pq.add(newState);
            }
        }
        return -1;
    }

    private static boolean isOuter(int i, int j) {
        return i < 2 || j < 2 || i >= m - 2 || j >= n - 2;
    }

    private static boolean isChar(int i, int j) {
        return input.get(i).charAt(j) >= 'A' && input.get(i).charAt(j) <= 'Z';
    }

    private static boolean isMaze(int i, int j) {
        return input.get(i).charAt(j) == '#' || input.get(i).charAt(j) == '.';
    }

    private static void printGates() {
        for (Map.Entry<Integer, String> entry : coordMap.entrySet()) {
            System.out.printf("(%d, %d): %s\n", entry.getKey() / n, entry.getKey() % n, entry.getValue());
        }
    }

    private static void printGraph() {
        Map<String, List<Edge>> edgesMap = g.edges;
        for (Map.Entry<String, List<Edge>> entry : edgesMap.entrySet()) {
            String nodeName = entry.getKey();
            List<Edge> edges = entry.getValue();
            System.out.printf("%s: ", nodeName);
            for (Edge edge : edges) {
                System.out.printf("[%s: %d] ", edge.to, edge.distance);
            }
            System.out.println();
        }
    }

    ////////////////////// Part 2 ////////////////////////

    private static int solve2() {
        readInput2();
        buildGraph2();
        return findShortestPath2();
    }

    private static void readInput2() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            input.add(scanner.nextLine());
        }
        m = input.size();
        n = input.get(0).length();

        coordMap = new HashMap<>();
        // find the coordinates of the gates
        for (int i = 0; i < m - 1; ++i) {
            for (int j = 0; j < n - 1; ++j) {
                if (isChar(i, j) && isChar(i, j + 1)) {
                    // horizontal gate
                    String name = input.get(i).substring(j, j + 2);
                    name = name + "-" + (isOuter(i, j) ? "O" : "I");
                    if (j - 1 >= 0 && isMaze(i, j - 1)) {
                        coordMap.put(i * n + j, name);
                    } else if (j + 2 < n && isMaze(i, j + 2)) {
                        coordMap.put(i * n + (j + 1), name);
                    }
                } else if (isChar(i, j) && isChar(i + 1, j)) {
                    // vertical gate
                    String name = "" + input.get(i).charAt(j) + input.get(i + 1).charAt(j);
                    name = name + "-" + (isOuter(i, j) ? "O" : "I");
                    if (i - 1 >= 0 && isMaze(i - 1, j)) {
                        coordMap.put(i * n + j, name);
                    } else if (i + 2 < m && isMaze(i + 2, j)) {
                        coordMap.put((i + 1) * n + j, name);
                    }
                }
            }
        }
    }

    private static void buildGraph2() {
        g = new Graph();
        for (Map.Entry<Integer, String> entry : coordMap.entrySet()) {
            Integer x = entry.getKey();
            int si = x / n, sj = x % n;
            String startNode = entry.getValue();

            int[][] visited = new int[m][n];
            for (int t = 0; t < m; ++t) {
                Arrays.fill(visited[t], -1);
            }

            Queue<Integer> queue = new LinkedList<>();
            queue.add(si * n + sj);
            visited[si][sj] = 0;

            while (!queue.isEmpty()) {
                Integer node = queue.poll();
                int i = node / n, j = node % n;

                if (coordMap.containsKey(node) && !coordMap.get(node).equals(startNode)) {
                    g.addEdge(startNode, coordMap.get(node), visited[i][j]);
                    continue;
                }


                for (int dir = 0; dir < 4; ++dir) {
                    int ni = i + di[dir], nj = j + dj[dir];
                    if (ni >= 0 && ni < m && nj >= 0 && nj < n && visited[ni][nj] == -1 &&
                        (input.get(ni).charAt(nj) != '#' && input.get(ni).charAt(nj) != ' ')) {
                        queue.add(ni * n + nj);
                        visited[ni][nj] = visited[i][j] + 1;
                    }
                }
            }
        }
    }

    private static int findShortestPath2() {
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> s.distance));
        pq.add(new State("AA-O", -1, 0));
        Set<String> visited = new HashSet<>();
        while (!pq.isEmpty()) {
            State state = pq.poll();
            if (visited.contains(state.node + "-" + state.level)) {
                continue;
            }
            visited.add(state.node + "-" + state.level);
            if (state.node.equals("ZZ-O") && state.level == -1) {
                return state.distance;
            }
            if (state.level < 0) {
                continue;
            }
            for (Edge edge: g.edges.get(state.node)) {
                if (state.level != 0 && (edge.to.startsWith("AA") || edge.to.startsWith("ZZ"))) {
                    // AA and ZZ are not gates at levels above 0
                    continue;
                }
                int deltaLevel = edge.to.endsWith("I") ? 1 : -1;
                State newState = new State(otherGate(edge.to), edge.distance + state.distance - 1, state.level + deltaLevel);
                pq.add(newState);
            }
        }
        return -1;
    }

    private static String otherGate(String gate) {
        if (gate.substring(0, 2).equals("AA") || gate.substring(0, 2).equals("ZZ")) {
            return gate;
        }
        return gate.substring(0, 3) + (gate.charAt(3) == 'O' ? 'I' : 'O');
    }
}
