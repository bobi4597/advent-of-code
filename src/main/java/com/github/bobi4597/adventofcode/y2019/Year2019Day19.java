package com.github.bobi4597.adventofcode.y2019;

import static com.github.bobi4597.adventofcode.y2019.BigIntCodeComputer.readInput;

public class Year2019Day19 {

    public static void main(String[] args) {
        String inputString = readInput();
        System.out.printf("Part 1: %d\n", solve1(inputString));
        System.out.printf("Part 2: %d\n", solve2(inputString));

    }

    private static int solve1(String inputString) {
        int sum = 0;
        for (int i = 0; i < 50; ++i) {
            for (int j = 0; j < 50; ++j) {
                // note: the program has to be re-initialized before every call
                BigIntCodeComputer a = new BigIntCodeComputer(inputString);
                BigRunResult runResult = a.run(new int[] {j, i}, true);
                int out = runResult.output.get(0).intValue();
                System.out.print(out == 1 ? '#' : ' ');
                sum += out;
            }
            System.out.println();
        }

        return sum;
    }

    private static int solve2(String inputString) {
        int sum = 0;
        int[][] b = new int[1001][2]; // first and last index (x) of the bean on that row (y)
        int minX = 421, maxX = 571;
        for (int y = 200; y <= 1000; ++y) {
            boolean isInBeam = false;
            int x = minX;
            while (true) {
                // note: the program has to be re-initialized before every call
                BigIntCodeComputer a = new BigIntCodeComputer(inputString);
                BigRunResult runResult = a.run(new int[] {x, y}, true);
                int out = runResult.output.get(0).intValue();
                if (!isInBeam && out == 1) {
                    isInBeam = true;
                    b[y][0] = x;
                    minX = x;
                    x = maxX;
                } else if (isInBeam && out == 0) {
                    b[y][1] = x - 1;
                    maxX = x - 1;
                    break;
                } else {
                    ++x;
                }
            }
        }

        for (int endY = 509; endY <= 1000; ++endY) {
            int startX = b[endY][0];
            int startY = endY - 99;
            int endX = b[startY][1];
            if (endX >= startX + 99) {
                return startX * 10000 + startY;
            }
        }
        return -1;
    }
}
