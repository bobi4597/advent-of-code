package com.github.bobi4597.adventofcode;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Year2019Day3 {

    private static Map<Character, Integer> deltaX = new HashMap<>();
    private static Map<Character, Integer> deltaY = new HashMap<>();
    static {
        deltaX.put('U', 0); deltaX.put('D', 0); deltaX.put('L', -1); deltaX.put('R', 1);
        deltaY.put('U', 1); deltaY.put('D', -1); deltaY.put('L', 0); deltaY.put('R', 0);
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String line1 = scanner.nextLine();
        String line2 = scanner.nextLine();
        System.out.printf("%d\n", solve1(line1, line2));
        System.out.printf("%d\n", solve2(line1, line2));
    }

    private static int solve1(String line1, String line2) {
        Map<Pair<Integer, Integer>, Integer> map1 = parseInput(line1);
        Map<Pair<Integer, Integer>, Integer> map2 = parseInput(line2);
        int minDistance = Integer.MAX_VALUE;
        for (Pair<Integer, Integer> point2: map2.keySet()) {
            if (map1.containsKey(point2) && (point2.getKey() != 0 || point2.getValue() != 0)) {
                minDistance = Math.min(minDistance, point2.getKey() + point2.getValue());
            }
        }
        return minDistance;
    }

    private static int solve2(String line1, String line2) {
        Map<Pair<Integer, Integer>, Integer> map1 = parseInput(line1);
        Map<Pair<Integer, Integer>, Integer> map2 = parseInput(line2);
        int minDistance = Integer.MAX_VALUE;
        for (Map.Entry<Pair<Integer, Integer>, Integer> entry1: map1.entrySet()) {
            Pair<Integer, Integer> pair = entry1.getKey();
            if (map2.containsKey(pair) && (pair.getKey() != 0 || pair.getValue() != 0)) {
                minDistance = Math.min(minDistance, entry1.getValue() + map2.get(pair));
            }
        }
        return minDistance;
    }

    private static Map<Pair<Integer, Integer>, Integer> parseInput(String input) {
        String[] tokens = input.split(",");
        Map<Pair<Integer, Integer>, Integer> map = new HashMap<>();

        int x = 0, y = 0, count = 0;
        map.put(new Pair<>(0, 0), 0);

        for (String token: tokens) {
            Character dir = token.charAt(0);
            int dist = Integer.parseInt(token.substring(1));
            int dx = deltaX.get(dir), dy = deltaY.get(dir);

            for (int i = 0; i < dist; ++i) {
                x += dx;
                y += dy;
                ++count;
                Pair<Integer, Integer> pair = new Pair<>(x, y);
                if (!map.containsKey(pair)) {
                    map.put(pair, count);
                }
            }
        }
        return map;
    }
}
