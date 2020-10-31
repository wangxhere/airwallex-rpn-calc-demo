package com.berrycube.demos.airwallex.rpncalc.controller;

import com.berrycube.demos.airwallex.rpncalc.domain.RpnCalculator;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CliControllerImpl implements CliController {

    private static final String DELIMITER = "\\s+";
    private static final String ERROR_MSG_TEMPLATE = "operator %s (position: %d): %s";
    private static final String UNKNOWN_ERROR = "unknown error";
    private static final String INSUFFICIENT_PARAMETERS = "insufficient parameters";
    private static final String ARITHMETIC_EXCEPTION = "arithmetic exception";
    private static final String NUM_FORMAT_EXCEPTION = "number format exception";

    private RpnCalculator calculator;

    public CliControllerImpl(RpnCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public String processInput(String line) {
        String[] tokens = line.split(DELIMITER);
        Optional<String> errorMessage = Optional.empty();

        for (int i=0; i<tokens.length; i++) {
            String token = tokens[i];
            Optional<BigDecimal> maybeDigits = tryParseDigits(token);
            maybeDigits.ifPresent(digit -> calculator.push(digit));
            if (!maybeDigits.isPresent()) {
                try {
                    handleOperator(token);
                } catch (RuntimeException ex)  {
                    errorMessage = buildErrorMessage(ex, line, tokens, i);
                    break;
                }
            }
        }
        return errorMessage.map(msg -> msg + System.lineSeparator()).orElse("") +
                String.format("stack: %s", visualize(calculator.dumpStack()));
    }

    private Optional<String> buildErrorMessage(RuntimeException ex, String line, String[] tokens, int i) {
        String errorMessage = UNKNOWN_ERROR;
        if (ex instanceof EmptyStackException) {
            errorMessage = INSUFFICIENT_PARAMETERS;
        } else if (ex instanceof ArithmeticException) {
            errorMessage = ARITHMETIC_EXCEPTION;
        } else if (ex instanceof NumberFormatException) {
            errorMessage = NUM_FORMAT_EXCEPTION;
        }
        return Optional.of(String.format(ERROR_MSG_TEMPLATE, tokens[i], backtrackFaultPosition(line, tokens, i), errorMessage));
    }

    private String visualize(List<BigDecimal> dumpStack) {
        return dumpStack.stream().map(this::visualize).collect(Collectors.joining(" "));
    }

    private int backtrackFaultPosition(String line, String[] tokens, int failingTokenPos) {
        int charCountTokensBeforeFailure = Arrays.stream(tokens)
            .limit(failingTokenPos)
            .mapToInt(String::length)
            .sum();
        int charCountDelimitersBeforeFailure = Arrays.stream(line.split("\\S+"))
            .limit(failingTokenPos + 1)
            .mapToInt(String::length)
            .sum();
        return charCountDelimitersBeforeFailure + charCountTokensBeforeFailure + 1;
    }

    private String visualize(BigDecimal bd) {
        bd = bd.setScale(10, BigDecimal.ROUND_DOWN);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(10);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);
        return df.format(bd);
    }

    private Optional<BigDecimal> tryParseDigits(String token) {
        try {
            return Optional.of(new BigDecimal(token));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    private void handleOperator(String token) {
        switch (token) {
            case "+":
                calculator.add();
                break;
            case "-":
                calculator.sub();
                break;
            case "*":
                calculator.mul();
                break;
            case "/":
                calculator.div();
                break;
            case "sqrt":
                calculator.sqrt();
                break;
            case "undo":
                calculator.undo();
                break;
            case "clear":
                calculator.clear();
                break;
            default:
                throw new IllegalArgumentException(String.format("no such operator %s", token));
        }
    }

}
