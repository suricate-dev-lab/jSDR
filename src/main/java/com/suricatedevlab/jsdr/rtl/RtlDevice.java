package com.suricatedevlab.jsdr.rtl;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.suricatedevlab.jsdr.Device;
import com.suricatedevlab.jsdr.SdrException;
import com.suricatedevlab.jsdr.TunerStatement;

import java.util.Arrays;

class RtlDevice implements Device {

    private final int index;
    private final String manufacturer;
    private final String product;
    private final String serial;
    private final RtlNativeLibrary nativeLibrary;
    private final PointerByReference handle;

    public RtlDevice(int index, RtlNativeLibrary nativeLibrary, String manufacturer,
                     String product, String serial) {
        this.index = index;
        this.manufacturer = manufacturer;
        this.product = product;
        this.serial = serial;
        this.nativeLibrary = nativeLibrary;
        handle = new PointerByReference();
    }

    public RtlNativeLibrary getNativeLibrary() {
        return nativeLibrary;
    }

    public PointerByReference getHandle() {
        return handle;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public String getName() {
        return nativeLibrary.rtlsdr_get_device_name(getIndex());
    }

    @Override
    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public String getProduct() {
        return product;
    }

    @Override
    public String getSerial() {
        return serial;
    }

    @Override
    public TunerStatement createTunerStatement() {
        int result = getNativeLibrary().rtlsdr_open(handle, getIndex());
        long f = getNativeLibrary().rtlsdr_get_center_freq(handle.getValue());
        //result = getNativeLibrary().rtlsdr_open(handle, getIndex());

        //result = getNativeLibrary().rtlsdr_set_center_freq(handle.getValue(), 109000000);
        //long f2 = getNativeLibrary().rtlsdr_get_center_freq(handle.getValue());
        return new RtlTunerStatement(this);
    }

    @Override
    public void close() throws Exception {
        try {
            nativeLibrary.rtlsdr_close(handle.getValue());
        }
        catch (Exception ex) {
            throw new SdrException("Failed to close device at index "+getIndex(), ex);
        }
    }

}
