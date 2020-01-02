package com.github.bobi4597.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Year2018Day1 {

    public static final String FOLDER_PATH = "src/main/java/com/github/bobi4597/adventofcode/y2018/";

    public static void main(String[] args) throws IOException {
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

    private static int solve1(List<Integer> a) {
        return a.stream()
            .reduce(0, Integer::sum);
    }

    private static List<Integer> readInput() throws IOException {
        return Files.lines(Paths.get(FOLDER_PATH + "day1.txt"))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
    }
}
