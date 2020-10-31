package com.berrycube.demos.airwallex.rpncalc.domain;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class RpnCalculatorImpl implements RpnCalculator {

    private Stack<Stack<BigDecimal>> history;
    private Stack<BigDecimal> stack;

    public RpnCalculatorImpl(Stack<Stack<BigDecimal>> history, Stack<BigDecimal> stack) {
        this.history = history;
        this.stack = stack;
    }

    @Override
    public void push(BigDecimal operand) {
        saveHistory();
        stack.push(operand);
    }

    @Override
    public void add() {
        saveHistory();
        Stack<BigDecimal> operands = getOperands(2);
        stack.push(operands.pop().add(operands.pop()));
    }

    @Override
    public void sub() {
        saveHistory();
        Stack<BigDecimal> operands = getOperands(2);
        stack.push(operands.pop().subtract(operands.pop()));
    }

    @Override
    public void mul() {
        saveHistory();
        Stack<BigDecimal> operands = getOperands(2);
        stack.push(operands.pop().multiply(operands.pop()));
    }

    @Override
    public void div() {
        saveHistory();
        Stack<BigDecimal> operands = getOperands(2);
        stack.push(operands.pop().divide(operands.pop(), MathContext.DECIMAL128));
    }

    @Override
    public void sqrt() {
        saveHistory();
        Stack<BigDecimal> operands = getOperands(1);
        stack.push(sqrt(operands.pop()));
    }

    @Override
    public void undo() {
        try {
            stack = history.pop();
        } catch (EmptyStackException ignored) {

        }
    }

    @Override
    public void clear() {
        saveHistory();
        while (!stack.isEmpty()) {
            stack.pop();
        }
    }

    @Override
    public List<BigDecimal> dumpStack() {
        BigDecimal[] buffer = new BigDecimal[stack.size()];
        return Arrays.asList(stack.toArray(buffer));
    }

    private Stack<BigDecimal> getOperands(int num) {
        Stack<BigDecimal> operands = new Stack<>();
        try {
            for (int i=0; i<num; i++) {
                operands.push(stack.pop());
            }
        } catch (EmptyStackException ex) {
            while (!operands.isEmpty()) {
                stack.push(operands.pop());
            }
            throw ex;
        }
        return operands;
    }

    private BigDecimal sqrt(BigDecimal operand) {
        BigDecimal interm = BigDecimal.valueOf(Math.sqrt(operand.doubleValue()));
        return interm.add(BigDecimal.valueOf(operand.subtract(interm.multiply(interm)).doubleValue() / (interm.doubleValue() * 2.0)));
    }

    @SuppressWarnings("unchecked")
    private void saveHistory() {
        history.push((Stack<BigDecimal>) stack.clone());
    }

}
