package com.github.bobi4597.adventofcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Year2019Day6 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> input = new ArrayList<>();
        while (scanner.hasNextLine()) {
            input.add(scanner.nextLine());
        }
        Map<String, String> map = parseInput(input);
        System.out.printf("%d\n", solve1(map));
        System.out.printf("%d\n", solve2(map));
    }

    private static int solve1(Map<String, String> map) {
        int totalOrbitCount = 0;
        for (String planet: map.keySet()) {
            int count = -1;
            while (planet != null) {
                planet = map.get(planet);
                ++count;
            }
            totalOrbitCount += count;
        }

        return totalOrbitCount;
    }

    private static int solve2(Map<String, String> map) {
        Map<String, Integer> sanMap = new HashMap<>();
        String san = map.get("SAN");
        int count = 0;
        while (san != null) {
            sanMap.put(san, count);
            ++count;
            san = map.get(san);
        }

        count = 0;
        String you = map.get("YOU");
        while (!sanMap.containsKey(you)) {
            ++count;
            you = map.get(you);
        }

        return count + sanMap.get(you);
    }

    private static Map<String, String> parseInput(List<String> input) {
        Map<String, String> map = new HashMap<>();
        for (String orbit: input) {
            String a = orbit.substring(0, 3);
            String b = orbit.substring(4);
            map.put(b, a);
        }
        return map;
    }
}

