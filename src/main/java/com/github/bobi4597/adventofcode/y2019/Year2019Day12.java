package com.github.bobi4597.adventofcode.y2019;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Year2019Day12 {
    private static int[][] p = new int[4][3];
    private static int[][] v = new int[4][3];
    private static final int STEPS = 10;

    private static final Map<Integer, Map<String, Integer>> xyzMap = new HashMap<>();
    private static final Map<Integer, Long> done = new HashMap<>();

    static {
        for (int i = 0; i < 3; ++i) xyzMap.put(i, new HashMap<>());

        // My input
        p[0] = new int[] {3, 2, -6};
        p[1] = new int[] {-13, 18, 10};
        p[2] = new int[] {-8, -1, 13};
        p[3] = new int[] {5, 10, 4};

//        // Example 1
//        p[0] = new int[] {-1, 0, 2};
//        p[1] = new int[] {2, -10, -7};
//        p[2] = new int[] {4, -8, 8};
//        p[3] = new int[] {3, 5, -1};
//
//        // Example 2
//        p[0] = new int[] {-8, -10, 0};
//        p[1] = new int[] {5, 5, 10};
//        p[2] = new int[] {2, -7, 3};
//        p[3] = new int[] {9, -8, -3};
    }

    public static void main(String[] args) {
        System.out.printf("Total Energy: %s\n", solve1());
        System.out.printf("Same position after: %d\n", solve2());
    }

    private static int solve1() {
        for (int step = 0; step < STEPS; ++step) {
            // update velocities
            for (int i1 = 0; i1 < 4; ++i1) {
                for (int i2 = i1 + 1; i2 < 4; ++i2) {
                    for (int j = 0; j < 3; ++j) {
                        if (p[i1][j] < p[i2][j]) {
                            v[i1][j] += 1;
                            v[i2][j] -= 1;
                        } else if (p[i1][j] > p[i2][j]){
                            v[i1][j] -= 1;
                            v[i2][j] += 1;
                        }
                    }
                }
            }
            // update positions
            for (int i = 0; i < 4; ++i) {
                for (int j = 0; j < 3; ++j) {
                    p[i][j] += v[i][j];
                }
            }
        }

        List<Integer> potentialEnergies = Arrays.stream(p)
            .map(moonPositions -> Arrays
                .stream(moonPositions)
                .reduce(0, (total, moonPosition) -> total + Math.abs(moonPosition)))
            .collect(Collectors.toList());

        List<Integer> kineticEnergies = Arrays.stream(v)
            .map(moonVelocities -> Arrays
                .stream(moonVelocities)
                .reduce(0, (total, moonVelocity) -> total + Math.abs(moonVelocity)))
            .collect(Collectors.toList());

        int totalEnergy = 0;
        for (int i = 0 ; i < 4; ++i) {
            totalEnergy += potentialEnergies.get(i) * kineticEnergies.get(i);
        }
        return totalEnergy;
    }

    /////////////////// part 2 /////////////////////
    /** Main observation: each dimension (x, y or z) is independent of each other. */
    private static long solve2() {

        for (int j = 0; j < 3; ++j) {
            xyzMap.get(j).put(toXYZKey(j), 0);
        }

        int step = 0;
        while (true) {
            // update velocities
            for (int i1 = 0; i1 < 4; ++i1) {
                for (int i2 = i1 + 1; i2 < 4; ++i2) {
                    for (int j = 0; j < 3; ++j) {
                        if (p[i1][j] < p[i2][j]) {
                            v[i1][j] += 1;
                            v[i2][j] -= 1;
                        } else if (p[i1][j] > p[i2][j]){
                            v[i1][j] -= 1;
                            v[i2][j] += 1;
                        }
                    }
                }
            }
            ++step;

            // update positions
            for (int i = 0; i < 4; ++i) {
                for (int j = 0; j < 3; ++j) {
                    p[i][j] += v[i][j];
                }
            }

            for (int j = 0; j < 3; ++j) {
                String key = toXYZKey(j);
                if (done.containsKey(j)) {
                    continue;
                }
                if (xyzMap.get(j).containsKey(key)) {
                    int prevStep = xyzMap.get(j).get(key);
                    System.out.printf("Found repetition for dimension %d: prevStep: %d, currentStep: %d, duration: %d \n", j, prevStep, step, step - prevStep);
                    done.put(j, (long) step - prevStep);
                } else {
                    xyzMap.get(j).put(key, step);
                }
            }
            if (done.size() == 3) {
                long g = gcd(gcd(done.get(0), done.get(1)), done.get(2));
                return (done.get(0) / g) * (done.get(1) / g) * (done.get(2) / g) * g;
            }
        }
    }

    private static String toXYZKey(int j) {
        StringBuilder xyzKey = new StringBuilder();
        for (int i = 0; i < 4; ++i) {
            xyzKey.append(p[i][j]);
            xyzKey.append(":");
        }
        for (int i = 0; i < 4; ++i) {
            xyzKey.append(v[i][j]);
            if (i < 3) {
                xyzKey.append(":");
            }
        }
        return xyzKey.toString();
    }

    private static long gcd(long a, long b) {
        if (a < b) {
            return gcd(b, a);
        }
        if (b == 0L) return a;
        return gcd(b, a % b);
    }
}
