package com.github.bobi4597.adventofcode.y2018;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.bobi4597.adventofcode.y2018.Year2018Day1.FOLDER_PATH;

public class Year2018Day2 {

    public static void main(String[] args) throws IOException {
        List<String> a = readInput();
        System.out.printf("Part 1: %d\n", solve1(a));
        System.out.printf("Part 2: %s\n", solve2(a));
    }

    private static String solve2(List<String> a) {
        return a.stream()
            .filter(s1 -> a.stream().anyMatch(s2 -> getDiffIndex(s1, s2) != -1))
            .reduce("", (acc, val) -> {
                if (acc.equals("")) return val;
                int diffIndex = getDiffIndex(acc, val);
                return acc.substring(0, diffIndex) + acc.substring(diffIndex + 1);
              }
            );
    };

    private static int getDiffIndex(String s1, String s2) {
        int diffIndex = -1;
        for (int i = 0; i < s1.length(); ++i) {
            if (s1.charAt(i) != s2.charAt(i)) {
                if (diffIndex != -1) {
                    return -1;
                } else {
                    diffIndex = i;
                }
            }
        }
        return diffIndex;
    }

    private static int solve1(List<String> a) {
        int[] counts = a.stream()
            .map(s -> s.chars()
                .mapToObj(i -> (char) i)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())))
            .map(countMap -> new int[] {
                countMap.entrySet().stream().anyMatch(entry -> entry.getValue() == 2) ? 1 : 0,
                countMap.entrySet().stream().anyMatch(entry -> entry.getValue() == 3) ? 1 : 0}
            )
            .reduce(new int[] {0, 0}, (acc, val) -> new int[] {acc[0] + val[0], acc[1] + val[1]});

        return counts[0] * counts[1];
    }

    private static List<String> readInput() throws IOException {
        return Files.lines(Paths.get(FOLDER_PATH + "day2.txt"))
            .collect(Collectors.toList());
    }
}
