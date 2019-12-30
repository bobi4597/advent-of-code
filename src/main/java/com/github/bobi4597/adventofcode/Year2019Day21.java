package com.github.bobi4597.adventofcode;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import static com.github.bobi4597.adventofcode.BigIntCodeComputer.readInput;

public class Year2019Day21 {

    public static void main(String[] args) {
        String inputString = readInput();
        System.out.printf("\nPart 1: %d\n", solve1(inputString));
        System.out.printf("\nPart 2: %d\n", solve2(inputString));
    }

    private static int solve1(String inputString) {
        // @: 1 2 3 4
        // @: A B C D
        // The following says: if A, B or C is a hole, and D is not a hole, then jump
        String[] ins = new String[] {
            "OR A T",
            "AND B T",
            "AND C T",
            "NOT T T",

            "OR D J",

            "AND T J",

            "WALK"
        };
        return solve(inputString, ins);
    }

    private static int solve2(String inputString) {
        // @: 1 2 3 4 5 6 7 8 9
        // @: A B C D E F G H I
        // [(E and (F or I)) or H] and D and (!A or !B or !C)
        String[] ins = new String[] {
            "OR F J",
            "OR I J",
            "AND E J",
            "OR H J",

            "AND D J",

            "OR A T",
            "AND B T",
            "AND C T",
            "NOT T T",

            "AND T J",

            "RUN"
        };
        return solve(inputString, ins);
    }

    private static int solve(String inputString, String[] instructions) {
        BigIntCodeComputer a = new BigIntCodeComputer(inputString);
        byte[] bytes = (String.join("\n", instructions) + "\n").getBytes(StandardCharsets.US_ASCII);
        int[] ints = new int[bytes.length];
        for (int i = 0; i < bytes.length; ++i) ints[i] = (int) bytes[i];
        BigRunResult runResult = a.run(ints, false);
        // prints the robot walking/jumping on the hull
        runResult.printAscii();
       return runResult.output.get(runResult.output.size() - 1).intValue();
    }

}
