package com.github.bobi4597.adventofcode.y2019;

import java.util.Scanner;

public class Year2019Day1 {

    /** Part 1 */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int sum = 0;
        while (scanner.hasNextInt()) {
            int mass = scanner.nextInt();
            sum += mass / 3 - 2;
        }

        System.out.printf("%d\n", sum);
    }

    /** Part 2 */
    public static void main2(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int total = 0;
        while (scanner.hasNextInt()) {
            int mass = scanner.nextInt();
            int sum = 0;
            while (mass > 0) {
                mass = mass / 3 - 2;
                if (mass > 0) {
                    sum += mass;
                }
            }

            total += sum;
        }

        System.out.printf("%d\n", total);
    }
}
