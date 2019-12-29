package com.github.bobi4597.adventofcode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.bobi4597.adventofcode.BigIntCodeComputer.readInput;

public class Year2019Day13 {

    private static int LEFT = -1, RIGHT = 1, NEUTRAL = 0, PADDLE = 3, BALL = 4;

    public static void main(String[] args) {
        String inputString = readInput();
        System.out.printf("Part 1: %d\n", solve1(inputString));
        System.out.printf("Part 2: %d\n", solve2(inputString));
    }

    private static int solve1Simple(String inputString) {
        BigIntCodeComputer a = new BigIntCodeComputer(inputString);
        BigRunResult runResult = a.run(new int[0], false);
        List<BigInteger> o = runResult.output;
        int countBlocks = 0;
        for (int i = 0; i < o.size(); i += 3) {
            int tileId = o.get(i + 2).intValue();
            if (tileId == 2) {
                ++countBlocks;
            }
        }
        return countBlocks;
    }

    private static int solve1(String inputString) {
        BigIntCodeComputer a = new BigIntCodeComputer(inputString);
        BigRunResult runResult = a.run(new int[0], false);
        List<BigInteger> o = runResult.output;
        int countBlocks = 0;
        List<List<Integer>> triplets = new ArrayList<>();
        int maxX = 0, maxY = 0;
        for (int i = 0; i < o.size(); i += 3) {
            int x = o.get(i).intValue();
            int y = o.get(i + 1).intValue();
            int tileId = o.get(i + 2).intValue();
            triplets.add(Arrays.asList(y, x, tileId));
            maxX = Math.max(x, maxX);
            maxY = Math.max(y, maxY);

            if (tileId == 2) {
                ++countBlocks;
            }
        }

        int[][] game = new int[maxY + 1][maxX + 1];
        for (List<Integer> t: triplets) {
            game[t.get(0)][t.get(1)] = t.get(2);
        }
        String printedGame = printGame(game);
        System.out.println(printedGame);
        return countBlocks;
    }

    private static int solve2(String inputString) {
        String modifiedInputString = "2" + inputString.substring(1);
        BigIntCodeComputer a = new BigIntCodeComputer(modifiedInputString);

        int joystickPosition = 0;
        int ballX = 0, paddleX = 0;
        int score = 0;

        while (true) {
            BigRunResult runResult = a.run(new int[] {joystickPosition}, true);
            if (runResult.isDone) {
                break;
            }
            int x = runResult.output.get(0).intValue();
            int y = a.run(new int[] {joystickPosition}, true).output.get(0).intValue();
            int tileId = a.run(new int[] {joystickPosition}, true).output.get(0).intValue();

            if (x == -1 && y == 0) {
                score = tileId;
            } else {
                if (tileId == PADDLE) {
                    paddleX = x;
                } else if (tileId == BALL) {
                    ballX = x;
                }
            }

            joystickPosition = ballX == paddleX ? NEUTRAL : (paddleX < ballX ? RIGHT : LEFT);
        }
        return score;
    }

    /**
     * Needed only to see what the game looks like.
     * It is Arkanoid :-)  (see bellow)
     */
    private static String printGame(int[][] game) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < game.length; ++y) {
            for (int x = 0; x < game[y].length; ++x) {
                int t = game[y][x];
                if (t == 0) sb.append(" "); // empty
                if (t == 1) sb.append("#"); // Wall
                if (t == 2) sb.append("\u25a1"); // block
                if (t == 3) sb.append("_"); // horizontal paddle
                if (t == 4) sb.append("o"); // ball
            }
            sb.append("\n");
        }
        return sb.toString();
    }
//    ######################################
//    #                                    #
//    #    □□□ □□□  □□□ □ □□ □□□□□ □□□     #
//    #  □□□□□□□□ □□  □□  □□□□     □□□□  □ #
//    #  □□□ □□□□□□□ □ □□   □□ □□ □□□□□□□□ #
//    #   □ □□□ □□□□□□□□ □ □□□□□□□□□□□□ □□ #
//    #  □  □□□ □□□□ □ □ □□□□□□□  □□□□□ □□ #
//    # □□ □ □□ □□□□ □□ □□ □□□□□□ □□□ □ □  #
//    # □□□□□□□□ □ □□□   □ □□□ □□□□□  □□ □ #
//    #  □□ □□□□ □□□□□□□□□□□□□□□  □ □□□□□□ #
//    # □ □□□ □□□  □□□□   □□□  □  □□□□ □   #
//    # □□□□□□    □□□□□□     □□□□ □□□□□ □  #
//    # □□□ □□  □□□□  □□□  □□ □ □ □ □□ □□□ #
//    #   □□□□□□□□□□□□    □  □□□  □□□ □□□□ #
//    #                                    #
//    #                o                   #
//    #                                    #
//    #                                    #
//    #                  _                 #
//    #                                    #
}
