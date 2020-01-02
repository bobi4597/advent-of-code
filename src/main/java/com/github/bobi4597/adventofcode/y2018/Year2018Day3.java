package com.github.bobi4597.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.bobi4597.adventofcode.y2018.Year2018Day1.FOLDER_PATH;

public class Year2018Day3 {

    public static void main(String[] args) throws IOException {
        List<String> a = readInput();
        System.out.printf("Part 1: %d\n", solve1(a));
        System.out.printf("Part 2: %d\n", solve2(a));
    }

    static class Rectangle {

        int id, x, y, w, h;
        Rectangle(int id, int x, int y, int w, int h) {
            this.id = id; this.x = x; this.y = y; this.w = w; this.h = h;
        }
        static Rectangle fromString(String s) {
            String[] parts = s.split(" ");
            int id = Integer.parseInt(parts[0].substring(1));
            String[] xy = parts[2].substring(0, parts[2].length() - 1).split(",");
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            String[] wh = parts[3].split("x");
            int w = Integer.parseInt(wh[0]);
            int h = Integer.parseInt(wh[1]);
            return new Rectangle(id, x, y, w, h);
        }
    }

    private static int solve2(List<String> a) {
        List<Rectangle> r = a.stream()
            .map(Rectangle::fromString)
            .collect(Collectors.toList());

        return r
            .stream()
            .filter(rec -> r.stream()
                .filter(rec2 -> rec2.id != rec.id)
                .noneMatch(rec2 -> isOverlapping(rec, rec2)))
            .peek(rec -> System.out.println(rec.id))
            .findFirst()
            .get()
            .id;
    }

    private static boolean isOverlapping(Rectangle r1, Rectangle r2) {
        return
            ((r1.x <= r2.x && r1.x + r1.w >= r2.x) || (r2.x <= r1.x && r2.x + r2.w >= r1.x)) &&
            ((r1.y <= r2.y && r1.y + r1.h >= r2.y) || (r2.y <= r1.y && r2.y + r2.h >= r1.y));
    }

    private static long solve1(List<String> a) {
        return a.stream()
            .map(Rectangle::fromString)
            .map(rec -> {
                    return IntStream.range(rec.x, rec.x + rec.w)
                        .boxed()
                        .map(x -> IntStream.range(rec.y, rec.y + rec.h)
                            .boxed()
                            .map(y -> x + "_" + y)
                            .collect(Collectors.toList())
                        ).collect(Collectors.toList());
                }
            )
            .flatMap(Collection::stream)
            .flatMap(Collection::stream)
            .collect(Collectors.groupingBy(key -> key, Collectors.counting()))
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() > 1)
            .count();
    }

    private static List<String> readInput() throws IOException {
        return Files.lines(Paths.get(FOLDER_PATH + "day3.txt"))
            .collect(Collectors.toList());
    }

}
