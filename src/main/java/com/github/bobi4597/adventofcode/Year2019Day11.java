package com.github.bobi4597.adventofcode;

import java.util.HashMap;
import java.util.Map;

import static com.github.bobi4597.adventofcode.BigIntCodeComputer.readInput;

public class Year2019Day11 {

    //                         up right down left
    private static int[] dx = { 0,  1,   0,  -1};
    private static int[] dy = { 1,  0,  -1,   0};
    private static int BLACK = 0, WHITE = 1, LEFT = 0, RIGHT = 1;

    public static void main(String[] args) {
        String inputString = readInput();
        System.out.printf("Part 1: %d\n", solve1(inputString));
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

    private static String toKey(int x, int y) {
        return x + ":" + y;
    }

}
