package com.github.bobi4597.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class IntCodeComputer {

    private static int PARAMETER_MODE = 0, IMMEDIATE_MODE = 1, RELATIVE_MODE = 2;

    private int[] a;
    private int pc;
    private int relativeBase;

    public IntCodeComputer(int[] a) {
        this.a = a;
        this.pc = 0;
        this.relativeBase = 0;
    }

    public RunResult run(int[] input) {
        int inputIndex = 0;
        List<Integer> output = new ArrayList<>();
        int n = a.length;
        boolean doWork = true;
        while (doWork) {
            int opcode = a[pc];
            int arg1 = pc + 1 < n ? a[pc + 1] : 0;
            int arg2 = pc + 2 < n ? a[pc + 2] : 0;
            int arg3 = pc + 3 < n ? a[pc + 3] : 0;
            int mode1 = (opcode / 100) % 10;
            int mode2 = (opcode / 1000) % 10;
            int val1 = getParameterValue(mode1, arg1);
            int val2 = getParameterValue(mode2, arg2);

            switch (opcode % 100) {
                case 1: // add
                    a[arg3] = val1 + val2;
                    pc += 4;
                    break;
                case 2: // multiply
                    a[arg3] = val1 * val2;
                    pc += 4;
                    break;
                case 3: // input
                    a[arg1] = input[inputIndex++];
                    pc += 2;
                    break;
                case 4: // output
                    pc += 2;
                    return new RunResult(false, val1);
                case 5: // jump-if-true
                    if (val1 != 0) {
                        pc = val2;
                    } else {
                        pc += 3;
                    }
                    break;
                case 6: // jump-if-false
                    if (val1 == 0) {
                        pc = val2;
                    } else {
                        pc += 3;
                    }
                    break;
                case 7: // less than
                    if (val1 < val2) {
                        a[arg3] = 1;
                    } else {
                        a[arg3] = 0;
                    }
                    pc += 4;
                    break;
                case 8: // equals
                    if (val1 == val2) {
                        a[arg3] = 1;
                    } else {
                        a[arg3] = 0;
                    }
                    pc += 4;
                    break;
                case 9: // adjust relative base
                    relativeBase += val1;
                    pc += 2;
                    break;
                default:  // 99
                    doWork = false;
                    break;
            }
        }
        return new RunResult(true, -1);
    }

    private int getParameterValue(int mode, int arg) {
        if (mode == PARAMETER_MODE) {
            return arg < a.length ? a[arg] : 0;
        }
        if (mode == IMMEDIATE_MODE) {
            return arg;
        }
        if (mode == RELATIVE_MODE) {
            int absoluteArg = relativeBase + arg;
            return absoluteArg >= 0 && absoluteArg < a.length ? a[absoluteArg] : 0;
        }
        return 0;
    }

    public static String readInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static int[] parseInput(String input) {
        List<Integer> list = Arrays
            .stream(input.split(","))
            .map(Integer::parseInt)
            .collect(Collectors.toList());

        int[] a = new int[list.size()];
        for (int i = 0; i < a.length; ++i) {
            a[i] = list.get(i);
        }
        return a;
    }

}
