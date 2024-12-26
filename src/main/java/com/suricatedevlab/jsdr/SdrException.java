package com.suricatedevlab.jsdr;

public final class SdrException extends Exception {
    public SdrException(String message) {
        super(message);
    }

    public SdrException(String message, Throwable cause) {
        super(message, cause);
    }
}
