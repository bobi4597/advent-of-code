package com.github.bobi4597.adventofcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Year2019Day22 {

    private static final int LENGTH = 10_007;
    private static final int INDEX1 = 2019;

    public static void main(String[] args) {
        List<int[]> deals = readInput1(LENGTH);
        System.out.printf("%d\n", solve11(LENGTH, deals));
    }

    private static int solve11(int L, List<int[]> deals) {
        int[] f1 = new int[]{1, 0};
        for (int[] f2 : deals) {
            f1 = composition(f2, f1, L);
        }
        int y = ((f1[0] * INDEX1 + f1[1]) % L + L) % L;
        return y;
    }

    private static int[] dealIntoNewStack1(int L) {
        return new int[]{-1, L - 1};
    }

    private static int[] cut1(int n) {
        return new int[]{1, -n};
    }

    private static int[] dealWithIncrement1(int n) {
        return new int[]{n, 0};
    }

    private static int[] composition(int[] f2, int[] f1, int L) {
        return new int[]{(f1[0] * f2[0]) % L, (f2[0] * f1[1] + f2[1]) % L};
    }

    private static List<int[]> readInput1(int L) {
        List<int[]> f = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.startsWith("deal into new stack")) {
                f.add(dealIntoNewStack1(L));
            } else if (input.startsWith("cut")) {
                int n = Integer.parseInt(input.substring(4));
                if (n < 0) {
                    n = L + n;
                }
                f.add(cut1(n));
            } else {
                int n = Integer.parseInt(input.substring(20));
                f.add(dealWithIncrement1(n));
            }
        }
        return f;
    }

}
