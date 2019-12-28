package com.github.bobi4597.adventofcode;

import java.util.Arrays;

import static com.github.bobi4597.adventofcode.BigIntCodeComputer.readInput;

public class Year2019Day7 {

    public static void main(String[] args) {
        String inputString = readInput();
        System.out.printf("Part 1: %s\n", solve1(inputString));
        System.out.printf("Part 2: %s\n", solve2(inputString));
    }

    private static int solve1(String inputString) {
        int[] p = new int[] {0, 1, 2, 3, 4};
        int bestSignal = Integer.MIN_VALUE;
        while (true) {
            int inputSignal = 0;
            for (int i = 0; i < 5; ++i) {
                BigIntCodeComputer a = new BigIntCodeComputer(inputString);
                BigRunResult runResult = a.run(new int[] {p[i], inputSignal}, true);
                inputSignal = runResult.output.get(0).intValue();
            }
            bestSignal = Math.max(bestSignal, inputSignal);
            if (!nextPermutation(p)) {
                break;
            }
        }
        return bestSignal;
    }

    private static int solve2(String inputString) {
        int[] p = new int[] {5, 6, 7, 8, 9};
        int bestSignal = Integer.MIN_VALUE;
        while (true) {
            BigIntCodeComputer[] a = new BigIntCodeComputer[5];
            for (int i = 0; i < 5; ++i) {
                a[i] = new BigIntCodeComputer(inputString);
            }
            int inputSignal = 0;
            int i = 0;
            while (true) {
                int[] input = i < 5 ? new int[] {p[i % 5], inputSignal} : new int[] {inputSignal};
                BigRunResult runResult = a[i % 5].run(input, true);
                if (runResult.isDone) {
                    break;
                }
                ++i;
                inputSignal = runResult.output.get(0).intValue();
            }
            bestSignal = Math.max(bestSignal, inputSignal);
            if (!nextPermutation(p)) {
                break;
            }
        }
        return bestSignal;
    }

    private static boolean nextPermutation(int[] a) {
        // 1. start from the last index and find the first index where the numbers do not increase anymore
        int i = a.length - 1;
        while (i > 0 && a[i-1] > a[i]) {
            --i;
        }
        --i;
        if (i < 0) {
            return false;
        }

        // 2. then replace it with the next bigger number which is on the right side
        int j = i + 1;
        int nextBigger = Integer.MAX_VALUE;
        int nextIndex = a.length;
        while (j < a.length) {
            if (a[j] > a[i] && nextBigger > a[j]) {
                nextBigger = a[j];
                nextIndex = j;
            }
            if (nextBigger == a[i] + 1) {
                break;
            }
            ++j;
        }
        swap(a, i, nextIndex);

        // 3. then sort all the numbers after index i
        Arrays.sort(a, i + 1, a.length);
        return true;
    }

    private static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
}
