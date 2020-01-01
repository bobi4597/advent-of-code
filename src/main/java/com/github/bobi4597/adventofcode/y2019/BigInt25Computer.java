package com.github.bobi4597.adventofcode.y2019;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TEN;
import static java.math.BigInteger.TWO;
import static java.math.BigInteger.ZERO;

/**
 * Intcode computer for the 25th day.
 * The output command returns when the last 8 characters in the output list are "Command?" (ascii).
 */
public class BigInt25Computer {

    private static BigInteger PARAMETER_MODE = ZERO,
        IMMEDIATE_MODE = BigInteger.ONE,
        RELATIVE_MODE = TWO;

    private static BigInteger THREE = BigInteger.valueOf(3), FOUR = BigInteger.valueOf(4), FIVE = BigInteger.valueOf(5),
        SIX = BigInteger.valueOf(6), SEVEN = BigInteger.valueOf(7), EIGHT = BigInteger.valueOf(8), NINE = BigInteger.valueOf(9);

    private Map<BigInteger, BigInteger> a;
    private BigInteger pc;
    private BigInteger relativeBase;

    public BigInt25Computer(String inputString) {
        List<BigInteger> inputA = parseInput(inputString);
        this.a = new HashMap<>();
        for (int i = 0; i < inputA.size(); ++i) {
            a.put(BigInteger.valueOf(i), inputA.get(i));
        }
        this.pc = ZERO;
        this.relativeBase = ZERO;
    }

    public BigRunResult run(int[] input) {
        int inputIndex = 0;
        List<BigInteger> output = new ArrayList<>();

        boolean doWork = true;
        while (doWork) {
            BigInteger opcode = a.get(pc);
            BigInteger arg1 = a.getOrDefault(pc.add(ONE), ZERO);
            BigInteger arg2 = a.getOrDefault(pc.add(TWO), ZERO);
            BigInteger arg3 = a.getOrDefault(pc.add(THREE), ZERO);
            BigInteger mode1 = opcode.divide(BigInteger.valueOf(100)).remainder(TEN);
            BigInteger mode2 = opcode.divide(BigInteger.valueOf(1000)).remainder(TEN);
            BigInteger mode3 = opcode.divide(BigInteger.valueOf(10000)).remainder(TEN);
            BigInteger val1 = getParameterValue(arg1, mode1);
            BigInteger val2 = getParameterValue(arg2, mode2);
            BigInteger val3 = getParameterValue(arg3, mode3);
            BigInteger address1 = getAddress(arg1, mode1);
            BigInteger address2 = getAddress(arg2, mode2);
            BigInteger address3 = getAddress(arg3, mode3);

            switch (opcode.remainder(BigInteger.valueOf(100)).intValue()) {
                case 1: // add
                    a.put(address3, val1.add(val2));
                    pc = pc.add(FOUR);
                    break;
                case 2: // multiply
                    a.put(address3, val1.multiply(val2));
                    pc = pc.add(FOUR);
                    break;
                case 3: // input
                    a.put(address1, BigInteger.valueOf(input[inputIndex]));
                    ++inputIndex;
                    pc = pc.add(TWO);
                    break;
                case 4: // output
                    pc = pc.add(TWO);
                    output.add(val1);
                    // This is "Command?" in ASCII
                    // 067 111 109 109 097 110 100 063
                    if (val1.intValue() == 63 && output.get(output.size() - 2).intValue() == 100) {
                        return new BigRunResult(false, output);
                    }
                    break;
                //return new BigRunResult(false, val1);
                case 5: // jump-if-true
                    if (!val1.equals(ZERO)) {
                        pc = val2;
                    } else {
                        pc = pc.add(THREE);
                    }
                    break;
                case 6: // jump-if-false
                    if (val1.equals(ZERO)) {
                        pc = val2;
                    } else {
                        pc = pc.add(THREE);
                    }
                    break;
                case 7: // less than
                    if (val1.compareTo(val2) < 0) {
                        a.put(address3, ONE);
                    } else {
                        a.put(address3, ZERO);
                    }
                    pc = pc.add(FOUR);
                    break;
                case 8: // equals
                    if (val1.equals(val2)) {
                        a.put(address3, ONE);
                    } else {
                        a.put(address3, ZERO);
                    }
                    pc = pc.add(FOUR);
                    break;
                case 9: // adjust relative base
                    relativeBase = relativeBase.add(val1);
                    pc = pc.add(TWO);
                    break;
                default:  // 99
                    doWork = false;
                    break;
            }
        }
        return new BigRunResult(true, output);
    }

    private BigInteger getAddress(BigInteger arg, BigInteger mode) {
        if (mode.equals(PARAMETER_MODE)) {
            return arg;
        }
        if (mode.equals(RELATIVE_MODE)) {
            return relativeBase.add(arg);
        }
        return ZERO;
    }

    private BigInteger getParameterValue(BigInteger arg, BigInteger mode) {
        if (mode.equals(PARAMETER_MODE)) {
            return a.getOrDefault(arg, ZERO);
        }
        if (mode.equals(IMMEDIATE_MODE)) {
            return arg;
        }
        if (mode.equals(RELATIVE_MODE)) {
            BigInteger absoluteArg = relativeBase.add(arg);
            return a.getOrDefault(absoluteArg, ZERO);
        }
        return ZERO;
    }

    public static String readInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static List<BigInteger> parseInput(String input) {
        return Arrays.stream(input.split(","))
            .map(BigInteger::new)
            .collect(Collectors.toList());
    }

    public static int[] strToInput(String command) {
        byte[] bytes = (String.join("\n", command) + "\n").getBytes(StandardCharsets.US_ASCII);
        int[] ints = new int[bytes.length];
        for (int i = 0; i < bytes.length; ++i) ints[i] = (int) bytes[i];
        return ints;
    }

}
