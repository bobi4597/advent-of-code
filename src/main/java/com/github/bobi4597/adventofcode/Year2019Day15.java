package com.github.bobi4597.adventofcode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static com.github.bobi4597.adventofcode.BigIntCodeComputer.readInput;

public class Year2019Day15 {

    private static final int NORTH = 1, SOUTH = 2, WEST = 3, EAST = 4;
    private static final int WALL = 0, EMPTY = 1, OXYGEN = 2;

    private static int[] dx = {0, 0, 0, -1, 1};
    private static int[] dy = {0, 1, -1, 0, 0};

    private static int[] reverse = {0, SOUTH, NORTH, EAST, WEST};

    public static void main(String[] args) {
        String inputString = readInput();
        Map<String, Integer> map = new HashMap<>();
        Oxygen oxygen = new Oxygen();
        System.out.printf("Part 1: %d\n", solve1(inputString, map, oxygen));
        printMap(map, oxygen);
        Map<String, Integer> visited = new HashMap<>();
        System.out.printf("Part 2: %d\n", solve2(map, oxygen, visited));
        printVisitedMap(visited);
    }

    /** Wrapper to hold state whether the oxygen is found */
    static class Oxygen {
        int x, y, distance;
        Oxygen() {
            distance = -1;
            x = 0;
            y = 0;
        }
    }

    static class State {
        int x, y, distance;
        State(int x, int y, int distance) {
            this.x = x; this.y = y; this.distance = distance;
        }
    }

    private static int solve2(Map<String, Integer> map, Oxygen oxygen, Map<String, Integer> visited) {
        Queue<State> queue = new LinkedList<>();
        queue.add(new State(oxygen.x, oxygen.y, 0));
        int maxDistance = 0;

        visited.put(toKey(oxygen.x, oxygen.y), 0);

        while (!queue.isEmpty()) {
            State state = queue.poll();
            maxDistance = Math.max(maxDistance, state.distance);
            String key = toKey(state.x, state.y);

            for (int dir = 1; dir <= 4; ++dir) {
                int nx = state.x + dx[dir];
                int ny = state.y + dy[dir];
                String newKey = toKey(nx, ny);
                if (map.containsKey(newKey) && map.get(newKey) != -1 && !visited.containsKey(newKey)) {
                    queue.add(new State(nx, ny, state.distance + 1));
                    visited.put(newKey, state.distance + 1);
                }
            }
        }
        return maxDistance;
    }

    private static int solve1(String inputString, Map<String, Integer> map, Oxygen oxygen) {
        BigIntCodeComputer a = new BigIntCodeComputer(inputString);
        dfs(a, 0, 0, 0, map, oxygen);
        return oxygen.distance;
    }

    private static void dfs(BigIntCodeComputer a, int x, int y, int distance, Map<String, Integer> map, Oxygen oxygen) {
        map.put(toKey(x, y), distance);
        for (int dir = 1; dir <= 4; ++dir) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if (!map.containsKey(toKey(nx, ny))) {
                int moveResult = a.run(new int[] {dir}, true).output.get(0).intValue();
                if (moveResult == WALL) {
                    // do nothing (the droid is still at the same position). just mark the place as wall (with -1)
                    map.put(toKey(nx, ny), -1);
                } else if (moveResult == EMPTY) {
                    // the droid has move to the new position (nx, ny). Explore this position...
                    dfs(a, nx, ny, distance + 1, map, oxygen);
                    // ... and move back
                    a.run(new int[] {reverse[dir]}, true);
                } else if (moveResult == OXYGEN) {
                    oxygen.distance = distance + 1;
                    oxygen.x = nx;
                    oxygen.y = ny;
                    dfs(a, nx, ny, distance + 1, map, oxygen);
                    a.run(new int[] {reverse[dir]}, true);
                }
            }
        }
    }

    private static String toKey(int x, int y) {
        return x + ":" + y;
    }

    private static void printMap(Map<String, Integer> map, Oxygen oxygen) {
        int max = 25;
        for (int y = -7; y <= 22; ++y) {
            for (int x = -22; x <= 12; ++x) {
                if (x == oxygen.x && y == oxygen.y) {
                    System.out.printf("%s", "o");
                } else {
                    int val = map.getOrDefault(toKey(x, y), -2);
                    System.out.printf("%s", val == 0 ? "s" : (val == -1 ? "#" : " "));
                }
            }
            System.out.println();
        }
    }

    private static void printVisitedMap(Map<String, Integer> map) {
        int max = 25;
        for (int y = -19; y <= 21; ++y) {
            for (int x = -21; x <= 19; ++x) {
                int val = map.getOrDefault(toKey(x, y), -1);
                if (val == -1) System.out.print("#####");
                if (val == 0) System.out.print("     ");
                if (val > 0) System.out.printf(" %3d ", val);
            }
            System.out.println();
        }
    }
}
