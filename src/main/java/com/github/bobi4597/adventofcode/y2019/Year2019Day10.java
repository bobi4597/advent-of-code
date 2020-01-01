package com.github.bobi4597.adventofcode.y2019;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Year2019Day10 {

    private static int bestX = -1, bestY = -1;

    static class Station {
        int x,y, distance;
        boolean isDestroyed;
        Station(int x, int y, int distance) {
            this.x = x; this.y = y; this.distance = distance;
            this.isDestroyed = false;
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + "]";
        }
    }

    public static void main(String[] args) {
        List<String> input = readInput();
        int m = input.size();
        int n = input.get(0).length();
        System.out.printf("%d\n", solve1(input, m, n));
        System.out.printf("%d\n", solve2(input, m, n, 200));
    }

    private static List<String> readInput() {
        List<String> input = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            input.add(scanner.nextLine());
        }
        return input;
    }

    private static int solve1(List<String> input, int m, int n) {

        int max = 0;

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (input.get(i).charAt(j) == '#') {
                    int count = countVisible(input, m, n, i, j);
                    if (max < count) {
                        max = count;
                        bestY = i;
                        bestX = j;
                    }
                }
            }
        }
        System.out.printf("Best station: [%d, %d]\n", bestX, bestY);
        return max;
    }

    private static int countVisible(List<String> input, int m, int n, int i, int j) {
        int count = 0;
        for (int i1 = 0; i1 < m; ++i1) {
            for (int j1 = 0; j1 < n; ++j1) {
                if (input.get(i1).charAt(j1) == '#' && !(i1 == i && j1 == j)) {
                    // we've found another asteroid at location [i1, j1]
                    // lets check if there are other asteroids in between [i, j] and [i1, j1]
                    int di = Math.abs(i1 - i);
                    int dj = Math.abs(j1 - j);
                    int gcd = gcd(di, dj);
                    int stepi = di / gcd;
                    int stepj = dj / gcd;
                    if (i1 < i) stepi = -stepi;
                    if (j1 < j) stepj = -stepj;

                    boolean isVisible = true;
                    for (int i2 = i + stepi, j2 = j + stepj; i2 != i1 || j2 != j1; i2 += stepi, j2 += stepj) {
                        if (input.get(i2).charAt(j2) == '#') {
                            isVisible = false;
                            break;
                        }
                    }
                    if (isVisible) {
                        ++count;
                    }
                }
            }
        }
        return count;
    }


    private static int gcd(int a, int b) {
        if (a < b) {
            return gcd(b, a);
        }
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    private static int solve2(List<String> input, int m, int n, int maxDestroy) {
        List<Station> stations = new ArrayList<>();
        for (int y = 0; y < m; ++y) {
            for (int x = 0; x < n; ++x) {
                if (!(x == bestX && y == bestY) && input.get(y).charAt(x) == '#') {
                    Station station = new Station(x, y, (y - bestY) * (y - bestY) + (x - bestX) * (x - bestX));
                    stations.add(station);
                }
            }
        }

        Comparator<Station> stationComparator = (s1, s2) -> {
            int x1 = s1.x - bestX, y1 = bestY - s1.y, x2 = s2.x - bestX, y2 = bestY - s2.y;
            int q1 = getQuadrant(x1, y1), q2 = getQuadrant(x2, y2);
            // 1. first compare by quadrant
            if (q1 != q2) return q1 - q2;
            int comparison = 0;
            if (q1 == 1) comparison = x1 * y2 - y1 * x2;
            if (q1 == 2) comparison = -y1 * x2 - (x1 * -y2);
            if (q1 == 3) comparison = x1 * y2 - y1 * x2;
            if (q1 == 4) comparison = (y1 * -x2) - (y2 * -x1);

            if (comparison != 0) {
                // 2. second compare by the angle
                return comparison;
            } else {
                // 3. if the angle is the same, then compare the distance
                return s1.distance - s2.distance;
            }
        };

        stations.sort(stationComparator);

        int lastDestroyed = -1;
        int destroyed = 0;
        int i = 0;

        while (destroyed < maxDestroy) {
            // find the next station that is not destroyed
            while (stations.get(i).isDestroyed) {
                i = (i + 1) % stations.size();
            }
            // destroy it
            stations.get(i).isDestroyed = true;
            lastDestroyed = i;
            ++destroyed;
            // skip the ones that are behind the newly destroyed one (i.e. have the same angle)
            int j = (i + 1) % stations.size();
            while ((stations.get(i).x - bestX) * (bestY - stations.get(j).y) == (bestY - stations.get(i).y) * (stations.get(j).x - bestX)) {
                j = (j + 1) % stations.size();
            }
            i = j;
        }
        Station last = stations.get(lastDestroyed);
        System.out.printf("Last destroyed: [%d, %d]\n", last.x, last.y);
        return last.x * 100 + last.y;
    }

    private static int getQuadrant(int x, int y) {
        if (y > 0 && x >= 0) return 1;
        if (y <= 0 && x > 0) return 2;
        if (y < 0 && x <= 0) return 3;
        if (y >= 0 && x < 0) return 4;
        return 1;
    }


}
