package com.suricatedevlab.jsdr;

import java.util.Iterator;
import java.util.Locale;
import java.util.ServiceLoader;

public interface DriverManager {

    static Driver getDriver(String identifier) {
        if (identifier == null || identifier.isBlank())
        {
            return null;
        }
        ServiceLoader<Driver> loader = ServiceLoader .load(Driver.class);
        Iterator<Driver> iterator = loader.iterator();
        Driver result = null;
        while (iterator.hasNext())
        {
            Driver instance = iterator.next();
            if(instance.acceptsIdentifier(identifier))
            {
                result = instance;
                break;
            }
        }
        return result;
    }
}