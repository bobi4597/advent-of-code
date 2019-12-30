package com.github.bobi4597.adventofcode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.github.bobi4597.adventofcode.BigIntCodeComputer.readInput;
import static java.math.BigInteger.ZERO;

public class Year2019Day23 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String inputString = readInput();
//        System.out.printf("Part 1: %d\n", solve1(inputString));
         System.out.printf("Part 2: %d\n", solve2(inputString));
    }

    static class XY {
        BigInteger x, y;
        XY() {
            x = ZERO; y = ZERO;
        }
    }

    private static int solve2(String inputString) throws ExecutionException, InterruptedException {
        ConcurrentMap<Integer, Integer> idleSet = new ConcurrentHashMap<>();
        final XY xy = new XY();

        // init computers
        BigIntQueueCodeComputer[] a = new BigIntQueueCodeComputer[50];
        for (int i = 0; i < 50; ++i) {
            a[i] = new BigIntQueueCodeComputer(inputString, i, idleSet, 3);
            a[i].addInput(Collections.singletonList(BigInteger.valueOf(i)));
        }

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
        scheduler.scheduleAtFixedRate(() -> {
            //System.out.println("inside the scheduler");
            if (idleSet.size() == 50 && xy.x != null) {
                System.out.printf("NAT sends to address 0: (%d, %d)\n", xy.x.intValue(), xy.y.intValue());
                a[0].addInput(Arrays.asList(xy.x, xy.y));
                xy.x = null;
                xy.y = null;

            }
        }, 1000, 10, TimeUnit.MILLISECONDS);


        // run the computers
        List<Callable<Integer>> callables = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 50; ++i) {
            final BigIntQueueCodeComputer ai = a[i];
            Callable<Integer> callable = () -> {
                BigRunResult runResult = null;
                while (true) {
                    if (runResult != null) {
                        if (!runResult.isDone) {
                            int destinationAddress = runResult.output.get(0).intValue();
                            BigInteger x = runResult.output.get(1);
                            BigInteger y = runResult.output.get(2);
                            if (destinationAddress == 255) {
                                xy.x = x;
                                xy.y = y;
                            } else {
                                a[destinationAddress].addInput(Arrays.asList(x, y));
                            }
                        }
                    }
                    runResult = ai.run(null, true);
                }
            };
            callables.add(callable);
        }
        executorService.invokeAll(callables);
        return -1;
    }


    private static int solve1(String inputString) throws ExecutionException, InterruptedException {
        // init computers
        BigIntQueueCodeComputer[] a = new BigIntQueueCodeComputer[50];
        for (int i = 0; i < 50; ++i) {
            a[i] = new BigIntQueueCodeComputer(inputString, 3);
            a[i].addInput(Collections.singletonList(BigInteger.valueOf(i)));
        }

        // run the computers
        List<Callable<Integer>> callables = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 50; ++i) {
            final BigIntQueueCodeComputer ai = a[i];
            Callable<Integer> callable = () -> {
                BigRunResult runResult = null;
                while (true) {
                    if (runResult != null) {
                        if (!runResult.isDone) {
                            int destinationAddress = runResult.output.get(0).intValue();
                            BigInteger x = runResult.output.get(1);
                            BigInteger y = runResult.output.get(2);
                            if (destinationAddress == 255) {
                                System.out.printf("First packet to 255: y = %d\n", y.intValue());
                                return y.intValue();
                            } else {
                                a[destinationAddress].addInput(Arrays.asList(x, y));
                            }
                        }
                    }
                    runResult = ai.run(null, true);
                }
            };
            callables.add(callable);
        }
        List<Future<Integer>> futures = executorService.invokeAll(callables);

        return -1;
    }
}
