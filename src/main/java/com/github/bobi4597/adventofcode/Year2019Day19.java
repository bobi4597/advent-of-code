package com.github.bobi4597.adventofcode;

import static com.github.bobi4597.adventofcode.BigIntCodeComputer.readInput;

public class Year2019Day19 {

    public static void main(String[] args) {
        String inputString = readInput();
        System.out.printf("Part 1: %d\n", solve1(inputString));

    }

    private static int solve1(String inputString) {
        int sum = 0;
        for (int i = 0; i < 50; ++i) {
            for (int j = 0; j < 50; ++j) {
                // note: the program has to be re-initialized before every call
                BigIntCodeComputer a = new BigIntCodeComputer(inputString);
                BigRunResult runResult = a.run(new int[] {j, i}, false);
                int out = runResult.output.get(0).intValue();
                System.out.print(out == 1 ? '#' : ' ');
                sum += out;
            }
            System.out.println();
        }

        return sum;
    }
}