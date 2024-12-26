package com.suricatedevlab.jsdr.rtl;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import java.math.BigInteger;

interface RtlNativeLibrary extends Library {

    static RtlNativeLibrary getInstance() {
        return Native.load("librtlsdr.2.0.1.dylib", RtlNativeLibrary.class);
    }

    int rtlsdr_open(PointerByReference dev, int index); // Open RTL-SDR device
    void rtlsdr_close(Pointer dev); // Close RTL-SDR device
    int rtlsdr_set_sample_rate(PointerByReference dev, int rate); // Set sample rate
    int rtlsdr_get_sample_rate(PointerByReference dev); // Get current sample rate
    void rtlsdr_reset_buffer(PointerByReference dev); // Reset buffer
    int rtlsdr_read_sync(PointerByReference dev, byte[] buf, int len, int timeout); // Read samples
    int rtlsdr_set_freq_correction(Pointer dev, int ppm); // Set frequency correction
    int rtlsdr_get_freq_correction(Pointer dev); // Get frequency correction
    int rtlsdr_set_center_freq(Pointer dev, long freq); // Set center frequency
    long rtlsdr_get_center_freq(Pointer dev); // Get center frequency
    int rtlsdr_set_tuner_gain(Pointer dev, int gain); // Set tuner gain
    int rtlsdr_get_tuner_gain(Pointer dev); // Get tuner gain
    int rtlsdr_set_agc_mode(PointerByReference dev, int enable); // Set AGC mode
    int rtlsdr_set_direct_sampling(PointerByReference dev, int mode); // Set direct sampling mode
    int rtlsdr_set_offset_tuning(PointerByReference dev, int enable); // Enable offset tuning
    int rtlsdr_get_device_count(); // Get the number of devices connected
    String rtlsdr_get_device_name(int index); // Get name of the device
    int rtlsdr_get_device_usb_strings(int index, byte[] manufacturer, byte[] product, byte[] serial); // Get USB strings for device
}
