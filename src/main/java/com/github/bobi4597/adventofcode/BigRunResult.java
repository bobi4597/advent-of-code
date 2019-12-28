package com.github.bobi4597.adventofcode;

import java.math.BigInteger;
import java.util.List;

public class BigRunResult {
    public boolean isDone;
    public List<BigInteger> output;

    public BigRunResult(boolean isDone, List<BigInteger> output) {
        this.isDone = isDone;
        this.output = output;
    }
}