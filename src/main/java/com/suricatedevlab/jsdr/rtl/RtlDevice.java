package com.suricatedevlab.jsdr.rtl;

import com.sun.jna.ptr.PointerByReference;
import com.suricatedevlab.jsdr.Device;
import com.suricatedevlab.jsdr.SdrException;
import com.suricatedevlab.jsdr.TunerDefinition;

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
    public TunerDefinition getTunerDefinition() throws SdrException {
        int result = getNativeLibrary().rtlsdr_open(handle, getIndex());
        if (result < 0) {
            throw new SdrException("Can not open device at index # "+getIndex());
        }
        return new RtlTunerDefinition(this);
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
