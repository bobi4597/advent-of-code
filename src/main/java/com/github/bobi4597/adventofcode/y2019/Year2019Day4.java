package com.github.bobi4597.adventofcode.y2019;

public class Year2019Day4 {

    public static void main(String[] args) {
        System.out.printf("%d\n", solve1());
        System.out.printf("%d\n", solve2());
    }

    private static int solve1() {
        int count = 0;
        for (int i = 264360; i <= 746325; ++i) {
            if (isOk1(i)) {
                ++count;
            }
        }
        return count;
    }

    private static boolean isOk1(int n) {
        int prev = -1;

        boolean hasSameAdjacent = false;
        while (n > 0) {
            int d = n % 10;
            n = n / 10;
            if (prev != -1) {
                if (prev < d) {
                    return false;
                }
                if (prev == d) {
                    hasSameAdjacent = true;
                }
            }
            prev = d;
        }

        return hasSameAdjacent;
    }

    private static int solve2() {
        int count = 0;
        for (int i = 264360; i <= 746325; ++i) {
            if (isOk2(i)) {
                ++count;
            }
        }
        return count;
    }

    private static boolean isOk2(int n) {
        int prev = -1;
        int adjLength = 1;
        boolean hasGroupOfTwo = false;
        while (n > 0) {
            int d = n % 10;
            n = n / 10;
            if (prev != -1) {
                if (prev < d) {
                    return false;
                }
                if (prev == d) {
                    ++adjLength;
                } else {
                    if (adjLength == 2) {
                        hasGroupOfTwo = true;
                    }
                    adjLength = 1;
                }
            }
            prev = d;
        }

        if (adjLength == 2) {
            hasGroupOfTwo = true;
        }

        return hasGroupOfTwo;
    }

}

