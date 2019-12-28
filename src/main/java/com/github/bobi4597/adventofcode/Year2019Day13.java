package com.github.bobi4597.adventofcode;

import java.math.BigInteger;
import java.util.List;

import static com.github.bobi4597.adventofcode.BigIntCodeComputer.readInput;

public class Year2019Day13 {

    public static void main(String[] args) {
        String inputString = readInput();
        System.out.printf("Part 1: %d\n", solve1(inputString));
    }

    private static int solve1(String inputString) {
        BigIntCodeComputer a = new BigIntCodeComputer(inputString);
        BigRunResult runResult = a.run(new int[0], false);
        List<BigInteger> o = runResult.output;
        int countBlocks = 0;
        for (int i = 0; i < o.size(); i+=3) {
            int x = o.get(i).intValue();
            int y = o.get(i + 1).intValue();
            int tileId = o.get(i + 2).intValue();
            if (tileId == 2) {
                ++countBlocks;
            }
        }
        return countBlocks;
    }
}
