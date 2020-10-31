package com.berrycube.demos.airwallex.rpncalc;

import com.berrycube.demos.airwallex.rpncalc.controller.CliController;
import com.berrycube.demos.airwallex.rpncalc.controller.CliControllerImpl;
import com.berrycube.demos.airwallex.rpncalc.domain.RpnCalculator;
import com.berrycube.demos.airwallex.rpncalc.domain.RpnCalculatorImpl;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    private static final String PROMPT = "Input empty line to terminate.";
    private static final String EXIT_MSG = "Thank you for using.";

    public static void main(String[] args) {
	    // design consideration: given this is a CLI without much scalable concerns, stack size is not explicitly controlled
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculator calculator = new RpnCalculatorImpl(history, stack);

        CliController cli = new CliControllerImpl(calculator);

        System.out.println(PROMPT);

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        while (! line.isEmpty()) {
            System.out.println(cli.processInput(line));
            line = scanner.nextLine();
        };

        System.out.println(EXIT_MSG);

        scanner.close();
    }
}
