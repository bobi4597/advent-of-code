package com.github.bobi4597.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

import static java.util.Comparator.comparingInt;

public class Year2019Day18 {

    private static char[][] c;
    private static int m, n, k;
    private static int[] dr = {-1, 0, 0, 1}, dc = {0, -1, 1, 0};
    private static Graph g;

    static class Node {
        int r, c, keys;
        Node(int r, int c, int keys) {
            this.r = r; this.c = c; this.keys = keys;
        }
    }

    public static void main(String[] args) {
        readInput();
        System.out.printf("%d\n", solve1());
        System.out.printf("%d\n", solve2());
    }

    private static void readInput() {
        List<String> input = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            input.add(scanner.nextLine());
        }
        m = input.size();
        n = input.get(0).length();
        c = new char[m][n];
        for (int i = 0; i < m; ++i) {
            c[i] = input.get(i).toCharArray();
        }
    }

    private static String toKey(Node node) {
        return toKey(node.r , node.c, node.keys);
    }
    private static String toKey(int r, int c, int keys) {
        return r + "-" + c + "-" + keys;
    }

    private static int solve1() {
        // start node
        k = 0;
        Node startNode = null;
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (c[i][j] == '@') {
                    startNode = new Node(i, j, 0);
                }
                if (c[i][j] >= 'a' && c[i][j] <= 'z') {
                    ++k;
                }
            }
        }

        Map<String, Integer> dpMap = new HashMap<>();

        Queue<Node> queue = new LinkedList<>();
        queue.add(startNode);
        dpMap.put(toKey(startNode), 0);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node.keys == ((1 << k) - 1)) {
                return dpMap.get(toKey(node));
            }

            for (int dir = 0; dir < dr.length; ++dir) {
                int nr = node.r + dr[dir], nc = node.c + dc[dir];
                if (nr >= 0 && nr < m && nc >= 0 && nc < n &&
                    (c[nr][nc] == '.' ||
                        c[nr][nc] == '@' ||
                        c[nr][nc] >= 'a' && c[nr][nc] <= 'z' ||
                        c[nr][nc] >= 'A' && c[nr][nc] <= 'Z' && ((node.keys & (1 << (c[nr][nc] - 'A'))) != 0)
                    )
                ) {
                    int nkeys = node.keys;
                    if (c[nr][nc] >= 'a' && c[nr][nc] <= 'z') {
                        nkeys = nkeys | (1 << (c[nr][nc] - 'a'));
                    }
                    String newMapKey = toKey(nr, nc, nkeys);
                    if (!dpMap.containsKey(newMapKey)) {
                        dpMap.put(newMapKey, dpMap.get(toKey(node)) + 1);
                        Node newNode =new Node(nr, nc, nkeys);
                        queue.add(newNode);
                    }
                }
            }

        }
        return -1;
    }

    //////////////////////// Part 2 ////////////////////////

    static class Edge {
        Character from, to;
        Integer distance;
        Edge (Character from, Character to, Integer distance) {
            this.from = from; this.to = to; this.distance = distance;
        }
    }

    static class Graph {
        Map<Character, List<Edge>> edges;
        Graph() {
            edges = new HashMap<>();
        }
        void addEdge(Character from, Character to, Integer distance) {
            List<Edge> e1 = edges.getOrDefault(from, new ArrayList<>());
            e1.add(new Edge(from, to, distance));
            edges.put(from, e1);
        }
    }

    /** A state containing:
     * 1) the locations of the 4 robots,
     * 2) plus the keys that are collected so far,
     * 3) as well the steps needed to reach this state.
     */
    static class State {
        Character[] nodes;
        int keys;
        int steps;

        State(Character[] nodes, int keys, int steps) {
            this.nodes = nodes; this.keys = keys; this.steps = steps;
        }
    }

    private static int solve2() {
        modifyMaze();
        buildGraph();
        return findShortestPath2();
    }

    private static void modifyMaze() {
        // find the start node
        k = 0;
        Node startNode = null;
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (c[i][j] == '@') {
                    startNode = new Node(i, j, 0);
                }
                if (c[i][j] >= 'a' && c[i][j] <= 'z') {
                    ++k;
                }
            }
        }
        if (startNode == null) {
            return;
        }
        // modify map with 4 walls around the start point
        for (int dir = 0; dir < 4; ++dir) {
            c[startNode.r + dr[dir]][startNode.c + dc[dir]] = '#';
        }
        // modify the 4 cell diagonally from the start point
        char cc = '0';
        for (int i = -1; i < 2; i = i + 2) {
            for (int j = -1; j < 2; j = j + 2) {
                c[startNode.r + i][startNode.c + j] = cc;
                ++cc;
            }
        }
    }

    /**
     * Builds a graph from the input maze.
     * Each key/door in the maze is a node in the graph.
     * The distances between the keys/doors are the lengths of the edges in the graph.
     */
    private static void buildGraph() {
        g = new Graph();
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (c[i][j] != '.' && c[i][j] != '#') {
                    // do BFS from this node
                    Character from = c[i][j];
                    int[][] visited = new int[m][n];
                    for (int t = 0; t < m; ++t) {
                        Arrays.fill(visited[t], -1);
                    }

                    Queue<Integer> queue = new LinkedList<>();
                    queue.add(i * n + j);
                    visited[i][j] = 0;
                    while (!queue.isEmpty()) {
                        Integer x = queue.poll();
                        int ii = x / n, jj = x % n;
                        if (visited[ii][jj] > 0 && c[ii][jj] != '.') {
                            // found a key/door. we want to stop here
                            g.addEdge(from, c[ii][jj], visited[ii][jj]);
                        } else {
                            for (int dir = 0; dir < 4; ++dir) {
                                int ni = ii + dr[dir], nj = jj + dc[dir];
                                if (ni >= 0 && ni < m && nj >= 0 && nj < n && c[ni][nj] != '#' && visited[ni][nj] == -1) {
                                    visited[ni][nj] = visited[ii][jj] + 1;
                                    queue.add(ni * n + nj);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static int findShortestPath2() {
        Map<String, Integer> dp2Map = new HashMap<>();
        // Now the start position is the position of the 4 robots (diagonally to the initial start position)
        State startState = new State(
            new Character[] {'0', '1', '2', '3'},
            0,
            0);

        PriorityQueue<State> pq = new PriorityQueue<>(comparingInt(s -> s.steps));
        pq.add(startState);
        dp2Map.put(toKey22(startState), 0);

        int count = 0;

        while (!pq.isEmpty()) {
            ++count;
            State state = pq.poll();
            if (state.keys == ((1 << k) - 1)) {
                return state.steps;
            }

            for (int robot = 0; robot < 4; ++robot) {
                Character nc = state.nodes[robot];

                List<Edge> edges = g.edges.get(nc);
                for (Edge edge: edges) {
                    if (edge.to >= '0' && edge.to <= '3' ||
                        edge.to >= 'a' && edge.to <= 'z' ||
                        edge.to >= 'A' && edge.to <= 'Z' && ((state.keys & (1 << (edge.to - 'A'))) != 0)

                    ) {
                        int newKeys = state.keys;
                        if (edge.to >= 'a' && edge.to <= 'z') {
                            newKeys = newKeys | (1 << (edge.to - 'a'));
                        }
                        Character[] newNodes = new Character[4];
                        System.arraycopy(state.nodes, 0, newNodes, 0, 4);
                        newNodes[robot] = edge.to;

                        String newMapKey = toKey22(newNodes, newKeys);
                        if (!dp2Map.containsKey(newMapKey)) {
                            int newSteps = state.steps + edge.distance;
                            dp2Map.put(newMapKey, newSteps);
                            State newState = new State(newNodes, newKeys, newSteps);
                            pq.add(newState);
                        }
                    }
                }
            }

        }
        return -1;
    }

    private static String toKey22(State state) {
        return toKey22(state.nodes, state.keys);
    }

    private static String toKey22(Character[] nodes, int keys) {
        StringBuilder sb = new StringBuilder();
        for (Character node: nodes) {
            sb.append(node);
        }
        sb.append("-");
        sb.append(keys);
        return sb.toString();
    }
}

