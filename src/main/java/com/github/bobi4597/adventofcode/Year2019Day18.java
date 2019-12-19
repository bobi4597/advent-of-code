package com.github.bobi4597.adventofcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class Year2019Day18 {

    private static char[][] c;
    private static int m, n, k;
    private static int[] dr = {-1, 0, 0, 1}, dc = {0, -1, 1, 0};
    private static Map<String, Integer> dpMap;
    private static Map<String, Integer> dp2Map;

    static class Node {
        int r, c, keys;
        Node(int r, int c, int keys) {
            this.r = r; this.c = c; this.keys = keys;
        }
    }

    static class Node2 {
        int[] r, c;
        int keys;
        int steps;
        Node2(int[] r, int[] c, int keys, int steps) {
            this.r = r; this.c = c; this.keys = keys; this.steps = steps;
        }
    }

    public static void main(String[] args) {
        readInput();
        //System.out.printf("%d\n", solve1());
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

        dpMap = new HashMap<>();

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

    // TODO: This solution is too slow for the second case. Needs optimization...
    private static int solve2() {
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
        if (startNode == null) {
            return -1;
        }
        // modify map with 4 walls around the start point
        for (int dir = 0; dir < 4; ++dir) {
            c[startNode.r + dr[dir]][startNode.c + dc[dir]] = '#';
        }

        dp2Map = new HashMap<>();

        // Now the start position is the position of the 4 robots (diagonally to the initial start position)
        Node2 startNode2 = new Node2(
            new int[] {startNode.r - 1, startNode.r - 1, startNode.r + 1, startNode.r + 1},
            new int[] {startNode.c - 1, startNode.c + 1, startNode.c - 1, startNode.c + 1},
            0,
            0);
        Queue<Node2> queue = new LinkedList<>();
        queue.add(startNode2);

        dp2Map.put(toKey2(startNode2), 0);

        int count = 0;

        while (!queue.isEmpty()) {
            ++count;
            Node2 node = queue.poll();
            if (count % 10000 == 0) {
                System.out.printf("steps: %d\n", node.steps);
            }
            if (node.keys == ((1 << k) - 1)) {
                //return dp2Map.get(toKey2(node));
                return node.steps;
            }

            for (int robot = 0; robot < 4; ++robot) {
                for (int dir = 0; dir < dr.length; ++dir) {
                    int nr = node.r[robot] + dr[dir], nc = node.c[robot] + dc[dir];
                    if (nr >= 0 && nr < m && nc >= 0 && nc < n &&
                        (c[nr][nc] == '.' ||
                            c[nr][nc] == '@' ||
                            c[nr][nc] >= 'a' && c[nr][nc] <= 'z' ||
                            c[nr][nc] >= 'A' && c[nr][nc] <= 'Z' && ((node.keys & (1 << (c[nr][nc] - 'A'))) != 0)
                        )
                    ) {
                        int newKeys = node.keys;
                        if (c[nr][nc] >= 'a' && c[nr][nc] <= 'z') {
                            newKeys = newKeys | (1 << (c[nr][nc] - 'a'));
                        }
                        int[] newRows = toNewCoord(node.r, robot, nr);
                        int[] newCols = toNewCoord(node.c, robot, nc);

                        String newMapKey = toKey2(newRows, newCols, newKeys);
                        if (!dp2Map.containsKey(newMapKey)) {
                            int newSteps = node.steps + 1;
                            dp2Map.put(newMapKey, newSteps);
                            Node2 newNode = new Node2(newRows, newCols, newKeys, newSteps);
                            queue.add(newNode);
                        }
                    }
                }
            }

        }
        return -1;
    }

    private static int[] toNewCoord(int[] a, int robot, int newValue) {
        int[] newA = new int[4];
        System.arraycopy(a, 0, newA, 0, 4);
        newA[robot] = newValue;
        return newA;
    }

    private static String toKey2(int[] rows, int[] cols, int keys) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; ++i) {
            sb.append(rows[i] + "-" + cols[i] + ":");
        }
        sb.append(keys);
        return sb.toString();
    }

    private static String toKey2(Node2 node2) {
        return toKey2(node2.r, node2.c, node2.keys);
    }

}

