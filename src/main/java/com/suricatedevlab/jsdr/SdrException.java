package com.suricatedevlab.jsdr;

/**
 * Custom exception class for errors related to Software Defined Radio (SDR) operations.
 * <p>
 * {@code SdrException} is thrown to indicate specific errors or issues encountered within the
 * jSDR library or when interacting with SDR devices and components. It provides a mechanism to
 * handle SDR-specific exceptions in a way that is distinct from general {@link Exception} handling.
 * </p>
 * <p>
 * This class extends {@link Exception} and can be used to pass error messages or underlying
 * causes (via a {@link Throwable}) specific to SDR-related operations, such as driver failures,
 * device communication issues, invalid configurations, or unsupported features.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>
 * try {
 *     // SDR-related operation
 * } catch (SdrException e) {
 *     // Handle SDR-specific exception
 *     System.err.println("SDR error: " + e.getMessage());
 * }
 * </pre>
 *
 * @see com.suricatedevlab.jsdr.Driver
 * @see com.suricatedevlab.jsdr.Device
 * @see com.suricatedevlab.jsdr.TunerDefinition
 */
public final class SdrException extends Exception {

    /**
     * Constructs a new {@code SdrException} with the specified detail message.
     * <p>
     * This constructor is used when an error occurs in the context of SDR operations, and the
     * exception can be described with a specific error message.
     * </p>
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method)
     */
    public SdrException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code SdrException} with the specified detail message and cause.
     * <p>
     * This constructor is used when an error occurs in the context of SDR operations, and the
     * exception can be described with both a specific error message and an underlying cause (such
     * as another exception).
     * </p>
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method)
     * @param cause the cause of the exception (which is saved for later retrieval by the {@link Throwable#getCause()} method).
     *              A {@code null} value indicates that the cause is nonexistent or unknown.
     */
    public SdrException(String message, Throwable cause) {
        super(message, cause);
    }
}
