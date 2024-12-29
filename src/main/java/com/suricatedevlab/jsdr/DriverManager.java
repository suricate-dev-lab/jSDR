package com.suricatedevlab.jsdr;

import java.util.*;

/**
 * The basic service for managing a set of Software Defined Radio drivers (SDR).
 * <p>
 * The DriverManager methods getDrivers and getDriver are using the Java Standard Edition Service Provider mechanism.
 * <br/>
 * jSDR Drivers must include the file META-INF/services/com.suricatedevlab.jsdr.Driver. This file contains the name of the jSDR drivers implementation of com.suricatedevlab.jsdr.Driver.
 * <br/>
 * <br/>
 * For example, to load the my.sdr.Driver class, the META-INF/services/com.suricatedevlab.jsdr.Driver file would containthe entry:
 *  my.sdr.Driver
 * <br/>
 * <br/>
 * When the method getDriver is called, the DriverManager will attempt to locate a suitable driver based on the provided identifier.
 * </p>
 *
 * @see java.util.ServiceLoader
 * @see com.suricatedevlab.jsdr.Driver
 */
public class DriverManager {

    private DriverManager() {
        throw new IllegalStateException("Can not create instance of DriverManager");
    }

    /**
     * Retrieves an Enumeration with all of the currently loaded Software Defined Radio drivers (SDR) drivers to which the current caller has access.
     * @return the drivers
     */
    static Enumeration<Driver> getDrivers() {
        List<Driver> result = new ArrayList<>();
        ServiceLoader<Driver> loader = ServiceLoader .load(Driver.class);
        Iterator<Driver> iterator = loader.iterator();
        Driver entry = null;
        while (iterator.hasNext())
        {
            Driver instance = iterator.next();
            result.add(instance);
        }
        return Collections.enumeration(result);
    }

    /**
     * Retrieves an instance of the currently loaded Software Defined Radio drivers (SDR) driver based on its identifier that the current caller has access.
     *
     * @param identifier the SDR driver identifier
     * @return the driver
     */
    static Driver getDriver(String identifier) {
        if (identifier == null || identifier.isBlank())
        {
            return null;
        }

        return Collections.list(getDrivers()).stream()
                .filter(driver -> driver.acceptsIdentifier(identifier))
                .findFirst().orElse(null);
    }
}