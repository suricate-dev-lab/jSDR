package com.suricatedevlab.jsdr.rtl;

import com.suricatedevlab.jsdr.SampleSet;
import com.suricatedevlab.jsdr.TunerStatement;

class RtlTunerStatement implements TunerStatement {

    private final RtlDevice device;

    public RtlTunerStatement(RtlDevice device) {
        this.device = device;
    }

    @Override
    public void setCorrectionFrequency(int correctionFrequency) {
        int result = device.getNativeLibrary().rtlsdr_set_freq_correction(device.getHandle().getValue(), correctionFrequency);
        if (result < 0) {
            throw new IllegalArgumentException("Invalid correction frequency (PPM)");
        }
    }

    @Override
    public void setCenterFrequency(long centerFrequency) {
        int result = device.getNativeLibrary().rtlsdr_set_center_freq(device.getHandle().getValue(), centerFrequency);
        if (result < 0) {
            throw new IllegalArgumentException("Invalid center frequency");
        }
    }

    @Override
    public void setGain(int gain) {
        /*
                int result = device.getNativeLibrary().rtlsdr_set_tuner_gain(device.getHandle().getValue(), gain);
        if (result < 0) {
            throw new IllegalArgumentException("Invalid gain");
        }
         */

    }

    @Override
    public SampleSet tune() {
        return null;
    }

    @Override
    public void close() throws Exception {
        device.close();
    }
}
