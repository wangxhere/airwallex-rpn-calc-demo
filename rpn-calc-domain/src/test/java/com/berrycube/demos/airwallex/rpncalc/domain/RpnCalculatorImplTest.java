package com.berrycube.demos.airwallex.rpncalc.domain;

import net.jqwik.api.constraints.*;
import org.junit.jupiter.api.Test;

import net.jqwik.api.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RpnCalculatorImplTest {

    private static final int TOLERANCE_EFFECTIVE_DIGITS = 8;    // lower than requirement due to double precision

    @Property
    void push_shouldPushNumberIntoStack(@ForAll List<BigDecimal> original, @ForAll BigDecimal operand) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when
        uut.push(operand);

        // then
        List<BigDecimal> dump = uut.dumpStack();
        List<BigDecimal> expected = new ArrayList<>(original);
        expected.add(operand);
        assertIterableEquals(expected, dump);
    }

    @Property
    void push_shouldSupportUndo(@ForAll List<BigDecimal> original, @ForAll BigDecimal operand) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when
        uut.push(operand);
        uut.undo();

        // then
        List<BigDecimal> dump = uut.dumpStack();
        assertIterableEquals(original, dump);
    }

    @Property
    void add_shouldAddTop2Operands(@ForAll List<BigDecimal> original, @ForAll @Scale(8) BigDecimal operand1, @ForAll @Scale(8) BigDecimal operand2) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when
        uut.push(operand1);
        uut.push(operand2);
        uut.add();

        // then
        List<BigDecimal> dump = uut.dumpStack();

        assertIterableEquals(original, dump.subList(0, dump.size() - 1));
        assertEqualWithTolerance(operand1.doubleValue() + operand2.doubleValue(), dump.get(dump.size() - 1).doubleValue(), TOLERANCE_EFFECTIVE_DIGITS);
    }

    @Property
    void add_shouldSupportUndo(@ForAll List<BigDecimal> original, @ForAll @Scale(8) BigDecimal operand1, @ForAll @Scale(8) BigDecimal operand2) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when
        uut.push(operand1);
        uut.push(operand2);
        uut.add();
        uut.undo();
        uut.undo();
        uut.undo();

        // then
        List<BigDecimal> dump = uut.dumpStack();
        assertIterableEquals(original, dump);
    }

    @Property
    void add_shouldThrowEmptyStackException_whenInsufficientOperands(@ForAll @Size(min=0, max=1) List<BigDecimal> original) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when -> then
        assertThrows(EmptyStackException.class, uut::add);
    }

    @Property
    void sub_shouldSubTop2Operands(@ForAll List<BigDecimal> original, @ForAll @Scale(8) BigDecimal operand1, @ForAll @Scale(8) BigDecimal operand2) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when
        uut.push(operand1);
        uut.push(operand2);
        uut.sub();

        // then
        List<BigDecimal> dump = uut.dumpStack();

        assertIterableEquals(original, dump.subList(0, dump.size() - 1));
        assertEqualWithTolerance(operand1.doubleValue() - operand2.doubleValue(), dump.get(dump.size() - 1).doubleValue(), TOLERANCE_EFFECTIVE_DIGITS);
    }

    @Property
    void sub_shouldSupportUndo(@ForAll List<BigDecimal> original, @ForAll @Scale(8) BigDecimal operand1, @ForAll @Scale(8) BigDecimal operand2) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when
        uut.push(operand1);
        uut.push(operand2);
        uut.sub();
        uut.undo();
        uut.undo();
        uut.undo();

        // then
        List<BigDecimal> dump = uut.dumpStack();

        assertIterableEquals(original, dump);
    }

    @Property
    void sub_shouldThrowEmptyStackException_whenInsufficientOperands(@ForAll @Size(min=0, max=1) List<BigDecimal> original) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when -> then
        assertThrows(EmptyStackException.class, uut::sub);
    }

    @Property
    void mul_shouldMulTop2Operands(@ForAll List<BigDecimal> original, @ForAll @Scale(8) BigDecimal operand1, @ForAll @Scale(8) BigDecimal operand2) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when
        uut.push(operand1);
        uut.push(operand2);
        uut.mul();

        // then
        List<BigDecimal> dump = uut.dumpStack();

        assertIterableEquals(original, dump.subList(0, dump.size() - 1));
        assertEqualWithTolerance(operand1.doubleValue() * operand2.doubleValue(), dump.get(dump.size() - 1).doubleValue(), TOLERANCE_EFFECTIVE_DIGITS);
    }

    @Property
    void mul_shouldSupportUndo(@ForAll List<BigDecimal> original, @ForAll @Scale(8) BigDecimal operand1, @ForAll @Scale(8) BigDecimal operand2) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when
        uut.push(operand1);
        uut.push(operand2);
        uut.mul();
        uut.undo();
        uut.undo();
        uut.undo();

        // then
        List<BigDecimal> dump = uut.dumpStack();

        assertIterableEquals(original, dump);
    }

    @Property
    void mul_shouldThrowEmptyStackException_whenInsufficientOperands(@ForAll @Size(min=0, max=1) List<BigDecimal> original) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when -> then
        assertThrows(EmptyStackException.class, uut::mul);
    }

    @Property
    void div_shouldDivTop2Operands(@ForAll List<BigDecimal> original, @ForAll @Scale(8) BigDecimal operand1, @ForAll("nonZeroBigDecimal") @Scale(8) BigDecimal operand2) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when
        uut.push(operand1);
        uut.push(operand2);
        uut.div();

        // then
        List<BigDecimal> dump = uut.dumpStack();

        assertIterableEquals(original, dump.subList(0, dump.size() - 1));
        assertEqualWithTolerance(operand1.doubleValue() / operand2.doubleValue(), dump.get(dump.size() - 1).doubleValue(), TOLERANCE_EFFECTIVE_DIGITS);
    }

    @Property
    void div_shouldSupportUndo(@ForAll List<BigDecimal> original, @ForAll @Scale(8) BigDecimal operand1, @ForAll("nonZeroBigDecimal") @Scale(8) BigDecimal operand2) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when
        uut.push(operand1);
        uut.push(operand2);
        uut.div();
        uut.undo();
        uut.undo();
        uut.undo();

        // then
        List<BigDecimal> dump = uut.dumpStack();

        assertIterableEquals(original, dump);
    }

    @Property
    void div_shouldThrowArithmeticException_whenDividerIsZero(@ForAll List<BigDecimal> original, @ForAll @Scale(8) BigDecimal operand1) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when -> then
        uut.push(operand1);
        uut.push(BigDecimal.ZERO);
        assertThrows(ArithmeticException.class, uut::div);
    }

    @Property
    void div_shouldThrowEmptyStackException_whenInsufficientOperands(@ForAll @Size(min=0, max=1) List<BigDecimal> original) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when -> then
        assertThrows(EmptyStackException.class, uut::div);
    }

    @Property
    void sqrt_shouldSqrtTopOperand(@ForAll List<BigDecimal> original, @ForAll @Negative BigDecimal negateOperand) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);
        BigDecimal operand = negateOperand.negate();

        original.forEach(uut::push);

        // when
        uut.push(operand);
        uut.sqrt();

        // then
        List<BigDecimal> dump = uut.dumpStack();

        assertIterableEquals(original, dump.subList(0, dump.size() - 1));
        assertEqualWithTolerance(Math.sqrt(operand.doubleValue()), dump.get(dump.size() - 1).doubleValue(), TOLERANCE_EFFECTIVE_DIGITS);
    }

    @Property
    void sqrt_shouldSupportUndo(@ForAll List<BigDecimal> original, @ForAll @Negative BigDecimal negateOperand) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);
        BigDecimal operand = negateOperand.negate();

        original.forEach(uut::push);

        // when
        uut.push(operand);
        uut.sqrt();
        uut.undo();
        uut.undo();

        // then
        List<BigDecimal> dump = uut.dumpStack();

        assertIterableEquals(original, dump);
    }

    @Property
    void sqrt_shouldThrowNumberFormatException_whenOperandIsNegative(@ForAll List<BigDecimal> original, @ForAll @Negative BigDecimal operand) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when -> then
        uut.push(operand);
        assertThrows(NumberFormatException.class, uut::sqrt);
    }

    @Test
    void sqrt_shouldThrowEmptyStackException_whenInsufficientOperands() {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        // when -> then
        assertThrows(EmptyStackException.class, uut::sqrt);
    }

    @Test
    void undo_shouldStillSucceed_whenHistoryIsEmpty() {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        // when -> then
        uut.undo();
    }

    @Property
    void clear_shouldAlwaysClearStack(@ForAll List<BigDecimal> original) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when
        uut.clear();

        // then
        List<BigDecimal> dump = uut.dumpStack();
        assertIterableEquals(new ArrayList<>(), dump);
    }

    @Property
    void clear_shouldSupportUndo(@ForAll List<BigDecimal> original) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when
        uut.clear();
        uut.undo();

        // then
        List<BigDecimal> dump = uut.dumpStack();
        assertIterableEquals(original, dump);
    }

    @Property
    void dumpStack_shouldAlwaysDumpWhateverOnStack(@ForAll List<BigDecimal> original) {
        // given
        Stack<Stack<BigDecimal>> history = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        RpnCalculatorImpl uut = new RpnCalculatorImpl(history, stack);

        original.forEach(uut::push);

        // when -> then
        List<BigDecimal> dump = uut.dumpStack();
        assertIterableEquals(original, dump);
    }

    @Provide
    Arbitrary<BigDecimal> nonZeroBigDecimal() {
        return Arbitraries.bigDecimals().filter(aNumber -> aNumber.abs().compareTo(BigDecimal.valueOf(1, 8)) > 0);
    }

    void assertEqualWithTolerance(double expected, double actual, int effectiveDigits) {
        double scaleOfDifference = Math.log10(Math.abs(expected - actual));
        double expectedDifferenceCeiling = Math.log10(Math.abs(expected)) - effectiveDigits;
        assertTrue(
                Double.isInfinite(expected) || Double.isInfinite(actual) || scaleOfDifference <= Double.NEGATIVE_INFINITY || scaleOfDifference < expectedDifferenceCeiling,
                "expected " + expected + " but got " + actual + " - scale of difference " + scaleOfDifference + ", expected difference ceiling " + expectedDifferenceCeiling
        );
    }
}