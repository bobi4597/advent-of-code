package com.github.bobi4597.adventofcode;

import java.util.HashMap;
import java.util.Map;

import static com.github.bobi4597.adventofcode.BigIntCodeComputer.readInput;

public class Year2019Day15 {

    private static final int NORTH = 1, SOUTH = 2, WEST = 3, EAST = 4;
    private static final int WALL = 0, EMPTY = 1, OXYGEN = 2;

    private static int[] dx = {0, 0, 0, -1, 1};
    private static int[] dy = {0, 1, -1, 0, 0};

    private static int[] reverse = {0, SOUTH, NORTH, EAST, WEST};

    public static void main(String[] args) {
        String inputString = readInput();
        System.out.printf("Part 1: %d\n", solve1(inputString));
    }

    /** Wrapper to hold state whether the oxygen is found */
    static class Oxygen {
        int distance;
        Oxygen() {
            distance = -1;
        }
    }

    private static int solve1(String inputString) {
        BigIntCodeComputer a = new BigIntCodeComputer(inputString);
        Map<String, Integer> map = new HashMap<>();
        Oxygen oxygen = new Oxygen();
        dfs(a, 0, 0, 0, map, oxygen);
        return oxygen.distance;
    }

    private static void dfs(BigIntCodeComputer a, int x, int y, int distance, Map<String, Integer> map, Oxygen oxygen) {
        if (oxygen.distance != -1) {
            return;
        }

        map.put(toKey(x, y), distance);
        for (int dir = 1; dir <= 4; ++dir) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if (!map.containsKey(toKey(nx, ny))) {
                int moveResult = a.run(new int[]{dir}, true).output.get(0).intValue();
                if (moveResult == WALL) {
                    // do nothing (the droid is still at the same position)
                } else if (moveResult == EMPTY) {
                    // the droid has move to the new position (nx, ny). Explore this position...
                    dfs(a, nx, ny, distance + 1, map, oxygen);
                    // ... and move back
                    a.run(new int[] {reverse[dir]}, true);
                } else if (moveResult == OXYGEN) {
                    oxygen.distance = distance + 1;
                }
            }
        }
    }

    private static String toKey(int x, int y) {
        return x + ":" + y;
    }
}
