package com.github.bobi4597.adventofcode.y2019;

import java.util.Scanner;

/**
 *  https://adventofcode.com/2019/day/16
 */
public class Year2019Day16 {

    private static final int[] PATTERN = {0, 1, 0, -1};
    private static int[] a, b;
    private static int n, offset;
    private static String input;
    private static final int REPEAT2 = 10_000;
    private static int N2;

    public static void main(String[] args) {
        readInput();
        System.out.printf("%s\n", solve1());
        System.out.printf("%s\n", solve2());
    }

    private static void readInput() {
        Scanner scanner = new Scanner(System.in);
        input = scanner.nextLine();
        n = input.length();
        a = new int[n];
        b = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = input.charAt(i) - '0';
        }

        // part 2 //
        N2 = input.length() * REPEAT2;
        offset = Integer.parseInt(input.substring(0, 7));
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

    /////////////////////// part 2 ////////////////////////
//    15: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
//    16: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
//    17: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
//    18: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
//    19: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
//    20: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
//    21: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1]
//    22: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1]
//    23: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1]
//    24: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1]
//    25: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1]
//    26: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1]
//    27: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1]
//    28: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1]
//    29: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1]
    /**
     * Calculate only the digits that are after the offset.
     * The pattern for the digits after the offset is as above (only 1s).
     */
    private static String solve2() {

        a = new int[N2 - offset];
        b = new int[N2 - offset];

        for (int i = offset; i < N2; ++i) {
            a[i - offset] = input.charAt(i % input.length()) - '0';
        }

        for (int step = 0; step < 100; ++step) {
            for (int i = N2 - 1; i >= offset; --i) {
                b[i - offset] = ((i + 1 < N2 ? b[i - offset + 1] : 0) + a[i - offset]) % 10;

            }
            System.arraycopy(b, 0, a, 0, N2 - offset);
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 8; ++i) {
            result.append(a[i]);
        }
        return result.toString();
    }
}
