package com.berrycube.demos.airwallex.rpncalc.core;

import java.math.BigDecimal;
import java.util.List;

public interface RpnCalculator {
    void push(BigDecimal operand);
    void add();
    void sub();
    void mul();
    void div();
    void sqrt();
    void undo();
    void clear();
    List<BigDecimal> dumpStack();
}
