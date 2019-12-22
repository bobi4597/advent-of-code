package com.github.bobi4597.adventofcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Year2019Day22 {

    private static final int N = 10007;
    private static final int RESULT1 = 2019;
    private static List<String> input;
    private static int[] a = new int[N];

//    private static final long N2 = 10007L;
//    private static final long SHUFFLES = 1_000_000L;
//    private static final int RESULT2 = 2019;
//    private static long currentPosition = 0L;
//    private static Map<Long, Long> positionMap = new HashMap<>();
//    private static Map<Long, Long> reverseMap = new HashMap<>();

    private static final long N2 = 119_315_717_514_047L;
//    private static final long SHUFFLES = 101_741_582_076_661L;
    private static final long SHUFFLES = 1L;
    private static final int RESULT2 = 2020;
    private static long currentPosition = 0L;
    private static Map<Long, Long> positionMap = new HashMap<>();
    private static Map<Long, Long> reverseMap = new HashMap<>();

    public static void main(String[] args) {
        init();
        readInput();
        System.out.printf("Part 1: %d\n", solve1());
        System.out.printf("Part 2: %d\n", solve2());
    }

    private static void init() {
        for (int i = 0; i < N; ++i) {
            a[i] = i;
        }
    }

    private static void readInput() {
        input = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            input.add(scanner.nextLine());
        }
    }

    private static int solve1() {
        for (String s: input) {
            if (s.equals("deal into new stack")) {
                dealIntoNewStack();
            } else if (s.startsWith("deal with increment")) {
                dealWithIncrement(Integer.parseInt(s.substring(20)));
            } else {
                cut(Integer.parseInt(s.substring(4)));
            }
        }
        for (int i = 0; i < N; ++i) {
            if (a[i] == RESULT1) {
                return i;
            }
        }
        return -1;
    }

    private static void dealIntoNewStack() {
        reverse(0, N - 1);
    }

    private static void reverse(int start, int end) {
        int i = start, j = end;
        while (i < j) {
            // swap
            int tmp = a[i]; a[i] = a[j]; a[j] = tmp;
            ++i; --j;
        }
    }

    private static void dealWithIncrement(int step) {
        int[] b = new int[N];
        int bi = 0;
        for (int ai = 0; ai < N; ++ai) {
            b[bi] = a[ai];
            bi = (bi + step) % N;
        }
        System.arraycopy(b, 0, a, 0, N);
    }

    private static void cut(int cutSize) {
        int cutPoint = cutSize > 0 ? cutSize: N + cutSize;
        reverse(0, cutPoint - 1);
        reverse(cutPoint, N - 1);
        reverse(0, N - 1);
    }

    /////////////////////  Part 2  /////////////////////////

    private static long solve2() {
        currentPosition = RESULT2;
        positionMap.put(currentPosition, 0L);
        reverseMap.put(0L, currentPosition);
        long shuffle = 0;
        while (shuffle < SHUFFLES) {

            for (String s: input) {
                if (s.equals("deal into new stack")) {
                    dealIntoNewStack2();
                } else if (s.startsWith("deal with increment")) {
                    dealWithIncrement2(Integer.parseInt(s.substring(20)));
                } else {
                    cut2(Integer.parseInt(s.substring(4)));
                }
            }
            ++shuffle;
            System.out.printf("%d\n", currentPosition);
            if (positionMap.containsKey(currentPosition)) {
                long remainder = SHUFFLES % shuffle;
                return reverseMap.get(remainder);
            } else {
                positionMap.put(currentPosition, shuffle);
                reverseMap.put(shuffle, currentPosition);
            }
        }
        return currentPosition;
    }

    private static void dealIntoNewStack2() {
        reverse2(0, N2 - 1);
    }

    private static void reverse2(long start, long end) {
        if (currentPosition >= start && currentPosition <= end) {
            currentPosition = end - (currentPosition - start);
        }
    }

    private static void dealWithIncrement2(long step) {
        long newPosition = (step * currentPosition) % N2;
        currentPosition = newPosition;
    }

    private static void cut2(long cutSize) {
        long cutPoint = cutSize > 0 ? cutSize : N2 + cutSize;
        reverse2(0, cutPoint - 1);
        reverse2(cutPoint, N - 1);
        reverse2(0, N - 1);
    }
}
