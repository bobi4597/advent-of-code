package com.github.bobi4597.adventofcode;

import static com.github.bobi4597.adventofcode.BigIntCodeComputer.readInput;

public class Year2019Day9 {

    public static void main(String[] args) {
        String inputString = readInput();
        System.out.printf("Part 1: %s\n", solve1(inputString));
    }

    private static String solve1(String inputString) {
        BigIntCodeComputer a = new BigIntCodeComputer(inputString);
        BigRunResult runResult = a.run(new int[] {1});
        return runResult.output.toString();
    }
}
