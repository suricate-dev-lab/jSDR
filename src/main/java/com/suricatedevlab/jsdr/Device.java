package com.suricatedevlab.jsdr;

/**
 * The {@code Device} interface represents a physical or virtual SDR (Software Defined Radio) device
 * that can be interacted with through various operations, such as reading data and configuring parameters.
 * It provides methods for retrieving essential metadata about the device, such as its name, manufacturer,
 * product details, and serial number. The interface also offers a method to access the device's tuner definition.
 *
 * <p>This interface extends {@link AutoCloseable}, so implementations are responsible for releasing any resources
 * (such as hardware connections) when the device is no longer in use.</p>
 *
 * <h2>Implementation Requirements</h2>
 * Any class implementing this interface must provide concrete implementations for the methods defined here and
 * ensure that proper resource management is carried out when {@link AutoCloseable#close()} is invoked.
 *
 * @see AutoCloseable
 * @see TunerDefinition
 * @see SdrException
 */
public interface Device extends AutoCloseable{

    /**
     * Returns the index of the device within the driver or device manager.
     *
     * <p>The index is a unique identifier for the device within a collection of devices managed
     * by the driver. The index may be used for selecting a device in scenarios where multiple
     * devices are available.</p>
     *
     * @return the index of the device
     */
    int getIndex();

    /**
     * Returns the name of the device.
     *
     * <p>The name is typically a human-readable string identifying the device. It can represent
     * a model name, a device type, or other relevant descriptors depending on the device.</p>
     *
     * @return the name of the device
     */
    String getName();

    /**
     * Returns the manufacturer of the device.
     *
     * <p>The manufacturer is typically a string representing the company or entity that produced
     * the device. This information is useful for identifying the source or brand of the device.</p>
     *
     * @return the manufacturer of the device
     */
    String getManufacturer();

    /**
     * Returns the product details of the device.
     *
     * <p>The product information provides further identification of the device, including its
     * model number, version, or other specific product details.</p>
     *
     * @return the product details of the device
     */
    String getProduct();

    /**
     * Returns the serial port identifier of the device.
     *
     * <p>Specific serial port identifier is assigned to each device</p>
     *
     * @return the serial port identifier of the device
     */
    String getSerial();

    /**
     * Retrieves the tuner definition associated with this device.
     *
     * <p>The tuner definition provides important details about the tuner capabilities of the device,
     * such as supported frequency gains or set configuration options such as sample rate. This method
     * may throw a {@link SdrException} if there is an error retrieving the tuner definition (e.g., if the device
     * is not properly initialized or does not have a valid tuner).</p>
     *
     * @return the {@link TunerDefinition} for the device
     * @throws SdrException if there is an error retrieving the tuner definition
     */
    TunerDefinition getTunerDefinition() throws SdrException;
}
