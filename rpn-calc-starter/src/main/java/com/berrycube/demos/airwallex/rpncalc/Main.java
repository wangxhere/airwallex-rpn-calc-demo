package com.berrycube.demos.airwallex.rpncalc;

import com.berrycube.demos.airwallex.rpncalc.controller.CliControllerImpl;
import com.berrycube.demos.airwallex.rpncalc.domain.RpnCalculator;
import com.berrycube.demos.airwallex.rpncalc.domain.RpnCalculatorImpl;

import java.math.BigDecimal;
import java.util.Stack;

public class Main {

    public static void main(String[] args) {
	    // design consideration: given this is a CLI without much scalable concerns, stack size is not explicitly controlled
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculator calculator = new RpnCalculatorImpl(history, stack);

        CliControllerImpl uut = new CliControllerImpl(calculator);
    }
}
