package com.github.bobi4597.adventofcode.y2019;

import java.util.HashMap;
import java.util.Map;

import static com.github.bobi4597.adventofcode.y2019.BigIntCodeComputer.readInput;

public class Year2019Day11 {

    //                         up right down left
    private static int[] dx = { 0,  1,   0,  -1};
    private static int[] dy = { 1,  0,  -1,   0};
    private static int BLACK = 0, WHITE = 1, LEFT = 0, RIGHT = 1;

    public static void main(String[] args) {
        String inputString = readInput();
        System.out.printf("Part 1: %d\n", solve1(inputString));
        System.out.printf("Part 2:\n%s\n", solve2(inputString));
        // My input and output for part 2:
        // 3,8,1005,8,309,1106,0,11,0,0,0,104,1,104,0,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,1,10,4,10,1001,8,0,29,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,0,10,4,10,102,1,8,51,3,8,102,-1,8,10,1001,10,1,10,4,10,108,0,8,10,4,10,1002,8,1,72,1,1104,8,10,2,1105,15,10,2,1106,0,10,3,8,1002,8,-1,10,1001,10,1,10,4,10,1008,8,1,10,4,10,101,0,8,107,3,8,102,-1,8,10,1001,10,1,10,4,10,108,1,8,10,4,10,101,0,8,128,2,6,8,10,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,0,10,4,10,102,1,8,155,1006,0,96,2,108,10,10,1,101,4,10,3,8,1002,8,-1,10,101,1,10,10,4,10,1008,8,0,10,4,10,1002,8,1,188,2,1,5,10,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,0,10,4,10,102,1,8,214,2,6,18,10,1006,0,78,1,105,1,10,3,8,1002,8,-1,10,1001,10,1,10,4,10,1008,8,1,10,4,10,102,1,8,247,2,103,8,10,2,1002,10,10,2,106,17,10,1,1006,15,10,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,1,10,4,10,101,0,8,285,1,1101,18,10,101,1,9,9,1007,9,992,10,1005,10,15,99,109,631,104,0,104,1,21102,387507921664,1,1,21102,1,326,0,1106,0,430,21102,932826591260,1,1,21102,337,1,0,1106,0,430,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,21101,206400850983,0,1,21101,0,384,0,1105,1,430,21102,3224464603,1,1,21102,395,1,0,1106,0,430,3,10,104,0,104,0,3,10,104,0,104,0,21102,838433657700,1,1,21102,418,1,0,1106,0,430,21101,825012007272,0,1,21101,429,0,0,1106,0,430,99,109,2,21202,-1,1,1,21101,40,0,2,21101,461,0,3,21102,1,451,0,1105,1,494,109,-2,2105,1,0,0,1,0,0,1,109,2,3,10,204,-1,1001,456,457,472,4,0,1001,456,1,456,108,4,456,10,1006,10,488,1102,1,0,456,109,-2,2106,0,0,0,109,4,1202,-1,1,493,1207,-3,0,10,1006,10,511,21101,0,0,-3,21202,-3,1,1,21201,-2,0,2,21102,1,1,3,21102,1,530,0,1106,0,535,109,-4,2106,0,0,109,5,1207,-3,1,10,1006,10,558,2207,-4,-2,10,1006,10,558,22101,0,-4,-4,1106,0,626,22102,1,-4,1,21201,-3,-1,2,21202,-2,2,3,21101,0,577,0,1106,0,535,22102,1,1,-4,21101,1,0,-1,2207,-4,-2,10,1006,10,596,21102,0,1,-1,22202,-2,-1,-2,2107,0,-3,10,1006,10,618,21201,-1,0,1,21102,618,1,0,105,1,493,21202,-2,-1,-2,22201,-4,-2,-4,109,-5,2105,1,0
        // ABCLFUHJ
    }


    private static int solve1(String inputString) {
        Map<String, Integer> map = new HashMap<>();
        int x = 0, y = 0, dir = 0;

        BigIntCodeComputer a = new BigIntCodeComputer(inputString);

        while (true) {
            int currentColor = map.getOrDefault(toKey(x, y), BLACK);
            BigRunResult colorResult = a.run(new int[] {currentColor}, true);
            if (colorResult.isDone) {
                break;
            }
            BigRunResult turnResult = a.run(new int[0], true);
            int newColor = colorResult.output.get(0).intValue();
            int newTurn = turnResult.output.get(0).intValue();
            map.put(toKey(x, y), newColor);
            dir = newTurn == LEFT ? dir - 1 : dir + 1;
            dir = (dir + 4) % 4;
            x += dx[dir];
            y += dy[dir];
        }
        return map.size();
    }

    private static String solve2(String inputString) {
        Map<String, Integer> map = new HashMap<>();
        int x = 0, y = 0, dir = 0;

        BigIntCodeComputer a = new BigIntCodeComputer(inputString);

        while (true) {
            int currentColor = map.getOrDefault(toKey(x, y), x == 0 && y == 0 ? WHITE : BLACK);
            BigRunResult colorResult = a.run(new int[]{currentColor}, true);
            if (colorResult.isDone) {
                break;
            }
            BigRunResult turnResult = a.run(new int[0], true);
            int newColor = colorResult.output.get(0).intValue();
            int newTurn = turnResult.output.get(0).intValue();
            map.put(toKey(x, y), newColor);
            dir = newTurn == LEFT ? dir - 1 : dir + 1;
            dir = (dir + 4) % 4;
            x += dx[dir];
            y += dy[dir];
        }

        int minX = 0, minY = 0, maxX = 0, maxY = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String[] xyStrings = entry.getKey().split(":");
            int xx = Integer.parseInt(xyStrings[0]);
            int yy = Integer.parseInt(xyStrings[1]);
            minX = Math.min(minX, xx);
            minY = Math.min(minY, yy);
            maxX = Math.max(maxX, xx);
            maxY = Math.max(maxY, yy);
        }

        int[][] ship = new int[maxY - minY + 1][maxX - minX + 1];
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String[] xyStrings = entry.getKey().split(":");
            int xx = Integer.parseInt(xyStrings[0]) - minX;
            int yy = Integer.parseInt(xyStrings[1]) - minY;
            ship[yy][xx] = entry.getValue();
        }

        return printShip(ship);
    }

    private static String printShip(int[][] a) {
        StringBuilder sb = new StringBuilder();
        for (int y = a.length - 1; y >= 0; --y) {
            for (int x = 0; x < a[y].length; ++x) {
                sb.append(a[y][x] == 1 ? "*" : " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static String toKey(int x, int y) {
        return x + ":" + y;
    }

}
