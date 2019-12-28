package com.github.bobi4597.adventofcode;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Year2019Day5 {

    public static void main(String[] args) {
        int[] a = readInput();
        System.out.printf("%s\n", solve1(a, 1));
    }

    private static String solve1(int[] a, int input) {
        StringBuilder output = new StringBuilder();
        int n = a.length;
        int pc = 0;
        while (true) {
            int opcode = a[pc];
            int arg1 = pc + 1 < n ? a[pc + 1] : -1;
            int arg2 = pc + 2 < n ? a[pc + 2] : -1;
            int arg3 = pc + 3 < n ? a[pc + 3] : -1;

            int mode1 = (opcode / 100) % 10;
            int mode2 = (opcode / 1000) % 10;
            int mode3 = (opcode / 10000) % 10;

            if (opcode % 100 == 1) {
                int val1 = mode1 == 0 ? a[arg1]: arg1;
                int val2 = mode2 == 0 ? a[arg2]: arg2;
                a[arg3] = val1 + val2;
                pc += 4;
            } else if (opcode % 100 == 2) {
                int val1 = mode1 == 0 ? a[arg1]: arg1;
                int val2 = mode2 == 0 ? a[arg2]: arg2;
                a[arg3] = val1 * val2;
                pc += 4;
            } else if (opcode % 100 == 3) {
                a[arg1] = input;
                pc += 2;
            } else if (opcode % 100 == 4) {
                int val1 = mode1 == 0 ? a[arg1]: arg1;
                if (val1 != 0) {
                    output.append(val1);
                }
                pc += 2;
            } else { // 99
                break;
            }
        }
        return output.toString();
    }

    private static int[] readInput() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        List<Integer> list = Arrays
            .stream(input.split(","))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
        int[] a = new int[list.size()];
        for (int i = 0; i < a.length; ++i) a[i] = list.get(i);
        return a;
    }
}
