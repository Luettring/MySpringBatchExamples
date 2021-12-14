package de.dps.springbatch.exception;

public class NoCasesFoundException extends Exception {

    public NoCasesFoundException(String s) {
        super(s);
    }

    public NoCasesFoundException(String s, Throwable throwable) {
        super(s,throwable);
    }
}
