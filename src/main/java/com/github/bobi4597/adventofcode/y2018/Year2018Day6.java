package com.github.bobi4597.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.bobi4597.adventofcode.y2018.Year2018Day1.FOLDER_PATH;

public class Year2018Day6 {

    public static void main(String[] args) {
        List<String> a = readInput();
        System.out.printf("Part 1: %d\n", solve1(a));
    }

    private static long solve1(List<String> a) {
        List<Point> points = a
            .stream()
            .map(Point::fromString)
            .collect(Collectors.toList());

        MinMaxStats minMaxStats = points
            .stream()
            .map(MinMaxStats::fromPoint)
            .reduce((acc, val) ->
                new MinMaxStats(
                    Math.min(acc.minX, val.minX),
                    Math.max(acc.maxX, val.maxX),
                    Math.min(acc.minY, val.minY),
                    Math.max(acc.maxY, val.maxY)
                )
            )
            .get();

        System.out.println(minMaxStats.toString());

        Map<Point, Point> closestLetterMap = new HashMap<>();

        IntStream
            .range(minMaxStats.minX - 1, minMaxStats.maxX + 1)
            .boxed()
            .flatMap(x -> IntStream
                .range(minMaxStats.minY - 1, minMaxStats.maxY + 1)
                .boxed()
                .map(y -> new Point(x, y)))
            .forEach(mapPoint -> {
                Point closestLetter = new Point(-1, -1);
                List<Point> sortedPoints = points
                    .stream()
                    .sorted(Comparator.comparingInt((Point p) -> distance(p, mapPoint)))
                    .limit(2)
                    .collect(Collectors.toList());

                if (distance(sortedPoints.get(0), mapPoint) != distance(sortedPoints.get(1), mapPoint)) {
                    closestLetter = sortedPoints.get(0);
                }
                closestLetterMap.put(mapPoint, closestLetter);
            });

        Set<String> infiniteLetters = new HashSet<>();
        closestLetterMap
            .forEach((mapPoint, letterPoint) -> {
                if (mapPoint.x == minMaxStats.minX - 1 || mapPoint.x == minMaxStats.maxX + 1 || mapPoint.y == minMaxStats.minY - 1 || mapPoint.y == minMaxStats.maxY + 1) {
                    infiniteLetters.add(toKey(letterPoint));
                }
            });

        Map<Point, Long> pointCountMap = closestLetterMap
            .entrySet()
            .stream()
            .filter(entry -> !infiniteLetters.contains(toKey(entry.getValue())))
            .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting()));

        Map.Entry<Point, Long> topEntry = pointCountMap
            .entrySet()
            .stream()
            .max(Comparator.comparing(Map.Entry::getValue))
            .get();

        return topEntry.getValue();
    }

    private static Point toPoint(String key) {
        String[] xy = key.split(":");
        return new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
    }

    private static String toKey(Point point) {
        return String.format("%d:%d", point.x, point.y);
    }

    private static int distance(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    static class Point {
        int x, y;
        Point (int x, int y) {
            this.x = x; this.y = y;
        }
        static Point fromString(String line) {
            String[] xy = line.split(", ");
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            return new Point(x, y);
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Point)) return false;
            Point p = (Point) other;
            if (p == this) return true;
            if (x == p.x && y == p.y) return true;
            return false;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", x, y);
        }
    }

    static class MinMaxStats {
        int minX, maxX, minY, maxY;

        MinMaxStats(int minX, int maxX, int minY, int maxY) {
            this.minX = minX; this.maxX = maxX; this.minY = minY; this.maxY = maxY;
        }

        static MinMaxStats fromPoint(Point p) {
            return new MinMaxStats(p.x, p.x, p.y, p.y);
        }

        @Override
        public String toString() {
            return String.format("minX - maxX: (%d - %d), minY - maxY: (%d - %d)", minX, maxX, minY, maxY);
        }
    }

    private static List<String> readInput() {
        try {
            return Files.lines(Paths.get(FOLDER_PATH + "day6.txt"))
                .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
