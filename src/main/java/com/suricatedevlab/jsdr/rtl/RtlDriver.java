package com.suricatedevlab.jsdr.rtl;

import com.suricatedevlab.jsdr.Device;
import com.suricatedevlab.jsdr.Driver;
import com.suricatedevlab.jsdr.SdrException;

import java.io.UnsupportedEncodingException;
import java.util.*;

public final class RtlDriver implements Driver {

    private static final String IDENTIFIER = "RTL-SDR";
    private static final RtlNativeLibrary NATIVE_LIBRARY;

    static {
        NATIVE_LIBRARY = RtlNativeLibrary.getInstance();
    }

    @Override
    public boolean acceptsIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return false;
        }
        return IDENTIFIER.contentEquals(identifier);
    }

    @Override
    public Enumeration<Device> getDevices() throws SdrException {
        List<Device> result = new ArrayList<>();
        for (int i = 0, deviceCount = NATIVE_LIBRARY.rtlsdr_get_device_count(); i < deviceCount; i++) {

            try {
                byte[] manufacturerAsBytes = new byte[256];
                byte[] productAsBytes = new byte[256];
                byte[] serialAsBytes = new byte[256];
                NATIVE_LIBRARY.rtlsdr_get_device_usb_strings(0, manufacturerAsBytes, productAsBytes, serialAsBytes);

                String manufacturer = new String(ByteUtils.trim(manufacturerAsBytes), "UTF-8");
                String product = new String(ByteUtils.trim(productAsBytes), "UTF-8");
                String serial = new String(ByteUtils.trim(serialAsBytes), "UTF-8");
                result.add(new RtlDevice(i, NATIVE_LIBRARY, manufacturer, product, serial));
            } catch (UnsupportedEncodingException e) {
                throw new SdrException("Failed to load device info at index "+i, e);
            }
        }
        return Collections.enumeration(result);
    }

    @Override
    public Device getDevice(int index) throws SdrException {
        Device result = null;
        Device entry;
        Iterator<Device> availableDevices = getDevices().asIterator();
        while(availableDevices.hasNext()) {
            entry = availableDevices.next();
            if (entry.getIndex() == index) {
                result = entry;
                break;
            }
        }
        return result;
    }
}
