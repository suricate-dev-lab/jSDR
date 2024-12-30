package com.suricatedevlab.jsdr;

import java.util.*;

/**
 * The {@code DriverManager} class provides the basic service for managing a set of Software Defined Radio (SDR) drivers.
 * <p>
 * This class utilizes the Java Standard Edition Service Provider mechanism to dynamically load available SDR driver implementations.
 * <br/>
 * jSDR drivers must include the service definition file {@code META-INF/services/com.suricatedevlab.jsdr.Driver} within their JAR. This file contains the name of the driver implementation class (e.g., {@code my.sdr.Driver}).
 * <br/>
 * <b>Example:</b> To load the {@code my.sdr.Driver} class, the {@code META-INF/services/com.suricatedevlab.jsdr.Driver} file would contain the entry:
 * <pre>
 * my.sdr.Driver
 * </pre>
 * <br/>
 * When the method {@link #getDriver(String)} is called, the {@code DriverManager} will attempt to locate a suitable driver based on the provided identifier.
 * </p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * Driver myDriver = DriverManager.getDriver("my_driver_identifier");
 * </pre>
 *
 * @see java.util.ServiceLoader
 * @see com.suricatedevlab.jsdr.Driver
 */
public class DriverManager {

    // Prevent instantiation of the DriverManager class.
    private DriverManager() {
        throw new IllegalStateException("Cannot create an instance of DriverManager");
    }

    /**
     * Retrieves an {@link Enumeration} of all currently loaded Software Defined Radio (SDR) drivers that the current caller has access to.
     * <p>
     * This method loads all available SDR drivers via the Java ServiceLoader mechanism and returns them as an enumeration.
     * </p>
     *
     * @return an enumeration of all available SDR drivers
     */
    public static Enumeration<Driver> getDrivers() {
        List<Driver> result = new ArrayList<>();
        ServiceLoader<Driver> loader = ServiceLoader.load(Driver.class);
        Iterator<Driver> iterator = loader.iterator();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return Collections.enumeration(result);
    }

    /**
     * Retrieves an instance of the currently loaded Software Defined Radio (SDR) driver based on the provided identifier.
     * <p>
     * This method searches through all the loaded drivers and returns the first one that accepts the specified identifier.
     * If no driver matches the identifier, {@code null} is returned.
     * </p>
     *
     * @param identifier the unique identifier of the desired SDR driver
     * @return the driver that accepts the specified identifier, or {@code null} if no matching driver is found
     */
    public static Driver getDriver(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return null;
        }

        return Collections.list(getDrivers()).stream()
                .filter(driver -> driver.acceptsIdentifier(identifier))
                .findFirst()
                .orElse(null);
    }
}
