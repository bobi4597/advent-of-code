package com.github.bobi4597.adventofcode;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.bobi4597.adventofcode.BigIntCodeComputer.readInput;

public class Year2019Day17 {

    private static int[] dr = {-1, 0, 1, 0};
    private static int[] dc = {0, 1, 0, -1};

    public static void main(String[] args) {
        String inputString = readInput();
        List<List<Character>> map = new ArrayList<>();
        System.out.printf("Part 1: %d\n", solve1(inputString, map));
        System.out.printf("Part 2: %d\n", solve2(inputString, map));
    }

    static class Solution {
        String routine = null;
        String a, b, c;
        Solution(String a, String b, String c) {
            this.a = a; this.b = b; this.c = c;
        }
    }

    private static int solve2(String inputString, List<List<Character>> map) {
        String modifiedInputString = "2" + inputString.substring(1);
        int m = map.size(), n = map.get(0).size();
        int startRow = -1, startCol = -1, dir = -1;
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (isStartPosition(map.get(i).get(j))) {
                    startRow = i;
                    startCol = j;
                    dir = getDirection(map.get(i).get(j));
                }
            }
        }
//        System.out.printf("Start: %d %d, direction: %d\n", startRow, startCol, dir);
        List<String> instructions = new ArrayList<>();

        int r = startRow, c = startCol;
        while (true) {
            // try to turn left or right
            String instruction = "";
            int leftDir = (dir - 1 + 4) % 4, rightDir = (dir + 1) % 4;
            int leftRow = r + dr[leftDir], leftCol = c + dc[leftDir];
            int rightRow = r + dr[rightDir], rightCol = c + dc[rightDir];
            if (leftRow >= 0 && leftRow < m && leftCol >= 0 && leftCol < n && map.get(leftRow).get(leftCol) == '#') {
                instruction += "L,";
                dir = leftDir;
            } else if (rightRow >= 0 && rightRow < m && rightCol >= 0 && rightCol < n && map.get(rightRow).get(rightCol) == '#') {
                instruction += "R,";
                dir = rightDir;
            } else {
                break;
            }

            int countSteps = 0;
            while (true) {
                int nr = r + dr[dir];
                int nc = c + dc[dir];
                if (nr >= 0 && nr < m && nc >= 0 && nc < n && map.get(nr).get(nc) == '#') {
                    ++countSteps;
                    r = nr; c = nc;
                } else {
                    break;
                }
            }
            instruction += countSteps;
            instructions.add(instruction);
        }
        String ins = String.join(",", instructions);
        Solution sol = findRoutine(instructions);
        BigIntCodeComputer a = new BigIntCodeComputer(modifiedInputString);
        String in = sol.routine + "\n" + sol.a + "\n" + sol.b + "\n" + sol.c + "\n" + "n" + "\n";
        byte[] bytes = in.getBytes(StandardCharsets.US_ASCII);
        int[] ints = new int[bytes.length];
        for (int i = 0; i < bytes.length; ++i) ints[i] = (int) bytes[i];

        BigRunResult runResult = a.run(ints, false);
        // return the last value from the output!!!
        return runResult.output.get(runResult.output.size() - 1).intValue();
    }

    private static Solution findRoutine(List<String> ins) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < ins.size(); ++i) {
            for (int j = i; j < ins.size(); ++j) {
                StringBuilder sb = new StringBuilder();
                for (int k = i; k <= j; ++k) {
                    if (k > i) sb.append(",");
                    sb.append(ins.get(k));
                }
                String s = sb.toString();
                if (s.length() <= 20) {
                    Integer count = map.getOrDefault(s, 0);
                    ++count;
                    map.put(s, count);
                }

            }
        }
        List<String> sections = new ArrayList<>(map.keySet());
        String route = String.join(",", ins).replace(",", "");
        for (int i = 0; i < sections.size(); ++i) {
            for (int j = i + 1; j < sections.size(); ++j) {
                for (int k = j + 1; k < sections.size(); ++k) {
                    Solution solution = new Solution(sections.get(i), sections.get(j), sections.get(k));
                    backtrack(route, 0,
                        Arrays.asList(
                            sections.get(i).replace(",", ""),
                            sections.get(j).replace(",", ""),
                            sections.get(k).replace(",", "")
                        ),
                        new ArrayList<String>(),
                        solution);
                    if (solution.routine != null) {
                        return solution;
                    }
                }
            }
        }
        return null;
    }

    /** route is without commas (as well a, b and c) */
    private static void backtrack(String route, int startIndex, List<String> abc, List<String> routine, Solution solution) {
        if (startIndex == route.length()) {
            solution.routine = String.join(",", routine);
            return;
        }

        for (int i = 0; i < 3; ++i) {
            if (route.substring(startIndex, startIndex + abc.get(i).length()).equals(abc.get(i))) {
                routine.add("" + (char) ((int) 'A' + i));
                backtrack(route, startIndex + abc.get(i).length(), abc, routine, solution);
                routine.remove(routine.size() - 1);
            }
        }
    }
    // A: L,6,R,12,L,4,L,6,R,6
    // B: R,12,L,6,L,10,L,10,R,6
    // L, 6, R, 6, L, 6, R, 12, L, 6, L, 10, L, 10, R, 6


    private static boolean isStartPosition(Character c) {
        return "^>v<".indexOf(c) >= 0;
    }

    private static int getDirection(Character c) {
        return "^>v<".indexOf(c);
    }
    private static int solve1(String inputString, List<List<Character>> map) {
        BigIntCodeComputer a = new BigIntCodeComputer(inputString);
        BigRunResult runRes = a.run(new int[]{}, false);

        boolean newLine = true;
        for (BigInteger o: runRes.output) {
            Character c = (char) o.intValue();
            // this will print the map
            System.out.print(c);
            if (newLine) {
                map.add(new ArrayList<>());
                newLine = false;
            }
            if (o.intValue() == 10) {
                newLine = true;
            } else {
                map.get(map.size() - 1).add((char) o.intValue());
            }
        }
        // remove the last empty row
        map.remove(map.size() - 1);

        return computeAlignmentParameter(map);
    }


    private static int computeAlignmentParameter(List<List<Character>> map) {
        int result = 0;
        int m = map.size(), n = map.get(0).size();
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (map.get(i).get(j) != '.') {
                    int countNeighbors = 0;
                    for (int dir = 0; dir < 4; ++dir) {
                        int ni = i + dr[dir], nj = j + dc[dir];
                        if (ni >= 0 && ni < m && nj >= 0 && nj < n && map.get(ni).get(nj) != '.') {
                            ++countNeighbors;
                        }
                    }
                    if (countNeighbors == 4) {
                        result += i * j;
                    }
                }
            }
        }
        return result;
    }
}
