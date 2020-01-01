package com.github.bobi4597.adventofcode.y2018;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Year2018Day1 {

    public static void main(String[] args) throws FileNotFoundException {
        List<Integer> a = readInput();
        System.out.printf("Part 1: %d\n", solve1(a));
        System.out.printf("Part 2: %d\n", solve2(a));
    }

    private static int solve2(List<Integer> a) {
        Set<Integer> seen = new HashSet<>();
        int sum = 0;
        while (true) {
            for (int x : a) {
                sum += x;
                if (seen.contains(sum)) {
                    return sum;
                }
                seen.add(sum);
            }
        }
    }

    private static Object solve1(List<Integer> a) {
        int sum = 0;
        for (int x: a) {
            sum += x;
        }
        return sum;
    }

    private static List<Integer> readInput() throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream("src/main/java/com/github/bobi4597/adventofcode/y2018/day1.txt"));
        List<Integer> a = new ArrayList<>();
        while (scanner.hasNextLine()) {
            a.add(Integer.parseInt(scanner.nextLine()));
        }
        return a;
    }
}
