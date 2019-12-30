package com.github.bobi4597.adventofcode;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import static com.github.bobi4597.adventofcode.BigIntCodeComputer.readInput;

public class Year2019Day21 {

    public static void main(String[] args) {
        String inputString = readInput();
        System.out.printf("\nPart 1: %d\n", solve1(inputString));
    }

    private static int solve1(String inputString) {
        BigIntCodeComputer a = new BigIntCodeComputer(inputString);
        // The following says: if A, B or C is a hole, and D is not a hole, then jump
        String instructions = "NOT A T\nNOT B J\nOR J T\nNOT C J\nOR J T\nNOT D J\nNOT J J\nAND T J\nWALK\n";
        byte[] bytes = instructions.getBytes(StandardCharsets.US_ASCII);
        int[] ints = new int[bytes.length];
        for (int i = 0; i < bytes.length; ++i) ints[i] = (int) bytes[i];
        BigRunResult runResult = a.run(ints, false);
        // prints the robot walking/jumping on the hull
        for (BigInteger bigInt: runResult.output) {
            int out = bigInt.intValue();
            System.out.print((char) out);
        }
        int hullDamage = runResult.output.get(runResult.output.size() - 1).intValue();
        return hullDamage;
    }
}
