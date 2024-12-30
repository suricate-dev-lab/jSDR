package com.suricatedevlab.jsdr;

/**
 * The {@code TunerSample} interface provides methods for asynchronously and synchronously
 * reading samples from an SDR (Software Defined Radio) tuner. It is used for accessing raw
 * signal data from the tuner, either through non-blocking asynchronous reads or blocking
 * synchronous reads. This interface extends {@link AutoCloseable}, meaning implementations
 * should properly release any resources when no longer needed.
 *
 * <p>The interface provides two main ways to receive data:</p>
 * <ul>
 *     <li><b>Asynchronous Reading</b>: Allows non-blocking reads, where data is processed via a callback when ready.</li>
 *     <li><b>Blocking Synchronous Reading</b>: Reads a specific amount of data and blocks the caller until the data is available.</li>
 * </ul>
 *
 * <h2>Implementation Requirements</h2>
 * Any class implementing this interface must provide concrete implementations for the
 * methods defined here and manage resources such as buffers and connections to the hardware
 * tuner. Implementations should ensure that {@link AutoCloseable#close()} is invoked to
 * properly release resources when the tuner is no longer in use.
 *
 * @see AutoCloseable
 */
public interface TunerSample extends AutoCloseable {

    /**
     * Callback interface for handling asynchronous read operations.
     *
     * <p>Implementers of this callback will receive a byte array containing the
     * data samples once the asynchronous read is complete.</p>
     */
    interface ReadAsyncCallback {
        /**
         * This method is called when a chunk of data has been received asynchronously.
         *
         * @param data the data received from the tuner, typically raw signal samples
         */
        void onReceive(byte[] data);
    }

    /**
     * Initiates an asynchronous read operation. The provided callback will be called
     * when the requested data is available.
     *
     * <p>This method is non-blocking and allows the caller to continue performing
     * other tasks while waiting for the data. The {@link ReadAsyncCallback#onReceive(byte[])}
     * method will be triggered once the data is ready.</p>
     *
     * @param callback the callback to be invoked when the data is available
     * @throws IllegalArgumentException if the callback is {@code null}
     * @throws SdrException if there is an error during the reading process (e.g., hardware failure, timeout)
     */
    void readAsync(ReadAsyncCallback callback) throws SdrException;

    /**
     * Reads data synchronously from the tuner.
     *
     * <p>This method blocks until the specified amount of data is received. It is a
     * blocking operation, meaning the caller will be paused until the requested number
     * of bytes is available. The method returns the raw sample data in the form of a
     * byte array.</p>
     *
     * @param bufferSize the number of bytes to read from the tuner
     * @return a byte array containing the raw samples
     * @throws IllegalArgumentException if the buffer size is less than or equal to zero
     * @throws SdrException if there is an error during the reading process (e.g., hardware failure, timeout)
     */
    byte[] readSync(int bufferSize) throws SdrException;
}
