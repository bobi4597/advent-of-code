package com.github.bobi4597.adventofcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Year2019Day24 {
    static int N = 5;
    static int startPosition;
    private static int[] di = {-1, 0, 0, 1};
    private static int[] dj = {0, -1, 1, 0};

    public static void main(String[] args) {
        readInput();
        System.out.printf("%d\n", solve1());
    }

    private static void readInput() {
        startPosition = 0;
        int pow = 1;
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < 5; ++i) {
            String line = scanner.nextLine();
            for (int j = 0; j < 5; ++j) {
                if (line.charAt(j) == '#') {
                    startPosition += pow;
                }
                pow *= 2;
            }
        }
        System.out.println(startPosition);
    }

    //////////////// part 1 /////////////////
    private static int solve1() {
        Map<Integer, Integer> map = new HashMap<>();
        int x = startPosition;
        int index = 0;
        while (!map.containsKey(x)) {
            map.put(x, index++);
            x = next(x);
            //print(x);
        }
        return x;
    }

    private static int next(int x) {
        int y = 0;

        for (int i1 = 0; i1 < N; ++i1) for (int j1 = 0; j1 < N; ++j1) {
            int index1 = i1 * N + j1;
            int x1 = x & (1 << index1);
            int bugs = 0;
            for (int d = 0; d < 4; ++d) {
                int i2 = i1 + di[d];
                int j2 = j1 + dj[d];
                if (i2 >= 0 && i2 < N && j2 >= 0 && j2 < N) {
                    int index2 = i2 * N + j2;
                    int x2 = x & (1 << index2);
                    if (x2 != 0) {
                        ++bugs;
                    }
                }
            }

            if (x1 != 0) {
                if (bugs == 1) {
                    // bug -> bug: 1 bug around the bug
                    y = y | (1 << index1);
                } else {
                    // bug -> empty: 0, 2, 3 bugs around the bug
                    y = y & (~ (1 << index1));
                }
            } else {
                if (bugs == 1 || bugs == 2) {
                    // empty -> bug: 1 or 2 bugs around the empty
                    y = y | (1 << index1);
                } else {
                    // empty -> empty: 0 or 3 bugs around the empty
                    y = y & (~(1 << index1));
                }
            }
        }
        return y;
    }

    private static void print(int x) {
        for (int i = 0; i < 25; ++i) {
            System.out.printf("%s", (x & (1 << i)) == 0 ? "." : "#");
            if (i % 5 == 4) {
                System.out.println();
            }
        }
        System.out.println();
    }
}
