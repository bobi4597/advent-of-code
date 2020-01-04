package com.github.bobi4597.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.bobi4597.adventofcode.y2018.Year2018Day1.FOLDER_PATH;
import static java.lang.Character.toLowerCase;

public class Year2018Day5 {

    public static void main(String[] args) throws IOException {
        List<String> a = readInput();
        System.out.printf("Part 1: %d\n", solve1(a.get(0)));
        System.out.printf("Part 2: %d\n", solve2(a.get(0)));
    }

    private static int solve2(String s) {
        return IntStream
            .range(0, 26)
            .mapToObj(i -> (char) (i + (int) 'a'))
            .map(unit -> removeUnit(s, unit))
            .map(Year2018Day5::reducePolymer)
            .min(Comparator.comparingInt(String::length))
            .get()
            .length();
    }

    private static String removeUnit(String s, char unit) {
        return s.chars()
            .mapToObj(i -> String.valueOf((char) i))
            .reduce("", (acc, val) -> (val.charAt(0) == unit || toLowerCase(val.charAt(0)) == unit) ? acc : acc + val);
    }

    private static int solve1(String s) {
        return reducePolymer(s).length();
    }

    private static String reducePolymer(String polymer) {
        return polymer
            .chars()
            .mapToObj(i -> String.valueOf((char) i))
            .reduce("", (acc, val) ->
               acc.length() == 0 ? acc + val :
                   isMatching(acc.charAt(acc.length() - 1), val) ? acc.substring(0, acc.length() - 1) : acc + val
            );
    }

    private static boolean isMatching(char c, String s) {
        return Character.isUpperCase(c) && toLowerCase(c) == s.charAt(0) ||
            Character.isLowerCase(c) && Character.toUpperCase(c) == s.charAt(0);
    }

    private static List<String> readInput() throws IOException {
        return Files.lines(Paths.get(FOLDER_PATH + "day5.txt"))
            .collect(Collectors.toList());
    }

}
