package com.github.bobi4597.adventofcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Year2019Day12 {
    private static int[][] p = new int[4][3];
    private static int[][] v = new int[4][3];
    private static final int STEPS = 1000;

    // my input
    static {
        p[0] = new int[] {3, 2, -6};
        p[1] = new int[] {-13, 18, 10};
        p[2] = new int[] {-8, -1, 13};
        p[3] = new int[] {5, 10, 4};
    }

    public static void main(String[] args) {
        System.out.printf("Total Energy: %s\n", solve1());
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
}
