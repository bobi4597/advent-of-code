package com.github.bobi4597.adventofcode.y2019;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Year2019Day24 {

    private static int N = 5;
    private static int startPosition;
    private static int[] di = {-1, 0, 0, 1};
    private static int[] dj = {0, -1, 1, 0};

    private static int N2 = 203;
    private static int MINUTES = 200;
    private static int[][][] a = new int[N2][N][N], b = new int[N2][N][N];

    // neighbours for part 2 :-). Each element is: [delta level, i, j]
    private static int[][][][] d = new int[5][5][][];
    static {
        // outer circle
        d[0][0] = new int[][] {{0, 0, 1},            {0, 1, 0}, {-1, 2, 1}, {-1, 1, 2}};
        d[0][1] = new int[][] {{0, 0, 0}, {0, 0, 2}, {0, 1, 1},             {-1, 1, 2}};
        d[0][2] = new int[][] {{0, 0, 1}, {0, 0, 3}, {0, 1, 2},             {-1, 1, 2}};
        d[0][3] = new int[][] {{0, 0, 2}, {0, 0, 4}, {0, 1, 3},             {-1, 1, 2}};
        d[0][4] = new int[][] {{0, 0, 3},            {0, 1, 4}, {-1, 2, 3}, {-1, 1, 2}};

        d[1][0] = new int[][] {{0, 0, 0}, {0, 2, 0}, {0, 1, 1}, {-1, 2, 1}};
        d[2][0] = new int[][] {{0, 1, 0}, {0, 3, 0}, {0, 2, 1}, {-1, 2, 1}};
        d[3][0] = new int[][] {{0, 2, 0}, {0, 4, 0}, {0, 3, 1}, {-1, 2, 1}};

        d[1][4] = new int[][] {{0, 0, 4}, {0, 2, 4}, {0, 1, 3}, {-1, 2, 3}};
        d[2][4] = new int[][] {{0, 1, 4}, {0, 3, 4}, {0, 2, 3}, {-1, 2, 3}};
        d[3][4] = new int[][] {{0, 2, 4}, {0, 4, 4}, {0, 3, 3}, {-1, 2, 3}};

        d[4][0] = new int[][] {{0, 4, 1},            {0, 3, 0}, {-1, 2, 1}, {-1, 3, 2}};
        d[4][1] = new int[][] {{0, 4, 0}, {0, 4, 2}, {0, 3, 1},             {-1, 3, 2}};
        d[4][2] = new int[][] {{0, 4, 1}, {0, 4, 3}, {0, 3, 2},             {-1, 3, 2}};
        d[4][3] = new int[][] {{0, 4, 2}, {0, 4, 4}, {0, 3, 3},             {-1, 3, 2}};
        d[4][4] = new int[][] {{0, 4, 3},            {0, 3, 4}, {-1, 2, 3}, {-1, 3, 2}};

        // inner circle (corners)
        d[1][1] = new int [][] {{0, 0, 1}, {0, 2, 1}, {0, 1, 0}, {0, 1, 2}};
        d[1][3] = new int [][] {{0, 0, 3}, {0, 2, 3}, {0, 1, 2}, {0, 1, 4}};
        d[3][1] = new int [][] {{0, 2, 1}, {0, 4, 1}, {0, 3, 0}, {0, 3, 2}};
        d[3][3] = new int [][] {{0, 2, 3}, {0, 4, 3}, {0, 3, 2}, {0, 3, 4}};

        // inner circle (not corners)
        d[1][2] = new int[][] {{0, 1, 1}, {0, 1, 3}, {0, 0, 2},   {1, 0, 0}, {1, 0, 1}, {1, 0, 2}, {1, 0, 3}, {1, 0, 4}};
        d[3][2] = new int[][] {{0, 3, 1}, {0, 3, 3}, {0, 4, 2},   {1, 4, 0}, {1, 4, 1}, {1, 4, 2}, {1, 4, 3}, {1, 4, 4}};
        d[2][1] = new int[][] {{0, 1, 1}, {0, 3, 1}, {0, 2, 0},   {1, 0, 0}, {1, 1, 0}, {1, 2, 0}, {1, 3, 0}, {1, 4, 0}};
        d[2][3] = new int[][] {{0, 1, 3}, {0, 3, 3}, {0, 2, 4},   {1, 0, 4}, {1, 1, 4}, {1, 2, 4}, {1, 3, 4}, {1, 4, 4}};

        // middle point
        d[2][2] = new int[][] {};
    }

    public static void main(String[] args) {
        readInput();
        System.out.printf("%d\n", solve1());
        System.out.printf("%d\n", solve2());
    }

    private static void readInput() {
        startPosition = 0;
        int pow = 1;
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < 5; ++i) {
            String line = scanner.nextLine();
            for (int j = 0; j < 5; ++j) {
                if (line.charAt(j) == '#') {
                    a[N2 / 2][i][j] = 1;
                    startPosition += pow;
                }
                pow *= 2;
            }
        }
    }

    //////////////// part 1 /////////////////
    private static int solve1() {
        Map<Integer, Integer> map = new HashMap<>();
        int x = startPosition;
        int index = 0;
        while (!map.containsKey(x)) {
            map.put(x, index++);
            x = next(x);
            //print(x);
        }
        return x;
    }

    private static int next(int x) {
        int y = 0;

        for (int i1 = 0; i1 < N; ++i1) for (int j1 = 0; j1 < N; ++j1) {
            int index1 = i1 * N + j1;
            int x1 = x & (1 << index1);
            int bugs = 0;
            for (int d = 0; d < 4; ++d) {
                int i2 = i1 + di[d];
                int j2 = j1 + dj[d];
                if (i2 >= 0 && i2 < N && j2 >= 0 && j2 < N) {
                    int index2 = i2 * N + j2;
                    int x2 = x & (1 << index2);
                    if (x2 != 0) {
                        ++bugs;
                    }
                }
            }

            if (x1 != 0) {
                if (bugs == 1) {
                    // bug -> bug: 1 bug around the bug
                    y = y | (1 << index1);
                } else {
                    // bug -> empty: 0, 2, 3 bugs around the bug
                    y = y & (~ (1 << index1));
                }
            } else {
                if (bugs == 1 || bugs == 2) {
                    // empty -> bug: 1 or 2 bugs around the empty
                    y = y | (1 << index1);
                } else {
                    // empty -> empty: 0 or 3 bugs around the empty
                    y = y & (~(1 << index1));
                }
            }
        }
        return y;
    }

    private static void print(int x) {
        for (int i = 0; i < 25; ++i) {
            System.out.printf("%s", (x & (1 << i)) == 0 ? "." : "#");
            if (i % 5 == 4) {
                System.out.println();
            }
        }
        System.out.println();
    }

    ////////////// part 2 //////////////////
    private static int solve2() {
        for (int t = 0; t < MINUTES; ++t) {
            for (int level = 1; level < N2 - 1; ++level) {
                for (int i = 0; i < N; ++i) {
                    for (int j = 0; j < N; ++j) {
                        int[][] neighbours = d[i][j];
                        int bugs = 0;
                        for (int k = 0; k < neighbours.length; ++k) {
                            int deltaLevel = neighbours[k][0];
                            int ni = neighbours[k][1];
                            int nj = neighbours[k][2];
                            if (a[level + deltaLevel][ni][nj] == 1) {
                                ++bugs;
                            }
                        }

                        if (a[level][i][j] == 1) {
                            if (bugs == 1) {
                                // bug -> bug: 1 bug around the bug
                                b[level][i][j] = 1;
                            } else {
                                // bug -> empty: 0, 2, 3 bugs around the bug
                                b[level][i][j] = 0;
                            }
                        } else {
                            if (bugs == 1 || bugs == 2) {
                                // empty -> bug: 1 or 2 bugs around the empty
                                b[level][i][j] = 1;
                            } else {
                                // empty -> empty: 0 or 3 bugs around the empty
                                b[level][i][j] = 0;
                            }
                        }
                    }
                }
            }
            // move b -> a, and clear b
            for (int level = 1; level < N2 - 1; ++level) {
                for (int i = 0; i < N; ++i) {
                    for (int j = 0; j < N; ++j) {
                        a[level][i][j] = b[level][i][j];
                        b[level][i][j] = 0;
                    }
                }
            }
        }
        // count bugs
        int bugs = 0;
        for (int level = 1; level < N2 - 1; ++level) {
            for (int i = 0; i < N; ++i) {
                for (int j = 0; j < N; ++j) {
                    if (a[level][i][j] == 1) {
                        ++bugs;
                    }
                }
            }
        }
        //printA();
        return bugs;
    }

    private static void printA() {
        for (int level = 0; level < N2; ++level) {
            for (int i = 0; i < N; ++i) {
                for (int j = 0; j < N; ++j) {
                    System.out.printf("%s", a[level][i][j] == 1 ? "#" : ".");
                }
                System.out.println();
            }
            System.out.println();
        }

    }
}
