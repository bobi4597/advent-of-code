package com.github.bobi4597.adventofcode;

import java.util.Scanner;

public class Year2019Day16 {

    private static final int[] PATTERN = {0, 1, 0, -1};
    private static int[] a, b;
    private static int n;

    public static void main(String[] args) {
        readInput();
        System.out.printf("%s\n", solve1());
    }

    private static void readInput() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        n = input.length();
        a = new int[n];
        b = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = input.charAt(i) - '0';
        }
    }

    /////////////// part 1 ///////////////////
    private static String solve1() {
        for (int phase = 1; phase <= 100; ++phase) {
            for (int calculatedPosition = 0; calculatedPosition < n; ++calculatedPosition) {
                int x = 0;
                for (int index = 0; index < n; ++index) {
                    int patternIndex = getPatternIndex(calculatedPosition + 1, index);
                    x += a[index] * PATTERN[patternIndex];
                }
                b[calculatedPosition] = Math.abs(x) % 10;
            }
        }
        String output = "";
        for (int position = 0; position < 8; ++position) {
            output += "" + a[position];
        }
        return output;
    }

    private static int getPatternIndex(int calculatedPosition, int index) {
        // example: calculatedPosition = 8
        // 0 0 0 0 0 0 0  0 0 0 1 1 1 1 1  1 1 1 1 1 2 2 2  2 2 2 2 2 2 2 3
        // 0 1 2 3 4 5 6  7 8 9 0 1 2 3 4  5 6 7 8 9 0 1 2  3 4 5 6 7 8 9 0
        // ------------------------------------------------------------------
        // 0 0 0 0 0 0 0  1 1 1 1 1 1 1 1  2 2 2 2 2 2 2 2  3 3 3 3 3 3 3 3   0 0 0 0 0 0 0 0  1 1 1 1 1 1 1 1 ...

        //  0 -  6: --> 0
        //  7 - 14: --> 1
        // 15 - 22: --> 2
        // 23 - 30: --> 3

        if (index < calculatedPosition * PATTERN.length - 1) {
            return (index + 1) / calculatedPosition;
        } else {
            index = (index - (calculatedPosition * PATTERN.length - 1)) % (calculatedPosition * PATTERN.length);
            return index / calculatedPosition;
        }
    }


}
