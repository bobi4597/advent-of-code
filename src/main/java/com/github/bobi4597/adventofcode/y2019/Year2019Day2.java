package com.github.bobi4597.adventofcode.y2019;

import java.util.Scanner;

public class Year2019Day2 {
    /** Part 1 */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        System.out.printf("%s\n", line);
        String[] input = line.split(",");
        int n = input.length;
        int[] a = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = Integer.parseInt(input[i]);
        }
        a[1] = 12; a[2] = 2;

        int i = 0;
        while (a[i] != 99) {
            if (a[i] == 1) {
                a[a[i + 3]] = a[a[i + 1]] + a[a[i + 2]];
            } else {
                a[a[i + 3]] = a[a[i + 1]] * a[a[i + 2]];
            }
            i += 4;
        }

        System.out.printf("%d\n", a[0]);
    }

    /** Part 2 */
    public static void main2(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        System.out.printf("%s\n", line);
        String[] input = line.split(",");
        int n = input.length;
        int[] original = new int[n];
        for (int i = 0; i < n; ++i) {
            original[i] = Integer.parseInt(input[i]);
        }
        int[] a = new int[n];

        for (int noun = 0; noun < 100; ++noun) {
            for (int verb = 0; verb < 100; ++verb) {
                System.arraycopy(original, 0, a, 0, n);
                a[1] = noun; a[2] = verb;

                int i = 0;
                while (a[i] != 99) {
                    if (a[i] == 1) {
                        a[a[i + 3]] = a[a[i + 1]] + a[a[i + 2]];
                    } else {
                        a[a[i + 3]] = a[a[i + 1]] * a[a[i + 2]];
                    }
                    i += 4;
                }

                if (a[0] == 19690720) {
                    System.out.printf("%d\n", 100 * noun + verb);
                }

            }
        }
    }
}
