package com.suricatedevlab.jsdr;

import java.util.Enumeration;

public interface Driver {

    boolean acceptsIdentifier(String identifier);

    Enumeration<Device> getDevices() throws SdrException;

    Device getDevice(int index) throws SdrException;
}
