package com.github.bobi4597.adventofcode.y2019;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Year2019Day22 {

    private static final long LENGTH = 10_007L;
    private static final long INDEX1 = 2019L;

    private static final long LENGTH2 = 119315717514047L;
    private static final long POWER2 = 101741582076661L;
    private static final long INDEX2 = 2020L;


    public static void main(String[] args) {
//        List<BigInteger[]> deals = readInput1(LENGTH);
//        System.out.printf("%s\n", solve1(LENGTH, deals));
        List<BigInteger[]> deals = readInput2(LENGTH2);
        System.out.printf("%s\n", solve2(LENGTH2, deals));
    }

    ///////////////////////////////////////////////////////////
    /////////////////////// part 2 ////////////////////////////
    ///////////////////////////////////////////////////////////

    private static String solve2(long L, List<BigInteger[]> deals) {
        BigInteger[] f1 = new BigInteger[]{BigInteger.ONE, BigInteger.ZERO};
        for (int i = deals.size() - 1; i >= 0; --i) {
            BigInteger[] f2 = deals.get(i);
            f1 = composition2(f2, f1, L);
        }


        // f1 is the function after applying all the deals from the input
        // now we have to shuffle POWER2 times
        BigInteger[] f = pow(f1, POWER2, LENGTH2);

        BigInteger bigL = BigInteger.valueOf(L);
        BigInteger index2 = BigInteger.valueOf(INDEX2);
        BigInteger y = f[0].multiply(index2).add(f[1]).remainder(bigL).add(bigL).remainder(bigL);
        return y.toString();
    }

    private static BigInteger[] dealIntoNewStack2(long L) {
        return new BigInteger[]{BigInteger.ONE.negate(), BigInteger.valueOf(L - 1)};
    }

    private static BigInteger[] cut2(long n) {
        return new BigInteger[]{BigInteger.ONE, BigInteger.valueOf(n).negate()};
    }

    private static BigInteger[] dealWithIncrement2(long n) {
        return new BigInteger[]{BigInteger.valueOf(n), BigInteger.ZERO};
    }

    private static BigInteger[] composition2(BigInteger[] f2, BigInteger[] f1, long L) {
        BigInteger bigL = BigInteger.valueOf(L);
        return new BigInteger[]{
            f1[0].multiply(f2[0]).remainder(bigL),
            f2[0].multiply(f1[1]).add(f2[1]).remainder(bigL)
        };
    }

    /**
     * Calculate f^p(x).
     * f^p = {
     * p % 2 == 0: (f^2)^(p / 2)
     * p % 2 == 1: f * f^(p - 1)
     * }
     */
    private static BigInteger[] pow(BigInteger[] f, long p, long L) {
        if (p == 1) return f;
        if (p % 2 == 0) {
            return pow(composition2(f, f, L), p / 2, L);
        } else {
            return composition2(f, pow(f, p - 1, L), L);
        }
    }

    private static List<BigInteger[]> readInput2(long L) {
        BigInteger bigL = BigInteger.valueOf(L);
        List<BigInteger[]> f = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.startsWith("deal into new stack")) {
                f.add(dealIntoNewStack2(L));
            } else if (input.startsWith("cut")) {
                long n = Long.parseLong(input.substring(4));
                if (n < 0L) {
                    n = L + n;
                }
                n = L - n;
                f.add(cut2(n));
            } else {
                long n = Long.parseLong(input.substring(20));
                BigInteger modInverse = BigInteger.valueOf(n).modInverse(bigL);
                long invN = modInverse.longValue();
                f.add(dealWithIncrement2(invN));
            }
        }
        return f;
    }

    ///////////////////////////////////////////////////////////
    /////////////////////// part 1 ////////////////////////////
    ///////////////////////////////////////////////////////////

    private static String solve1(long L, List<BigInteger[]> deals) {
        BigInteger[] f1 = new BigInteger[]{BigInteger.ONE, BigInteger.ZERO};
        for (BigInteger[] f2 : deals) {
            f1 = composition2(f2, f1, L);
        }
        BigInteger bigL = BigInteger.valueOf(L);
        BigInteger startIndex = BigInteger.valueOf(INDEX1);
        BigInteger endIndex = f1[0].multiply(startIndex).add(f1[1]).remainder(bigL).add(bigL).remainder(bigL);
        return endIndex.toString();
    }

    private static List<BigInteger[]> readInput1(long L) {
        BigInteger bigL = BigInteger.valueOf(L);
        List<BigInteger[]> f = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.startsWith("deal into new stack")) {
                f.add(dealIntoNewStack2(L));
            } else if (input.startsWith("cut")) {
                long n = Long.parseLong(input.substring(4));
                if (n < 0L) {
                    n = L + n;
                }
                f.add(cut2(n));
            } else {
                long n = Long.parseLong(input.substring(20));
                f.add(dealWithIncrement2(n));
            }
        }
        return f;
    }
}