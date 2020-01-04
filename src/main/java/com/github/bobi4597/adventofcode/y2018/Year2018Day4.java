package com.github.bobi4597.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.bobi4597.adventofcode.y2018.Year2018Day1.FOLDER_PATH;

public class Year2018Day4 {

    public static void main(String[] args) throws IOException {
        List<String> a = readInput();
        System.out.printf("Part 1: %d\n", solve1(a));
    }

    static class Event {
        LocalDateTime dateTime;
        LocalDateTime fallsAsleep;
        boolean isFallsAsleep, isWakesUp;
        int guardId;
        String fullText;
        String text;

        private static final Pattern p = Pattern.compile("^\\[([0-9]+)-([0-9]+)-([0-9]+)\\s([0-9]+):([0-9]+)]\\s([^#]*(#([0-9]+).*)?)");

        Event (String s) {
            Matcher m = p.matcher(s);
            if (m.find()) {
                this.fullText = m.group(0);
                this.dateTime = LocalDateTime.of(
                    Integer.parseInt(m.group(1)),
                    Integer.parseInt(m.group(2)),
                    Integer.parseInt(m.group(3)),
                    Integer.parseInt(m.group(4)),
                    Integer.parseInt(m.group(5))
                );
                this.text = m.group(6);
                if (m.group(7) != null) {
                    this.guardId = Integer.parseInt(m.group(8));
                } else {
                    this.guardId = -1;
                }
                if ("falls asleep".equals(this.text)) this.isFallsAsleep = true;
                if ("wakes up".equals(this.text)) this.isWakesUp = true;
            }
        }
    }

    private static int solve1(List<String> a) {
        int[] lastGuardId = new int[1];
        LocalDateTime[] lastFallenAsleep = new LocalDateTime[1];

        Map<Integer, Map<Integer, Integer>> guardToMinuteMap = a
            .stream()
            .map(Event::new)
            // first sort by date
            .sorted(Comparator.comparing(e -> e.dateTime))
            .peek(e -> {
                if (e.guardId != -1) lastGuardId[0] = e.guardId;
                else e.guardId = lastGuardId[0];
                if (e.isFallsAsleep) lastFallenAsleep[0] = e.dateTime;
                if (e.isWakesUp) e.fallsAsleep = lastFallenAsleep[0];
            })
            .sorted(Comparator.comparing((Event e1) -> e1.guardId).thenComparing((Event e2) -> e2.dateTime))
            .filter(e -> e.isWakesUp)
            .collect(Collectors.groupingBy(
                (Event e3) -> e3.guardId,
                Collectors.reducing(
                    new HashMap<Integer, Integer>(),
                    (Event e4) -> IntStream.range(e4.fallsAsleep.getMinute(), e4.dateTime.getMinute())
                            .boxed()
                            .collect(Collectors.toMap(x -> x, x -> 1))
                    ,
                    (acc, val) -> {
                        final Map<Integer, Integer> newAcc = new HashMap<>(acc);
                        val.forEach((key, value) -> newAcc.merge(key, value, Integer::sum));
                        return newAcc;
                    }
                )
            ));

        int[] topGuard = guardToMinuteMap.entrySet()
            .stream()
            .map(entry -> new int[] {
                    entry.getKey(),
                    entry.getValue().values().stream().mapToInt(x -> x).sum(),
                    entry.getValue().entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get().getKey()
            })
            .max(Comparator.comparingInt((int[] arr) -> arr[1]))
            .get();

        return topGuard[0] * topGuard[2];
    }


    private static List<String> readInput() throws IOException {
        return Files.lines(Paths.get(FOLDER_PATH + "day4.txt"))
            .collect(Collectors.toList());
    }
}
