package com.berrycube.demos.airwallex.rpncalc.core.ex;

public class InsufficientParametersException extends RuntimeException {
    InsufficientParametersException(String operator, long pos) {
        super(String.format("operator %s (position: %d): insufficient parameters", operator, pos));
    }
}
