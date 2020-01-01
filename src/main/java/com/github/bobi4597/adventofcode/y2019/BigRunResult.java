package com.github.bobi4597.adventofcode.y2019;

import java.math.BigInteger;
import java.util.List;

public class BigRunResult {
    public boolean isDone;
    public List<BigInteger> output;

    public BigRunResult(boolean isDone, List<BigInteger> output) {
        this.isDone = isDone;
        this.output = output;
    }

    public void printAscii() {
        System.out.println(toAscii());
    }

    public String toAscii() {
        StringBuilder sb = new StringBuilder();
        for (BigInteger bigInt: output) {
            int out = bigInt.intValue();
            sb.append((char) out);
        }
        return sb.toString();
    }
}
