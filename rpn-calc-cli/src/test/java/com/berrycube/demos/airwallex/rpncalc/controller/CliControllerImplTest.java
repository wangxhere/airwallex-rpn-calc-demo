package com.berrycube.demos.airwallex.rpncalc.controller;

import com.berrycube.demos.airwallex.rpncalc.core.RpnCalculator;
import com.berrycube.demos.airwallex.rpncalc.core.RpnCalculatorImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CliControllerImplTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/cliControllerImplTest.csv", numLinesToSkip = 1)
    void processInput(String input, String output) {
        List<String> inputLines = Arrays.stream(input.split("\\n")).map(String::trim).collect(Collectors.toList());
        List<String> outputLines = Arrays.stream(output.split("\\n")).map(String::trim).collect(Collectors.toList());

        // skipping unit test but directly providing integration test
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculator calculator = new RpnCalculatorImpl(history, stack);

        CliControllerImpl uut = new CliControllerImpl(calculator);
        for (int i=0; i<inputLines.size(); i++) {
            String response = uut.processInput(inputLines.get(i));
            assertEquals(outputLines.get(i), response.replace(System.lineSeparator(), "||").trim());
        }
    }
}