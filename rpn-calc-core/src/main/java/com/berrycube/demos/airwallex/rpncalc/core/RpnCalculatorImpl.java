package com.berrycube.demos.airwallex.rpncalc.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class RpnCalculatorImpl implements RpnCalculator {

    private Queue<Stack<BigDecimal>> history;
    private Stack<BigDecimal> stack;

    public RpnCalculatorImpl(Queue<Stack<BigDecimal>> history, Stack<BigDecimal> stack) {
        this.history = history;
        this.stack = stack;
    }

    @Override
    public void push(BigDecimal operand) {
        history.offer(stack);
        stack.push(operand);
    }

    @Override
    public void add() {
        history.offer(stack);
        Stack<BigDecimal> operands = getOperands(2);
        stack.push(operands.pop().add(operands.pop()));
    }

    @Override
    public void sub() {
        history.offer(stack);
        Stack<BigDecimal> operands = getOperands(2);
        stack.push(operands.pop().subtract(operands.pop()));
    }

    @Override
    public void mul() {
        history.offer(stack);
        Stack<BigDecimal> operands = getOperands(2);
        stack.push(operands.pop().multiply(operands.pop()));
    }

    @Override
    public void div() {
        history.offer(stack);
        Stack<BigDecimal> operands = getOperands(2);
        stack.push(operands.pop().divide(operands.pop(), RoundingMode.HALF_UP));
    }

    @Override
    public void sqrt() {
        history.offer(stack);
        Stack<BigDecimal> operands = getOperands(1);
        stack.push(BigDecimal.valueOf(Math.sqrt(operands.pop().doubleValue())));
    }

    @Override
    public void undo() {
        Optional.ofNullable(history.poll()).ifPresent(s -> stack = s);
    }

    @Override
    public void clear() {
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
        Stack<BigDecimal> operands = new Stack<BigDecimal>();
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

}
