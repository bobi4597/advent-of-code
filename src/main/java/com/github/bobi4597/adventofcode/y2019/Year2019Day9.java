package com.github.bobi4597.adventofcode.y2019;

import static com.github.bobi4597.adventofcode.y2019.BigIntCodeComputer.readInput;

public class Year2019Day9 {

    public static void main(String[] args) {
        String inputString = readInput();
        System.out.printf("Part 1: %s\n", solve1(inputString));
        System.out.printf("Part 2: %s\n", solve2(inputString));
    }

    private static String solve1(String inputString) {
        BigIntCodeComputer a = new BigIntCodeComputer(inputString);
        BigRunResult runResult = a.run(new int[] {1}, false);
        return runResult.output.toString();
    }

    private static String solve2(String inputString) {
        BigIntCodeComputer a = new BigIntCodeComputer(inputString);
        BigRunResult runResult = a.run(new int[] {2}, false);
        return runResult.output.toString();
    }
}
