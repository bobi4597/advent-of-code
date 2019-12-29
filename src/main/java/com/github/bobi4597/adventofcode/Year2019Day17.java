package com.github.bobi4597.adventofcode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.github.bobi4597.adventofcode.BigIntCodeComputer.readInput;

public class Year2019Day17 {

    private static int[] di = {-1, 1, 0, 0};
    private static int[] dj = {0, 0, -1, 1};

    public static void main(String[] args) {
        String inputString = readInput();
        System.out.printf("Part 1: %d\n", solve1(inputString));
    }

    private static int solve1(String inputString) {
        BigIntCodeComputer a = new BigIntCodeComputer(inputString);
        BigRunResult runRes = a.run(new int[]{}, false);
        List<List<Character>> map = new ArrayList<>();
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
                        int ni = i + di[dir], nj = j + dj[dir];
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
