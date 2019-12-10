package com.github.bobi4597.adventofcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Year2019Day10 {

    public static void main(String[] args) {
        List<String> input = readInput();
        int m = input.size();
        int n = input.get(0).length();
        System.out.printf("%d\n", solve1(input, m, n));
    }

    private static List<String> readInput() {
        List<String> input = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            input.add(scanner.nextLine());
        }
        return input;
    }

    private static int solve1(List<String> input, int m, int n) {

        int max = 0;
        int maxi, maxj = 0;

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (input.get(i).charAt(j) == '#') {
                    int count = countVisible(input, m, n, i, j);
                    if (max < count) {
                        max = count;
                        maxi = i;
                        maxj = j;
                        System.out.printf("Found new max (%d), at position [%d, %d]\n", max, maxi, maxj);
                    }
                }
            }
        }
        return max;
    }

    private static int countVisible(List<String> input, int m, int n, int i, int j) {
        int count = 0;
        for (int i1 = 0; i1 < m; ++i1) {
            for (int j1 = 0; j1 < n; ++j1) {
                if (input.get(i1).charAt(j1) == '#' && !(i1 == i && j1 == j)) {
                    // we've found another asteroid at location [i1, j1]
                    // lets check if there are other asteroids in between [i, j] and [i1, j1]
                    int di = Math.abs(i1 - i);
                    int dj = Math.abs(j1 - j);
                    int gcd = gcd(di, dj);
                    int stepi = di / gcd;
                    int stepj = dj / gcd;
                    if (i1 < i) stepi = -stepi;
                    if (j1 < j) stepj = -stepj;

                    boolean isVisible = true;
                    for (int i2 = i + stepi, j2 = j + stepj; i2 != i1 || j2 != j1; i2 += stepi, j2 += stepj) {
                        if (input.get(i2).charAt(j2) == '#') {
                            isVisible = false;
                            break;
                        }
                    }
                    if (isVisible) {
                        ++count;
                    }
                }
            }
        }
        return count;
    }


    private static int gcd(int a, int b) {
        if (a < b) {
            return gcd(b, a);
        }
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }
}
