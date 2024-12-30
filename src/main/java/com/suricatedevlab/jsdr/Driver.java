package com.suricatedevlab.jsdr;

import java.util.Enumeration;

/**
 * The {@code Driver} interface provides methods for interacting with
 * Software Defined Radio (SDR) devices. It defines the contract for classes
 * that can manage and access SDR devices, allowing the user to query available
 * devices and retrieve individual devices based on an identifier or index.
 *
 * <p>This interface is designed to abstract the access to various types of SDR hardware,
 * providing a consistent way of discovering and interacting with SDR devices that are
 * compatible with the driver implementation.</p>
 *
 * <h2>Overview</h2>
 * The interface provides the following functionality:
 * <ul>
 *     <li><b>acceptsIdentifier</b>: Determines whether the driver can handle a specific device identifier.</li>
 *     <li><b>getDevices</b>: Retrieves a list of all SDR devices supported by the driver.</li>
 *     <li><b>getDevice</b>: Retrieves a specific SDR device based on the given index.</li>
 * </ul>
 *
 * <h2>Implementation Requirements</h2>
 * Any class implementing this interface must provide concrete implementations for the methods
 * defined here, with proper exception handling (e.g., {@link SdrException}) for device access errors.
 *
 * @see Device
 * @see SdrException
 */
public interface Driver {

    /**
     * Determines whether this driver accepts the provided identifier.
     * <p>This method is typically used to validate whether the driver is compatible with a specific SDR device
     * identified by the given identifier.</p>
     *
     * @param identifier The identifier of the SDR device to check.
     * @return <code>true</code> if this driver can handle the given identifier,
     *         <code>false</code> otherwise.
     */
    boolean acceptsIdentifier(String identifier);

    /**
     * Retrieves an enumeration of all SDR devices that are supported by this driver.
     * <p>This method allows the application to discover the available SDR devices associated with this driver.</p>
     *
     * @return An enumeration of <code>Device</code> objects representing all devices detected by this driver.
     * @throws SdrException If an error occurs while retrieving the devices (e.g., no devices found, or a communication error).
     */
    Enumeration<Device> getDevices() throws SdrException;

    /**
     * Retrieves a specific SDR device by its index.
     * <p>This method is used to obtain a particular device from the set of devices supported by this driver.
     * The index is typically zero-based.</p>
     *
     * @param index The index of the device to retrieve.
     * @return A <code>Device</code> object representing the SDR device at the specified index.
     * @throws SdrException If an error occurs while retrieving the device, such as an invalid index or communication failure.
     */
    Device getDevice(int index) throws SdrException;
}
